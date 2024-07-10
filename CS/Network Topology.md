# 네트워크 토폴로지

- 버스
- 스타
- 트리
- 링
- 메시
- 토폴로지의 필요성과 병목현상
<br><br>
# 1. 네트워크 토폴로지란?

<aside>
💡 노드와 링크가 어떻게 구성되어있는지를 말하며 
    <b>버스, 스타, 트리</b> 등의
      토폴로지가 있음.

</aside>

### 1. 버스 토폴로지

![https://postfiles.pstatic.net/MjAyNDA2MTVfMTE1/MDAxNzE4NDM3NDY4OTU3.U9gvc5j8-vglYyqHPxpvl2kiEO3o_Rtz3vTkFl8qiD8g.KFReIn9dwxtWzmV5JChBVmSgDcKV_hi4q8qJ_5Sm0owg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfMTE1/MDAxNzE4NDM3NDY4OTU3.U9gvc5j8-vglYyqHPxpvl2kiEO3o_Rtz3vTkFl8qiD8g.KFReIn9dwxtWzmV5JChBVmSgDcKV_hi4q8qJ_5Sm0owg.PNG/image.png?type=w580)

✅ 하나의 통신 회선에 여러 대의 노드가 연결되는 방식 

✅ 특징

- **브로드 캐스팅 방식**이라 통신망에 연결된 **모든 단말기에서 데이터를 수신할 수 있음.**
    - 브로드캐스팅 ?
        
        브로드캐스팅(Broadcasting)은 정보나 콘텐츠를 여러 수신자에게 동시에 전달하는 
        방법을 의미. 
        
- 양방향 전송
- 각 노드는 고유한 주소를 가짐. (메시지가 지정된 단말기로만 전달)
- 노드 추가, 삭제 쉬움
- 회선 끝에 터미네이터라는 장비 존재, 더 이상 데이터가 진행하지 않도록 막아줌.

👍 장점

- 소규모 네트워크를 구축하기 매우 쉬움.
- **한 노드에 장애가 발생해도 다른 노드에 영향X**
- **설치비용 적음**

⛔ 단점

- 메인 링크에 많은 트래픽이 생기면 정체현상 발생가능성 높음.(패킷 손실율 높음)
- **메인 링크 망가지면 큰 문제**
- **통신회선의 길이가 제한됨** (연결된 단말기가 늘어날수록 전속력이 약해짐)

### 2. 스타 토폴로지

![Untitled](/CS/img/starTo.png)

✅ 중앙 노드에 각 End-Device를 직접 연결시킨 형태로 방사형(중심에서 바깥으로 뻗어나가는 형태) 이라 부르기도 함. 

특징

- 중앙에 있는 노드를 기반으로 연결된 형태
- 노드 추가, 삭제 쉬움
- 많은 통신회선 필요, 모든 통신 제어는 중앙에 있는 노드가 수행

👍 장점

- 중앙노드가 아닌 한 노드에 장애가 발생해도 다른 노드에 영향 X
- 안정성이 높음. 중앙노드가 아닌 한 노드에 침해가 발생했을 때 다른 노드로 확장하기가 어렵기 때문. 
<br> <br> ⇒ 다른 노드로 가려면 중앙노드를 무조건 거쳐야 하고 보통의 스타토폴로지는 중앙노드의 방화벽 등을 더욱 깐깐하게 해놓음.

- 한 링크에 문제가 생겨도 해당 부분만 영향을 받고 나머지 부분은 정상적으로 작동함.

⛔ 단점

- 중앙노드 에러시 토폴로지 전체가 통신이 되지 않음.
<Br><br>

### 3. 트리 토폴로지

![Untitled](/CS/img/treeTo.png)

✅ 최상위에 중앙 컴퓨터를 중심으로 단계적으로 하위, 상위 개념을 계층적으로 적용한 형태

⇒ 통신에 우선순위를 부여 or Backbone 망을 형성하여 데이터의 통신을 트래픽에 따라 최적화 가능.  <br><Br>=> 현재는 일반적으로 이 방법을 가장 많이 사용

✅ 특징

- 트리형태(계층적 토폴로지라고도 함)
- 노드 추가, 삭제 보통(리프노드를(마지막) 기반으로 확장은 용이하지만 다른 노드는 어려움)
- 버스 토폴로지와 스타토폴로지의 하이브리드 형태

👍 장점

- 노드 확장이 용이(주로 리프노드로 확장함)
- 리프노드의 에러는 나머지 부분에 영향을 미치지 않음

⛔ 단점

- 특정 노드 트래픽 집중시 하위노드에 영향
- 루트노드에 문제가 생기면 전체네트워크에 큰 문제

ex) 백본케이블 : 여러 소형 네트워크들을 묶어 대규모 파이프라인을 통해 극도로 높은 대역폭으로 다른 네트워크들의 집합과 연결되는 네트워크(대규모 패킷 통신망)

### 4. 링형 토폴로지

![https://postfiles.pstatic.net/MjAyNDA2MTVfMjYz/MDAxNzE4NDM3ODU1OTk0.AgpYD-V1C002YMwjEfHP-WH0ZLbKq3YqDNJJPDSA9R4g.kiukms2bh_rqpwLtBrWfQjctSalpS4LUqnLkkG8Lui0g.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfMjYz/MDAxNzE4NDM3ODU1OTk0.AgpYD-V1C002YMwjEfHP-WH0ZLbKq3YqDNJJPDSA9R4g.kiukms2bh_rqpwLtBrWfQjctSalpS4LUqnLkkG8Lui0g.PNG/image.png?type=w580)

✅ Ring형은 인접해 있는 양 옆의 두 노드를 연결하는 단방향 전송 형태이다. 

✅ 특징

- 고리형태
- 노드 추가, 삭제가 쉬움

👍 장점

- 하나의 통신회선에서 장애가 발생하더라도 다른 방향의 통신회선을 사용할 수 있으므로 C자 형태의 구조가 되어 신뢰성을 확보할 수 있다.
- 노드의 연결 최소화할 수 있다.
- 노드 수가 많아져도 데이터 손실이 없음. 토큰을 기반으로 연속적으로 노드를 거치며 통신권한 여부를 따지고 해당 권한이 없는 노드는 데이터를 전달받지 않음.

⛔ 단점

- 링크 또는 노드가 하나만 에러 발생해도 전체 네트워크에 영향
- 토큰이 없는 노드는 통신에 참여를 못하며 데이터 공유가 안됨.
<Br><Br>
### 5. 메시 토폴로지

![https://postfiles.pstatic.net/MjAyNDA2MTVfMjc1/MDAxNzE4NDM3ODY5Mjcx.Wi5G9QragEVszeuReChe1e4Z_x4kjeKuNDaLnK3FZLEg.3HaT_NY3I7gTKHvPfU_4IJuv0klkrAJr-ukDP_q7XaIg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfMjc1/MDAxNzE4NDM3ODY5Mjcx.Wi5G9QragEVszeuReChe1e4Z_x4kjeKuNDaLnK3FZLEg.3HaT_NY3I7gTKHvPfU_4IJuv0klkrAJr-ukDP_q7XaIg.PNG/image.png?type=w580)

✅ 그물형 토폴로지라고 불리며, 단말기 또는 컴퓨터를 다른 모든 단말기와 서로 연결시킨 형태.

특징

- 그물망 형태
- 노드 추가, 삭제 어려움
- 풀(full)메시 토폴로지의 경우 n * (n - 1) / 2 의 회선이 필요함.

👍 장점

- 안정성이 높음. 한 노드가 장애가 나도 다른 경로를 이용하여 다른 노드에 영향을 미치지 않음

- 트래픽을 분산할 수 있음.

⛔ 단점

- 회선이 비효율적으로 많기 때문에 구축비용이 고가임

⇒ 중요한 회선이 아니면 매우 비싸서 잘 사용 x 
<br><br><br>

# 토폴로지의 필요성

토폴로지를 파악함으로써 병목현상을 해결하는 척도가 된다.

### 병목현상

![https://postfiles.pstatic.net/MjAyNDA2MTVfMjkw/MDAxNzE4NDQxNzY1ODM0.OnfBciO18PfmrU65EKnCxBkPjEszeoio0WZeGXB5P30g.fzkyH6Ctq-zvze_b6TQlf0IBzPntgYUVrheHV-edWsog.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfMjkw/MDAxNzE4NDQxNzY1ODM0.OnfBciO18PfmrU65EKnCxBkPjEszeoio0WZeGXB5P30g.fzkyH6Ctq-zvze_b6TQlf0IBzPntgYUVrheHV-edWsog.PNG/image.png?type=w580)

****

병목(bottleneck) 현상은 여러가지 의미로 쓰이나 네트워크에서는 

트래픽에 의해 데이터 흐름이 제한되는 상황을 말한다 

일명 `핫스팟`이라고도 합니다. (네트워크 한정)

### 해결 사례

![https://postfiles.pstatic.net/MjAyNDA2MTVfODkg/MDAxNzE4NDQxNzcyNjMx.cALfhX_T2ER32cemC5k4MVTHuyUkhcha7cr6e2XxQMYg.3Eksn0XlZp_0TbOyzb8foW_CZ885QsZmrK13wC-2cWgg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfODkg/MDAxNzE4NDQxNzcyNjMx.cALfhX_T2ER32cemC5k4MVTHuyUkhcha7cr6e2XxQMYg.3Eksn0XlZp_0TbOyzb8foW_CZ885QsZmrK13wC-2cWgg.PNG/image.png?type=w580)

구축된 시스템의 토폴로지를 알고 있다면 어떠한 부분에 어떠한 회선 또는 어떠한 서버의 용량을 증가시켜야하는지 알 수 있음.!!

위 사진을 보면, 웹서버와 db 그리고 사용자가 연결되어 있자나요.

트래픽이 많아져서 서버가 다운되면

>자원량 체크 => 웹서버나 db 스팩 늘리기 ,  메모리 8에서 30gb로 >늘리기
>가 있지만, <br>

이런방법보다는 회선을 늘려서 노드와 링크가 어떻게 구성되어있는지 토폴로지를 확인해서, `병목현상을 해결`하는 `방안 중 하나`가 될 수 있다는 것.