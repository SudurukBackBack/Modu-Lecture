package com.sudurukbackback.modulecture.domain.search.controller;

import com.sudurukbackback.modulecture.domain.search.document.LectureDocument;
import com.sudurukbackback.modulecture.domain.search.service.LectureSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search/lectures")
@RequiredArgsConstructor
public class LectureSearchController {

    private final LectureSearchService lectureSearchService;

    @GetMapping
    public ResponseEntity<List<LectureDocument>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(lectureSearchService.search(keyword));
    }
}
