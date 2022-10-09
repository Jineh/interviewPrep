import React from 'react';
import styled from 'styled-components';

import Problem from './Problem';
const Test = () => {
  const problems = [
    ['네트워크', 'TCP와 UDP의 차이', '상', '35명'],
    ['자바스크립트', '호이스팅', '상', '10명'],
    ['네트워크', '3Way-handshaking', '하', '5명'],
    ['자바스크립트', '클러져', '중', '5명'],
    ['인성문제', '당신의 장단점은', '중', '15명'],
    ['React', '가상 돔을 쓰는 이유', '상', '5명'],
    ['네트워크', 'OSI 7계층을 설명하시오', '상', '5명'],
  ];

  return (
    <Container>
      {problems.map((problem, index) => {
        return <Problem key={index} problem={problem} />;
      })}
    </Container>
  );
};

export default Test;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  text-align: center;
`;
