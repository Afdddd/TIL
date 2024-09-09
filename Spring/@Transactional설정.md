# @Transactional 설정

`@Transactional` 은 Spring 프레임워크에서 트랜잭션 관리를 위한 애노테이션이다.

<br>

```
💡트랜잭션이란? 
트랜잭션은 데이터베이스 작업(삽입, 수정, 삭제 등)을 하나의 작업 단위로 처리하며, 
중간에 오류가 발생하면 전체 작업을 롤백(rollback)해서 데이터의 일관성을 유지하도록 한다.
```

`@Transactional`에는 여러 설정 옵션들이 있다.

<br><br>

### 전파(Propagation) 옵션

트랜잭션의 전파를 설정하는 옵션으로 현재 트랜잭션이 존재하는지 여부에 따라 새로운 트랜잭션을 시작할지, 기존 트랜잭션에 합류할지, 아니면 트랜잭션 없이 실행할지 등을 결정한다.

<br>

```java
@Transactional(propagation = Propagation.REQUIRED)
```

<br>

- **REQUIRED**(기본값) : 메서드가 호출되면 현재 트랜잭션이 존재할 경우 그 트랜잭션에 합류하고, 없으면 새로운 트랜잭션을 시작한다.
- **REQUIRES_NEW**: 항상 새로운 트랜잭션을 시작한다. 기존 트랜잭션이 있으면 일시 중지하고 새로운 트랜잭션을 시작한다.
- **MANDATORY**: 반드시 기존 트랜잭션이 존재해야 하며, 존재하지 않으면 예외가 실행된다.
- **SUPPORTS**: 트랜잭션이 있으면 트랜잭션 내에서 실행하고, 없으면 트랜잭션 없이 실행된다.
- **NOT_SUPPORTED**: 트랜잭션이 있더라도 트랜잭션을 일시 중지하고 트랜잭션 없이 실행된다.
- **NEVER**: 트랜잭션이 존재하면 예외를 발생시키고, 없을 때만 실행된다.
- **NESTED**: 부모 트랜잭션 내에서 중첩 트랜잭션을 시작한다.

<br><br>

### **격리 수준 (Isolation) 옵션**

데이터의 동시 접근에 대한 설정하는 옵션으로 트랜잭션 간의 격리 수준을 설정하여 동시에 발생하는 데이터베이스 접근에서 발생할 수 있는 문제를 방지할 수 있다.

<br>

```java
@Transactional(isolation = Isolation.DEFAULT)
```

<br>

- **DEFAULT**: 데이터베이스 기본 격리 수준을 따른다.
- **READ_UNCOMMITTED**: 다른 트랜잭션이 커밋하지 않은 변경사항을 읽을 수 있다.
- **READ_COMMITTED**: 다른 트랜잭션이 커밋한 데이터만 읽을 수 있다.
- **REPEATABLE_READ**: 트랜잭션 동안 동일한 데이터를 읽을 때 항상 같은 결과가 반환되도록 한다.
- **SERIALIZABLE**: 가장 높은 수준의 격리로, 트랜잭션이 완료되기 전까지 다른 트랜잭션이 해당 데이터에 접근할 수 없게 한다.

<br><br>

### **읽기 전용 (readOnly)**

트랜잭션이 읽기 전용임을 명시하는 설정으로 데이터 수정이 일어나지 않는 트랜잭션에 대해 성능 최적화를 위해 사용한다. 쓰기 작업이 시도되면 예외가 발생할 수 있다.

```java
@Transactional(readOnly = true)
```

<br><br>

### **타임아웃 (timeout)**

트랜잭션 최대 지속 시간을 설정하는 옵션으로 트랜잭션이 지정된 시간 내에 완료되지 않으면 롤백된다. 단위는 초이다.

```java
@Transactional(timeout = 30)  // 30초 내에 완료되지 않으면 롤백
```

<br><br>

### 롤백 조건 (rollbackFor, noRollbackFor)

예외에 따른 롤백 제어를 설정하는 옵션

```java
@Transactional(rollbackFor = Exception.class)  // 모든 예외에 대해 롤백
@Transactional(noRollbackFor = IllegalArgumentException.class)  // 특정 예외에 대해 롤백하지 않음
```
<br>

- **rollbackFor**: 특정 예외가 발생할 때 트랜잭션을 롤백한다.
- **noRollbackFor**: 특정 예외가 발생해도 트랜잭션을 롤백하지 않는다.

<br><br>

<i>예제</i>

```java
@Transactional(
    propagation = Propagation.REQUIRED,  // 트랜잭션이 없으면 새로 시작, 있으면 합류
    isolation = Isolation.READ_COMMITTED,  // 커밋된 데이터만 읽음
    timeout = 30,  // 30초 내에 완료되지 않으면 롤백
    readOnly = false,  // 읽기 전용 아님 (쓰기 가능)
    rollbackFor = Exception.class  // 예외가 발생하면 롤백
)
public void processOrder(OrderRequest orderRequest) {
    // 트랜잭션 처리 로직
}
```

<br><br>

### 트랜잭션이 전파가 가능한 이유?

Spring의 트랜잭션 관리 방식이 프록시 기반으로 동작하기 때문이다.

**프록시가 트랜잭션를 관리하고, 트랜잭션 전파 설정에 따라 트랜잭션을 제어한다.**

`@Transactional`이 적용된 메서드를 호출하면, Spring은 **프록시 객체**를 통해 메서드 호출을 가로채고, 트랜잭션을 시작하거나 기존 트랜잭션에 참여하는 등의 트랜잭션 제어 작업을 수행한다.

`@Transactional`로 선언된 메서드가 **프록시를 통해 호출**되면, 프록시는 현재 트랜잭션 상태를 확인하고 전파 설정에 따라 트랜잭션을 처리한다.

<br><br>

### self-invocation 문제

```java
@Service
public class OrderService {

    @Transactional
    public void processOrder(OrderRequest orderRequest) {
        updateOrder(orderRequest);  // 같은 클래스 내에서 직접 호출
    }

    @Transactional
    public void updateOrder(OrderRequest orderRequest) {
      // 이미 processOrder()에서 시작된 트랜잭션 내에서 실행됨
      // 예외 발생 시 전체 트랜잭션 롤백됨
    }
}
```

위의 예시는 `@Transactional`이 적용된 `processOrder()` 메서드가 **같은 클래스 내**에서 `updateOrder()` 메서드를 호출하는 상황이다.

`updateOrder()`에도 `@Transactional`이 적용되어야 하는데, **트랜잭션 전파**는 어떻게 될까?

이 경우에는 `updateOrder()` 의 `@Transactional`은 시작되지 않는다.

그 이유는 프록시가 개입하지 않기 때문이다.

Spring에서는 `@Transactional`이 있는 클래스나 메서드가 있으면, 해당 클래스의 **프록시 객체**를 생성한다. 이 프록시는 트랜잭션 경계를 관리하는 역할을 한다. 하지만 **같은 클래스 내**에서 메서드가 호출될 때는 `this`를 통해 호출되기 때문에 **프록시가 아닌 실제 객체**가 호출된다.  

결국 자기 자신을 호출하는 경우에는 프록시를 거치지 않고 원래 객체가 직접 호출되므로 트랜잭션 관리가 적용되지 않는 것이다.

<br>

```java
@Service
public class OrderService { // 분리

    @Autowired
    private OrderUpdateService orderUpdateService;

    @Transactional
    public void processOrder(OrderRequest orderRequest) {
        orderUpdateService.updateOrder(orderRequest);  // 다른 빈 호출
    }
}

@Service
public class OrderUpdateService { // 분리

    @Transactional
    public void updateOrder(OrderRequest orderRequest) {
        // 트랜잭션 적용됨
    }
}
```

이 문제를 해결하기 위한 방법은 **트랜잭션 처리가 필요한 메서드를 다른 빈으로 분리하는 것이다.**
프록시는 **클래스 외부**에서 해당 메서드가 호출될 때만 개입하기 때문에 트랜잭션 처리가 필요한 메서드를 다른 서비스 빈으로 분리하면, 해당 메서드는 외부에서 호출되므로 프록시를 통해 트랜잭션이 적용된다.

