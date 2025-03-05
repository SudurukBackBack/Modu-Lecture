package com.sudurukbackback.modulecture.domain.lecture.service;

import com.sudurukbackback.modulecture.domain.lecture.entity.Category;
import com.sudurukbackback.modulecture.domain.lecture.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}