# wooajeong - Backend

<p align="center">
  <img width="240" alt="아이콘" src="https://github.com/user-attachments/assets/9d048ee2-8883-4d74-b64a-c14c8db15d3f">
  
</p>

<p align="center">
  <strong>
    블록체인 기반 안심 중고거래 AI 플랫폼
  </strong>
</p>
<br>

## 🔍 Introduction
### 1. 서비스 목표
개인 간 중고 거래에서는 사기, 허위 광고, 상품 미수령 등으로 인한 피해가 빈번하게 발생하고, 거래 상대방의 신뢰성을 확인하기 어렵습니다. <br>
또한 시세 정보가 불투명하여 합리적인 가격 판단이 어렵고, 대부분 사용자가 현장 직거래만 선호하면서 거래 편의성이 제한됩니다. <br>
이러한 구조적 한계로 인해 안전하고 편리한 중고 거래 환경에 대한 수요가 지속적으로 증가하고 있습니다.

### 2. 기능 및 특징

본 서비스는 블록체인과 MCP(Model Context Protocol) 기반 기술을 활용하여 안전하고 편리한 중고 거래 환경을 제공합니다.  
주요 기능은 다음과 같습니다.

1. **중고 매물 검색 (MCP 기반)**  
   - MCP를 활용하여 상품 시세와 사용자의 선호를 고려한 맞춤형 검색 제공
   - 시세 및 가격 비교 정보를 함께 제공하여 합리적인 거래 지원

2. **관심 매물 알림 (MCP 기반)**  
   - 사용자가 관심 있는 상품 등록 시, MCP 분석을 통해 적절한 시점에 알림 제공
   - 빠른 거래 기회를 놓치지 않도록 지원

3. **블록체인 기반 에스크로 시스템**  
   - 거래 대금과 거래 기록을 블록체인에 안전하게 저장
   - 거래 완료 시점까지 위변조가 불가능하여 사기 위험 최소화

4. **실시간 채팅**  
   - 매물 구매자와 판매자 사이의 안전한 소통 지원


## 📹 Demo



## 🛠️ Tech Stack
Backend|Security|Database|Deployment|Other|
|:------:|:------:|:------:|:------:|:------:|
|<img src="https://smartcart-s3-bucket.s3.ap-northeast-2.amazonaws.com/badge_SpringBoot.svg" alt="[ Spring Boot ]"/><br><img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/><br><img src="https://img.shields.io/badge/modelcontextprotocol-000000?style=flat-square&logo=modelcontextprotocol&logoColor=white"/><br><img src="https://smartcart-s3-bucket.s3.ap-northeast-2.amazonaws.com/badge_STOMP.svg" alt="STOMP"/><br><img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=flat-square&logo=RabbitMQ&logoColor=white"/>|<img src="https://smartcart-s3-bucket.s3.ap-northeast-2.amazonaws.com/badge_SpringSecurity.svg" alt="[ Spring Security ]"/><br><img src="https://smartcart-s3-bucket.s3.ap-northeast-2.amazonaws.com/badge_JSONWebToken.svg" alt="[ JSON Web Token ]"/><br><img src="https://img.shields.io/badge/OAuth2-3423A6?style=flat-square&logo=Authelia&logoColor=white"/>|<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/><br><img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"/>|<img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=AmazonAWS&logoColor=white"/><br><img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/><br><img src="https://smartcart-s3-bucket.s3.ap-northeast-2.amazonaws.com/badge_GithubActions.svg" alt="[ Github Actions ]"/>|<img src="https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=OpenAI&logoColor=white"/>
```
- Backend : Spring Boot, Java, MCP
- Security : Spring Security, JWT, OAuth2
- Database : MySQL, MongoDB
- Deployment : AWS, Docker, Github Actions
- External API : OpenAI API
```


## 💻 Architecture
<img width="2880" height="1619" alt="시스템아키텍처" src="https://github.com/user-attachments/assets/e1143a8c-b9b1-4559-a1dd-056d4b0c700b" />

<!--
## 📕 API 
<img width="977" height="797" alt="image" src="https://github.com/user-attachments/assets/902a14f2-8c99-44ea-a243-996ed685d802" />
-->

## 🗂️ Database
<img width="1090" height="376" alt="image" src="https://github.com/user-attachments/assets/3e9aa2b3-a98f-4737-af41-27ece481f10e" />
