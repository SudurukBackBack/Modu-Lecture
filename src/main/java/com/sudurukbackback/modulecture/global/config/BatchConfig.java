package com.sudurukbackback.modulecture.global.config;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<User> pendingUsersReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("pendingUsersReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u WHERE u.userStatus = :status AND u.deleteAt <= :oneWeekAgo")
                .parameterValues(Map.of("status", UserStatus.PENDING, "oneWeekAgo", LocalDateTime.now().minusWeeks(1)))
                .pageSize(100)
                .build();
    }

    @Bean
    public Step deletePendingUsersStep() {
        return new StepBuilder("deletePendingUsersStep", jobRepository)
                .<User, User>chunk(100, transactionManager) // transactionManager 사용
                .reader(pendingUsersReader())
                .processor(deletePendingUserProcessor())
                .writer(deletePendingUsersWriter())
                .build();
    }

    @Bean
    public ItemProcessor<User, User> deletePendingUserProcessor() {
        return user -> {
            user.setUserStatus(UserStatus.DELETED);
            user.setPassword(null); // 비밀번호 삭제
            return user;
        };
    }

    @Bean
    public ItemWriter<User> deletePendingUsersWriter() {
        return users -> {
            for (User user : users) {
                userRepository.save(user);
            }
        };
    }
}