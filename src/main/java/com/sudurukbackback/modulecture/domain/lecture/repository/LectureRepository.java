package com.sudurukbackback.modulecture.domain.lecture.repository;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // 강의 검색 쿼리 (제목, 강사명, 카테고리, 가격 조건)
    @Query("SELECT l FROM Lecture l " +
            "LEFT JOIN CategoryRel cr ON l.id = cr.lectureId " +
            "LEFT JOIN Category c ON cr.categoryId = c.id " +
            "WHERE (:keyword IS NULL OR l.title LIKE %:keyword% OR l.description LIKE %:keyword%) " +
            "AND (:category IS NULL OR c.categoryName = :category) " +
            "AND (:minPrice IS NULL OR l.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR l.price <= :maxPrice) " +
            "ORDER BY l.createdAt DESC")
    List<Lecture> searchLectures(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );
}
