package com.example.interviewPrep.quiz.Member.repository;

import com.example.interviewPrep.quiz.domain.Member;
import com.example.interviewPrep.quiz.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;


    Member member;
    String email;
    String password;
    String type;

    @BeforeEach
    void setUp(){

        // Given
        email = "hello@gmail.com";
        password = "1234";
        type = "google";

        member = Member.builder()
                .email(email)
                .password(password)
                .type(type)
                .build();

    }


    @Test
    @DisplayName("회원을 DB에 저장")
    public void save(){

        // When
        memberRepository.save(member);

        // Then
        String savedEmail = member.getEmail();
        assertEquals(member, memberRepository.findByEmailAndType(savedEmail,type).get());

    }

    /*
    @Test
    @DisplayName("Email로 회원 찾기")
    public void findByEmail(){

        // When
        memberRepository.save(member);
        List<Member> searchedMembers = memberRepository.findByEmail(email);

        // Then
        assertThat(email).isEqualTo(searchedMembers.get(0).getEmail());
    }

     */


}
