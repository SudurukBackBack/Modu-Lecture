package com.sudurukbackback.modulecture.domain.lecture.service;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.CategoryRel;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.entity.LectureStatus;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.CategoryRelRepository;
import com.sudurukbackback.modulecture.domain.lecture.repository.CategoryRepository;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final CategoryRelRepository categoryRelRepository;

    //강의 생성
    @Transactional
    public LectureResponseDto createLecture(LectureCreateRequestDto request, Long userId) throws IOException {

        // 강의 정보 저장
        Lecture lecture = Lecture.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(LectureStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        Lecture savedLecture = lectureRepository.save(lecture);

        // 선택된 카테고리와의 관계 저장
        List<Long> categoryIds = request.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                CategoryRel categoryRel = CategoryRel.builder()
                        .lectureId(savedLecture.getId())
                        .categoryId(categoryId)
                        .build();
                categoryRelRepository.save(categoryRel);
            }
        }

        return new LectureResponseDto(savedLecture);
    }

    // 강의 상세 조회 (S3 파일 URL 포함)
    @Transactional(readOnly = true)
    public LectureResponseDto getLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("해당 강의를 찾을 수 없습니다."));

        // + contentService단의 조회 로직

        // return new LectureResponseDto(lecture, lecture.getVideoUrl(), lecture.getImageUrl());
        return new LectureResponseDto(lecture);
    }

    // 강의 수정 (PATCH)
    @Transactional
    public LectureResponseDto updateLecture(Long lectureId, LectureUpdateRequestDto requestDto, MultipartFile file) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("해당 강의를 찾을 수 없습니다."));

        if (requestDto.getTitle() != null) {
            lecture.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            lecture.setDescription(requestDto.getDescription());
        }
        if (requestDto.getPrice() != null) {
            lecture.setPrice(requestDto.getPrice());
        }

        return new LectureResponseDto(lecture);
    }

    // 강의 삭제 (S3 파일 삭제 포함)
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("해당 강의를 찾을 수 없습니다."));

        lectureRepository.delete(lecture);
    }
}
