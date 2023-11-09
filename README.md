## Branch strategy

### Git Flow

```bash
# Branch명
master : 운영 서버로 배포하기 위한 branch
develop : 안정되고 배포 가능한 branch
FE/develop : frontend develop
BE/develop : backend develop
{포지션}/feat/{기능}#{이슈 번호} : 기능 개발을 위한 branch
{포지션}/refactor/{기능}#{이슈 번호} : 리팩토링 branch
{포지션}/hotfix : dev 브랜치에서 발생한 버그를 수정하는 branch

// 예시
BE/feat/user#1

# Pull Request명
브랜치명
# Pull Request 내용
템플릿
```

- **New Issues**
    - Title : `[BE] 회원 도메인 생성`
    - Description : 템플릿 사용
    - Assignee : 담당자 지정
    - **Create Issues**
    - 생기는 이슈 번호를 이용해서 브랜치 생성

## Commit convention
- feat: 새로운 기능 추가 또는 기능 관련

- fix: 버그 수정

- perf: 성능 개선을 위한 변경

- refactor: 기능 추가, 버그 수정의 목적이 아닌 코드 리팩토링을 담은 변경

- docs: 마크 다운 작성, 주석 작성 등의 문서 작업

- style: 코드의 의미를 변경하지 않는 formatting 등의 변경

- test: 테스트 관리를 위한 변경

- ci: CI를 위한 변경

- build: 빌드 설정, 개발툴 변경 등 사용자와 관련 없는 변경

- chore: 소스 파일 혹은 테스트 파일의 변화가 없는 단순 작업

- revert: 이전 커밋 취소
