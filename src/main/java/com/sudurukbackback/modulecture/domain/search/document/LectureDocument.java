package com.sudurukbackback.modulecture.domain.search.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "lectures")
public class LectureDocument {
    @Id
    private Long id;
    private String title;
    private String description;
    private String instructor;
    private String category;
}
