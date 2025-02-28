package com.sudurukbackback.modulecture.domain.lecture.service;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.lecture.entity.LectureStatus;
import com.sudurukbackback.modulecture.domain.lecture.exception.LectureNotFoundException;
import com.sudurukbackback.modulecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final S3Service s3Service; //  S3 연동 추가

    /** 📌 강의 생성 (S3 파일 업로드 포함) */
    @Transactional
    public Lecture createLecture(LectureCreateRequestDto requestDto, MultipartFile file) throws IOException {
        Lecture lecture = Lecture.builder()
                .userId(requestDto.getUserId())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .instructor(requestDto.getInstructor())
                .category(Integer.parseInt(requestDto.getCategory()))
                .price(requestDto.getPrice())
                .createdAt(LocalDateTime.now())
                .status(requestDto.getStatus())
                .build();

        // 파일이 포함된 경우 S3에 업로드하고 파일 URL을 저장
        if (file != null) {
            String fileUrl = s3Service.uploadFile(file);  //S3 Service에 클래스 연동이 필요한 부분입니다. 임의로 만들었고 추후에 필요 없으면 수정하겠습니다.
            lecture.setFileUrl(fileUrl);
        }

        return lectureRepository.save(lecture);
    }

    // 강의 상세 조회 (S3 파일 URL 포함)
    @Transactional(readOnly = true)
    public LectureResponseDto getLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);
        return new LectureResponseDto(lecture);
    }

    // 강의 수정 (S3 파일 변경 가능)
    @Transactional
    public LectureResponseDto updateLecture(Long lectureId, LectureUpdateRequestDto requestDto, MultipartFile newFile) throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);

        if (requestDto.getTitle() != null) {
            lecture.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            lecture.setDescription(requestDto.getDescription());
        }

        // 🚀 새로운 파일이 업로드되면 기존 파일 삭제 후 새 파일 업로드
        if (newFile != null) {
            if (lecture.getFileUrl() != null) {
                s3Service.deleteFile(lecture.getFileUrl()); // 기존 파일 삭제
            }
            String newFileUrl = s3Service.uploadFile(newFile);
            lecture.setFileUrl(newFileUrl);
        }

        lectureRepository.save(lecture);
        return new LectureResponseDto(lecture);
    }

    // 강의 삭제 (S3 파일 삭제 포함)
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(LectureNotFoundException::new);

        // 🚀 강의 파일이 있으면 S3에서 삭제
        if (lecture.getFileUrl() != null) {
            s3Service.deleteFile(lecture.getFileUrl());
        }

        lectureRepository.delete(lecture);
    }
}
