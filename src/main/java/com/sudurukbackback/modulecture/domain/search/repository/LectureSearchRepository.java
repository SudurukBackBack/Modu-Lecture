package com.sudurukbackback.modulecture.domain.search.repository;

import com.sudurukbackback.modulecture.domain.search.document.LectureDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LectureSearchRepository extends ElasticsearchRepository<LectureDocument, Long> {
    List<LectureDocument> findByTitleContaining(String keyword);
}