package com.example.interviewPrep.quiz.service;

import com.example.interviewPrep.quiz.domain.Question;
import com.example.interviewPrep.quiz.dto.FilterDTO;
import com.example.interviewPrep.quiz.repository.FilterRepository;
import com.example.interviewPrep.quiz.repository.QuestionRepository;
import com.example.interviewPrep.quiz.dto.QuestionDTO;
import com.example.interviewPrep.quiz.exception.QuestionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FilterRepository filterRepository;

    public List<Question> getQuestions() {return questionRepository.findAll();}


    //@Cacheable(value = "question", key="#id")
    public QuestionDTO getQuestion(Long id) {
        Question question = questionRepository.findById(id).get();

        return QuestionDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .type(question.getType())
                .build();
    }


    public Question createQuestion(QuestionDTO questionDTO){
        Question question = Question.builder()
                        .id(questionDTO.getId())
                        .title(questionDTO.getTitle())
                        .type(questionDTO.getType())
                        .build();
        questionRepository.save(question);
        return question;
    }

    public Question updateQuestion(Long id, QuestionDTO questionDTO){
        Question question = findQuestion(id);
        question.change(questionDTO.getTitle(), questionDTO.getType());
        return question;
    }

    public Question deleteQuestion(Long id){
        Question question = findQuestion(id);
        questionRepository.delete(question);
        return question;
    }

    public List<Question> findQuestionsByType(String type){
        return questionRepository.findByType(type);
    }


    //@Cacheable(value = "questionDTO", key="#pageable.pageSize.toString().concat('-').concat(#pageable.pageNumber)")
    public Page<QuestionDTO> findByType(String type, Pageable pageable){
        Page<Question> questions;
        if(type==null) questions = questionRepository.findAllBy(pageable);
        else questions = questionRepository.findByType(type, pageable); //문제 타입과 페이지 조건 값을 보내어 question 조회, 반환값 page

        return questions.map(q -> QuestionDTO.builder()   //question list 값들을 dto로 변경
                .id(q.getId())
                .type(q.getType())
                .title(q.getTitle())
                .build());
    }


    public Question findQuestion(Long id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }


    public List<FilterDTO> findFilterLanguage(){
        List<FilterDTO> filterDTOS = new ArrayList<>();

        List<String> languages = filterRepository.findAllByLanguage();

        for(String language: languages){
            FilterDTO filterDTO = FilterDTO.builder()
                    .language(language)
                    .build();
            filterDTOS.add(filterDTO);
        }

        return filterDTOS;
    }


}
