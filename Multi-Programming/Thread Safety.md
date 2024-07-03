# Thread Safety



> 멀티 쓰레드 환경에서 프로그램이 예상대로 동작하도록 보장하는 것

<br>

Java는 멀티 쓰레드를 지원하기 때문에 모든 쓰레드들은 Heap영역을 공유한다.

그렇기 때문에 멀티 쓰레드 환경에서 주의해야 하는 문제는 가시성(visibility)과 원자성(atomicity)이다.

<br>

### 가시성이란?

가시성(visibility)이란 **대상의 존재에 대하여 잘 볼 수 있는 정도 또는 상태**를 말한다.

여기서 대상은 데이터를 말한다.우리가 인스턴스 변수를 생성하면 JVM이 메모리에 할당해주고 변수를 사용할때는 메모리에서 참조해 사용한다라고 알고 있다.

하지만 사실 메모리에서 다이렉트로 참조하는게 아니고 CPU Cache라는 작은 공간에 복사해둔 데이터를 가져와서 사용하는 것이다.

**CPU Cache**란 CPU가 자주 사용할 만한 데이터를 메인 메모리에서 꺼내와 저장해 놓는 임시 저장소이다.

![Untitled](/Multi-Programming/img/cpu_cache.png)

그래서 값을 변경하면 다이렉트로 메모리에 적용되는게 아니고 CPU Cache에 먼저 적용이된다.

하나의 CPU를 사용하면 발생할 문제가 없지만 CPU를 여러개 사용하는 멀티 코어 환경에서는 문제가 발생할 수 있다. 바로 CPU Cache가 저장해둔 데이터를 언제 메모리에 적용하는지에 대해서는 모른다는 것이다. 


<br>

![Untitled](/Multi-Programming/img/cpu_cache(1).png)

모든 CPU마다 CPU Cache를 가지고있어 데이터를 읽고 쓰는 시기에 따라 myVar이 동기화가 되지 않을 수 있다. 즉 쓰레드마다 다른 값을 참조할 수 있다는 거다.

Java는  `volatile` 이라는 키워드로 이 문제를 해결한다.

<br><br>


### volatile

번역하면 휘발성 물질이라고 한다.

왜 이런 이름이 붙었는지는 ([https://m.blog.naver.com/classic2u/50003118713](https://m.blog.naver.com/classic2u/50003118713)) 이 블로그에서 참고하자.


`volatile`  키워드를 사용하면 JVM은 CPU Cache의 값을 읽고 사용하는것이 아니라 실제 메모리에서 값을 읽고 사용한다.

```java
volatile int myVar = 1;
```

- `volatile` 은 변수에만 사용이 가능하다.

다시 가시성이란 **모든 쓰레드가 동일한 데이터를 같은 값으로 보는 상태**라고 정리할 수 있다.

하지만 가시성을 확보한다 해도 완벽한 Thead Safety를 만들 수 없다.

밑에 예제를 보면

<br>

```java
class UnatomicityClass{
	
	volatile int cnt = 0;
	
	public int increase(){
		return cnt++;
	}
}
```

<br>

`cnt++` 은 그냥 봤을때는 값을 증가해주는 하나의 동작을 하는 것 같지만 메모리 레벨에서 보면 메모리에서 `cnt` 값을 가져오고(Read) 값을 증가시켜주고(Modify) 다시 메모리에 저장(Write)해주는 과정을 수행한다. 이런 패턴을 “Read-Modify-Write”라고 한다.

위의 과정을 멀티 프로그래밍 환경에서 수행하면 어떻게 될까? `volatile` 키워드를 사용해서 가시성을 확보했으면 문제가 발생하지 않을까?

Thread1과 Thread2가 위의 `increase()`메서드에  접근하면 `cnt`의 값은 2라고 생각이 들것이다.

문제가 발생할 수 있는 상황을 보자

<br>

1. Thread1은 메모리에서 cnt의 값을 조회한다.(cnt=0) ← Thread1 접근
2. Thread1이 cnt의 값을 증가시킨다.(cnt=1)
3. Thread2가 메모리에서 cnt의 값을 조회한다.(cnt = 0) ← Thread2 접근
4. Thread1이 메모리에 저장한다.(cnt = 1)
5. Thread2가 cnt의 값을 증가시킨다.(cnt = 1)
6. Thread2가 메모리에 값을 저장한다.(cnt = 1)

<br>

위처럼 두 개 이상의 쓰레드가 동시에 접근해서 예기치 않은 결과를 초래하는 상황을  **경쟁 상태(Condition Race)** 라고 한다.

이렇게 가시성을 확보해도 멀티 쓰레드 환경에서 안전하지 않다.

이런 문제는 원자성을 지키지 못한 문제이다.

<br><br>

### 원자성이란?

원자성(atomicity)이란 **어떤 것이 더 이상 쪼개질 수 없는 상태**를 말한다. 

여기서 말하는 어떤 것이란은 연산이다.

 `cnt++` 라는 연산은 “Read-Modify-Write” 라는 3가지의 연산이다.

3가지의 연산을 하나의 연산으로 묶어 원자상태로 만들면 경쟁 상태가 일어나지 않게 될것이다.

Java에서는 `synchronized` 라는 키워드로 이 문제를 해결한다.

<br><br>

### synchronized

> 한 번에 하나의 쓰레드만이 Lock을 사용해 특정 객체의 임계 영역에 접근할 수 있도록 동기화를 제공
> 

- Lock : 여러 쓰레드가 공유자원에 안전하게 접근할 수 있도록 도와주는 매커니즘.
- 임계영역 : 여러 쓰레드들이 공유하는 데이터에 접근하여 작업하는 코드 블럭(공유 자원)

**동기화 메서드**

메서드의 앞에 `synchronized` 키워드를 붙여 메서드 전체가 임계영역으로 설정

```java
Lock lock;

public synchronized void method(){
 //...
}
```


**동기화 블럭**

메서드 내의 코드 일부를 `synchronized{}` 로 감싸 임계영역을 설정

```java
synchronized(this){
	//...
}
```

여기서 this는 lock이다. 모든 객체는 하나의 고유 락을 가지고 있다.
해당 인스턴스의 설정된 임게영역에 접근하기 위해선 해당 인스턴스의 락을 가지고 들어간다 라고 생각하자


<br>

**동기화 메서드 VS 동기화 블럭**

성능을 위해서라면 동기화 블럭을 사용하는것이 맞다. 동기화 블럭은 필요한 부분만 동기화하여 Lock이 걸리는 시간을 최소화 시킬 수 있기 때문이다. 

<br>

`synchronized` 키워드가 붙은 메서드나 블럭으로 연산들을 묶어 하나의 원자로 만들어 경쟁상태를 방지한다. 따라서 하나의 쓰레드가 원자를 처리하고 락을 반납 할 때까지 다른 쓰레드들은 다른 작업을 하지못하고 기다리고 있어야한다.

이런 상태를 **블로킹(Blocking)** 이라고 한다.

블로킹 상태가 지속되면 속도가 느려진다는 단점이 있다.

그리고 두 개 이상의 쓰레드가 서로의 락을 필요해 기다리며 무한 대기 상태인 **교착 상태(Dead Lock)** 에 빠질 수 있다.

<br>

```java
public methodA(){
   synchronized(lockA){
     synchronized(lockB){
     }
   }
}

public methodB(){
   synchronized(lockB){
     synchronized(lockA){
     }
   }
}
```



1. Thread1은 `methodA`에 진입하여 `lockA`를 획득
2. 동시에 Thread2는 `methodB`에 진입하여 `lockB`를 획득
3. Thread1은 이미 `lockA`를 가지고 있고, `methodA`의 실행 중에 `lockB`를 획득해야 하고 Thread2도 이미 `lockB`를 가지고 있고, `methodB`의 실행 중에 `lockA`를 획득해야 한다.

<br>

이 상황에서 Thread1은 `lockA`를 가지고 있으므로 `lockB`를 얻기 위해 대기한다. 동시에 Thread2는 `lockB`를 가지고 있으므로 `lockA`를 얻기 위해 대기한다. 서로가 서로의 자원을 기다리기 때문에 둘 다 상태가 변하지 않고 영원히 대기하게 되는 상황을 **교착 상태(DeadLock)** 라고 한다.
<br>

해결 방법은 스레드가 자원을 일정 시간 내에 얻지 못하면 Lock을 반납하도록 타임아웃을 설정하는 방법과 모든 쓰레드가 Lock을 동일한 순서로 요청하도록 수정한다.

```java
public methodA(){
   synchronized(lockA){
     synchronized(lockB){
     }
   }
}

public methodB(){
   synchronized(lockA){ //methodA와 동일한 순서로 Lock 획득
     synchronized(lockB){
     }
   }
}
```

`synchronized` 를 사용하면 편리하게 원자성을 지킬 수 있지만 블럭킹을 인한 성능 문제와 교착상태에 빠질 위험이 있어 신중하게 사용해야 한다.

<br>

 `volatile` 과  `synchronized`를 함께 사용하면  원자성과 가시성을 지킬수 있다.

```java
private volatile boolean flag;

public synchronized void setFlag(boolean newValue) {
    flag = newValue;
}

public synchronized boolean getFlag() {
    return flag;
}
```

<br><br>

### Atomic 패키지

<br>

Atomic  타입은 CAS라는 알고리즘을 사용해 원자성과 Non-Blocking을 구현하고 내부에서 `volatile` 키워드를 사용해 가시성도 확보해 Thread Safety하게 해준다.

Non-Blocking이란 Thread1이 methodA를 수행하고 있더라도 Thread2가 동일한 methodA를 수행할 수 있음을 의미한다.

CAS(Compare And Swap 또는 Comapare And Set) 알고리즘은 자원값, 기대값, 새로운 값 세 개의 인수가 필요하다.

현재값 : 비교 및 교환할 대상이 되는 변수의 메모리 주소값

기대값 : 현재 값이 이 값과 같은지 비교할 값

새로운 값 : 자원값과 기대값이 일치할 때 설정할 값

1. Thread1이 `methodA`를 수행 중 일 때 Thread2도 methodA를 수행한다.
2. Thread2는 현재값을 복사해 기대값을 가지고 연산을 수행한다. 
3. Thread2가 연산을 마치고 값을 저장할때 현재값과 기대값을 비교해 같으면 새로운값을 적용한다.
4. 만약 현재값과 기대값이 다르면 false를 반환한다.

<br><br>

```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASExample {
    private AtomicInteger value = new AtomicInteger(0);

    public void increment() {
        int oldValue, newValue;
        do {
            oldValue = value.get(); // 현재 값을 읽어온 기대값
            newValue = oldValue + 1; // 새로운 값을 계산
        } while (!value.compareAndSet(oldValue, newValue)); // CAS 연산을 통해 업데이트
    }

    public int getValue() {
        return value.get(); // 현재 값을 반환
    }

    public static void main(String[] args) {
        CASExample example = new CASExample();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        };

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

        System.out.println("최종 카운트 값: " + example.getValue());
    }
}
```

`oldValue` 라는 기대값과 `newValue` 라는 새로운값을 `compareAndSet(oldValue, newValue)`를 실행해 메모리 값이 `oldValue`와 같으면 `true`를 반환해 새로운 값을 적용할 것이다.


<br>


다른 쓰레드가 값을 수정하더라도 CAS는 비교하는 과정에서 다른 값을 감지해 연산을 실패 시키기 때문에 원자성을 지킬 수 있다. 그리고 각 쓰레드들은 독립적으로 수행하고 비교하기 때문에 기다리지 않는다. Non-Blocking이기 때문에 경쟁 상태를 방지하고 Lock을 사용하지 않기 때문에 DeadLock을 방지한다. 

하지만 범위가 단일 변수에 적합하고 복잡한 연산이나 더 정교한 동기화 요구사항에는 맞지 않을 수 있다. 

대표적으로 `AtomicBoolean`, `AtomicInteger`, `AtomicReference`가 있다

Java는 동시성 문제를 다양한 방법으로 해결할 수 있는다. 글에서 설명한 synchronized, volatile, atomic 외 에도 각 스레드별로 고유한 데이터를 유지하고 관리하는 Thread Local과 동기화된 컬렉션 프레임 워크인 Synchronized Collection과 Current Collection이라는 다양한 방식을 제공해준다. 어떤 기술을 사용해 동기화 문제를 해결하는 지는 개발자의 몫이다. 요구사항과 성능을 고려하여 적절한 기술을 선택하는 것이 중요하다.





<br><br><br><br><br>

## Reference

[https://medium.com/javarevisited/master-the-art-of-multithreading-learn-how-to-use-the-volatile-keyword-in-java-for-improved-5d0a1a86dfaf](https://medium.com/javarevisited/master-the-art-of-multithreading-learn-how-to-use-the-volatile-keyword-in-java-for-improved-5d0a1a86dfaf)

[https://rightnowdo.tistory.com/entry/JAVA-concurrent-programming-Atomic원자성?category=396739](https://rightnowdo.tistory.com/entry/JAVA-concurrent-programming-Atomic%EC%9B%90%EC%9E%90%EC%84%B1?category=396739)

[https://www.geeksforgeeks.org/cache-memory-in-computer-organization/](https://www.geeksforgeeks.org/cache-memory-in-computer-organization/)