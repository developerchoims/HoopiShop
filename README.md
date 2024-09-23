> # 공예품 판매 B2C 사이트 #

# 개인 간의 금 구매, 판매를 지원하는 C2C 서비스입니다. <br/>
## 구현 중이며 배포되어 있습니다. 회원가입 후 이용 가능합니다. (로그인, 회원가입, 상품 및 상품 상세 구현 완료)
## http://hoopi.p-e.kr/

----------------------------------------------------------

# 프로젝트 구조 간단 요약 :star2:

### 공예품 판매 B2C 서비스를 제공합니다.
- hoopi : 백(JAVA), hoopifront : 프론트(React)
- JWT Token 인증을 이용한 로그인 서비스를 제공합니다.
- 소비자는 로그인하지 않으면 상품 가격을 확인할 수 없습니다.
<br/><br/><br/><br/>

# 사용 기술
<div>
    <h2>Technology Stack</h2>
    <section>
        <h3>Backend</h3>
        <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=IntelliJ-&logoColor=white" alt="IntelliJ">
        <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot-&logoColor=white" alt="Spring Boot">
        <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA-&logoColor=white" alt="JPA">
        <img src="https://img.shields.io/badge/Java-4B4B77?style=for-the-badge&logo=Java-&logoColor=white" alt="Java">
    </section>
    <section>
        <h3>Database</h3>
        <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL-&logoColor=white" alt="MySQL">
        <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis-&logoColor=white" alt="Redis">
    </section>
    <section>
        <h3>Cloud</h3>
        <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS-&logoColor=white" alt="Amazon RDS">
        <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3-&logoColor=white" alt="Amazon S3">
        <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2-&logoColor=white" alt="Amazon EC2">
        <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=github actions-&logoColor=white" alt="GitHub Actions">
    </section>
    <section>
        <h3>Frontend</h3>
        <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React-&logoColor=white" alt="React">
    </section>
    <section>
        <h3>Collaboration and Testing Tools</h3>
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub-&logoColor=white" alt="GitHub">
        <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white" alt="Postman">
    </section>
</div>


<br/><br/><br/><br/>
# DB Diagram을 활용한 데이터 모델링 :card_index_dividers:
 ![image](https://github.com/user-attachments/assets/3a10a777-eab6-480d-b028-1e4f79dbc2db)


# 구현 기능
## 공통 구현 ##
- [x] JWT Token 인증(cookie, redis 이용)
- [x] 아이디/비밀번호 exception 다르게 처리
- [x] 동적 배너 및 검색 카테고리 구현
- [ ] 장기 휴면 고객 처리


## 회원 페이지 구현 ##
### 메인 페이지 
- [x] 신상품 컨테이너
- [x] 인기상품 컨테이너

### 회원 페이지
- [ ] 회원 정보 메인 페이지
- [ ] 회원 정보 수정 페이지(핸드폰 번호, 이메일 수정)
- [ ] 배송지 수정 및 추가 삭제 페이지
- [ ] 주문 내역 확인 페이지
- [ ] 상제 주문 내역 확인 페이지
- [ ] 탈퇴 버튼
      
### 상품 페이지
- [x] 상품 메인
- [x] 상품 디테일

### 장바구니
- [ ] 장바구니 메인

### 주문
- [ ] 주문 메인
- [ ] 결제창
- [ ] 결제 완료 or 실패 페이지

### 공지
- [ ] 공지 메인
- [ ] 공지 상세


## 관리자 페이지 구현 ##
### 회원 관리 페이지
- [x] 회원 페이지
- [x] 회원 디테일 페이지
- [x] 회원 탈퇴 처리

### 상품 관리 페이지
- [x] 상품 메인(회원 동일)
- [x] 상품 디테일(회원 동일)
- [x] 상품 추가 페이지
- [ ] 상품 삭제, 수정 버튼

### 주문 관리 페이지
- [ ] 주문 메인
- [ ] 주문 상세
- [ ] 주문 상태 변경

### 공지 페이지
- [ ] 공지 메인(회원 동일)
- [ ] 공지 상세(회원 동일)
- [x] 공지 추가 페이지
- [ ] 공지 삭제, 수정 버튼
