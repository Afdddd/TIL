# RabbitMQ 기본기

---

<br>

## RabbitMQ란?

![image.png](/Message%20Broker/img/rabbitMQ(1).png)

메시지 브로커(Message Broker)로, 서로 다른 애플리케이션 간에 데이터를 주고받을 수 있도록 중개 역할을 하는 소프트웨어이다. 일반적으로 메시지를 큐(queue)에 저장하고, 해당 메시지를 다른 애플리케이션(소비자 또는 Consumer)에게 전달하는 방식으로 작동한다.

<br><br>


--- 

## 주요 개념

<br>

### **메시지(Message)**


- **정의**: 시스템 간에 주고받는 **데이터의 단위**. 생산자가 생성하여 브로커로 보내고, 소비자가 처리하는 내용이다. 메시지에는 텍스트, JSON, 바이너리 등 다양한 형식의 데이터가 포함될 수 있다.

<br>

### **생산자(Producer)**

- **정의**: 메시지를 생성하고 브로커로 보내는 애플리케이션.

<br>

### **소비자(Consumer)**

- **정의** : 브로커에서 큐로부터 메시지를 가져와 처리하는 애플리케이션.

<br>

### **큐 (Queue)**

- **정의**: 큐는 메시지를 일시적으로 저장하는 공간. 프로듀서가 보낸 메시지는 큐에 쌓이고, 소비자가 읽어갈 때까지 대기한다.
- **특징**
    - 큐에 쌓인 메시지는 **FIFO(First In, First Out)** 방식으로 처리된다. 즉, 먼저 도착한 메시지가 먼저 소비된다.
    - 메시지가 처리되지 않으면 큐에 계속 남아있다.
    - 메시지의 영구 저장을 설정할 수 있어, RabbitMQ 서버가 재시작되더라도 메시지를 잃지 않도록 할 수 있다.
    
<br>

### **익스체인지 (Exchange)**

- **정의**: 익스체인지는 프로듀서가 보낸 메시지를 받아서, 그 메시지를 적절한 큐로 라우팅하는 역할을 한다. 메시지의 중계소 역할
- **종류**
    - **Direct Exchange**: 특정한 **라우팅 키**를 기반으로 메시지를 큐에 전달. 프로듀서가 보낸 라우팅 키와 정확히 일치하는 라우팅 키를 가진 큐로 메시지를 보낸다.
    - **Topic Exchange**: 라우팅 키에 패턴 매칭을 사용. 와일드카드(`*`, `#`)를 사용해 유연하게 라우팅한다. 예를 들어, `error.*` 같은 패턴을 사용해 `error.log`, `error.alert` 등 메시지를 처리할 수 있다.
    - **Fanout Exchange**: 라우팅 키와 관계없이 모든 연결된 큐로 메시지를 브로드캐스트한다. 예를 들어, 한 번에 여러 소비자에게 동일한 메시지를 전달할 때 사용된다.
    - **Headers Exchange**: 메시지의 헤더 값에 따라 라우팅된다. 라우팅 키 대신, 헤더에 있는 정보를 기반으로 메시지를 큐로 라우팅한다.
    
<br>

### **라우팅 키 (Routing Key)**

- **정의**: 메시지가 큐에 전달되는 경로를 지정하는 키. 익스체인지가 메시지를 큐로 라우팅할 때 사용하는 기준이 된다.
- **사용 방식**
    - **Direct Exchange**에서는 프로듀서가 메시지를 보낼 때 특정 라우팅 키를 지정하고, 이 키와 일치하는 큐로 메시지가 전달된다.
    - **Topic Exchange**에서는 라우팅 키 패턴 매칭을 사용해 좀 더 유연하게 메시지를 전달할 수 있다. 예를 들어, `sensor.temperature`와 같은 키를 사용해 `sensor.#` 패턴을 가진 큐로 메시지를 라우팅할 수 있다.

<br>

### **바인딩 (Binding)**

- **정의**: 익스체인지와 큐를 연결하는 설정입니다. 익스체인지가 특정 라우팅 키에 따라 메시지를 어떤 큐로 보낼지 결정하는 규칙을 정의
- **역할**
    - 바인딩을 통해 큐는 익스체인지로부터 메시지를 받을 수 있다.
    - 바인딩에 라우팅 키를 설정해, 특정 키를 가진 메시지만 큐로 들어가도록 할 수 있다.
    - 예를 들어, `error`라는 라우팅 키를 가진 메시지는 `error_logs` 큐로 가도록 바인딩을 설정할 수 있다.

<br><br>

---

## 메시징 패턴

어플리케이션 간의 메시지 전달 방식에 따라 여러 메시징 패턴이 존재한다.

<br>

### 작업 큐 (Work Queue)

![image.png](/Message%20Broker/img/rabbitMQ(2).png)

여러 소비자가 **한 큐**에서 메시지를 가져가 **작업을 분산**하는 패턴. 이 패턴에서는 큐에 메시지가 쌓이면, 여러 소비자가 동시에 처리할 수 있도록 분산시킨다.

- **사용 예시**: 대규모 데이터 처리 작업을 여러 서버가 동시에 처리할 때 유용하다.


<br>
<br>

### 게시/구독 (Publish/Subscribe)

![image.png](/Message%20Broker/img/rabbitMQ(3).png)

**하나의 생산자가 여러 소비자에게 메시지를 동시에 전송**하는 패턴. 이때 **Fanout Exchange**를 사용해 모든 연결된 큐로 메시지를 복사하여 보낸다.

- **사용 예시**: 실시간 알림 시스템, 뉴스 피드 배포, 여러 곳에 동시에 동일한 데이터를 보내야 할 때 유용


<br>
<br>

### 라우팅 (Routing)

![image.png](/Message%20Broker/img/rabbitMQ(4).png)

**Direct Exchange**를 사용해 특정 라우팅 키(Routing Key)를 기반으로 메시지를 **특정 큐로** 전송하는 패턴.  생산자가 메시지를 보낼 때 라우팅 키를 지정하면, 그 키와 일치하는 큐로 메시지가 전달된다.

- **사용 예시**: 특정 주제에 관심이 있는 소비자에게만 메시지를 보내야 할 때, 예를 들어 특정 지역에만 알림을 보내거나 특정 카테고리의 주문만 처리하는 경우에 사용


<br>
<br>

### 토픽 (Topic)

![image.png](/Message%20Broker/img/rabbitMQ(5).png)

**Topic Exchange**는 라우팅 키의 패턴과 바인딩 키(Binding Key)를 비교하여 메시지를 큐로 라우팅한다. **와일드카드** 문자를 사용해 더 복잡한 패턴 매칭이 가능하다.

- **`*` (별 하나)**: **단일 단어**와 일치.
- **`#` (샵 하나)**: **0개 이상의 단어**와 일치.

```
*.orange.*
```

- 이 바인딩 키는 **첫 번째와 세 번째 단어가 어떤 것이든 상관없이**, 두 번째 단어가 `"orange"`인 라우팅 키와 일치하는 메시지를 받는다.
- 예를 들어, 라우팅 키가 `"fast.orange.apple"`이나 `"slow.orange.banana"`인 경우, 이 메시지는 **Q1**으로 라우팅된다.


<br>
<br>

### **Remote procedure call (RPC)**

![image.png](/Message%20Broker/img/rabbitMQ(6).png)

**클라이언트-서버 통신**에서 사용되며, 클라이언트가 원격 서버에 요청을 보내고 그에 대한 응답을 받는 구조.  RabbitMQ를 사용해 비동기 메시지 기반으로 **동기식 요청/응답 통신**을 구현할 수 있다.

- **Client (클라이언트)**: 요청을 생성하고 서버로 보낸 후, 서버로부터 응답을 기다린다.
- **rpc_queue**: 서버가 요청 메시지를 받는 **큐**. 클라이언트는 이 큐로 요청 메시지를 전송한다.
- **Server (서버)**: **rpc_queue**에서 메시지를 읽고, 요청을 처리한 뒤 클라이언트에게 응답을 보낸다.
- **Reply Queue (응답 큐)**: 서버가 처리 결과를 클라이언트로 보내는 **응답 큐**. 클라이언트는 **익명 큐**를 생성해 이 큐의 주소를 서버로 전송하여 응답을 받을 준비를 한다.


<br>
<br>

--- 

## Spring AMQP 튜토리얼


<br>

### RabbitMQ Docker 이미지 다운로드 및 실행


<br>

docker 명령어로 바로 실행할 수 있다.

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```

- **d** : 백그라운드에서 컨테이너를 실행
- **--name rabbitmq** : 컨테이너 이름을 rabbitmq로 지정
- **-p 5672:5672** : RabbitMQ의 기본 통신 포트(AMQP)인 5672를 호스트와 연결
- **-p 15672:15672** : RabbitMQ 관리 대시보드의 포트(HTTP)인 15672를 호스트와 연결
- **rabbitmq:management** : RabbitMQ 공식 이미지 중 관리 플러그인이 활성화된 버전을 사용

RabbitMQ 컨테이너가 실행된 후, 브라우저에서 ([http://localhost:15672](http://localhost:15672/))로 접속하면 관리 대시보드에 접속할 수 있다.

![image.png](/Message%20Broker/img/rabbitMQ(7).png)

Username :  guest

Password : guest


<br>

### 프로젝트 생성

![image.png](/Message%20Broker/img/rabbitMQ(8).png)

[https://start.spring.io/](https://start.spring.io/) 에서 프로젝트 생성 Dependencise로는 Srping Web과 Spring for RabbitMQ를 설정해준다.


<br>

### 프로젝트 구성

**Spring Boot**에서 **RabbitMQ**에 연결하기 위해 설정 파일 작성
`yml` 파일에 작성해줬다.

```
spring:
  rabbitmq:
    host : localhost
    port : 5672
    username : guest
    password : guest
    
    listener:
      simple:
        acknowledge-mode: manual
```

- **host** : RabbitMQ 서버의 호스트 주소(IP 또는 도메인 이름)를 설정
    - 로컬에서 실행중이기 때문에 localhost로 설정해줬다.
- **port** : RabbitMQ 서버가 사용하는 **포트 번호**
    - 기본적으로 RabbitMQ는 **AMQP 프로토콜**을 사용하며, 기본 포트는 **5672**
- username : RabbitMQ 서버에 연결할 때 사용할 **사용자 이름**
    - guest는 기본 사용자 계정
- password : RabbitMQ 서버에 연결할 때 사용할 **비밀번호**
    - guest는 기본 비밀번호
- acknowledge-mode: manual : 메시지를 소비자가 명시적으로 Ack를 보내야만 확인하도록 설정


<br>

### Spring과 RabbitMQ 통신 설정

Spring이 RabbitMQ와의 통신을 위해 필요한 Queue, Exchange, Binding을 정의 해주어야 한다.

```java
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("helloQueue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("helloExchange");
    }

    @Bean
    public Binding binding(Queue helloQueue, DirectExchange exchange) {
        return BindingBuilder.bind(helloQueue).to(exchange).with("helloRoutingKey");
    }
}
```

Queue, Exchange, Binding을 스프링 빈으로 등록해 스프링 컨텍스트에서 관리되도록 해준다.

- `new Queue("helloQueue", true)`: 이름이 "helloQueue"인 큐를 생성
    - 두 번째 매개변수 `true`는 큐의 지속성(Durable)을 의미한다. RabbitMQ 서버가 재시작되더라도 삭제되지 않고 유지해주는 설정이다.
- `new DirectExchange("helloExchange")` : : 이름이 "helloExchange"인 **Direct Exchange**를 생성
    - **Direct Exchange**: 메시지를 특정 라우팅 키(Routing Key)와 일치하는 큐로 전달해주는 익스체인지이다.
- `BindingBuilder.bind(helloQueue).to(exchange).with("helloRoutingKey")`: **"helloQueue"** 큐를 **"helloExchange"** 익스체인지에 **"helloRoutingKey"** 라우팅 키로 연결
    - **라우팅 키("helloRoutingKey")**: 메시지가 익스체인지에 도착할 때 "helloRoutingKey"와 일치하는 라우팅 키가 있으면, 그 메시지는 "helloQueue"로 전달한다.


<br>

### Producer 생성

```java
@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("helloExchange", "helloRoutingKey", message);
        System.out.println("Sent message: " + message);
    }
}
```

**Spring AMQP**를 사용하여 RabbitMQ에 **메시지를 전송하는 클래스이다.**

- `RabbitTemplate`는 Spring AMQP에서 **RabbitMQ와 상호 작용**하기 위해 제공되는 기본 클래스.
- `convertAndSend(String exchange, String routingKey, Object message)` : 지정된 익스체인지(Exchange)에 라우팅 키(Routing Key)와 함께 메시지를 전송

sendMessage() 를 호출하면 `RabbitTemplate`는 “helloExchange”로 메시지를 전송하고, "helloRoutingKey"를 사용해 메시지를 라우팅한다.


<br>

### Consumer 생성

```java
@Service
public class MessageConsumer {

  @RabbitListener(queues = "helloQueue")
  public void receiverMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
      throws IOException {
    try {
      // 메시지 처리 로직
      System.out.println("Received message: " + message);

      // 메시지 성공적으로 처리 완료, Ack 전송
      channel.basicAck(tag, false);
    } catch (Exception e) {
      // 오류 처리: 메시지 재처리를 위해 Ack를 보내지 않음
      System.out.println("Error processing message: " + e.getMessage());
      channel.basicNack(tag, false, true);
    }
  }

}
```

<br>

`@RabbitListener` 어노테이션은 Spring AMQP에서 제공하는 기능으로, 특정 큐(Queue)를 모니터링하면서 그 큐에 새로운 메시지가 도착하면 자동으로 메서드를 호출한다. RabbitMQ는 `Channel`을 통해 메시지를 소비자에게 전달하며, 이때 메시지마다 `Delivery Tag (tag)`를 함께 전달한다. 

<br>

`Chennel`이란 **하나의 물리적 TCP 연결 안에서 여러 개의 가상 연결**을 의미한다. rabbitMQ서버와 연결된 통로이기 때문에 다시 메시지를 보낼수있다. `DELIVERY_TAG`는 RabbitMQ에서 **메시지의 고유 식별자**이다.

<br>

`Channel`과 `tag`는 소비자가 메시지를 성공적으로 받고 처리를 했다고 확인신호를 보내 때 사용된다. 위에 프로젝트 구성의 `yml`파일에  `acknowledge-mode: manual` 로 설정해주었는데 이 설정이 바로 수동으로 확인 메시지를 보낼때 사용되는 설정이다. 이 설정을 하지 않으면 자동으로 확인 메시지가 발송되지만 수동으로 하면 메시지의 신뢰성을 높이고 제어할 수 있기 때문에 설정해줬다이다.


- `@RabbitListener(**queues = "helloQueue")**`: "helloQueue"라는 이름의 큐를 지정한다. 즉, "helloQueue"에 새로운 메시지가 도착하면, `receiverMessage()` 메서드가 자동으로 호출된다.
- `channel.basicAck(long deliveryTag, boolean multiple)` : 메시지가 성공적으로 처리되었음을 RabbitMQ에 알리는 메서드
    - `deliveryTag`: 처리 성공한 메시지를 식별하는 태그. 이 태그를 사용하여 RabbitMQ에 해당 메시지가 처리되었음을 알린다.
    - `multiple` :  `false` 일 경우 단일 메시지에 대해서만 Ack를 전송, `true`일경우 `deliveryTag`에 도달하기까지의 **모든 메시지**를 한꺼번에 Ack한다. `deliveryTag`가 5라면, 1부터 5까지의 모든 메시지가 한꺼번에 Ack한다.
    
- `channel.basicNack(long deliveryTag, boolean multiple, boolean requeue);`: 메시지가 처리되지 않았음을 RabbitMQ에 알리는 메서드
    - **`deliveryTag`**: 메시지를 식별하는 고유 태그
    - `multiple` :  `false` 일 경우 단일 메시지에 대해서만 Ack를 전송, `true`일경우 `deliveryTag`에 도달하기까지의 **모든 메시지**를 한꺼번에 Ack한다. `deliveryTag`가 5라면, 1부터 5까지의 모든 메시지가 한꺼번에 Ack한다.
    - **`requeue` :** `true`일 경우 **처리되지 않은 메시지를 다시 큐에 넣는다.** `false`일 경우 메시지를 **큐에서 제거**하고, 다시 처리하지 않다.

<br>

### 메시지 보내기

간단하게 controller를 만들어 producer에서 consumer로 메시지를 보내보자.

```java
@RestController
public class MessageController {

  @Autowired
  private MessageProducer messageProducer;

  @GetMapping("/send")
  public String sendMessage(@RequestParam String message) {
    messageProducer.sendMessage(message);
    return "Message sent : " + message;
  }

}
```

[http://localhost:8083/send?message=HelloRabbitMQ](http://localhost:8083/send?message=HelloRabbitMQ) 쿼리스트링으로 “HelloRabbitMQ”를 전송하고 management를 확인해보면 

![image.png](/Message%20Broker/img/rabbitMQ(9).png)

![image.png](/Message%20Broker/img/rabbitMQ(10).png)

아무런 변화가 없어보이지만 메시지 처리가 너무 빨라서 안보이는것이다.

`Thread.sleep(15000)` 을 주고 다시해보면

```java
Thread.sleep(15000); // 15초 대기
System.out.println("Received message: " + message);
channel.basicAck(tag, false);
```

![image.png](/Message%20Broker/img/rabbitMQ(11).png)

Unacked 가 1로 올라갔는데 이는 소비자가 큐에서 메시지를 꺼냈지만 아직 Ack(확인)이 되지 않은 상태이다. `Thread.sleep`이 지나면 다시 0으로 떨어진다.