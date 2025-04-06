package com.sudurukbackback.modulecture.domain.lecture.controller;

import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureCreateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.request.LectureUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.ErrorResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureCreateResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.dto.response.LectureGetResponseDto;
import com.sudurukbackback.modulecture.domain.lecture.service.LectureService;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;
//    private final ContentService contentService;

    /**
     * 새로운 강의를 생성합니다. (파일 업로드 포함)
     *
     * @param request 강의 생성 요청 데이터를 담은 DTO 객체
     * @param auth 현재 인증된 사용자 정보를 담은 Authentication 객체
     * @return 생성된 강의 정보를 담은 LectureCreateResponseDto를 포함한 ResponseEntity
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    @PreAuthorize("hasRole('GOLD')")
    @PostMapping("/create")
    public ResponseEntity<?> createLecture(
            @ModelAttribute @Valid LectureCreateRequestDto request,
            BindingResult bindingResult,
            Authentication auth) throws IOException {

        log.info("요청된 데이터:" + request.toString());

        if (bindingResult.hasErrors()) {
            // 유효성 검사 에러 메세지
            log.info("유효성 검사 에러:" + bindingResult.getAllErrors());

            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            ErrorResponseDto errorResponse = new ErrorResponseDto("유효성 검사 실패", errorMessages);


            return ResponseEntity.badRequest().body(errorResponse);
        }

        // 현재 인증된 사용자의 id 가져오기
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        LectureCreateResponseDto response = lectureService.createLecture(request, userId);

        // 파일 업로드 처리
//        Long lectureId = response.getLectureId();
//        MultipartFile video = request.getVideo();
//        contentService.uploadContent(lectureId, video);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 강의의 상세 정보를 조회합니다.
     *
     * @param lectureId 조회할 강의의 ID
     * @return 조회된 강의 정보를 담은 LectureGetResponseDto를 포함한 ResponseEntity
     */
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureGetResponseDto> getLecturesById(@PathVariable Long lectureId) {
        LectureGetResponseDto lecture = lectureService.getLecture(lectureId);

        // contentService: getContent - S3에서 video, image 조회 (추후 구현)

        return ResponseEntity.ok(lecture);
    }

    /**
     * 특정 강의의 정보를 수정합니다.
     *
     * @param lectureId 수정할 강의의 ID
     * @param requestDto 강의 수정 요청 데이터를 담은 DTO 객체
     * @param file 업로드할 파일 (선택 사항)
     * @return 수정된 강의 정보를 담은 LectureResponseDto를 포함한 ResponseEntity
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    @PatchMapping("/{lectureId}")
    public ResponseEntity<LectureCreateResponseDto> updateLecture(
            @PathVariable Long lectureId,
            @RequestPart("requestDto") @Valid LectureUpdateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        LectureCreateResponseDto updatedLecture = lectureService.updateLecture(lectureId, requestDto, file);
        return ResponseEntity.ok(updatedLecture);
    }

    /**
     * 특정 강의를 삭제합니다. (파일 삭제 포함)
     *
     * @param lectureId 삭제할 강의의 ID
     * @return 내용이 없는 응답(ResponseEntity<Void>)으로, HTTP 상태 코드는 204 No Content입니다.
     */
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);

        // contentService: deleteContent - S3에서 video, image 삭제 (추후 구현)

        return ResponseEntity.noContent().build();
    }

    // 좋아요 토글
    @PostMapping("/like/{lectureId}")
    public ResponseEntity<Void> toggleLike(@PathVariable Long lectureId, Authentication auth) {
        String email = auth.getName();
        lectureService.toggleLike(lectureId, email);
        return ResponseEntity.ok().build();
    }

    // 좋아요 여부 확인 프론트 초기 표시 용도
    @GetMapping("/like/{lectureId}")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long lectureId, Authentication auth) {
        String email = auth.getName();
        boolean liked = lectureService.isLiked(lectureId, email);
        return ResponseEntity.ok(liked);
    }

    // 좋아요 수 조회
    @GetMapping("/like-count/{lectureId}")
    public ResponseEntity<Long> likeCount(@PathVariable Long lectureId) {
        long count = lectureService.getLikeCount(lectureId);
        return ResponseEntity.ok(count);
    }
}
