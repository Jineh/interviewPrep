package com.example.interviewPrep.quiz.Question.controller;

import com.example.interviewPrep.quiz.controller.QuestionController;
import com.example.interviewPrep.quiz.dto.QuestionDTO;
import com.example.interviewPrep.quiz.repository.QuestionJpaRepository;
import com.example.interviewPrep.quiz.service.QuestionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionReadWebControllerTest {

    @MockBean
    QuestionService questionService;

    @MockBean
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    MockMvc mockMvc;

    QuestionDTO questionDTO;
    List<QuestionDTO> questionDTOS;
    String jsonRequest;
    Pageable pageable;

    @BeforeEach
    void setUp() throws Exception{
        questionDTOS = new ArrayList<>();

        for(int i = 1; i<11; i++) {
            questionDTO = QuestionDTO.builder()
                    .title("problem1")
                    .id(Long.valueOf(i))
                    .type("java")
                    .build();
            questionDTOS.add(questionDTO);
        }

        when(questionService.findById(10L)).thenReturn(Optional.ofNullable((questionDTO)));


        pageable = PageRequest.of(0, 10);
        Page<QuestionDTO> questions = new PageImpl<>(questionDTOS);
        when(questionService.findByType("java", pageable)).thenReturn(Optional.of(questions));


    }




    @Test
    @DisplayName("Question valid type 조회")
    void findByValidType() throws Exception{
        //given
        String type ="java";

        //when
        mockMvc.perform(get("/question/"+type)
                .param("page", "0"))

        //then
        .andDo(print())
                .andExpect(status().isOk());

        verify(questionService).findByType(type, pageable);
    }


    @Test
    @DisplayName("Question invalid type 조회")
    void findByInvalidType() throws Exception{
        //given
        String type ="c++";

        //when
        mockMvc.perform(get("/question/"+type)
                .param("page", "0"))

                //then
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(questionService).findByType(type, pageable);
    }



    @Test
    @DisplayName("Question valid id 조회")
    void findByValidIdType() throws Exception{
        //given
        Long id = 10L;

        //when
        mockMvc.perform(get("/question/single/"+id))

        //then
                .andDo(print())
                .andExpect(status().isOk());

        verify(questionService).findById(id);

    }


    @Test
    @DisplayName("Question invalid id 조회")
    void findByInvalidIdType() throws Exception{
        //given
        Long id = 11L;

        //when
        mockMvc.perform(get("/question/single/"+id))

                //then
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(questionService).findById(id);

    }


}