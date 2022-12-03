package com.example.interviewPrep.quiz.answer.service;


import com.example.interviewPrep.quiz.answer.dto.SolutionDTO;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.heart.repository.HeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final HeartRepository heartRepository;

    public Answer createAnswer(AnswerDTO answerDTO){

        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId).get();
        Question question = questionRepository.findById(answerDTO.getQuestionId()).get();

        Answer answer =  Answer.builder()
                .member(member)
                .question(question)
                .content(answerDTO.getContent())
                .build();

        answerRepository.save(answer);
        return answer;
    }

    public AnswerDTO readAnswer(Long id){

        Answer answer = answerRepository.findById(id).get();

        return AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionId(answer.getQuestion().getId())
                .build();
    }


    public Answer deleteAnswer(Long id){

        Answer answer = answerRepository.findById(id).get();

        answerRepository.delete(answer);
        return answer;

    }


    public Page<SolutionDTO> getSolution(Long id, String type, Pageable pageable){

        Long memberId = JwtUtil.getMemberId();
        Page<Answer> answers;

        if(type.equals("my")) {
            answers = answerRepository.findMySolution(id, memberId, pageable);
            if(answers.getContent().isEmpty()) throw new CommonException(ErrorCode.NOT_FOUND_ANSWER);
            return makeSolutionDto(answers.getContent(), new ArrayList<>());
        }
        else {
            answers = answerRepository.findSolution(id, memberId, pageable);
            if(answers.getContent().isEmpty()) throw new CommonException(ErrorCode.NOT_FOUND_ANSWER);

            List<Long> aList = answers.getContent().stream().map(Answer::getId).collect(Collectors.toList());
            List<Long> myHeart = heartRepository.findMyHeart(aList, memberId);
            return makeSolutionDto(answers.getContent(), myHeart);
        }
    }


    public Page<SolutionDTO> makeSolutionDto(List<Answer> answers, List<Long> myHeart){
        List<SolutionDTO> solutionDTOS = new ArrayList<>();
        for(Answer a : answers){
            boolean status = myHeart.contains(a.getId());
            SolutionDTO solutionDTO = SolutionDTO.builder()
                    .answerId(a.getId())
                    .answer(a.getContent())
                    .heartCnt(a.getHeartCnt())
                    .name(a.getMember().getName())
                    .heart(status)
                    .build();
            solutionDTOS.add(solutionDTO);
        }

        return new PageImpl<>(solutionDTOS);
    }

}