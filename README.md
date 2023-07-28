## Tech Stack

<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Java-137CBD?style=flat-square&logo=Java&logoColor=white"/>

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=AmazonAWS&logoColor=white"/>

## Backend Developers

| 박현정 | 김태오 |
| :---------:|:----------:|
|<img width="300" alt="image" src="https://user-images.githubusercontent.com/81394850/210358708-f6139bed-c2b6-43d9-8dc6-525ac8c68e9f.jpg"> | <img width="300" alt="image" src="https://github.com/codingbongbongz/Backend/assets/81394850/e593d345-2bc7-4b41-ae77-fea9157376fe"> | 
| [hyeonjeongs](https://github.com/hyeonjeongs) | [ysbc1247](https://github.com/ysbc1247) |


## Convention

### ✔️ Commit Convention

- ✅ `[chore]` : 동작에 영향 없는 코드 or 변경 없는 변경사항(주석 추가 등)
- ✨ `[feat]` : 새로운 기능 구현
- ➕ `[add]` : Feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성
- 🔨 `[fix]` : 버그, 오류 해결
- ⚰️ `[del]` : 쓸모없는 코드 삭제
- 📝 `[docs]` : README나 WIKI 등의 문서 수정
- ✏️ `[correct]` : 주로 문법의 오류나 타입의 변경, 이름 변경시
- ⏪️ `[rename]` : 파일 이름 변경시
- ♻️ `[refactor]` : 전면 수정
- 🔀 `[merge]`: 다른 브랜치와 병합

ex) `commit -m "{#issue number} [feat] user API 구현”`

### ✔️ Branch Convention

- [feat] : 기능 추가
- [fix] : 에러 수정, 버그 수정
- [docs] : README, 문서
- [refactor] : 코드 리펙토링 (기능 변경 없이 코드만 수정할 때)
- [modify] : 코드 수정 (기능의 변화가 있을 때)
- [chore] : gradle 세팅, 위의 것 이외에 거의 모든 것

ex) `feat/#1-user-api`

### Git Flow

기본적으로 Git Flow 전략을 이용한다. Fork한 후 나의 repository에서 작업하고 구현 후 원본 repository에 pr을 날린다. 작업 시작 시 선행되어야 할 작업은 다음과 같다.

```java
1. Issue를 생성한다.
2. feature Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
4. Pull Request가 작성되면 작성자 이외의 다른 팀원이 Code Review를 한다.
5. Code Review가 완료되면 Pull Request 작성자가 develop Branch로 merge 한다.
6. merge된 작업이 있을 경우, 다른 브랜치에서 작업을 진행 중이던 개발자는 본인의 브랜치로 merge된 작업을 Pull 받아온다.
7. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.
```

- 기본적으로 git flow 전략을 사용합니다.
- main, develop, feature 3가지 branch 를 기본으로 합니다.
- main → develop → feature. feature 브랜치는 feat/기능명으로 사용합니다.
- 이슈를 사용하는 경우 브랜치명을 feature/[issue num]-[feature name]로 합니다.
