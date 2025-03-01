package com.sudurukbackback.modulecture.domain.lecture.service;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    //강의 생성 (S3 파일 업로드 포함)
    @Transactional
    public LectureResponseDto createLecture(LectureCreateRequestDto requestDto, MultipartFile file) throws IOException {
        // 강의 정보 DB 저장
        Lecture lecture = Lecture.builder()
                .userId(requestDto.getUserId())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .categoryIds(requestDto.getCategoryIds()) //  변경된 categoryIds 적용
                .price(requestDto.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        Lecture savedLecture = lectureRepository.save(lecture);
        //Long lectureId = savedLecture.getLectureId(); //  저장 후 ID 획득

        lectureRepository.save(savedLecture);

        return new LectureResponseDto(savedLecture, null, null);
    }
    // 강의 상세 조회 (S3 파일 URL 포함)
    @Transactional(readOnly = true)
    public LectureResponseDto getLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("해당 강의를 찾을 수 없습니다."));

        return new LectureResponseDto(lecture, lecture.getVideoUrl(), lecture.getImageUrl());
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
        if (requestDto.getCategoryIds() != null) {
            lecture.setCategoryIds(requestDto.getCategoryIds());
        }
        if (requestDto.getPrice() != null) {
            lecture.setPrice(requestDto.getPrice());
        }

        return new LectureResponseDto(lecture, lecture.getVideoUrl(), lecture.getImageUrl());
    }

    // 강의 삭제 (S3 파일 삭제 포함)
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("해당 강의를 찾을 수 없습니다."));

        lectureRepository.delete(lecture);
    }
}
