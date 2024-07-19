# Git Branch 전략

---
<br>

Git Branch란 브랜치 생성에 규칙을 만들어서 협업을 유연하게 하는 방법론이다.

그중 가장 많이 사용되는 전략은 Git Flow와 Github Flow가 있다.

<br>

```
💡Git과 Github의 차이
Git은 분산 버전 관리 시스템이고 ,GitHub는 Git 저장소를 호스팅하는 웹 기반 서비스이다.
Git은 origin 저장소를 복사한 remote 저장소와 CLI를 통한 명령어 기반으로 작동, 브랜치 병합 기능 같은 특징을 가지고있다.
Github는 Git을 기반으로한 협업 서비스이고 버그를 추적하는 Issues, 리뷰 및 병합을 요청하는 Pull Request,CI/CD 를 자동화하는
Action, 소셜 코딩 플랫폼으로 협업 지원 같은 특징을 가지고있다.
```


<br>

### Git Flow

![Untitled](/Git/img/git_flow.png)

Git Flow는 대규모 프로젝트를 관리하기 위해 체계적인 접근 방식을 제공하는 복잡한 모델이다.
<br>

Git Flow는 5가지 브랜치가 존재한다.

항상 유지되는 **메인 브랜치(master, develop)** 과 일정 기간 동안만 유지되는 **보조 브랜치들(feature, release, hotfix)** 이 있다.

- `master`: 제품으로 출시될 수 있는 브랜치
- `develop`: 다음 출시 버전을 개발하는 브랜치
- `feature`: 기능을 개발하는 브랜치, 완료가 되면 삭제
- `release`: 이번 출시 버전을 준비하는 브랜치
- `hotfix`: 출시 버전에서 발생한 버그를 수정하는 브랜치

<br>

**새로운 기능 개발 시**

1. 기능 개발을 위한 `feature`브랜치는 `develop`브랜치에서 분기된다.
2. 기능 개발이 완료되면 `develop` 브랜티에 병합한다.

<br>

**다음 버전 출시 준비**

1. 다음 버전 출시 준비를 위한 `release` 브랜치는 `develop` 브랜치에서 분기된다.
2. `release` 브랜치에서 발견된 버그는 `release` 브랜치에 반영한다.
3. 출시 준비가 완료되면 `release` 브랜치는 `master` 브랜치와 `develop` 브랜치에 병합된다.
4. `master` 브랜치에 병합될때 태그가 추가된다.

<br>

**운영중 버그 발생 시**

1. 운영중에 버그가 발생하면 `master` 브랜치에서 `hotfix` 브랜치를 분기시킨다.
2. 버그가 수정이 완료가 되면 `hotfix` 브랜치는 `master` 브랜치와 `develop` 브랜치에 병합된다.
3. `master`브랜치에 병합될때 새로운 태그가 추가된다.


<br><br><br>

### Github Flow

![Untitled](/Git/img/github_flow.png)

복잡하게 구조화되어있는 Git Flow를 대신해서 나온 Github Flow이다.


Github Flow 전략은 간단하고 유연한 개발 전략이다. 

Gihub Flow에는 2가지의 브랜치만이 존재한다.

- `master`: 항상 배포 가능한 상태를 유지하며, 안정적인 코드가 포함된 브랜치
- `feature`: 새로운 기능이나 버그 수정을 작업하는 브랜치. 작업이 완료되면 `master` 브랜치에 병합되고 삭제된다.

<br>

**새로운 기능 개발 시**

1. 기능 개발을 위한 `feature` 브랜치는 `master` 브랜치에서 분기된다.
2. 개발자들은 `feature` 브랜치에서 작업을 진행하며, 지속적으로 코드를 커밋한다.
3. 기능 개발이 완료되면 `master` 브랜치에 병합 요청(Pull Request)을 한다.
4. 코드 리뷰와 자동화된 테스트를 거쳐, `feature` 브랜치가 `master` 브랜치에 병합됩니다.

<br>

**코드 리뷰와 병합**

1. Pull Request가 제출되면 팀원들이 코드 리뷰를 진행한다.
2. 필요한 경우 추가 수정 작업을 수행한다.
3. 코드 리뷰가 통과되고 모든 테스트가 성공하면, `feature` 브랜치가 `master` 브랜치에 병합된다.
4. 병합이 완료되면 `feature` 브랜치는 삭제된다.

<br>

**배포**

1. `master` 브랜치의 최신 코드는 항상 배포 가능한 상태를 유지해야한다..
2. 새로운 기능이 `master` 브랜치에 병합되면, 자동으로 배포 파이프라인을 통해 프로덕션 환경에 배포된다.


<br><br><br>


### Git Flow VS Github Flow

Git Flow는 체계적인 릴리스 관리가 필요한 대규모 프로젝트에 적합하며, GitHub Flow는 빠른 개발과 빈번한 배포가 필요한 소규모 프로젝트에 적합합니다.

<br><br><br><br><br>

### Reference

[https://nvie.com/posts/a-successful-git-branching-model/](https://nvie.com/posts/a-successful-git-branching-model/)
[https://quangnguyennd.medium.com/git-flow-vs-github-flow-620c922b2cbd](https://quangnguyennd.medium.com/git-flow-vs-github-flow-620c922b2cbd)
