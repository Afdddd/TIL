# Thread UnSafety가 발생하는 케이스

# 1. Thread UnSafety란 무엇인가?

>Thread UnSafety는 멀티스레드 환경에서 여러 스레드가 <br>
동시에 공유 자원에 접근할 때 발생하는 문제를 설명하는 용어!<br> 이 문제는 동기화가 제대로 이루어지지 않아 데이터 일관성이나<br> 무결성이 깨질 때 발생. 

⇒ 이는<Br> `Race Condition(경쟁 상태)으로 인한 데이터 불일치`, <br>`상태 오염`, <br>`교착 상태(deadlock)` 등의 문제를 야기할 수 있다. 
<br><br>
# 2. Thread UnSafety가 발생하는 주요 케이스

### a. 공유 자원에 대한 동시 접근

- 여러 스레드가 동시에 공유 자원에 접근할 때,<br>
  예상하지 못한 결과가 발생

- 예시: 은행 계좌 잔액 갱신

```java
package com.hyunec.cosmicbaseball.handler;

public class Account {
  //balance가 기본 계좌 금액
  private int balance = 1000;

  //계좌 금액을 amount 만큼 차감
  public void withdraw(int amount) {
    if (balance >= amount) {
      balance -= amount;
    }
  }

  //기본 계좌 금액 값 가져오기
  public int getBalance() {
    return balance;
  }

  public static void main(String[] args) {
    Account account = new Account();

    Runnable task = () -> {
      for (int i = 0; i < 1000; i++) {
        account.withdraw(1);
      }
    };

    //동시에 2개의 스레드가 모두 withdraw를 실행
    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Final balance: " + account.getBalance());
  }
}

```

위의 예시 처럼 여러 스레드가 동시에 `withdraw` 메서드를 호출하면 잔액이 음수가 될 수 있어욤.

⇒ 한 스레드가 작업을 끝내기 전에 다른 스레드가 <br>
   값을 차감해줘서 생기는 일.

![Untitled](/Multi-Programming/img/Case1.png)

⇒ 진짜 실행해보면 음수가 나온다. <Br>
⇒ 매번은 아니지만, 0나오다가 -1 한 번씩 나옴. 

### b. 데이터 불일치 (Race Condition)

- 두 개 이상의 스레드가 경쟁적으로 자원에 접근하여<br>
데이터의 일관성이 깨지는 상황. => 이를 `경쟁상태` 라고도 함

- 예시: 하나의 값에 대해 읽기 및 쓰기 작업이 동시에 발생할 때.

```java
public class Counter {

  private int count = 0;

  public void increment() {
    count++;
  }

  public int getCount() {
    return count;
  }
  
  public static void main(String[] args) {
    Counter counter = new Counter();

    Runnable task = () -> {
      for (int i = 0; i < 1000; i++) {
        counter.increment();
      }
    };
  //스레드 2개 생성해서 increment()값 증가를 동시에 수행
    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Final count: " + counter.getCount());
  }
}
```

⇒ 예를 들어, 위의 예시처럼 두 스레드가 동시에 `increment` method를 <br>호출하여 카운터값을 증가 시키면 원래는 1000이여야 하는 값이 

![Untitled](/Multi-Programming/img/Case2.png)

⇒ 이렇게 1715가 나온다. (랜덤으러 1800나왔다가, 1670나왔다 한다)

### c. 교착 상태 (Deadlock)

- 두 개 이상의 스레드가 서로 자원이 풀리기를 기다리며<br>
  영원히 대기하는 상황. => 이런 상황을 `Blocking`이라고 한다. 

- 예시: 두 개의 스레드가 두 개의 자원에 대해 서로 잠금을<br>
        요청하는 경우.

```java
public class DeadlockExample {
    // 2개의 lock 생성
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    //처음에 lock1을 할당받은 후, 10밀리초 후에 lock2를 할당
    public void method1() {
        synchronized (lock1) {
            System.out.println("Thread 1: Holding lock 1...");
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            synchronized (lock2) {
                System.out.println("Thread 1: Holding lock 2...");
            }
        }
    }

    //처음에 lock2를 할당받은 후 , 10밀리초 후에 lock1을 할당
    public void method2() {
        synchronized (lock2) {
            System.out.println("Thread 2: Holding lock 2...");
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            synchronized (lock1) {
                System.out.println("Thread 2: Holding lock 1...");
            }
        }
    }

    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();

        Thread thread1 = new Thread(example::method1);
        Thread thread2 = new Thread(example::method2);

        thread1.start();
        thread2.start();
    }
}
```

![Untitled](/Multi-Programming/img/Case3.png)

⇒ 위 코드의 결과입니당. 
이게 뭐냐면.. ㅋㅋ

- `thread1`이 `method1`을 실행하며 `lock1`을 획득하고, <br>10밀리초 대기 후 `lock2`를 요청하잖아요.??
- `thread2` 는`method2`를 실행하며 `lock2`를 획득하고, <br>10밀리초 대기 후 `lock1`을 요청해요
- `thread1`은 `lock2`를 기다리고있고, `thread2`는 `lock1`을 <Br>기다리고 있어요. 뭐 하나가 끝나야 서로의 기다림이 끝날 텐데<Br>
- 둘 다 안 끝나니까 두 스레드는 서로의 자원을 기다리며 <Br>
무한 대기 상태에 빠지게 됩니당. 이 상태가 <mark><b>교착 상태</b></mark>입니다.

### d. 자원 고갈 (Resource Starvation)

- 하나의 스레드가 자원을 독점하여 다른 스레드가 필요한 자원을<br> 사용할 수 없는 상황.

```java
import java.util.concurrent.locks.ReentrantLock;

public class ResourceStarvationExample {

  private final ReentrantLock lock = new ReentrantLock();

  public void accessResource() {
    lock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + " is accessing the resource.");
      try {
        Thread.sleep(100); // 자원을 독점하는 시간
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    ResourceStarvationExample example = new ResourceStarvationExample();

    Runnable highPriorityTask = () -> {
      while (true) {
        example.accessResource();
      }
    };

    Runnable lowPriorityTask = () -> {
      while (true) {
        example.accessResource();
      }
    };

    Thread highPriorityThread = new Thread(highPriorityTask);
    Thread lowPriorityThread = new Thread(lowPriorityTask);

    //스레드 우선순위 지정해주기
    highPriorityThread.setPriority(Thread.MAX_PRIORITY);
    lowPriorityThread.setPriority(Thread.MIN_PRIORITY);

    highPriorityThread.start();
    lowPriorityThread.start();
  }
}
```

![Untitled](/Multi-Programming/img/Case4.png)

⇒ 위의 코드 결과인데요 

- `highPriorityThread`와 `lowPriorityThread`가 동일한 자원을<br> 사용하려고 하잖아용
- `highPriorityThread`는 높은 우선순위를 가지고 있어 <br>
자원을 먼저 획득할 가능성이 높아요 <br>(setPriority를 통해서 우선순위를 정합니당)
- `lowPriorityThread`는 낮은 우선순위를 가지고 있어 자원을 <Br>획득할 기회가 적고 ㅠㅠ
- 이로 인해 `lowPriorityThread`는 자원을 거의 획득하지 못하고<Br> 계속 대기 상태에 머무르게 됩니당.
- 결과적으로 `lowPriorityThread`가 자원을 사용할 수 없는 상태가<Br> 됩니다. 이 상태가 자원 고갈입니다.!!

위에만 보셔도 Thread-0이 거의 독차지 하고 1은 한 번이잖아요 

거의 그런 상태인거죠. 

*-참고-*

<aside>
💡 Java에서 스레드의 우선순위는 <Br>1에서 10까지의 값으로 설정할 수 있으며, <br>
`Thread.MIN_PRIORITY` (1), <br>
`Thread.NORM_PRIORITY` (5), <Br>
`Thread.MAX_PRIORITY` (10)의 상수를 제공

</aside>
<br><br>

# 3. 동기화 메커니즘

## a. Synchronized 키워드

- 메서드 또는 블록에 대해 단일 스레드 접근을 보장.
###a_1. 메소드 동기화

```java
public synchronized void withdraw(int amount) {
    if (balance >= amount) {
        balance -= amount;
    }
}
```

⇒ 위의 코드처럼 method에 `synchronized` 키워드를 추가해주시면, <Br>해당 메소드는 단일 스레드에서만 접근 가능합니당. 
   
###a_2 블록 동기화
⇒ 특정 코드 블록을 동기화 가능. <Br>
  `synchronized` 키워드 뒤에 지정된 객체의 `lock`을 사용함. 

```java
public class Account {
    private int balance = 1000;
    private final Object lock = new Object();
    
    public void withdraw(int amount) {
        synchronized (lock) {
        //이 블록 내부는 한 스레드만 실행 가능함. 
            if (balance >= amount) {
                balance -= amount;
            }
        }
    }

    public int getBalance() {
        synchronized (lock) {
        //이 블록 내부는 한 스레드만 실행 가능함.
            return balance;
        }
    }
}
```

⇒ 위에 처럼 하면 한 스레드가 balance를 확인하고 차감하는 동안 <br>다른 스레드는 이 블록을 처리할 수 없는 것입니당. 

### b. ReentrantLock

```java
Lock lock = new ReentrantLock();

public void withdraw(int amount) {
    lock.lock();
    try {
        if (balance >= amount) {
            balance -= amount;
        }
    } finally {
        lock.unlock();
    }
}
```

### ⇒ 이 `ReentrantLock` 과 **`synchronized`의 차이는**

 락의 획득과 해제

- **synchronized**: JVM이 자동으로 락을 획득하고 해제합니다. 개발자가 명시적으로 해제할 필요가 없습니다.
- **ReentrantLock**: 개발자가 명시적으로 `lock()`과 `unlock()`을 호출하여 락을 획득하고 해제해야 합니다.

인터럽트 처리 

⇒현재 수행 중인 작업을 중단하고, 즉시 처리해야 할 중요한 작업을<Br> 처리하도록 시스템에 신호를 보내는 것

- **synchronized : 인터럽트 처리 어려움**
- **ReentrantLock :**`lockInterruptibly()` 메서드를 사용하여 <Br>인터럽트 처리 가능.

## c. Atomic Classes

`java.util.concurrent.atomic` 패키지의 클래스들을 사용하여 

원자적 연산을 보장.

- 원자적 연산이 뭔데..
    
    <mark><b>원자적 연산(Atomic Operation)</b></mark>은 실행 도중 중단되거나 
    
    다른 연산과 섞이지 않고, 한 번에 완료되는 연산을 말합니당. 
    
    즉, 원자적 연산은 분할할 수 없는 단일 작업 단위로, 
    
    실행 도중 다른 스레드가 끼어들 수 없다!!! 중요하겠죵
    

### 주요 원자적 클래스

1. **AtomicInteger**: 정수형(int)의 원자적 연산을 제공.
2. **AtomicLong**: 정수형(long)의 원자적 연산을 제공.
3. **AtomicBoolean**: 부울형(boolean)의 원자적 연산을 제공.
4. **AtomicReference<V>**: 객체 참조형의 원자적 연산을 제공.
<br><br>

# ❓아니 근데 그냥 lock이랑 뭐가달라

⇒ 에 다릅니다.  Atomic Class는 `CAS`에 의해서 동작돼요 lock이 아니라.

### CAS의 작동 순서

1. **읽기**: 메모리 위치에서 현재 값을 읽습니다.
2. **비교**: 현재 값이 예상 값과 일치하는지 비교합니다.
3. **교체**: 현재 값이 예상 값과 일치하면, 새로운 값으로 교체합니다.

Java의 `AtomicInteger` 클래스는 내부적으로 <br>CAS를 사용하여 원자적 연산을 수행합니다. 

한 번 예시코드 보실게욤

```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASExample {
    //1. AtomicInteger 초기화. => 초기값 0으로 설정
    private AtomicInteger value = new AtomicInteger(0);

    /*
    * 2.원래 값(AtomicInteger 에 저장된 값)과
    *   oldValue값이 일치하면 newValue 로 교체
    *   값이 변경됐으면 compareAndSet은 false를 반환.
    */
    
    public void increment() {
        int oldValue, newValue;
        do {
            oldValue = value.get(); // 현재 값을 읽음
            newValue = oldValue + 1; // 새로운 값 계산
        } while (!value.compareAndSet(oldValue, newValue)); // CAS 연산 시도
    }

    public int getValue() {
        return value.get();
    }

    public static void main(String[] args) {
        CASExample example = new CASExample();

        // 두 개의 스레드를 생성하여 동시에
        // 각각 1000번씩 increment 메서드를 호출
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 최종 값 출력 (기대값은 2000)
        System.out.println("Final value: " + example.getValue());
    }
}
```

⇒ 만약에 스레드 A랑 스레드 B가 동시에 increment() 메소드를 호출했다고 쳐봅시당. 

일단 첫 번째는 

- `oldValue = 0`
- `newValue = 1`
- `value`의 현재 값이 `0`이므로, `compareAndSet(0, 1)`은 `true`를 반환하고, `value`는 `1`로 설정될거에욤.

그 다음부터, 

1️⃣ 스레드 A는 `oldValue = 1`, `newValue = 2`로 계산 할거잖아요.

2️⃣ 스레드 B는 `oldValue = 1`, `newValue = 2`로 계산할 거고

3️⃣스레드 A 가 먼저 `compareAndSet(1, 2)`를 호출하면 value의 <br>
 현재 값이 2니까 성공적으로 값을 변경시켜용. (3으로)<Br><br>
4️⃣ 이 때 , 스레드 B가 `compareAndSet(1, 2)`를 호출하면, value의 현재 값이 이미 `2` 로 변경되었기 때문에 false를 반환하고, 

다시 value.get()을 통해서 현재 값을 읽어오고 실행하기를 반복하는 거죵. ㄷㄷ
<Br><br>

## d. Concurrent Collections

- 동시성을 보장하는 컬렉션 클래스 사용
<Br>
<aside>
💡 Java의 `java.util.concurrent` 패키지는

동시성 문제를 해결하기 위해 설계된 여러 컬렉션 클래스를 

제공함. 이러한 컬렉션 클래스는 여러 스레드가 동시에 

접근하고 수정할 수 있도록 설계되어 있어, 

동기화 문제를 신경 쓰지 않고도 안전하게 사용 가능.

</aside>

⇒ `ConcurrentHashMap` , `CopyOnWriteArrayList` ,  `BlockingQueue`

등등… 제공하는 메소드가 많은데 

기본적으로 락 분할, 락 프리 알고리즘 사용

❓락 분할 
   <br> ⇒ 큰 락을 여러 개의 작은 락으로 나누어 동시성을 개선 

  ex) `ConcurrentHashMap`

❓락 프리 

`AtomicInteger`  등의 사용 예자와 CAS의 동작원리 

 
한, 번,,, 요약이라는 것을.,,,

| 특징 | Atomic Classes | synchronized | Concurrent Collections |
| --- | --- | --- | --- |
| 기본 원리 | CAS 연산 | JVM 락 관리 | 락 분할, 락 프리 알고리즘 사용 |
| 주요 클래스 | AtomicInteger, AtomicLong 등 | synchronized 메서드 또는 블록 | ConcurrentHashMap, CopyOnWriteArrayList 등 |
| 성능 | 매우 높음 | 경합이 심할 경우 성능 저하 가능 | 매우 높음 |
| 블로킹 여부 | 비블로킹 | 블로킹 | 비블로킹, 효율적 락 관리 |
| 주요 사용 사례 | 단일 변수 동기화 | 복잡한 객체 상태 관리 | 멀티스레드 환경에서의 안전한 컬렉션 사용 |
| 복잡성 | 낮음 | 낮음 | 중간~높음 |
| 동시성 문제 해결 | ABA 문제 | 교착 상태 가능 | 사용법이 복잡할 수 있음, 메모리 사용량 증가 가능 |

결론 : 

- 항상 최소한의 동기화만 사용하여 성능 저하를 막음.
- 가능한 불변 객체를 사용하여 상태 변화를 줄임.
- 병렬 처리가 필요한 부분은 잘게 나누어 동시성을 최대한 활용.

<aside>
💡 병렬 처리가 필요한 부분을 자잘하게 나눈다는 것 : <Br>
큰 작업을 여러 개의 작은 작업으로 나누어 각각의 작은 작업을 <Br>별도의 스레드에서 병렬로 처리.

</aside>
<br>

예를 들어, 대규모 데이터 처리 작업이 있다고 가정한다. <br>
이 작업을 단일 스레드에서 처리하면 시간이 많이 걸림. <Br>
이를 여러 개의 작은 작업으로 나누어 각각을 병렬로 처리하면<br>
더 빠르게 완료 가능. 

⇒ 병렬처리는 동시에 수행하는 것을 이야기함. 

```java
 public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(4);

    // 10개의 작업을 병렬로 실행
    for (int i = 0; i < 10; i++) {
      int taskNumber = i;
      executor.submit(() -> {
        try {
          // 작업마다 1초 지연을 추가하여 실행 시간 확인
          Thread.sleep(1000);
          System.out.println(
              "Executing task " + taskNumber + " by " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }
    executor.shutdown();
  }
}
```

⇒ 이 코드를 보면 4개의 스레드를 직접 지정할 수 있음. 

⇒ 스레드 풀 안에 4개의 스레드가 지정이 되고 

⇒ for문은 직렬로 (순차처리)처리가 되지만,  스레드 풀 내에 저장이 되면 4개의 스레드에서 4개씩 한 번에 병렬로 처리가 됨. !!

그래서, 위의 코드를 실행해보면  (1초씩 지연시켜서)

```java
//한 번
Executing task 1 by pool-1-thread-2
Executing task 3 by pool-1-thread-4
Executing task 0 by pool-1-thread-1
Executing task 2 by pool-1-thread-3
//두 번
Executing task 6 by pool-1-thread-7
Executing task 8 by pool-1-thread-9
Executing task 7 by pool-1-thread-8
Executing task 9 by pool-1-thread-10
//세 번
Executing task 4 by pool-1-thread-5
Executing task 5 by pool-1-thread-6
```

이렇게 병렬로 처리가 된다. 

- 동기화된 코드에서는 가능한 한 간단하고 짧게 유지.