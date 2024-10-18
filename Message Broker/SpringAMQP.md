# Spring AMQP

<br>

지난 포스트에서는 RabbitMQ의 기본 개념과 Spring AMQP의  `RabbitTemplate` 을 사용해 메시지를 보내고  `RabbitListener` 를 사용해  다이렉트 익스체인지와 라우팅 키를 사용하는 라우팅 패턴을 구현했었다. 이번에는 구독(Post/Subscribe) 패턴을 구현보고, Spring AMQP에서 제공해주는 트랜잭션 처리, 실패 처리, QoS, DLX에 대해 학습해보도록 하겠다.

<br><br>

## Spring AMQP란?

<br>

**Spring Framework**를 기반으로 AMQP (Advanced Message Queuing Protocol)을 쉽게 사용할 수 있도록 해주는 라이브러리이다. Spring의 의존성 주입, 구성, 어노테이션 기반 설정을 활용하여 RabbitMQ 작업을 더 간단하고 유지보수하기 쉽게 만들어준다.

<br>

```
💡 AMQP?
Advanced Message Queuing Protocol, 메시지 브로커 간의 메시지 송수신을 표준화한 프로토콜이다.
```

<br><br>

## 구독(Post/Subscribe) 패턴 구현하기

![image.png](/Message%20Broker/img/rabbitmq(2-1).png)

구독 패턴은 **Fanout Exchange**를 사용하여 모든 연결된 큐에 메시지를 브로드캐스트하는 방식이다. 

- 브로드캐스트 방식은 여러 컴포넌트나 객체에 동일한 메시지를 전송하는 것을 의미한다.
- **Fanout Exchange** 방식은 라우팅 키와 관계없이 모든 연결된 큐로 메시지를 브로드캐스트이다.

<br>

### application.yml에 RabbitMQ 서버 설정

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

- acknowledge-mode: manual : 소비자가 메시지를 성공적으로 받았다는 확인을 수동으로 보내도록 설정

<br>

### **구독 패턴 설정**

먼저, Spring AMQP 설정을 통해 Fanout Exchange를 생성하고, 여러 큐를 바인딩한다.

```java
@Configuration
public class RabbitConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout-exchange");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue-1");
    }

    @Bean
    public Queue queue2() {
        return new Queue("queue-2");
    }

    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }

    @Bean
    public Binding binding2(FanoutExchange fanoutExchange, Queue queue2) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }
}

```

`amqp.core.FanoutExchange`

- Fanout Exchange는 라우팅 키를 무시하고 모든 큐에 메시지를 전송한다.
- 여러 개의 큐가 바인딩되어 있으면 **모든 큐에 동일한 메시지**가 전달한다.

`amqp.core.Queue`

- **Queue**는 메시지가 전달되는 저장소.
- `queue-1`과 `queue-2`라는 이름의 큐를 생성하고 빈으로 등록한다.

`amqp.core.Binding`

- **Binding**은 **Exchange와 Queue를 연결**하는 역할.
- 바인딩을 통해 메시지가 어떤 큐로 전달되어야 하는지를 결정한다.

<br>

### **메시지 발행 (Producer)**

```java
@Component
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("fanout-exchange", "", message);
        System.out.println("Sent: " + message);
    }
}

```

**RabbitTemplate**

- `RabbitTemplate` 은 Spring AMQP에서 제공하는 클래스로, RabbitMQ와의 상호작용을 간단하게 할 수 있게 해준다.

**sendMessage()**

- 주어진 메시지를 RabbitMQ로 전송하는 메서드
- **`convertAndSend(exchange, routingKey, message)`** 메서드는 메시지를 지정된 **Exchange**로 전송
    - exchange : 전송될 exchange 파라타미터, `"fanout-exchange"` 생성한 fanout-exchange 설정.
    - routingKey : 라우팅키 파라미터, 빈 문자열로 설정하면 사용지 않음.
    - message : 전송할 메시지

<br>

### **메시지 소비 (Consumer)**

```java
@Component
public class PostAndSubConsumer {

  @RabbitListener(queues =  "queue-1")
  public void receiveMessage1(String Message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)long tag)
      throws IOException {
    try {
      Thread.sleep(10000);
      System.out.println("Queue 1 Received: " + Message);

      channel.basicAck(tag, false);
    }catch (Exception e){
      System.out.println("Queue 1 Failed");
      channel.basicNack(tag, false, true);
    }
  }

  @RabbitListener(queues = "queue-2")
  public void receiveMessage2(String Message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)long tag)
      throws IOException {
    try {
      Thread.sleep(10000);
      System.out.println("Queue 2 Received: " + Message);

      channel.basicAck(tag, false);
    }catch (Exception e){
      System.out.println("Queue 2 Failed");
      channel.basicNack(tag, false, true);
    }
  }
}
```

**@RabbitListener(queue = “”)**

- 이 어노테이션은 RabbitMQ에서 지정된 **큐로부터 메시지를 수신**하기 위해 사용한다.
- `@RabbitListener(queues = "queue-1")` 이 경우 `queue-1"` 큐를 모니터링하면서 그 큐에 새로운 메시지가 도착하면 자동으로  `receiveMessage1()`이 호출된다.


**receiveMessage1(String Message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)long tag)**

- `Channel` 파라미터는 **하나의 물리적 TCP 연결 안에서 여러 개의 가상 연결**을 의미한다. rabbitMQ서버와 연결된 통로이기 때문에 다시 메시지를 보낼수있다.
- `@Header(AmqpHeaders.DELIVERY_TAG)long tag` : RabbitMQ에서 **메시지의 고유 식별자**를 뜻한다. 


**channel.basicAck(long deliveryTag, boolean multiple)**
- 메시지가 성공적으로 처리되었음을 RabbitMQ에 알리는 메서드
- `deliveryTag`: 처리 성공한 메시지를 식별하는 태그. 이 태그를 사용하여 RabbitMQ에 해당 메시지가 처리되었음을 알린다.
- `multiple` :  `false` 일 경우 단일 메시지에 대해서만 Ack를 전송, `true`일경우 `deliveryTag`에 도달하기까지의 **모든 메시지**를 한꺼번에 Ack한다. `deliveryTag`가 5라면, 1부터 5까지의 모든 메시지가 한꺼번에 Ack한다.


**channel.basicNack(long deliveryTag, boolean multiple, boolean requeue)**

- 메시지가 처리되지 않았음을 RabbitMQ에 알리는 메서드
- **`deliveryTag`**: 메시지를 식별하는 고유 태그
- `multiple` :  `false` 일 경우 단일 메시지에 대해서만 Ack를 전송, `true`일경우 `deliveryTag`에 도달하기까지의 **모든 메시지**를 한꺼번에 Ack한다. `deliveryTag`가 5라면, 1부터 5까지의 모든 메시지가 한꺼번에 Ack한다.
- **`requeue` :** `true`일 경우 **처리되지 않은 메시지를 다시 큐에 넣는다.** `false`일 경우 메시지를 **큐에서 제거**하고, 다시 처리하지 않다.

`Thread.sleep(10000)` : 메시지 처리가 빨라 메시지처리가 정상적으로 되었는지 확인하기 위해 일부러 딜레이를 줬다.

<br>

### 메시지 전송 및 수신

```java
@RestController
public class MessageController {

	@Autowired
  private final PostAndSubProducer postAndSubProducer;

  @GetMapping("/sendPostAndSub")
  public String sendMessagePostAndSub(@RequestParam String message) {
    postAndSubProducer.sendMessage(message);
    return "Message sent : " + message;
  }
}
```

[http://localhost:8083/sendPostAndSub?message=hello](http://localhost:8083/sendPostAndSub?message=hello) 

쿼리스트링으로 메시지를 보내면 요청을 보내고 rabbitMQ management에서 확인할 수 있다.

![image.png](/Message%20Broker/img/rabbitmq(2-2).png)

![image.png](/Message%20Broker/img/rabbitmq(2-3).png)

Unacked 는 소비자가 메시지를 받았지만 아직 확인 요청을 rabbitMQ서버로 보내지 않은 상태이다.

Thread.sleep()때문에 Uacked 상태이다. 시간이 지나면 없어 사라진다.

<br><br>

## 추가 기능

<br>

### **트랜잭션 처리 (Transactional Messaging)**

트랜잭션을 사용하여 메시지의 신뢰성을 보장할 수 있다. 메시지를 성공적으로 처리하지 못한 경우 롤백할 수 있으며, Spring에서 `RabbitTemplate`을 통해 트랜잭션을 활성화할 수 있다.

<br>

### **트랜잭션 설정**

```java
@Configuration
public class TransactionConfig {

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setPublisherReturns(true);
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setChannelTransacted(true); // 트랜잭션 활성화
        return template;
    }
}

```

**`CachingConnectionFactory`**

- RabbitMQ 서버와의 연결을 효율적으로 관리하는 역할. **캐싱**을 사용하여 여러 번 재사용한다.
- `setPublisherReturns(true)` : 메시지가 특정 큐로 전달되지 않았을 경우 발행자에게 반환되도록 설정.
- `setPublisherConfirmType(~CORRELATED)` **: 발행자 확인기능** 설정`. CORRELATED `타입을 사용하면, 메시지가 성공적으로 큐에 도달했는지 여부를 발행자에게 알려주는 확인 절차를 제공

`template.setChannelTransacted(true)`

- RabbitMQ 메시지 전송에서 트랜잭션을 활성화한다.
- RabbitTemplate이 사용하는 채널이 **트랜잭션을 지원**하게 된다. 즉, 여러 메시지를 전송할 때, 모든 메시지 전송이 성공해야만 커밋되고, 그렇지 않으면 롤백된다.

<br>

### **QoS (Quality of Service)**

QoS(Quality of Service)는 RabbitMQ에서 메시지 처리 방식을 제어하는 기능이다.

주로 소비자가 한번에 얼마나 많은 메시지를 처리할 수 있을지 설정하기 위해 사용한다. 이를 통해 메시지 큐가 과도하게 소비자에게 메시지를 보내는 것을 방지하고, 소비자의 처리 부하를 제어할 수 있다. 

QoS의 중요한 파라미터는 `prefetchCount` 이다.

- `prefetchCount`는 소비자에게 전달되는 메시지의 수를 제한한다. 만약  `prefetchCount = 1`로 설정하면 소비자가 한 번에 하나의 메시지만 받아 처리할 수 있다.

<br>

### **QoS 설정 예제**

```java
@Configuration
public class RabbitMQListenerConfig {
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(1); // QoS 설정: 소비자는 한 번에 하나의 메시지만 받음
        return factory;
    }
}
```

- `SimpleRabbitListenerContainerFactory` : Spring에서 RabbitMQ 소비자를 설정하기 위해 사용되는 빈이다. `@RabbitListener`와 같은 RabbitMQ 메시지 소비자에 대해 메시지 수신 동작을 정의하는 **컨테이너를 생성하고 설정**하는 역할
- `setPrefetchCount(1)` : 소비자가 한 번에 하나의 메시지씩만 소비하도록 QoS를 설정

<br>

**RabbitMQ 메시지 소비자의 설정**
`SimpleRabbitListenerContainerFactory` 의 다양한 설정을 통해 소비자 설정할 수 있다.
```java
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3); // 동시에 메시지를 소비할 소비자 수 설정
        factory.setMaxConcurrentConsumers(10); // 최대 동시 소비자 수 설정
        factory.setPrefetchCount(5); // QoS 설정: 한 번에 소비자가 받을 메시지 수 (최대 5개)
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // Ack 모드 설정: 자동 확인 모드
        return factory;
    }

```

<br>

### **Dead Letter Exchange (DLX)**

Dead Letter Exchange (DLX)는 메시지가 소비되지 않거나 처리가 실패할 경우 해당 메시지를 다른 큐로 보내는 메커니즘을 말한다.

메시지가 특정 조건(TTL 초과, 큐가 꽉 찬 경우, Nack 처리된 경우)을 만족하지 못할 때 "데드 레터"로 지정된다. 이를 해결하기위해 DLX를 설정한다. 이를 통해 실패한 메시지를 모니터링하거나 추가적인 처리를 할 수 있다.

<br>

**실패 조건**
- **메시지 거부**: 메시지를 소비할 때 소비자가 `basic.reject` 또는 `basic.nack`를 호출하고, 이때 `requeue`를 `false`로 설정했을 경우, RabbitMQ는 메시지를 다른 큐로 재전송하지 않고 Dead Letter Queue로 보내진다.
- **메시지 TTL 초과** : 메시지의 수명(TTL, Time-To-Live)이 초과되면 해당 메시지는 기본 큐에서 제거되고 DLX로 이동한다. TTL은 큐 또는 메시지 단위로 설정할 수 있다.
- **Queue Overflow (큐가 가득 참)**: 큐가 가득 찼고 더 이상 메시지를 수용할 수 없는 경우 메시지를 Dead Letter Queue로 보낸다.

<br>

```java
@Configuration
public class RabbitMQConfig {

    public static final String MAIN_QUEUE = "main-queue";
    public static final String DLX_QUEUE = "dead-letter-queue";
    public static final String DLX_EXCHANGE = "dead-letter-exchange";

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE) // 지속성있는 큐 설정(재시작되어도 유지)
                .deadLetterExchange(DLX_EXCHANGE) // Dead Letter Exchange 설정
                .deadLetterRoutingKey("dlx.routing.key") // 메시지가 dead-letter로 지정  때 사용할 라우팅 키
                .build();
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE);
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, TopicExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with("dlx.routing.key");
    }
}

```

- **mainQueue**: 메인 큐이며, 실패한 메시지는 Dead Letter Exchange로 보내진다.
- **deadLetterExchange**: DLX로 사용될 교환기.
- **deadLetterQueue**: 실패한 메시지가 들어갈 큐.

이런 설정을 통해 `mainQueue`에서 처리되지 못한 메시지는 `dead-letter-exchange`를 통해 `dead-letter-queue`로 전달된다.

<br>

### TTL 설정
<br>

**큐 에서 TTL 설정**

큐 에서 TTL을 설정하면 해당 큐의 모든 메시지에 대해 TTL이 적용된다. 즉, 메시지가 큐에 들어온 후 설정된 시간 동안만 유지되고 그 이후에는 만료되어 DLX로 이동하거나 삭제된다.

```java
@Configuration
public class RabbitMQConfig {
  public static final String MAIN_QUEUE = "main-queue";

  @Bean
  public Queue mainQueue() {
      return QueueBuilder.durable(MAIN_QUEUE) 
              .ttl(60000) // TTL 설정 (밀리초 단위로 설정)
              .deadLetterExchange("dead-letter-exchange") // Dead Letter Exchange 설정
              .deadLetterRoutingKey("dlx.routing.key")
              .build();
  }
}
```

- **`.ttl(60000)`**: 메시지가 큐에 머무를 수 있는 시간을 설정. 여기서는 60,000밀리초(60초)로 설정되어 있다. TTL 시간이 초과되면 해당 메시지는 만료 처리된다.
- **Dead Letter Exchange 설정**: TTL이 초과된 메시지를 Dead Letter Exchange로 보낼 수 있도록 설정

<br>

**메시지에서 TTL 설정**

메시지 수준에서 TTL을 설정하면 특정 메시지마다 개별적인 TTL을 적용할 수 있다. `MessagePostProcessor`를 사용하여 메시지를 전송할 때 TTL을 설정한다.

```java
@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String exchange = "factory-exchange";
    private String routingKey = "factory.routing.key";

    public void sendMessage(String message) {
        MessagePostProcessor messagePostProcessor = msg -> {
            MessageProperties props = msg.getMessageProperties();
            props.setExpiration("60000"); // 메시지 TTL 설정 (밀리초 단위, 여기서는 60초)
            return msg;
        };

        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
        System.out.println("Message sent with TTL: " + message);
    }
}

```

- **`MessagePostProcessor` :** 메시지를 전송하기 전에 **메시지의 속성을 설정**하기 위해 사용하는 인터페이스
- `msg.getMessageProperties()` : 전송될 메시지의 속성을 가져온다. TTL, 메시지 헤더, 우선순위 등 정보를 설정할 수 있다.
- `props.setExpiration("60000")`: 메시지의 TTL을 설정
- `rabbitTemplate.convertAndSend()` 에 `messagePostProcessor` 를 넣어 설정한 값을 설정한다.