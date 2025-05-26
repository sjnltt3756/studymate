# 📘 StudyMate - 스터디 분석 플랫폼

> 사용자별 공부 기록을 분석하고 시각화하여 **자기 주도 학습을 돕는 백엔드 중심 프로젝트**

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.4.5-success?logo=spring-boot)
![Security](https://img.shields.io/badge/Security-JWT-green?logo=jwt)
![MySQL](https://img.shields.io/badge/DB-MySQL-orange?logo=mysql)
![Swagger](https://img.shields.io/badge/Docs-Swagger-85EA2D?logo=swagger)

---

## 🔧 기술 스택

| 구분       | 내용                          |
|------------|-------------------------------|
| Language   | Java 17                       |
| Framework  | Spring Boot 3.4.5             |
| ORM        | JPA (Hibernate)               |
| Database   | MySQL                         |
| Auth       | Spring Security + JWT         |
| Docs       | Swagger 3 (springdoc-openapi) |
| Test       | JUnit5, MockMvc, SpringBootTest |
| 기타       | OpenAI API, Git Submodule 관리 |

---

## 🧠 주요 기능

### ✅ 사용자 인증
- 회원가입 / 로그인 (JWT 발급)
- 비밀번호 암호화 및 로그인 검증

### 📚 공부 기록 관리
- 공부 시작/종료 시간, 태그, 회고 메모 저장
- 날짜별 회고 조회, 회고 수정
- OpenAI 기반 회고 요약 기능

### 🏷️ 태그 관리
- 태그 생성 / 조회 / 삭제

### 📊 통계 분석
- **일간 통계**: 시간대별 공부량 (0~23시)
- **주간 통계**: 요일별 공부량 (월~일 고정)
- **태그별 통계**: 태그 누적 공부 시간

---

## 🛡 예외 및 응답 처리

### ✅ 공통 응답 구조
```json
{
  "success": true,
  "data": ...,
  "message": "요청 성공"
}
