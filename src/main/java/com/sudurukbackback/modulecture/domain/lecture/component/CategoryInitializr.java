package com.sudurukbackback.modulecture.domain.lecture.component;

import com.sudurukbackback.modulecture.domain.lecture.entity.Category;
import com.sudurukbackback.modulecture.domain.lecture.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializr implements ApplicationRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> categoryNames = Arrays.asList(
                "프로그래밍", "데이터", "인공지능", "웹 개발", "디자인",
                "사진", "음악", "미술", "언어", "역사",
                "철학", "문학", "물리학", "화학", "생물학",
                "심리학", "경영학", "마케팅", "경제학", "금융",
                "의학", "영양학", "피트니스", "건강"
        );

        for (String name : categoryNames) {
            // 이미 존재하는지 확인
            if (!categoryRepository.existsByCategoryName(name)) {
                Category category = new Category();
                category.setCategoryName(name);
                categoryRepository.save(category);
            }
        }
    }
}
