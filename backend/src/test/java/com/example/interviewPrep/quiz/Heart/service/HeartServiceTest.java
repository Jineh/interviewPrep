package com.example.interviewPrep.quiz.Heart.service;

import com.example.interviewPrep.quiz.domain.Answer;
import com.example.interviewPrep.quiz.domain.Heart;
import com.example.interviewPrep.quiz.dto.HeartRequestDTO;
import com.example.interviewPrep.quiz.exception.AnswerNotFoundException;
import com.example.interviewPrep.quiz.exception.HeartNotFoundException;
import com.example.interviewPrep.quiz.infra.AnswerRepository;
import com.example.interviewPrep.quiz.infra.HeartRepository;
import com.example.interviewPrep.quiz.infra.MemberRepository;
import com.example.interviewPrep.quiz.service.HeartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Rollback(value = false)
public class HeartServiceTest {
    @Autowired
    HeartRepository heartRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    MemberRepository memberRepository;

    HeartService heartService;

    Answer answer;

    @BeforeEach
    void setUp() {
        answer = Answer.builder().build();
        answerRepository.save(answer);
        heartService = new HeartService(heartRepository, answerRepository, memberRepository);
    }


    @Test
    @DisplayName("좋아요 테스트")
    void create() throws InterruptedException {
        Heart savedHeart = heartService.createHeart(HeartRequestDTO.builder().answerId(1L).build());

        verify(heartRepository).save(savedHeart);
    }

    @Test
    @DisplayName("좋아요를 눌렀는데 답변이 없을경우, AnswerNotFoundException가 발생한다.")
    void create_notFoundAnswer_test() {
        assertThrows(AnswerNotFoundException.class, () -> {
            heartService.createHeart(HeartRequestDTO.builder().answerId(-1L).build());
        });
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void delete() throws InterruptedException {
        Heart heart = Heart.builder()
                .id(1L)
                .build();
        when(heartRepository.findById(1L)).thenReturn(Optional.of(heart));

        heartService.deleteHeart(HeartRequestDTO.builder().answerId(1L).build());

        verify(heartRepository).delete(any(Heart.class));
    }

    @Test
    @DisplayName("좋아요 취소를 눌렀는데 좋아요가 없다면, HeartNotFountException가 발생한다.")
    void delete_notFoundHeart_test() {
        assertThrows(HeartNotFoundException.class, () -> {
            heartService.deleteHeart(HeartRequestDTO.builder().answerId(-1L).build());
        });
    }

    @Test
    @DisplayName("하나의 답변에 동시에 좋아요가 눌렸을때, ")
    void concurrency_test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    heartService.createHeart(HeartRequestDTO.builder().answerId(1L).build());
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();

        assertThat(heartRepository.countHeartByAnswerId(answer.getId())).isEqualTo(100);
    }
}
