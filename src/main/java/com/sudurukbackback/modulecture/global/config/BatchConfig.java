package com.sudurukbackback.modulecture.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfig {

    // TODO: 추후 여력이 있을 때 Batch 구성

//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//    private final UserRepository userRepository;
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public ItemReader<User> pendingUsersReader() {
//        return new JpaPagingItemReaderBuilder<User>()
//                .name("pendingUsersReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("SELECT u FROM User u WHERE u.userStatus = :status AND u.deleteAt <= :oneWeekAgo")
//                .parameterValues(Map.of("status", UserStatus.PENDING, "oneWeekAgo", LocalDateTime.now().minusWeeks(1)))
//                .pageSize(100)
//                .build();
//    }
//
//    @Bean
//    public Step deletePendingUsersStep() {
//        return new StepBuilder("deletePendingUsersStep", jobRepository)
//                .<User, User>chunk(100, transactionManager) // transactionManager 사용
//                .reader(pendingUsersReader())
//                .processor(deletePendingUserProcessor())
//                .writer(deletePendingUsersWriter())
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<User, User> deletePendingUserProcessor() {
//        return user -> {
//            user.setUserStatus(UserStatus.DELETED);
//            user.setPassword(null); // 비밀번호 삭제
//            return user;
//        };
//    }
//
//    @Bean
//    public ItemWriter<User> deletePendingUsersWriter() {
//        return users -> {
//            for (User user : users) {
//                userRepository.save(user);
//            }
//        };
//    }
}