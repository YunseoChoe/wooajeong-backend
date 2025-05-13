package com.example.capstone.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    private String user_id;
    private String keyword;
    private String location;
    private String min_price;
    private String max_price;
}
