# Static, Final 키워드

<span style="color : pink;">
핵심 키워드 : 
</span> <br><br>
Static 개념, 사용이유, 장단점 , static의 메모리 공유 

Final 개념, 사용이유, 장단점 , Collection에서의 final 

Static & Final 함께 사용 
<br>
---

글을 작성하기 앞서 좀 요약해보자면 

### Static

- static 키워드는 주로 클래스 변수나 메소드에 사용되어 클래스 로드 시에 메모리에 할당 되며,프로그램 종료시까지 유지된다. 

- 메모리 영역 중 method area 영역에 위치하며, gc의 관리 대상이 아니다.
- 클래스 로드 시에 바로 메모리에 할당 되기 때문에 메모리를 효율적으로 사용 가능하고, 속도상의 이점이 있다.
- 하지만, 전체 thread가 공유하기 때문에, 값의 안정성이 좋지 않고 gc의 관리 대상이 아니라서 메모리 누수에 영향을 줄 수 있다.

### final

- final 키워드는 변수, 메소드, 클래스에 사용가능하며 선언된 대상의 변경을 금지한다.
- final 키워드를 사용한 변수는 변경이 불가능하기 때문에, 코드의 실행 과정에서 변수 값의 안정성을 보장할 수 있다. 

- 변수에 사용하면 값의 재할당 불가, 메소드는 오버라이딩 불가, 클래스는 상속 불가의 개념을 가진다.
- 장점으로는 컴파일 최적화, gc 최적화, thread safety, 예측 가능성이 있고
- 단점으로는 메모리 낭비가 있다.

---

구럼 이제 정리 시작해볼게용 파이널파이널파이널

---

# 1. final 키워드란 무엇이며 어디에 사용 가능한가  ?!

흔히 `불변(immutable)`이라고 불리지만 정확히 말하면 
**재할당이 불가능 하다.** 

붙이는 곳에 따라 의미가 달라진다. 

1️⃣  클래스 : 상속제한

2️⃣  메소드  : 오버라이딩 제한 

3️⃣  변수     : 재할당 제한 

⇒ 특히, 재할당 제한을 위한 final 키워드를 붙일 수 있는 곳은 <br>
     `파라미터`, <br>
     `로컬변수`, <br>
     `catch로 잡은 예외`,<br>
     `for each 문의 변수`, <br>
     `인스턴스 필드 변수` 
이다. 

그럼 이런 final 키워드를 왜 쓰는가를 보면
<br><br>

# 2. 장점  :

1️⃣ 변경 가능성을 최소화 한다. <br>
⇒ 예측 가능한 코드가 된다는 뜻이다. 

```java
public int minus(int a, int b) { 
   return a - b;
}
```

⇒ 만약 다음과 같은 함수가 있다고 가정해보장 신입 개발자 김민제 같은 애가 이거를 

```java
public int minus(int a, int b) { 
   a += 1;
   return a - b;
}
```

이런 코드로 변경을 했다면 minus 함수가 예측 가능한 함수라고 할 수 있을까 ? <br>
전혀 아니당. 
만약 

```java
public int minus(final int a,final int b) { 
   return a - b;
}
```

이렇게 final 키워드를 붙여준다면, <br>
a += 1; 를 시도할 경우 컴파일에러가 나게 된다. <br>
따라서, 한 번 할당한 값을 다시 재할당 할 수 없게 하여 예측가능한 코드가 되도록 해주는 것이다. <br>

2️⃣ 불변 객체는 근본적으로 thread-safe하여, 따로 동기화 할 필요가 없다. 

`중요한 문제다.` !!

멀티 쓰레드 환경에서, 여러 클라이언트에서 한 가지의 객체의 값을 동시에 수정할 때 `thread- safe를 신경쓰는 것은 매우매우 중요`하다. <br>
더군다나, 회사에서나 프로젝트에서나 이 플젝을 수정하는 게 한 사람이 아니기 때문에 

thread-safe 하도록 구현하는 것이 매우매우 중요한 것 !! <br>

thread safety하지 않은 경우는 여기에 정리해놨다. ⇒ [Thread UnSafety](/Multi-Programming/Thread%20UnSafety.md)

아무튼, 2개 이상의 thread가 하나의 공유 자원에 대해 접근할 때, 

1개 이상의 thread에서 값을 변경할 때 thread safe하지 않은 상황이 발생한다. 
이 때, final 키워드를 통해 애초에 값 변경을 막아버린다면, 근본적으로 thread- safe 하다. ! 

    ⚠️ 근데 명심할 게 , final 을 붙여주면 재할당을 제한하는 것이지 immutable하게 만들어주는 것은 아니다. Collection 같은 경우에도 final을 붙여주어도 원소를 추가하거나 삭제하는 것은 가능하다. 

```java
final List<Member> m = new ArrayList<>();
m.add("민제");  //가능 
m = new ArrayList<>(); //불가능 
```

⇒ 이렇듯, Collection에서의 final 키워드는 참조하는 객체를 변경할 수 없음이지, 객체 내부의 값을 변경할 수 없는 것을 의미하지 않는다 <br>⇒ 이에 대해서는 여기에 자세히 써놨다. 

컬렉션을 불변으로 만들기 위해서는 List.of(), Map.of() 를 사용하거나 

unmodifiableXXX를 사용해주면 된다. 

`Java9`에서 도입됐다. 

```java
import java.util.List;
import java.util.Map;

public class ImmutableCollectionsExample {
    public static void main(String[] args) {
        // 불변 리스트 생성
        List<String> immutableList = List.of("A", "B", "C");
        // immutableList.add("D"); // UnsupportedOperationException 발생
    }
}
```

`Java 1.2`에서부터 나온 

```jsx
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmodifiableCollectionsExample {
    public static void main(String[] args) {
        // 원본 리스트 생성
        List<String> originalList = new ArrayList<>();
        originalList.add("A");
        originalList.add("B");
        originalList.add("C");

        // 불변 리스트 생성
        List<String> unmodifiableList = Collections.unmodifiableList(originalList);
        // unmodifiableList.add("D"); // UnsupportedOperationException 발생

    }
}
```

⇒ 이렇게 사용하면 값의 재할당이 불가능한 불변 객체가 된다. 

3️⃣ setter-safe 하다. 

final 키워드가 붙은 인스턴스는 생성자를 통해 생성될 때 값을 초기화하거나, 
고정된 값을 초기화 해주어야만 선언이 가능하다. 
따라서, 외부에서 동적으로 값을 변경할 가능성이 줄어든다. 

4️⃣ GC 최적화 

<b>4_1. gc가 제거할 객체가 생겨나지 않음</b>

```jsx
public class Minje() {
 Member m = new Member("민제", "귀여움");
 m = new Member("인엽", "바보");
}
```

⇒ 만약에 다음과 같은 코드가 생긴다면, 
m 객체가 참조하던 민제 Member는 쓰레기 객체가 될 것이다. 

그래서 GC의 eden 영역에 머물다가 제거가 될 것이당. 

```jsx
public class Minje() {
 final Member m = new Member("민제", "귀여움");
 m = new Member("인엽", "바보"); //컴파일 에러
}
```

⇒ 그러나 다음과 같이 final 키워드를 붙여준다면, 객체의 재할당이 불가능해 

컴파일 에러가 날 것이기 때문에 gc가 제거해줄 객체가 생겨나지 않는다. 

<b>4_2. gc가 객체 검사를 스킵할 수 있게함</b>

```jsx
final String name = "김민제";
final Member m = new Member(name);
```

⇒ 만약, 다음과 같은 코드가 있다고 한다면, gc가 객체 하위의 불변 객체들은 스킵할 수 있게 해준다. 

왜냐면 m이 살아있다는 거 자체가, 하위의 불변객체인 name이 처음에 할당된 상태로 참조되고 있음을 의미하기 때문 (재할당이 불가능하니깐)

결론적으로, 불변 객체를 사용하면 GC가 스캔해야 하는 객체 수가 감소하고, 

스캔해야 하는 메모리 영역과 빈도수가 감소하며, gc의 STW 시간도 감소할 것이당. 

5️⃣ 컴파일러 최적화

```jsx
void TestMethod1() {
  String a = "a";
  String b = "b";
  System.out.println(a + b);
}

void TestMethod2() {
  final String a = "a";
  final String b = "b";
  System.out.println(a + b);
}
```

⇒ 위 두 코드의 실행 결과는 동일하나, 바이트 코드는 다르다 ! 

![스크린샷 2024-07-15 오후 8.54.45.png](/Java/img/finalStatic.png)

![스크린샷 2024-07-15 오후 8.54.55.png](/Java/img/finalStatic2.png)

위측  : final(x) 

아래측 : final(0)

=> final을 붙이지 않았을 때는 
makeConcatWithConstants 연산을 하여 ab를 만들어주는데, 

=> final을 붙였을 때에는 연산 없이 바로 ab를 만들어준당. <br>
final로 인해 컴파일러가 문자열 연결 결과가 절대절대 변하지 않을 것을 알고 있기 때문에 바로 연결하는 것이다. 

아예 연산을 하지 않아서 속도측에서도 final을 사용한 것이 2.5배나 빨랐다고 한다. ([https://www.baeldung.com/java-final-performance#1-performance-test](https://www.baeldung.com/java-final-performance#1-performance-test))

참고자료
<br><br>

# 단점 :

1️⃣ 값이 다르면 반드시 독립된 객체로 만들어야한다. <br>
⇒ 새로운 독립된 객체를 만들 일이 많아진다면,<br>
   이를 만들고 기존에 필요없어진 객체를 제거하는 데에 모두 비용이 들어간다. 



# 결론 :

장점 : 성능, 보안 
단점 : 비용, 메모리 

---

<br><br>

이제 static 이다 !!

# 1. Static 키워드란 ?

한 줄 요약 하자면,,, 
⇒ 클래스 레벨에서 사용되며 메모리 할당과 관리의 효율성을 높임. 
<br><br>

# 2. 특징

- 클래스가 로드될 때 딱 한 번만 메모리에 할당되어, 이후 생성되는 모든 인스턴스에서 공유한다.

       ⇒ 상수값이나 공유해야하는 자원을 관리할 때 유용함. 

            ex) db 연결 인스턴스나 공유 설정 값. 

- static method는 인스턴스 생성 없이 호출될 수 있어, 인스턴스 변수에 접근이 불가능하다.
- static 변수나 다른 static method에만 접근할 수 있다.

⇒ 클래스 레벨에서 작동하여 non static method 보다 빨리 생성되기 때문이다.

❓❔ static 메서드는 왜 인스턴스 변수를 호출하지 못할까?

        => static의 개념을 생각해보면 객체를 생성하지 않는다. 

           이 개념을 잘 생각해보면 다른 클래스의 인스턴스 객체를 생성하지 않은 상태에서 인스턴스 메서드를 호출하면 에러가 나는 것이 당연하다. 여기서 가장 중요한 것은 인스턴스 객체의 생성 여부이다.
<br><br>

# 3. Static 키워드의 사용처

1️⃣ static 변수

```java
public class Counter {
    //static 변수 
    public static int count = 0;
    
    public Counter() {
        count++;
    }
    
    public static void main(String[] args) {
        System.out.println(Counter.count); // 출력: 2
    }
}
```

- 클래스 변수(Class Variable)라고도 불립니당.
- 모든 인스턴스가 공유하는 변수로, 클래스 로딩 시 한 번만 메모리에 할당됩니다.
- 인스턴스 변수와 달리 객체마다 별도로 생성되지 않아용.

2️⃣ static 메소드 

```java
public class Utility {
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static void main(String[] args) {
        int sum = Utility.add(5, 10); // 출력: 15
    }
}
```

- *클래스 메소드(Class Method)**라고도 불립니다.
- 객체 인스턴스 없이 호출할 수 있으며, `static` 변수만 접근할 수 있습니다.

       ⇒ 인스턴스 변수나 메소드는 호출 불가하다는 것.

- 인스턴스 변수나 인스턴스 메소드를 직접 참조할 수 없습니다.

3️⃣ static 블록 

```java
public class StaticBlockExample {
    public static int value;
    
    static {
        value = 10;
        System.out.println("Static block executed.");
    }
    
    public static void main(String[] args) {
        System.out.println(StaticBlockExample.value); // 출력: 10
    }
}
```

- 클래스가 로딩될 때 한 번 실행되는 블록입니당.
- 주로 `static` 변수의 초기화에 사용되어요.

4️⃣ static 내부 클래스

```java
public class OuterClass {
    static class StaticNestedClass {
        public void display() {
            System.out.println("Inside static nested class.");
        }
    }
    
    public static void main(String[] args) {
        OuterClass.StaticNestedClass nested = new OuterClass.StaticNestedClass();
        nested.display(); // 출력: Inside static nested class.
    }
}
```

- 정적 내부 클래스는 외부 클래스의 인스턴스 없이 생성될 수 있습니다.
- 외부 클래스의 인스턴스 변수나 메소드를 직접 참조할 수 없습니담.

## **4. 메모리상 위치**

- 클래스 변수는 JVM Runtime Data Area에서 Method area 에 들어감
    - 클래스는 Method area에, 일반 객체는 heap에 올라감
    - 모든 객체가 메모리를 공유하고, GC의 관리 대상이 아니다.

- static 장점
    - 메모리를 효율적으로 사용 가능
        - 생성할 때마다 인스턴스가 힙에 올라가는 것이 아니라 고정 메모리이므로 효율적
    - 속도가 빠름
        - 객체 생성하지 않고 사용하기 때문에 빠름
            - 클래스가 메모리에 올라갈때 자동적으로 생성
- static 단점
    - 무분별한 static의 사용은 메모리 Leak의 원인이 됨
        - 프로그램 종료 시점에 메모리 반환하는데 gc의 관리 대상이 아니므로..
    - 많이 참조되는 static 변수는 오류 발생 시 디버깅이 힘들다. (전역이라서 추적이 힘듦)
    - 재사용성이 떨어짐
        - interface를 구현하는 데 사용될 수 없음.
        - (인터페이스를 `static`으로 선언할 수 없는 이유는 인터페이스의 목적과 `static` 키워드의 의미가 상충되기 때문. 인터페이스는 클래스가 따라야 할 계약을 정의하는 반면, `static` 키워드는 클래스 수준의 멤버를 의미. 자바의 설계 철학은 이러한 개념적 충돌을 피하기 위해 인터페이스를 `static`으로 선언하는 것을 허용하지 않음.)
        

# 5. 둘을 같이 사용하기  (실무에서 가장 많이 사용돼용)

static final로 선언된 변수는

클래스의 상수로서, 모든 인스턴스에서 공유되며 변경할 수 없는 값이 된다. 

프로그램 전체에서 일괄된 값을 유지해야 할 때 사용. 

```java
public class Constants {
    public static final String BASE_URL = "https://api.example.com";
    public static final int TIMEOUT = 5000;
}
```

- **싱글톤 패턴**: 전역적으로 하나의 인스턴스만 필요할 때 사용.
- **유틸리티 클래스**: 공통으로 사용되는 메소드들을 묶어 놓는 클래스.
- **상수 정의**: 불변의 값을 정의할 때 사용.

---

https://lazypazy.tistory.com/281[https://velog.io/@donghokim1998/final-키워드의-장단점](https://velog.io/@donghokim1998/final-%ED%82%A4%EC%9B%8C%EB%93%9C%EC%9D%98-%EC%9E%A5%EB%8B%A8%EC%A0%90)

[https://jobjava00.github.io/language/java/basic/static/](https://jobjava00.github.io/language/java/basic/static/)