package com.example.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "query_content") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery {
    @Id
    private String id;  // MongoDB가 자동 생성할 _id (ObjectId)
    private String user_id; // 사용자 id
    private String keyword; // 제품 종류
    private String location; // 장소
    private String brand; // 브랜드
    private String min_price; // 가격 최소값
    private String max_price; // 가격 최대값
    private String query; // 문장
}
