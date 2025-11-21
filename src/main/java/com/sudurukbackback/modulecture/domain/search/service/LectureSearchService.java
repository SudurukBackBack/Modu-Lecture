package com.sudurukbackback.modulecture.domain.search.service;

import com.sudurukbackback.modulecture.domain.lecture.entity.Lecture;
import com.sudurukbackback.modulecture.domain.search.document.LectureDocument;
import com.sudurukbackback.modulecture.domain.search.repository.LectureSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureSearchService {

    private final LectureSearchRepository lectureSearchRepository;

    public List<LectureDocument> search(String keyword) {
        return lectureSearchRepository.findByTitleContaining(keyword);
    }

    public void save(Lecture lecture) {
        LectureDocument doc = LectureDocument.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .build();

        lectureSearchRepository.save(doc);
    }

    public void delete(Long id) {
        lectureSearchRepository.deleteById(id);
    }
}
