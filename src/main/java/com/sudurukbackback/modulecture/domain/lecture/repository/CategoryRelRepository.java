package com.sudurukbackback.modulecture.domain.lecture.repository;

import com.sudurukbackback.modulecture.domain.lecture.entity.CategoryRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRelRepository extends JpaRepository<CategoryRel, Long> {

}