package com.sudurukbackback.modulecture.domain.lecture.service;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureCreateResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.CategoryRel;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.entity.LectureStatus;
import com.sudurukbackback.modulecture.domain.lecture.exception.InstructorNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.CategoryRelRepository;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 강의 생성
    @Transactional
    public LectureCreateResponseDto createLecture(LectureCreateRequestDto request, Long instructorId) throws IOException {

        // 강의 정보 저장
        Lecture lecture = Lecture.builder()
                .instructorId(instructorId)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(LectureStatus.ACTIVE) // 강의 상태를 ACTIVE로 설정
                .createdAt(LocalDateTime.now())
                .build();

        Lecture savedLecture = lectureRepository.save(lecture);

        // 선택된 카테고리와의 관계 저장
        List<Long> categoryIds = request.getCategoryIds(); // 요청에서 카테고리 ID 목록 가져옴
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                CategoryRel categoryRel = CategoryRel.builder()
                        .lectureId(savedLecture.getId()) // 저장된 강의의 ID
                        .categoryId(categoryId) // 카테고리 ID
                        .build();
                categoryRelRepository.save(categoryRel); // 카테고리 관계 저장
            }
        }

        return new LectureCreateResponseDto(savedLecture);
    }

    // 강의 상세 조회 (S3 파일 URL 포함)
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public LectureGetResponseDto getLecture(Long lectureId) {
        
      Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);

        User user = userRepository.findById(lecture.getInstructorId())
                .orElseThrow(InstructorNotFoundException::new);

        String instructor = user.getNickname();

        return new LectureGetResponseDto(instructor, lecture);
    }

    // 강의 수정 (PATCH)
    @Transactional
    public LectureCreateResponseDto updateLecture(Long lectureId, LectureUpdateRequestDto requestDto, MultipartFile file) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);


        // 요청된 필드가 있는 경우에만 업데이트
        if (requestDto.getTitle() != null) {
            lecture.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            lecture.setDescription(requestDto.getDescription());
        }
        if (requestDto.getPrice() != null) {
            lecture.setPrice(requestDto.getPrice());
        }

        return new LectureCreateResponseDto(lecture); // 업데이트된 강의 정보를 DTO로 변환하여 반환
    }

    // 강의 삭제
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);

        lectureRepository.delete(lecture); // 강의 삭제
    }
}