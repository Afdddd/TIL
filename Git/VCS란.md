# VCS이란

<br>

### VCS란?
<br>

> Version Controll System의 약자

<br>

버전 관리 시스템은 파일 변화를 시간 시점에 따라 기록했다가 나중에 특정 시점의 버전을 다시 꺼내올 수 있는 시스템이다.


<br><br>

### VCS의 장점

<br>

1. **이전 상태로 되돌리기**: 각 파일을 이전 상태로 되돌릴 수 있다. 실수로 파일을 잘못 수정했을 때 매우 유용하다.
2. **프로젝트 전체 복구**: 프로젝트를 통째로 이전 상태로 되돌릴 수 있어, 대규모 변경 후 문제가 발생했을 때 쉽게 복구할 수 있다.
3. **수정 내용 비교**: 시간에 따라 수정 내용을 비교해 볼 수 있어, 어떤 부분이 어떻게 변경되었는지 추적할 수 있다.
4. **문제 추적**: 누가 문제를 일으켰는지 추적할 수 있어, 문제 해결과 책임 소재를 명확히 할 수 있다.
5. **이슈 기록**: 누가 언제 만들어낸 이슈인지 알 수 있어, 이슈 관리가 용이해진다.
6. **복구 용이성**: 파일을 잃어버리거나 잘못 고쳤을 때 쉽게 복구할 수 있어, 데이터 손실의 위험을 최소화할 수 있다.

<br><br>

### VCS의 역사

<br>

**로컬 버전 관리**

![Untitled](/Git/img/Local_VCS.png)

초창기 VCS.

아주 간단한 데이터베이스를 사용해서 파일의 변경 정보를 관리했다.

로컬 컴퓨터에서 작업했기 때문에 협업이 불가능하다는 문제가 있다.

```java
💡 Checkout?
버전 관리 시스템에서 특정 파일의 버전을 로컬 컴퓨터로 가져오는 것을 의미한다.

💡 Checkout의 과정
1. 버전 선택: 사용자는 버전 데이터베이스에서 필요한 파일의 특정 버전을 선택한다.
2. 파일 복사: 선택한 버전의 파일이 로컬 컴퓨터로 복사된다.
3. 로컬 작업: 사용자는 로컬 컴퓨터에서 해당 파일을 편집하거나 수정할 수 있다.
```

<br><br>

**중앙집중식 버전 관리(CVCS)**

Central Version Control System

![Untitled](/Git/img/CVCS.png)

로컬 버전 관리의 협업이 불가능하다는 문제를 해결하기위해 CVCS(중앙집중식)이 개발됐다.

파일을 관리하는 서버가 따로있고 클라이언트가 중앙 서버에서 파일을 받아서 사용(Checkout)하는 방식이다.

모든 클라이언트의 로컬 CVS를 관리하는 것보다 중앙에서 하나의 VCS만 관리하기 때문에 관리가 용이하다는 장점이 있다.

하지만 중앙 서버가 다운이 되면 CVS기능을 사용 못하는다는 단점이 있다.


<br><br>

**분산 버전 관리 시스템(DVCS)**

Distributed Version Control System

![Untitled](/Git/img/DVCS.png)

우리가 사용하는 Git이 DVCS이다.

DVCS에서의 클라이언트는 단순히 파일의 마지막 스냅샷을 Checkout 하지 않는다.  대신, 클라이언트는 저장소 전체를 복제한다. 각 클라이언트가 저장소의 전체 기록을 가지고 있기 때문에 서버에 문제가 생기면 이 복제물로 다시 작업을 시작할 수 있으며, 클라이언트 중 아무거나 골라도 서버를 복원할 수 있다.

<br><br><br><br><br>

### Reference

[https://git-scm.com/book/ko/v2/시작하기-버전-관리란%3F#ch01-getting-started](https://git-scm.com/book/ko/v2/%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0-%EB%B2%84%EC%A0%84-%EA%B4%80%EB%A6%AC%EB%9E%80%3F#ch01-getting-started)