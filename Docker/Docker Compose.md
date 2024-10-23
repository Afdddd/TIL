# Docker compose

![image.png](/Docker/img/compose.png)

**Docker Compose**는 여러 개의 Docker 컨테이너를 손쉽게 정의하고 함께 실행할 수 있는 툴이다.
각 컨테이너는 애플리케이션의 일부(예: 데이터베이스, 웹 서버, 백엔드 등)를 담당하며, Compose를 사용하면 이 모든 컨테이너를 하나의 설정 파일(`docker-compose.yml`)에서 관리할 수 있다.

Docker Compose는 **개발 환경**에서 여러 컨테이너 기반 애플리케이션을 빠르고 효율적으로 배포하고, **로컬 환경에서 테스트**할 때 특히 유용하다.

<br><br>

### Docker Compose의 주요 개념

- **Service** : Compose 파일에서 정의된 각 컨테이너를 "서비스"라고 부른다. 각 서비스는 특정한 Docker 이미지와 실행 옵션을 설정할 수 있다.
- **Network** : Docker Compose는 각 서비스 간의 통신을 자동으로 설정해 준다. 기본적으로 각 서비스는 동일한 네트워크에 연결되고, 다른 서비스의 이름을 통해 서로 통신할 수 있다. 즉, 네트워크 구성을 직접 설정하지 않아도 서비스끼리 간편하게 연결된다.
- **Volume** : 데이터가 영구적으로 유지되도록 하기 위해 Docker 볼륨을 정의할 수 있다. 볼륨은 컨테이너가 재시작되어도 데이터가 유지되도록 해준다.
- **Environments**: Docker Compose에서 각 서비스에 대해 환경 변수를 정의할 수 있다. 예를 들어, 데이터베이스 설정이나 포트 번호 등을 환경 변수로 지정할 수 있다.

<br><br>

### 동작 흐름

1. **docker-compose.yml 파일 작성**: 애플리케이션의 여러 서비스를 정의하는 설정 파일을 작성.
2. **`docker-compose up` 실행**: 작성된 `docker-compose.yml` 파일을 기반으로 모든 서비스를 동시에 시작한다.
3. **`docker-compose down`**: 실행 중인 모든 컨테이너와 네트워크, 볼륨을 정리한다.

<br><br>

### Docker Compose 파일의 구조

**`docker-compose.yml`** 파일은 yml 형식으로 작성된다

```yaml
version: '3.8'

services:
  # 웹 애플리케이션 서비스
  web:
    image: "nginx" # NGINX 이미지 사용
    ports:
      - "8080:80" # 호스트의 8080 포트를 컨테이너의 80 포트로 매핑
    networks:
      - app-network  # app-network라는 네트워크에 연결
    volumes:
      - ./html:/usr/share/nginx/html  # 로컬 폴더를 NGINX 서버에 마운트

  # 데이터베이스 서비스
  db:
    image: "mysql:5.7" # MySQL 5.7 이미지 사용
    environment:
      MYSQL_ROOT_PASSWORD: example  # MySQL root 비밀번호 설정
    ports:
      - "3306:3306" # 호스트의 3306 포트를 컨테이너의 3306 포트로 매핑
    networks:
      - app-network
    volumes:
      - db-data:/var/lib/mysql  # 데이터가 영구적으로 저장되도록 볼륨 설정

networks:
  app-network: # 서비스들이 연결될 네트워크 정의

volumes:
  db-data: # 데이터 영구 저장을 위한 볼륨 정의

```

<br>

### Docker Compose 명령어

- **`docker-compose up`**: 모든 서비스를 정의된 설정대로 시작.
- **`docker-compose down`**: 실행 중인 모든 서비스를 중지하고, 네트워크와 볼륨도 제거.
- **`docker-compose ps`**: 현재 실행 중인 컨테이너를 확인.
- **`docker-compose logs`**: 각 서비스에서 발생하는 로그를 실시간으로 확인.
- **`docker-compose build`**: 정의된 Docker 이미지를 빌드.
- **`docker-compose exec`**: 실행 중인 컨테이너에 접속하여 명령어를 실행.

<br><br>

### Docker Compose의 장점

1. **컨테이너 관리**: Docker Compose를 사용하면 여러 컨테이너를 하나의 명령어로 쉽게 관리할 수 있다. 
2. **재사용 가능한 설정**: `docker-compose.yml` 파일은 애플리케이션의 환경을 재사용할 수 있다.
3. **독립적인 네트워킹**: Compose는 각 서비스가 동일한 네트워크에서 실행되도록 자동으로 설정하기 때문에 개발자가 별도로 네트워킹을 설정할 필요가 없다.

<br><br>

### 1. `docker-compose.yml` 파일을 작성

```yaml
version: '3'

services:
  app:
    image: "my-spring-app"        # Spring 애플리케이션 이미지
    ports:
      - "8080:8080"

  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"

```

`version: '3.7'` 은 Docker Compose 파일에서 사용하는 **Compose 파일의 버전**을 명시하는 부분으로 특정 버전마다 지원하는 기능이 다르다.

Docker Compose 파일에 `version` 을 명시하지 않으면, 기본적으로 Docker는 Compose V1 형식을 가진다. Docker Compose V1은 초기의 구문 형식으로, 많은 최신 기능들이 제한적이다.

버전 2.x대는 Docker Compose를 사용하여 컨테이너 오케스트레이션을 지원하며, 다양한 네트워크 및 볼륨 기능을 제공한다. 버전 3.x대는 Docker Swarm과  `deploy` 옵션을 사용하여 **리소스 제한** 같은 스케일링 설정을 정의하거나 `depends_on`과 같은 **서비스 간의 종속성** 설정을 지원한다.

<br>

### 2. 실행 명령어

```bash
docker-compose up
```

이 명령어는 `docker-compose.yml`에 정의된 모든 서비스를 시작한다. Spring 애플리케이션, Prometheus, Grafana가 동시에 실행된다.

<br>

### 3. 서비스 종료 명령어

```bash
docker-compose down
```

이 명령어로 모든 실행 중인 서비스와 네트워크, 볼륨을 종료하고 삭제할 수 있다.