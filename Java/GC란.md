# Garbage Collection
---


## GC란?

Garbage Colllection은 JVM의 Heap 영역에 동적으로 할당했던 메모리 중 필요없게 된 객체를 제거해주는 프로세스이다.

<br>

간단한 예제를 통해 GC가 어떤 역할을 하는지 알아보자.

```java
public void static main(String args){
	String name = new String("홍길동");
	name = new String("김인엽");
}
```

`name` 이라는 지역변수의 값을 바꾸는 아주 간단한 코드이다.

JVM 내에서 변수의 참조가 어떻게 변하는지 보자

<br>
<br>

1. **main 메서드 시작** 
main 메서드가 호출되면 JVM은 main 메서드에 대한 스택 프레임을 생성한다. 이 스택 프레임에는 `args`와 `name` 변수를 포함하고있다.(args는 그림에서 포함하지 않음)
<br>

2. **첫 번째 문자열 객체 생성(”홍길동”)**
`new String("홍길동");` 이 실행이 되면 JVM이 Heap영역에  String 객체를 생성하고 주소를 할당해준다. 이 객체는 “홍길동”이라는 문자열 데이터를 가지고있다.

![GC(1)](/Java/img/GC(1).png)
<br>

3. **객체 참조**
    
    `String name = new String(”홍길동”);`
    
    생성된 홍길동 객체의 주소가 main 메서드의 스택 프레임 내의 `name` 변수에 저장이 된다.
    
    `name` 변수가 홍길동 객체를 참조하고있다.
    

![GC(2)](/Java/img/GC(2).png)

<br>

4. **두 번째 문자열 객체 생성(”김인엽”)**
    
    `new String("김인엽");` 이 실행이 되면 JVM이 또 다시 Heap영역에 새로운 String 객체를 생성하고 주소를 할당해준다. 이 객체는 “김인엽”이라는 문자열 데이터를 가지고있다.
    
![GC(3)](/Java/img/GC(3).png)

<br>

5. **객체 참조**
    
    `name = new String(”김인엽”);`
    
    이전에 `name` 변수가 참조하던 홍길동 객체에 대한 참조는 사라지고 김인엽 객체의 주소를 참조하게 된다.
    

![GC(4)](/Java/img/GC(4).png)

결국 홍길동이라는 데이터를 가진 객체는 아무런 참조되고 있지 않다. 즉 쓸모가 없어진 객체가 된것이다.(Garbage) 이런 객체들이 Heap 영역에 쌓인다면 메모리가 꽉 차게 될것이다.

이런 객체(Garbage)들을 정리해주는 역할을 하는게 Garbage Collection이다.
자바는 이런 GC 덕분에 한정된 메모리를 효율적으로 사용할 수 있게 해준다. 

<br>
<br>

## GC의 수거 대상

GC는 위의 예제 처럼  **“아무런 참조가 되고있지 않은상태”** 들을 대상으로 제거가 된다. 
이런 상태를 **Unreachable**이라 한다.

- Reachable : 객체가 참조되고 있는 상태
- UnReachable : 객체가 참조되고 있지 않은 상태(GC의 대상)

조금 더 자세히 말하면 Root Set을 기준으로 객체가 참조 되고 있는 대상을 정한다.

<br>

**Root Set이란?**
- Root Set은 Garbage Detection을 마킹하기 위한 출발점이며, Heap 외부에서 내부로 접근한 상태의 참조 값들을 의미. 
- Method 영역의 정적 변수에 의한 참조
- Stack영역의  java 메서드 실행 시에 사용하는 지역 변수와 파라미터들에 의한 참조
- Native Stack 영역의 JNI에 의해 생성된 객체에 대한 참조


![GC(5)](/Java/img/GC(5).png)

위의 사진처럼 Root Set으로 부터 시작된 참조들은 모두 Reachable 한 객체들이고, Root Set과는 무관한 객체들은 Unreachable 객체들이다.
<br>

<aside>
💡 *java.lang.ref 패키지를 사용하면 GC의 객체 수거를 간접적으로 제어할수있다.*

</aside>

<br>
<br>

## GC의 기본 원리
<br>

GC는 기본적으로 Mark And Sweep이라는 알고리즘으로 동작한다.
GC는 Heap영역에서 Root Set으로부터 시작해 모든 변수를 스캔하면서 각각 어떤 객체를 참조하고 있는지 찾아서 마킹한다. 

1. Marking : Garbage 대상인지 아닌지 식별하는 작업
- GC는 Heap영역에서 Root Set으로부터 시작해 모든 변수를 스캔하면서 각각 어떤 객체를 참조하고 있는지 찾아서 마킹한다. 
    
    ![GC(6)](/Java/img/GC(6).png)
    
    

2. Sweep : Unreachable 객체를 삭제하는 작업
    
    ![GC(7)](/Java/img/GC(7).png)
  
3. Compact : Sweep 후에 분산된 객체들을 압축해주는 작업
- 사용하는 GC의 종류에 따라 실행될수도있고 안될수도있다.
- Compact과정까지 처리하는 알고리즘을 Mark And Compact 알고리즘이라 한다.
    
    ![GC(8)](/Java/img/GC(8).png)
    
    
    

하지만 이처럼 GC가 일어날때마다 매번 모든 객체들을 마킹을 하고 삭제하는 과정을 거친다면 매우 비효율적일것이다.
Heap 영역에 점점 더 많은 객체가 할당됨에 따라 시간도 점점 늘어날테니
그래서 GC는 Heap 영역을 Young 영역과 Old 영역으로 분리되어 메모리를 관리한다.

<br>

![GC(9)](/Java/img/GC(9).png)

**Young 영역**
- Young 영역은 생성된 지 얼마 안된 객체들이 할당 받는 영역
- 대부분의 객체  Unreachable 객체이기 때문에, 많은 객체들이 Young영역에 생성되었다 사라진다.
- Young 영역에서 발생한 GC를 **Minor GC**라 한다.
- Young 영역은 또 다시 Eden과 Survivor로 나뉜다.
    - Eden : 객체가 생성이 되면 가장 먼저 할당 받는 곳 , Eden이 꽉차면 Minor GC가 발생한다.
    - Survivor : Eden이 꽉차서 Minor GC가 발생하고 살아남은 객체들이 넘어오는곳(Survivor은 S0과 S1로 나뉘는데 Minor GC가 발생할때마다 살아남은 객체들이 왔다갔다 한다. 둘 중 한Survivor 공간은  꼭 비어있는 상태여야한다.)

**Old 영역**

- Young 영역에서 끝까지 살아남은 객체들이 복사되는 영역
- Young 영역보다 크게 할당받는다. 크기가 큰 만큼 Young 영역 보다 GC는 적게 발생한다.
- Old 영역에서 발생한 GC를 **Major GC**라 한다.

**Permanent 영역**

- Permanent Genration 영역으로 PermGen이라 줄여 말한다.
- PermGen == Methoda Area, 클래스의 정보들이 담겨져있다.
- java8 이후 부터는 MetaSpace로 대체되고 Native 영역으로 옮겨졌다.

<br><br>

## GC의 동작 과정

그러면 이제 분리된 Heap 영역에서 GC가 어떻게 동작하는지 알아보자.
<br>

1. 새로운 객체가 생성이 되면 Eden에 할당된다.
    
    ![GC(10)](/Java/img/GC(10).png)
    

2. 객체가 계속 생성되서 Eden 영역이 가득 차게 되면 Minor GC가 발생한다.
    
    ![GC(11)](/Java/img/GC(11).png)
    

3. Mark And Sweep 알고리즘을 통해 참조된 객체들은 Survivor 영역중 한 곳으로 이동되고, 참조되지 않은 객체들은 삭제되어 없어진다. Survivor 영역으로 넘어간 객체는 Age값이 1씩 증가한다. 
    
    <br>
    <aside>
    💡 Age 값이란 Survivor영역에서 객체가 살아남은 횟수를 의미. Age값이 임계값에 다다르면 Old 영역으로 넘어간다. Hot Spot JVM의 경우 임계값은 31이다.
    </aside>
    
    ![GC(12)](/Java/img/GC(12).png)
    

4. 또 다시 객체가 생성되서 Eden 영역이 가득차면 Minor GC가 발생한다.
이번에 살아남은 객체는 다른 Survivor 영역으로 이동된다. 마찬가지로 살아남은 객체들은 age값이 1 증가한다. 이전에 살아남은 객체들은 age가 2인게 보인다.
<br>
    <aside>
        💡 왜 Survivor 영역간의 이동 과정이 있는걸까?<br>
        객체가 Survivor 영역을 반복해서 이동할 때, 가비지 컬렉션 과정에서 비어있는 공간이 메모리 내에서 압축되어 메모리를 효율적으로 만들어준다.
    </aside>
    
![GC(13)](/Java/img/GC(13).png)
    

5. 계속 동일한 프로세스가 반복된다. 살아남은 객체는 S0에서 S1로 계속 옮겨 다니며 Age값이 올라간다.
    
    ![GC(14)](/Java/img/GC(14).png)
    

6. 이후 Age값이 임계값에 도달하면 (사진에선 8) Old 객체로 복사된다. 이를 Promotion이라 한다.
    
    ![GC(15)](/Java/img/GC(15).png)
    

7. 위의 동일한 프로세스가 반복되면 결국에는 Old 영역도 꽉차게 되는데 그러면 Major GC가 발생하게 되는것이다.
    
    ![GC(16)](/Java/img/GC(16).png)
    

JVM은 이러한 방식으로 Heap의 영역을 나누어 효과적으로 메모리를 관리한다.

이런 방식이 가능한 이유는 두 가지의 전제조건이 있기 때문이다.

- 대부분의 객체는 금방 접근 불가능 상태(Unreachable)가 된다.
- 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.

이러한 가설을 'weak generational hypothesis'라 한다.

<br>
<br>

## GC 알고리즘 종류

GC 알고리즘 종류를 알아보기 전에  Stop the world에 대해 알아보고 들어가자
<br>

**Stop The World**란?

GC를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는것. stop-the-world가 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다. GC 작업을 완료한 이후에야 중단했던 작업을 다시 시작한다. 어떤 GC 알고리즘을 사용하더라도 stop-the-world는 발생한다. GC 튜닝이란 이 stop-the-world 시간을 줄이는 것을 말한다.
<br>

### Serial GC

- 초기 GC
- GC를 처리하는 쓰레드가 1개(싱글 쓰레드)
- Young 영역에서는 Mark And Sweep, Old 영역에서는 Mark And Compact 알고리즘을 사용한다.
- 적은 메모리와 CPU 코어 개수가 적을 적합한 방식
- 거의 사용되지 않는다.
<br>


### Parallel GC

- Serial GC와 기본적으로 알고리즘이 같다.
- Serial GC는 GC를 처리하는 스레드가 하나인 것에 비해, Parallel GC는 GC를 처리하는 쓰레드가 여러 개이다(멀티 쓰레드)
- 자바 8버전 LTS 기본 GC
- 멀티 쓰레드가 필요한 동시성이 중요한 프로그램에 적합한 방식

<br>

### **Concurrent Mark Sweep(CMS)**

- 어플리케이션의 쓰레드와 GC 쓰레드가 동시에 실행되어 stop-the-world 시간을 최대한 줄이기 위해 고안된 GC
- CMS GC는 Java9 버젼부터 deprecated 되었고 결국 Java14에서는 사용이 중지되었다.
<br>


### G1(Garbage First) GC
![GC(17)](/Java/img/GC(17).png)

- G1 GC는 바둑판의 각 영역(region)에 객체를 할당하고 GC를 실행한다
- 메모리가 많이 차있는 영역(region)을 인식하는 기능을 통해 메모리가 많이 차있는 영역을 우선적으로 GC한다.
- Java 11 LTS, Java 17 LTS 의 기본 GC
<br>

### ZGC(Z Garbage Collector)

- Java 21LTs의 기본 GC
- G1의 Region처럼, ZGC는 ZPage라는 영역을 사용
- ZGC는 힙 크기가 증가하더라도 Stop The World의 시간이 절대 10ms를 넘지 않는다.
- G1의 Region은 크기가 고정인데 비해, ZPage는 동적으로 크기가 변한다.

<br><br>

## GC 튜닝

STW(Stop-The-World) 는 GC가 메모리 회수를 위해 어플리케이션이 중지되는 시간을 말한다.
STW가 많이 발생하면 어플리케이션의 성능이 안좋아질 수 밖에 없다.
STW를 줄여 어플리케이션의 지연시간을 최소화 하는것이 GC 튜닝이다.
<br>

**힙 크기 조절**

보통의 어플리케이션들은 데이터베이스나 타 서비스를 연결해서 사용한다. 이런 연결된 서비스의 경우 어플리케이션은 지속적으로 핑을 보내 연결 상태를 확인한다.  여기서 만약 STW의 시간이 커넥션의 타임아웃보다 길다면 커넥션이 끊어져 데이터베이스나 타 서비스와 통신이 불가능해진다.

커넥션의 비용은 아주 비싸다.  3-way handshake나 TCP/IP 연결과 같은 다양한 작업을 수행해야 하기 때문이다. 이렇게 GC가 발생할때마다 커넥션이 떨어지고 다시 획득한다면 어플리케이션의 성능이 저하 될것이다. 이럴 경우는 힙의 크기, 올드영역의 크기를 줄여Full GC의 빈도를 더 자주 발생하도록 하면, 커넥션이 끊어지기 전에 GC를 완료 할 수 있을 것이다. 

이렇게 힙 크키를 조절만 해줘도 GC튜닝이 가능하다. 밑의 표는 JVM옵션들이다.

| 구분 | 옵션 | 설명 |
| --- | --- | --- |
| 힙(heap) 영역 크기 | -Xms | JVM 시작 시 힙 영역 크기 |
|  | -Xmx | 최대 힙 영역 크기 |
| New 영역의 크기 | -XX:NewRatio | New 영역과 Old 영역의 비율 |
|  | -XX:NewSize | New 영역의 크기 |
|  | -XX:SurvivorRatio | Eden 영역과 Survivor 영역의 비율 |

<br>

**GC 알고리즘 선택**

운영 중인 시스템 특성에 따라 적합한 GC 방식이 다르므로 해당 시스템에 가장 적합한 방식을 찾아 GC 알고리즘을 선택해야 한다.

| 구분 | 옵션 |
| --- | --- |
| Serial GC | -XX:+UseSerialGC |
| Parallel GC | -XX:+UseParallelGC
-XX:ParallelGCThreads=value |
| G1 | -XX:+UnlockExperimentalVMOptions
-XX:+UseG1GC |


<br><br><br><br><br><br>

참조
---



[https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html](https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html)

[https://d2.naver.com/helloworld/329631](https://d2.naver.com/helloworld/329631)

[https://inpa.tistory.com/entry/JAVA-☕-가비지-컬렉션GC-동작-원리-알고리즘-💯-총정리#가비지_컬렉션_청소_방식](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EA%B0%80%EB%B9%84%EC%A7%80-%EC%BB%AC%EB%A0%89%EC%85%98GC-%EB%8F%99%EC%9E%91-%EC%9B%90%EB%A6%AC-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%F0%9F%92%AF-%EC%B4%9D%EC%A0%95%EB%A6%AC#%EA%B0%80%EB%B9%84%EC%A7%80_%EC%BB%AC%EB%A0%89%EC%85%98_%EC%B2%AD%EC%86%8C_%EB%B0%A9%EC%8B%9D)

[https://velog.io/@composite/그래도-자바-최신-버전이-좋은-이유-GC](https://velog.io/@composite/%EA%B7%B8%EB%9E%98%EB%8F%84-%EC%9E%90%EB%B0%94-%EC%B5%9C%EC%8B%A0-%EB%B2%84%EC%A0%84%EC%9D%B4-%EC%A2%8B%EC%9D%80-%EC%9D%B4%EC%9C%A0-GC)
