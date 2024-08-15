# Docker란?

![Untitled](/Docker/img/docker(1).png)

<br>

### Docker란?

> Docker는 컨테이너 기반 오픈소스 가상화 플랫폼이다.

<br>

### 가상화란?

하나의 단일 물리적 컴퓨터의 자원을 분할하여 여러 대의 컴퓨터가 실행되는 것처럼 보이게 하는 기술이다.

<br>


가상화가 등장한 배경을 알아보자

<br>

### 가상화가 등장한 배경

![Untitled](/Docker/img/docker(2).png)

<br>

**Traditional Deployment**

![Untitled](/Docker/img/docker(3).png)

초기의 애플리케이션 배포 환경에서는 여러 애플리케이션이 하나의 컴퓨터와 하나의 운영체제에서 실행되었다. 단일 하드웨어만 관리하면 되기 때문에 간단하지만, 각 애플리케이션에 대한 자원 제한을 정의하고 적용할 수 없다는 문제점이 있다.

하나의 애플리케이션이 갑자기 사용량이 급증하여 CPU와 메모리를 모두 사용해버리면, 같은 서버에서 실행 중인 다른 애플리케이션들은 자원 부족으로 인해 느려지거나 작동을 멈출 수 있다.

이 문제를 해결하기 위해 각 애플리케이션을 개별 하드웨어에 배포하면 되지만, 이는 자원을 낭비하고 비용이 많이 든다. 이러한 한계를 극복하기 위해 가상화 기술이 등장했다.

<br>



**Virtualized Deployment**

![Untitled](/Docker/img/docker(4).png)

가상화는 하나의 물리적 컴퓨터를 여러 가상 머신(VM)으로 나누어 애플리케이션을 독립적으로 실행할 수 있게 한다. 이는 하이퍼바이저가 운영체제(OS) 위에서 실행되어 VM을 생성하고 관리하기 때문에 가능하다.

각 VM은 독립된 OS를 가지므로 애플리케이션이 격리되어 한 애플리케이션이 다른 애플리케이션에 영향을 주지 않는다. 또한, 가상화를 통해 자원 낭비를 방지할 수 있다. 각 VM이 자체 OS를 가지기 때문에 다양한 OS를 실행할 수 있다는 장점들이 있다.

하지만 각 OS는 많은 메모리와 디스크를 필요로 하며, 이로 인해 오버헤드가 발생할 수 있다. 또한, 호스트 OS(기존 OS)와 게스트 OS(VM OS)가 동일하더라도, 각 OS는 별도로 업데이트해야 하므로 자원이 낭비될 수 있다. 이러한 문제를 해결하기 위해 컨테이너 기술이 등장했다.


<br>

**Container Deployment**

![Untitled](/Docker/img/docker(5).png)

컨테이너는 애플리케이션을 실행하는 데 필요한 모든 파일을 패키징하고 격리하는 기술이다. 가상 머신과 달리, 컨테이너는 호스트 OS(기존 OS)를 공유한다. 컨테이너 런타임 엔진이 OS 위에서 실행되어 컨테이너를 생성, 배포 및 관리한다. 컨테이너 런타임 엔진은 각 컨테이너를 격리하고 관리하여 다른 컨테이너와의 간섭을 방지한다.

컨테이너는 애플리케이션과 그 종속성을 패키징하여 동일한 개발 환경을 어디서나 재현할 수 있게한다.  하지만 컨테이너는 호스트 OS와 공유해야 하므로, 반드시 호스트 OS와 같은 종류의 OS를 실행해야한다. 컨테이너 런타임 엔진중 가장 유명한 오픈소스가 바로 **Docker**이다.


<br><br>

### Docker 구성 요소

<br>

**Docker Client**

Docker CLI를 사용해 사용자가 Docker와 상호작용하며, 명령어를 통해 Docker 데몬과 통신하여 이미지를 빌드, 컨테이너를 실행, 이미지를 레지스트리에서 가져오는 작업 등을 수행

<br>

**Image**

Docker 컨테이너를 생성하기 위한 읽기 전용 템플릿이다. 이미지는 소스코드, 라이브러리, 종속성, 도구 등 어플리케이션을 실행하기 위해 필요한 파일을 포함한 불변파일이다. 

자바의 클래스라고 생각하면 이해하기 편하다.

<br>

**Container**

이미지로 부터 생성된 실행 가능한 인스턴스이다. 
각 컨테이너는 독립된 환경에서 애플리케이션을 실행하며, 필요한 라이브러리 및 설정을 포함한다.

마찬가지로 자바의 클래스를 통해 생성된 객체라고 생각하면 편하다.

<br>

**Dockerfile**

Docker 이미지를 빌드하는 방법을 정의하는 스크립트 파일이다.

<br>

**Docker registries**

Docker 이미지를 저장하고 배포하는 저장소이다.  Docker Hub는 누구나 사용할 수 있는 공개 레지스트리이며 Docker는 기본적으로 Docker Hub([https://hub.docker.com/](https://hub.docker.com/)**)**에서 이미지를 찾는다. `docker pull` 을 사용하 registry에서 필요한 이미지를 가져온다.

<br>

**Docker Demon**

Docker 데몬(dockerd)은 Docker API 요청을 수신하고 이미지, 컨테이너 같은 Docker 객체를 관리한다.

<br>


### Docker 아키텍처

![Untitled](/Docker/img/docker(6).png)

Docker 아키텍처는 클라이언트-서버 모델을 기반으로 하며, 구성 요소들은 서로 상호 작용하여 컨테이너 기반 애플리케이션의 개발, 배포 및 관리를 가능하게 한다.

- **클라이언트 명령어 실행**
    - 사용자가 Docker CLI에서 `docker run`, `docker build`, `docker pull` 등의 명령어를 실행한다.
- **Docker 데몬과 통신**
    - Docker CLI는 Docker 데몬과 통신하여 명령어를 전달한다. Docker 데몬은 이를 처리하여 적절한 작업을 수행한다.
- **이미지 빌드 및 실행**
    - `docker build` 명령어는 Dockerfile을 사용하여 이미지를 빌드한다.
    - `docker run` 명령어는 빌드된 이미지를 기반으로 컨테이너를 시작한다.
- **이미지 다운로드 및 업로드**
    - `docker pull` 명령어는 Docker 레지스트리로부터 이미지를 다운로드한다.
    - `docker push` 명령어는 이미지를 Docker 레지스트리에 업로드한다.
- **컨테이너 관리**
    - Docker 데몬은 이미지를 사용하여 컨테이너를 생성하고, 시작, 중지, 삭제 등의 작업을 수행한다.
- **레지스트리와의 상호작용**
    - Docker 데몬은 이미지를 다운로드하거나 업로드하기 위해 Docker 레지스트리와 통신한다.


<br><br><br><br><br>

### Reference

[https://docs.docker.com/guides/docker-overview/#docker-objects](https://docs.docker.com/guides/docker-overview/#docker-objects)
[https://kubernetes.io/ko/docs/concepts/overview/](https://kubernetes.io/ko/docs/concepts/overview/)