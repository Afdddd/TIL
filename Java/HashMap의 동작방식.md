# HashMap

<span>
핵심 키워드 :  <br>
- 객체 참조 <br>
- 해시 테이블 <Br>
- 선형 복잡도 <Br>
- 배열 (버킷) <br>
- 해시 함수 <br>
- equals() / hashCode()와의 상관관계 및 성능
</span>

<Br>

- HashMap
    
    <aside>
    💡 Hash란  <b>(Key + Value)</b>를 한쌍으로 하고
    
    데이터를 효율적으로 저장하고 빠르게 찾기위한 자료구조이다.
    
    일반 배열과 Hash의 차이를 알아보고, HashMap의 원리에 대해 설명하겠다.
    <br><br>
    
    ### 1️⃣ 배열에서 데이터 찾기
    
    일반 배열에서 데이터를 찾기 위해선 **선형 구조로 모든 배열의 값을 조회해서** 
    
    해당 값을 찾는 구조이다.
    
    ❓선형 구조란? <br>
    **=> 자료를 구성하는 원소들을 하나씩 순차적으로 나열시킨 형태**
    
    ```java
    String[] arr = {"김","이", "박"};
    
    for(int i=0; i<arr.length; i++){
    	if(arr[i].equals("김")){
    		return i;
    	}
    }
    ```
    
    이렇게 길이가 작은 배열이라면 값을 찾는데 오래걸리지 않지만, 
    
    만약 배열의 길이가 100,000이상이라면 for문을 100,000이상 반복해야 한다.
    
    배열의 길이가 길어질수록 값을 찾는데 시간이 오래 걸릴 것이다.
    
    ![Untitled](/Java/img/HashMap(13).png)
    
    입력값이 증가함에 따라 시간 또한 같은 비율로 증가하는것을 
    
    <b>선형 복잡도(linear complexity)</b>라 한다. O(n)
    
    ### 2️⃣ Hash에서 데이터 찾기
    
    ```java
    HashMap hash = new HashMap<String,Integer>();
    hash.put("김",1);
    hash.put("이",2);
    hash.put("박",3);
    return hash.get("김");
    ```
    
    Hash는 put(key, value)을 통해 값을 저장하고 get(key)를 통해 값을 가져온다.
    
    일반 배열의 특정 인덱스 값을 지정해 데이터를 찾는 방식과 같다.
    
    ```java
    hash.get(”김”) == arr[0];
    ```
    
    Hash는 데이터를 찾을 때 모든 값을 조회하지않고 key값을 통해 value 값을 꺼내온다. Hash에 아무리 많은 데이터가 있어도 특정 Key값만 주어지면 즉시 값을 꺼내 올 수있다.
    
    ![Untitled](/Java/img/HashMap(1).png)
    
    이렇게 입력값이 증가하더라도 시간이 늘어나지 않는것을 
    
    **일정한** **복잡도(constant complexity)**라한다. O(1)
    
    Hash가 어떻게 값을 빨리 불러오고 저장하는지 알아보자
    
    ### 3️⃣HashMap의 동작원리
    
    HashMap은 배열(Bucket)과 
    
    해시 함수(Hash Function)로 구현되어있다.
    
    ![Untitled](/Java/img/HashMap(2).png)
    
    ❓Hash Fucnction이란?
    
    임의의 데이터를 정수로 변환하는 함수, 여기서 리턴된 정수를 Hash라 한다. Object의 `hashCode()`를 사용한다. `hashCode()`는 메모리 주소를 임의의 정수로 반환
    
    ```jsx
      public static int hashCode(Object a[]) {
            if (a == null)
                return 0;
    
            int result = 1;
    
            for (Object element : a)
                result = 31 * result + (element == null ? 0 : element.hashCode());
    
            return result;
        }
    ```
    
    ⇒ hashCode() method 이다. 
         만약, 어떤 값을 받으면 그 값의 유니코드를 기준으로 해시 코드값을 반환해준다. 
         따라서, 만약 같은 키값을 put 하게 되면 
         그 값들은 동일한 해시코드값을 반환한다. 
         
    <br><br>
    다시 돌아가서, <br>
    - 버킷(배열)의 크기를 capacity(용량)라 부른다. 
    
    - 해시 함수를 통해 반환된 정수 Hash를 이 capacity로 모듈러 연산을 수행한다. 
    
    - 모듈러 연산의 결과값으로 버킷의 인덱스에 접근한다.
    
    - 배열의 Index를 사용해 다이렉트로 값을 꺼내 오기 때문에 조회 성능이 뛰어나다.
    
    - capacity의 3/4 이상 데이터가 존재하면 capacity의 사이즈를 늘려준다. 
    => 2배씩 늘어난다.
    
    ❓모듈러 연산이란?
    
    => %, 나머지 연산을 말한다.
    
    ❓왜 모듈러 연산을 수행할까?
    
    => 버킷(배열)의 capacity 한정적이다. 
  
    
    > 자바는 HashMap을 생성할때 버킷의 초기 크기를 16으로 > 설정한다.
    > 
    >즉, 배열의 인덱스가 16을 넘어서면 안된다. 
    >
    >따라서 모듈러 연산을 하면 무조건 16보다 작은 수가 나오기 때문에 
    >
    >올바르게 인덱스값을 지정할수있도록 도와준다. 
    >
    
    다만 크기가 한정적이므로 **중복된 인덱스가 나올 수 있다.**
    
    ! ! 이제 실제로 내부에서 어떻게 값을 저장하고 꺼내오는지 보자
    
    > 사진에서 버킷의 크기는 16이다.
    
    다음과 같은 코드를 실행한다고 가정해보장. 
    
    ```java
    Person p = new Person("김인엽");
    HashMap<Person, String> map = mew HashMap<>();
    map.put(p, "111111-1111111");
    ```
    
    **put**
    
    ![Untitled](/Java/img/HashMap(4).png)
    
    1. 일단 Person 객체가 Java의 Heap 영역에 생성된다. (stack영역에서 heap영역 참조)
    2. 그 후 HashMap 객체가 Heap 영역에 생성된다. 
    3. put method가 실행되고, HashCode() 메소드가 호출된다. 
    4. key값을 Hash Function에 넣어 HashCode값 35를 리턴받는다.
    5. 리턴받은 Hash(35)를 버킷의 capacity(16)로 모듈러 연산을 한다. 35%16 = 3
    6. 연산된 값 3을 버킷의 인덱스로 사용해 버킷에 접근해 값을 저장한다.
    
    >⇒ 만약 이 상태에서, 똑같은 김인엽이라는 키값으로 map에 put을 하면 
    >
    >그 때는 equals() 메소드를 통해 내용을 비교하고, 같은 값이라고 판별이 나면
    >
    >값을 덮어씌워준다.
    
    **get**
    
    ![Untitled](/Java/img/HashMap(5).png)
    
    1. get(key)으로 값을 꺼내오려 한다.
    2. key값을 Hash Function에 넣어 Hash값 35를 리턴받는다.
    3. 리턴받은 Hash를 버킷의 capacity로 모듈러 연산을 한다.
    4. 연산된 값 3을 인덱스의 3번 버킷에 접근해 값을 꺼내온다.
    <br><br>
    여기서 발생할 수 있는 문제를 생각해보자
    <br>
    <mark>모듈러 연산을 통해 나온 연산값이 중복값이 나오거나 Hahs값이 중복이면 어떻게 될까?</mark>

    
    이 문제를 해시 충돌이라 한다.<mark>(Hash Collision)</mark>
    <br><br>
    ✅해시충돌이 발생할수있는 경우
    
    - key는 다른데 Hash가 같을 때
    - key도 Hash도 다른데 모듈러 연산을 통해 받은 인덱스가 같을 떄
    
    ![Untitled](/Java/img/HashMap(6).png)
    
    >“홍길동” 이라는 키를 이용해 값을 꺼내오려고한다. 
    >
    >해시 함수를 이용해 해시값을 구하고 모듈러 연산을 수행 해 인덱스 값(3)을 구한다.  
    >
    >하지만 3번 방에는 “홍길동”의 값이 아닌 이전에 저장한 “김인엽”의 값이 들어가 있다.
    >
    > 이럴경우 “김인엽”의 값을 꺼낼까?
    >
    >사실 버킷에는 해당 값에 대한 키값도 같이 들어가있다. 
    >
    >뿐만 아니라 해시 함수를 통해 얻은 Hash값도 함께 들어가 있다. 
    >
    >즉 버킷에는 3개의 값이 저장되는것이다.(키,값,Hash)
    
    ❓Hash값이 들어있는 이유?
    
       ⇒  해쉬함수를 한번만 수행하고 저장해두면 버킷의 사이즈를 늘려주는 과정에서
    
             다시 연산을 하지 않아도 된다.
    
    ![Untitled](/Java/img/HashMap(7).png)
    
    다시 한번 보자. “홍길동” 이라는 키로 값을 찾으려 할때 
    
    이미 해당 인덱스에는 “김인엽”이라는 키에대한 값들이 저장되어있다. 
    
    이럴 경우 “홍길동”과 버킷에 저장된 “김인엽”을  `equals()` 로 서로 같은 키인지 비교해, 
    
    맞으면 값을 반환하고 틀리면 해당 키에 대한 값이 없는걸로 판단되어탐색을 마친다. 
    
    결국 “홍길동”에 대한 값이 없으므로 연산은 종료된다. 

    <br><br>
    <b>값을 꺼낼때 발생할 수 있는 Hash Collison을 알아 보았다.</b>
    
    이번에는 저장할때 발생하는 Hash Collision에 대해 알아 보자.
    
    ![Untitled](/Java/img/HashMap(8).png)
    
    정상적으로  인덱스 값을 구해서 버킷에 저장하려 했지만 
    
    해당 인덱스는 이미 “김인엽”이라는 키의 값이 저장되어있다. 
    
    이 경우에는 어떻게 될까?
    
    바로 해당 인덱스에 linked list를 생성해 값을 저장한다. 
    
    이 방식을 **Separate Chining**이라 한다.
    
    ![Untitled](/Java/img/HashMap(9).png)
    
    계속 해시 출동이 발생하면 엔트리를 계속해서 생성해 저장한다.
    
    ![Untitled](/Java/img/HashMap(10).png)
    
    값을 꺼낼때는 linked list를 순회 하며 
    
    `equasl()` 를 실행해 같은 키의 값을 찾아 반환한다.
    
    ![Untitled](/Java/img/HashMap(11).png)
    
    ❓엔트리란? HashMap에서 키-값 쌍을 저장하는 단위
    
    ### **HashMap에서의 equasl()와 hashCode()**
    
    위에 설명한 HashMap은 두가지를 성립해야한다.
    
    1. `hashCode()` 메서드는 항상 동일한 객체에 대해 동일한 해시 코드를 반환해야 한다.
    2. 두 객체가 `equals()` 메서드에 의해 동일하다고 판명되면, 
    
           두 객체의 `hashCode()` 메서드는 동일한 해시 코드를 반환해야 한다.
    
    하지만 Java에서 `equals()`는 객체의 메모리 주소값을 비교하고 
    
    `hashCode()` 또한 메모리 객체를 바탕으로 Hash를 반환한다.. 
    
    따라서 물리적으로 같은지가 아니라 논리적으로 같은 객체임을 확인하려면 **재정의(Overrride)**를 해줘야한다.
    
    만약 hashCode()를 재정의 하지않으면 어떻게 될까?
    
    ![Untitled](/Java/img/HashMap(11).png)

    3번 인덱스에 저장된 “김인엽” 키와 
    
    `new` 키워드를 붙여 새로 생성한 “김인엽”이라는 키는 서로 다른 메모리 주소를 가진 다른 객체이다. 
    
    따라서 `hashCode()` 함수를 실행시키면 다른 Hash가 나오기 때문에 맞지 않는 인덱스에서 값을 찾을것이다. 
    결국에는 값을 찾지 못해 연산을 종료시킬것이다.
    
    그럼 만약 equasl()를 재정의하지 않으면 어떻게 될까?
    
    ![Untitled](/Java/img/HashMap(12).png)
    
    해시 충돌이 일어났을때 같은 키값을 가진 엔트리를 찾을수 없을 것이다. 재정의 하지 않은 equals()는 객체 주소를 비교하니까 `“김철수”.equlas(”김철수”)`는 false를 출력해 값을 찾지 못하고 연산은 종료될것이다.
    
    </aside>

<br><br>

## 추가적으로 HashMap에서 동기화를 하려면 ? 
두 가지 방법을 제안한다 
- HashTable 
- ConcurrentHashMap 

## HashTable 
- HashMap과 유사하나 다른점은 동기화를 보장한다는 점이다. 
- HashTable 내부는 모두 synchronized란 내용이 작성되어 있다. <br>
=> 이는 데이터의 여러 작업 (get포함)실행할 때에 다른 스레드의 작업을 block하여 thread-safe를 유지시킨다. 

장점 : 스레드 세이프티 하다는 것. <br>
단점 : 작업 도중 block을 걸어서 속도 이슈가 생긴다. 
<br>그리고 null값은 못 넣음 (HashMap은 가능)

## ConcurrentHashMap

Map내에서 사용하는 부분을 여러 segmewnt로 쪼개서 일부에만 block을 걸어줌. <br>
객체 전체에 block을 거는 HashTable과 달리. 

장점 : HashTable에 비해빠르다. 
  - HashTable은 A가 작업을 수행하는 동안 B,C,D는 접근 못함. 
  - ConcurrentHashMap은 A가 데이터를 읽는 동안 B는 데이터를 추가 가능. 
  <br>이것도 null은 불가하다. 

결론적으로 HashMap이 젤 빠르다. <br>
단일 쓰레드일 때는 HashMap <br>
멀티 쓰레드는 ConcurrentHashMap <br>
HashTable은 ㅎ.. Vector처럼 쓰지말자 ~! 

# 결론

Hash란
- Hash란 key value 한 쌍으로 하고 데이터를 효율적으로 저장하며 빠르게 찾기위한 자료구조임
- Java에서 HashMap은 key에 대한 hash값을 저장, 조회해서 키-값의 쌍 수의 개수에 따라 동적으로 크기가 증가하는 연관 배열. 

Hash에서 데이터 저장 + 조회 방법
- 데이터를 저장할 버킷을 찾는 방법은 객체의 hashCode() 메소드의 반환 값을 사용한 hashCode() % BUCKET_SIZE(모듈러 연산) 수식을사용하여 데이터를 저장/조회 할 버킷의 위치를 계산 
이 수식을 사용해서 데이터를 BUCKET_SIZE 내의 위치에 값을 저장할 수 있게 됨. 

Hash 저장 중 생기는 키값 충돌 해결법
- 해시값 충돌 Collision은 체이닝을 사용(체이닝은 각 버킷은 연결 리스트의 헤더를 가리키고, 충돌이 발생하면 연결 리스트에 데이터를 저장) 
특정 버킷에 데이터 개수가 일정 개수 이상이 되면 연결 리스트에서 트리로 자료구조 변경하여 탐색 시간 줄임 
자바 8에서는 8개의 데이터가 저장되면 트리, 6개 이하면 다시 연결 리스트 (2개의 차이가 나도록 한 이유는 만약 어떤 동일한 키 값이 반복적으로 삽입/삭제가 되는 경우에 불필요한 자료 구조 변환 작업이 일어나기 때문)

Hash 버킷 크기의 동적 확장
- 일정한 개수 이상이 되면 해시 버킷의 개수를 두 배로 늘림 
- 기본값은 16이고 최대 개수는 2^30 <br>
=> 데이터의 개수가 임계점이 이를 때마다 버킷 개수의 크기를 2배씩 증가 
(임계점은 load factor * 현재 해시 버킷 개수 : load factor의 기본값은 0.75)

HashMap이 식별자를 구별하는 방법
- equals()  hashCode() <br>
=>재정의 하는 이유는 원래 주소값 기반인데 내용기반으로 바꾸려고
- 둘의 hashCode가 같아야 하고 euqals()가 같다면 동일한 식별자라 판단 
- 해시코드는 무조건 같아야 함. 
<br><br>

+ HashMap은 기본적으로 threadUnSafety 하다. 
그래서 thread




