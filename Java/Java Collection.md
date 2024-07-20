# Java Collection

<span style="color : pink">
핵심 키워드 : <br>
</span>

- Collection (interface)
   - Set 
      - HashSet
      - TreeSet
      - LinkedHashSet
   - List
      - ArrayList
      - LinkedList
      - vector
      - stack
   - Queue
      - PriorityQueue
      - Deque
        - LinkedList
        - ArrayDeque
-  Map (interface)
      - HashTable
      - HashMap
      - TreeMap
<br><br>
+ 추가로 힙 정렬 + 이진트리 정렬 
<br>
<br>
드디어…. 자바 컬렉션 ㅠㅠ 흑 <br>
사실 실무에서 자료구조 하나 제대로 알지도 못하면서 <br>
값 담는거 같아 공부하고 싶었다. 이건
<br><br>

# 1️⃣ Java 에서 Collection이란 무엇인가.

⇒ `데이터의 집합, 그룹`을 의미. <br>
    JCF(Java Collections Framework)는 이러한 데이터, 자료구조인 컬렉션과 이를 구현하는 클래스를 정의하는 인터페이스를 제공한다. 

Collection 에는 대표적으로 

`list, Map, Set, Stack, Queue` 와 같은 것들이 있다. 

❓  **Collection을 왜 쓸까** 

- 다수의 data를 다루는데 `표준화된 클래스를 제공`해주어서 자료구조를 직접 구현 x 도 사용 가능
- 배열과 다르게 객체를 보관하기 위한 공간을 미리 지정 x 도 됨.  (`가변적`)

<aside>
<br>
<span style="color : red"> 알고가면 좋을 사항</span><br>
💡 컬렉션 프레임 워크에 저장할 수 있는 데이터는 오로지 `객체`이다. <Br>
⇒ <b>int 나 float 자료형과 같은 자바의 원시 타입은 적재를 못한다는 뜻</b>. <br><br>

따라서, primitive 타입을 wrapper타입으로 변환하며 
Integer 객체나 Float 객체로 `오토박싱`하여 저장하여야 한다. <br> 
⇒ 또한, 객체를 담는 다는 것은 곧 주소값을 담는다는 것이니 null 값도 가능하다.

</aside>

❓❔ 아니 `오토방식` `오토 언박싱`이 뭔데 ?

⇒ Java에는 `기본 타입`과 `Wrapper 클래스`가 존재한당.

`기본 타입`         : int, long, float, double, boolean등 <br>
`Wrapper class` : Integer, Long, Float, Double, Boolean 등 

⇒ 박싱과 언박싱 개념부터 보자 

```java
// 박싱
int i = 10;
Integer num = new Integer(i);

// 언박싱
Integer num = new Integer(10);
int i = num.intValue();
```

자바 1.5부터는 자바 컴파일러가 방식과 언박싱이 필요한 상황에 자동으로 처리를 해준다. 

하지만 .. 성능상의 문제가 있다.

### 성능 :

오토박싱 연산

```java
public static void main(String[] args) {

    // 1. 현재 시간을 밀리초 단위로 가져와 변수 t에 저장
    long t = System.currentTimeMillis();
    
    // 2. Long 타입의 변수 sum을 선언하고 0L로 초기화
    Long sum = 0L;
    for (long i = 0; i < 1000000; i++) {
        sum += i;
    }
    // 3. 현재 시간을 다시 측정하고, 처음 시간 t를 빼서 실행 시간을 계산
    System.out.println("실행 시간: " + (System.currentTimeMillis() - t) + " ms");
}

// 실행 시간 : 19 ms
```

 동일타입 연산 

```java
public static void main(String[] args) {
    long t = System.currentTimeMillis();
    long sum = 0L;
    for (long i = 0; i < 1000000; i++) {
        sum += i;
    }
    System.out.println("실행 시간: " + (System.currentTimeMillis() - t) + " ms") ;
}

// 실행 시간 : 4 ms
```

100만건 기준으로 약 `5배의 성능 차이`가 난다. 

따라서 서비스 개발하면서 **불필요한 오토 캐스팅이 일어나는 지 확인하는 습관**을 가지자. <br><br>

# 2️⃣ Java Collection Framework의 상속구조

![스크린샷 2024-07-16 오후 11.51.34.png](/Java/img/Collection.png)

⇒ Collection 인터페이스는 `list, set, queue`로 크게 3가지 상위 인터페이스로 분류할 수 있다. <br>
⇒ list와 set은 특징적으로 비슷한 구조를 지녀 같은 Collection 인터페이스를 상속받고, <br>
⇒ `Map`의 경우 Collection 인터페이스를 상속받고 있지 않지만, Collection으로 분류된다. 

![스크린샷 2024-07-16 오후 10.48.20.png](/Java/img/Collection2.png)

⇒ 대충 요약하자면 이렇다

## 1)  Set 인터페이스

![스크린샷 2024-07-17 오전 12.47.11.png](/Java/img/set.png)

 ⇒ `순서를 유지하지 않는` 데이터의 집합으로 `데이터의 중복을 허용하지 않음`. 

  

### 1_1) HashSet

![스크린샷 2024-07-17 오전 12.47.48.png](/Java/img/Hash.png)

- HashTable과 이중 연결 리스트의 조합 !! 배열과 연결 노드를 결합한 자료구조 형태
- 내부적으로 `HashMap` 사용 ⇒ 삭제, 삽입, 탐색 시간 복잡도 O(1)
- 가장 빠른 임의 접근 속도
- 추가 삭제 검색 접근성이 모두 뛰어남
- 순서 예측 전혀 불가능
- 순서 예측을 하고싶으면 LinkedHashSet 클래스 이용

### 1_2) TreeSet

- 정렬방법을 지정 가능
- 이진검색트리 자료구조의 형태로 데이터를 저장
- `요소의 자연 순서에 따라` 요소들을 `정렬`
- 오름차순으로 정렬한다는 뜻
- 내부적으로 TreeMap을 사용

<aside>

```java
//TreeSet생성
TreeSet<Integer> set = new TreeSet<Integer>();

//값추가
set.add(7); 
set.add(4);
set.add(9);
set.add(1);
set.add(5);
```
<br>
⇒ 다음과 같이값을 넣으면 내부적으로 

![스크린샷 2024-07-17 오전 12.47.11.png](/Java/img/set2.png)

⇒ 이렇게 값이 추가되는 순서대로 정렬이 된다. 

💡 이진 검색 트리 자료구조 

✅ 이진탐색 :

👍 탐색에 소요되는 시간복잡도는  **O(logN)**  

     ⇒ 탐색하고자하면 `중간값` 먼저 찾고 
     ⇒ 그 `중간값` 을 기준으로 찾고자 하는 값을 정함. 

     ⇒ 즉, 매 단계마다 탐색 범위가 절반으로 줄어듦]
⛔ but 삽입 삭제 불가

        //1. 여기서 7을 찾는 과정
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

        //2. 중간 값 '5'와 비교
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
             ^
        7 > 5 -> 오른쪽 절반 선택

        //3. 오른쪽 절반 '[6,7,8,9,10]' 의 중간값 8과 비교
        [6, 7, 8, 9, 10]
              ^
        7 < 8 -> 왼쪽 절반 선택

        //4. 왼쪽 절반 '[6,7]' 의 중간값 '7'과 비교 
        [6, 7]
            ^
        7 == 7 -> 값 찾음

✅ 연결리스트 : 

👍 삽입, 삭제의 시간 복잡도는 O(1) [입력 크기와 관계없이 항상 일정한 시간이 소요) <br>
⛔ but 탐색 시간복잡도가 O(N) [입력 값에 비례하여 시간이 증가]

이 두가지를 합하여 장점을 모두 얻는 것이 `이진탐색트리` 
즉, 효율적인 탐색 능력을 가지고, 자료의 삽입 삭제도 가능하게 만들자 !! 

![스크린샷 2024-07-17 오전 1.00.45.png](/Java/img/Tree.png)

특징 : 
- 각 노드의 자식이 2개 이하 

- 노드의 왼쪽 자식은 부모보다 작고, 오른쪽 자식은 부모보다 큼.
- 중복 노드 없어야함

⇒ 검색 목적 자료구조인데, 굳이 중복이 많은 경우에 트리를 사용하여 

     검색 속도를 느리게 할 필요가 없음. 

</aside>

## 2) List 인터페이스

![스크린샷 2024-07-17 오전 12.22.09.png](/Java/img/List.png)

✅ 순서가 있는 데이터의 집합으로 데이터의 중복을 허용 

✅ 직접 @Override 를 통해 사용자가 정의 사용 가능하고, 

⇒ 대표적인 구현체로 <br>
`ArrayList , LinkedList, Vector, Stack` 이 있다. <br>
✅ 리스트와 배열의 가장 큰 차이는 리스트는 자료형 크기가 고정이 아니라 `동적으로 늘어났다 줄어들 수 있다`는 것이다. 

### 2_1) LinkedList (배열이 아님)

![스크린샷 2024-07-17 오전 12.00.18.png](/Java/img/LinkedList.png)

연속적인 메모리 위치에 저장되지 않는 선형 데이터 구조 (포인터를 통해 연결)
각 노드는 데이터 필드와 다음 노드에 대한 참조를 포함하는 노드로 구성 

### ❓왜 쓰나 ❔

⇒ 배열은 크기가 고정되어 있어 미리 요소의 수에 대해 할당 받아야함.<Br>
⇒ 새로운 요소를 삽입하는 것은 비용이 많이 듦(공간을 만들고, 기존 요소 전부 이동)

👍 장점 : 동적 크기, 삽입/삭제 용이 

⛔ 단점 : 임의로 액세스 허용 불가. 

- 양방향 포인터 구조로 데이터의 삽입, 삭제가 빈번할 경우 
데이터의 위치정보만 수정하면 되기에 유용
- 스택, 큐, 양방향 큐 등을 만들기 위한 용도로 쓰임. 
⇒ 추후 나오지만, 큐와 같은 경우 선입선출 구조이기 때문에 LinkedList로 구현하는 것이 삭제된 데이터의 이동이 일어나지 않아서 좋다. 

### 2_2) Vector

- ArrayList의 구형 버전 (내부 구성이 거의 비슷)
- 과거에 대용량 처리를 위해 사용되었으며, 내부에서 자동으로 `동기화처리가 일어나 비교적 성능이 좋지 않고` 무거워 잘 안쓰임
- 구버전 자바와 호환성을 위해 남겨두어 잘 쓰이지는 않음.

![Untitled](/Java/img/Untitled.png)

⇒ 내부적으로 다 동기화 처리됨. 

<aside>
💡 만일, 현업에서 컬렉션에 동기화가 필요하면 
`Collections.synchronizedList()` 메소드를 이용해 <br>ArrayList를 동기화 처리하여 사용한다.

</aside>

### 2_3) ArrayList

![스크린샷 2024-07-17 오전 12.24.09.png](/Java/img/ArrayList.png)

- 단방향 포인터 구조로 각 데이터에 대한 인덱스를 가지고 있어 `조회 기능에 성능이 뛰어남`.
- 자바의 vector를 개선한 배열로 구현된 list. ⇒ 데이터가 저장된 순서가 같다는 뜻.

       ⇒ vetcor는 용량 초과 때 마다 두 배로 늘어나고 자동 동기화 처리가 되어있어서 성능이 별로,    

       => ArrayList는 1.5배로 늘어나고 동기화 처리 안되어있음. 

- `데이터의 삽입 삭제가 느리다`<br>
 ⇒  순차적으로 추가/삭제 하는 경우에는 가장 빠름. O(1) 시간 복잡도
### 2_4) stack

![스크린샷 2024-07-17 오전 12.27.30.png](/Java/img/Stack.png)

✅ 후입선출 구조 (LIFO) ⇒ 마지막에 들어온 원소가 처음으로 나감

✅ 들어올 때는 push, 나갈 때는 pop

✅ Vector를 상속하기 때문에 잘 안씀 (대신 ArrayDeque사용 ⇒ Queue 할 때 나옴)

![Untitled](/Java/img/Untitled%201.png)

## 3) Map 인터페이스

![스크린샷 2024-07-17 오전 1.02.53.png](/Java/img/Map.png)

⇒ `Key, Value`의 쌍으로 이루어진 데이터의 집합으로, 

⇒ `순서는 유지되지 않고` 

⇒ `키의 중복을 허용 x` `값의 중복은 허용 o` 

### 3_1) Hashtable

- 자바 초기 버전에 나온 레거시 클래스
- 키를 특정 해시 함수를 통해 해싱한 후 나온 결과를 배열의 인덱스로 사용하여 벨류를 찾는 방식
- HashMap보다는 느리지만 동기화 지원
- null불가 ⇒ 컴파일 에러 발생

### 3_2) HashMap

- 중복과 순서가 허용되지 않으며 null 가능
- 순서를 보장하기 위해서는 LinkedHashMap 사용
- 비동기로 동작하여 멀티 쓰레드에서는 어울리지 않음 ( 대신, ConcurrentHashMap 사용)

### 3_3) TreeMap

- 정렬된 순서대로 키와 값을 저장하여 검색이 빠름.
- tree구조라서 어느정도 순서를 보장
- 키의 자연순서에 따라 요소들을 정렬

### 3_4) properties 클래스

⇒ Properties(String, String)의 형태로 저장하는 단순화된 key-value 컬렉션 

## 4) Queue 인터페이스

![스크린샷 2024-07-17 오전 12.30.30.png](/Java/img/Queue.png)

- 선입선출  FIFO 구조

Queue는 인터페이스를 구현한 class 로는 `PriorityQueue, ArrayDeque` 가 있당. 

### 4_1 PriorityQueue

⇒ `우선 순위`를 가지는 큐

⇒ 원소에 우선순위를 부여하여 우선 순위가 높은 순으로 정렬되고 꺼낸다. 

⇒ 사용처는 수행할 작업이 여러개, 시간이 제한되어 있을 때 우선순위가 높은 것부터 수행할 때 쓰임. 

     (작업 스케쥴링)

⇒ 저장공간으로 배열을 사용하며, 각 요소를 `heap 형태`로 저장. 

### 4_2 deque 인터페이스

⇒ 조상은 Queue
⇒ 구현체로 ArrayDeque, LinkedList가 있음. 

⇒ 양쪽으로 넣고 빼는 것이 가능한 큐 
⇒ 스택과 큐를 하나로 합친 것으로 스택, 큐 모두 사용 가능. 

     4_2_1 .`ArrayDeque` 클래스 

⇒ stack 으로 사용될 때는 stack class보다 빠르고 
⇒ 대기열로 사용할 때는 LinkedList 보다 빠름. 

4_2_2. `LinkedList` 는 List 인터페이스와 Queue 인터페이스를 동시에 상속받아서 스택 큐로서도 응용 가능. 

<aside>
💡 Queue는 데이터를 꺼낼 때 항상 첫번째 저장된 데이터를 삭제하므로,

ArrayList와 같은 배열 기반의 컬렉션 클래스를 사용한다면, 

데이터를 꺼낼 때마다 빈 공간을 채우기 위해 데이터의 이동 & 복사가 발생하여 비효율적임. 
따라서, ArrayList보다는 LinkedList로 구현한당. 

</aside>
<br><br>

# 결론 : 

```
ArrayList
리스트 자료구조를 사용한다면 기본 선택임의의 요소에 대한 접근성이 뛰어남순차적인 추가/삭제 제일 빠름요소의 추가/삭제 불리

LinkedList
요소의 추가/삭제 유리임의의 요소에 대한 접근성이 좋지 않음

HashMap / HashSet
해싱을 이용해 임의의 요소에 대한 추가/삭제/검색/접근성 모두 뛰어남검색에 최고성능
(get 메서드의 성능이 O(1) )

TreeMap / TreeSet
요소 정렬이 필요할때 검색(특히 범위검색)에 적합 
그래도 검색 성능은 HashMap보다 떨어짐

LinkedHashMap / LinkedHashSet 
HashMap과 HashSet에 저장 순서 유지 기능을 추가

Queue : 스택(LIFO) / 큐(FIFO) 자료구조가 필요하면 ArrayDeque 사용

Stack, Hashtable : 가급적 사용 X (deprecated)
```

---

[https://gyoogle.dev/blog/computer-language/Java/Auto Boxing & Unboxing.html](https://gyoogle.dev/blog/computer-language/Java/Auto%20Boxing%20&%20Unboxing.html)

[https://inpa.tistory.com/entry/JCF-🧱-Collections-Framework-종류-총정리](https://inpa.tistory.com/entry/JCF-%F0%9F%A7%B1-Collections-Framework-%EC%A2%85%EB%A5%98-%EC%B4%9D%EC%A0%95%EB%A6%AC)

[https://gyoogle.dev/blog/computer-science/data-structure/Binary Search Tree.html](https://gyoogle.dev/blog/computer-science/data-structure/Binary%20Search%20Tree.html)

[https://inpa.tistory.com/entry/JCF-🧱-Collections-Framework-종류-총정리](https://inpa.tistory.com/entry/JCF-%F0%9F%A7%B1-Collections-Framework-%EC%A2%85%EB%A5%98-%EC%B4%9D%EC%A0%95%EB%A6%AC)

[Inpa Dev 👨‍💻:티스토리]
