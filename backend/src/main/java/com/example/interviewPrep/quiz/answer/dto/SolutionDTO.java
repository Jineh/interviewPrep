package com.example.interviewPrep.quiz.answer.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionDTO {

    private Long answerId;
    private String answer;
    private String name;
    private int heartCnt;
    private boolean heart;

}