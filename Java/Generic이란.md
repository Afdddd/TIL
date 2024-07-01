# Generic
---

### Generic이란?
<br>


> 클래스 내부에서 사용할 데이터의 타입을 외부에서 정해주는 기법
 

객체별로 다른 타입의 자료가 저장될 수 있다.

자바 1.5버전 부터 사용이 가능하다.

<br>

**Type Parameter**

제네릭 클래스나 메서드를 정의할 때 사용되는 변수 <>안에 타입을 정해준다.

```java
class Box<T>{}
```

- **T : 타입변수 또는 타입 매개변수**
- **E : 요소**
- **K : 키**
- **V : 값**
- **N : 숫자**
- **S,U,V : 두번째, 세번째, 네번째 선언된 타입**

<br>

타입 파라미터에 할당 받을 수 있는 타입은 Reference 타입 뿐이다.

int나 double 같은 Primitive타입은 할당 받을 수 없다.
<br><br>

**Generic class**

> 클래스 선언에 타입 매개변수가 쓰이면, 이를 제네릭 클래스라 한다.
> 

```java
class Fruit<T>{
	List<T> fruits = new ArrayList<>();
	
	public void add(T t){
		fruits.add(t);
	}

}
```
<br>

```java
public static void main(String[] args){
	Fruit<String> f = new Fruit<String>();	
}
```

이렇게 타입 파라미터를 할당해주면

```java
class Fruit<T>{
	List<String> fruits = new ArrayList<>();
	
	public void add(String t){
		fruits.add(t);
	}

}
```

이런식으로 컴파일시 T를 String 타입으로 대체한다.

<br>

**Generic interface**

> 인터페이스 레벨에서 하나 이상의 타입 매개변수를 사용할 수 있도록 정의된 인터페이스
> 

```java
public interface FruitInterface<T>{
	T add(T t);
}
```
<br>

**Generic method**

> 메서드 레벨에서 타입 매개변수를 정의하며, 매개변수, 반환타입에 사용 가능.
> 

```java
public <T> void add(T t){}
```

- 제네릭 메서드는 제네릭 클래스가 아니어도 정의할수있다.
- 제네릭 클래스와 제네릭 메서드의 타입 매개변수가 같다면 제네릭 메서드가 우선 순위이다.

<br><br>

### Generic을 사용하는 이유

---

**타입 안정성**

런타임에러(실행중 발생하는 에러)가 발생하면 프로그램 실행중에 강제로 종료되는 위험이 있기 때문에 코딩 중 수정이 가능한 컴파일에러로 끌어올 수 있다.

<br>

```java
ArrayList list = new ArrayList();
list.add(1);
list.add(2);
list.add("3");
Integer i = (Integer)list.get(2);
```

위의 코드는 컴파일에러는 발생하지 않지만 실행을 해보면 런타임에러가 발생한다.
ClassCastException 형변환 오류인데, ArrayList.get() 메서드는 반환타입이 Object타입 이기 때문에 Integer타입으로 형변환한 것 처럼보이지만 list.get(2)는 String타입이기때문에 타입이 맞지않는다.

<br>

```java
ArrayList<Integer> list = new ArrayList<Integer>();
list.add(1);
list.add(2);
list.add("3"); // 경고
Integer i = (Integer)list.get(2);
```

제네릭을 사용해 list에는 <Integer>타입만 들어올수있게 제한을 걸어두면 컴파일 타임에 자료형의 오류에 대한 검증을 수행하여 런타임에 자료형에 안전한 코드를 실행할 수 있다.

<br>

**캐스팅 삭제**

JDK1.5 이전에는 클래스를 정의할 때 어떤 타입이든 들어올 수 있게 Object타입으로 정의했다. 

ArrayList 일반클래스(JDK1.5이전)

```java
public class ArrayList extends AbstractList{ // 일부생략
    public Object[] list;
    public boolean add(Object o) { /* 내용 생략 */}
    public Object get(int index) { /* 내용 생략 */}
}
```
<br>

 ArrayList 지네릭클래스(JDK1.5이후)

```java
public class ArrayList<E> extends AbstractList<E>{ // 일부생략
    public <E>[] list;
    public boolean add(<E> o) { /* 내용 생략 */}
    public <E> get(int index) { /* 내용 생략 */}
}
```


JDK1.5 이후 부터는 Generic을 사용해 타입 파라미터로 클래스를 정의하고, 객체를 생성시 타입 변수(E) 대신 실제 타입을 지정할 수 있다.

```java
ArrayList<Tv> list = new ArrayList<Tv>();
```

실제 타입이 지정이되면, 형변환 생략가능해진다.

```java
ArrayList<Tv> list = new ArrayList<Tv>();
list.add(new Tv());
Tv t = list.get(0); // Tv t = (Tv)list.get(0)
```


<br><br>

### 타입 매개변수의 제한

---

```java
public class FruitBox<T> {
    private List<Fruit> fruits = new ArrayList<>();
    
    public void add(T t){
        fruits.add(t); // 오류
    }
}
```

FruitBox 클래스에는 모든 타입이 들어올 수 있다.

하지만 `fruits`는 Fruit 타입만 들어올수있게 제네릭을 제한해 놓았기 때문에 `add()`는 컴파일 오류가 뜬다.

`add(T t)` 에 다른 타입이 들어올 수 있기 때문이다.

<br>

**상한 경계**

![Untitled](/Java/img/Generic(1).png)

```java
T extends Fruit
```

<br>

타입 매개변수의 클래스는 Fruit 클래스이거나 Fruit의 하위 클래스만 들어올수있다.

이 제네릭 타입에 들어올 수 있는 클래스 중 가장 높은 클래스는 Fruit이기 때문에 상한 경계이다.


```java
public class FruitBox<T extends Fruit> {
    private List<Fruit> fruits = new ArrayList<>();
    
    public void add(T t){
        fruits.add(t); 
    }
}
```
<br>

이제는 Fruit 클래스나 하위클래스만 들어올수있게 제한해놓았기 때문에 실행가능해진다.
이렇게 특정 클래스나 하위클래스만 들어올수있게 제한하여 클래스를 설게할때 사용된다.
반대로 하한 경계선도 있다.

![Untitled](/Java/img/Generic(2).png)

```java
T super Fruit
```

Fruit의 상위클래스만 들어올수있다. 이말은 Object 클래스도 들어올 수 있단 뜻이다.

수 많은 클래스와 인터페이스가 들어올 수 있기 때문에 Object와 다르지 않아 사용되지 않는다.

<br><br>

### Wildcard

---

> 다양한 타입의 제네릭 매개변수를 표현하는데 사용, ? 키워드를 사용하고 어떤 타입의 매개변수가 사용될지 미리 정하지 않아 보다 유연하게 처리할수있도록 도와준다.
> 

<br><br>

### 자바의 공변성/반공변성

---

제네릭은 와일드카드를 통해 공변성을 얻을수있다.

원래 제네릭은 공변성과 반공변성을 지원하지 않는다.

우선 변성은 상속 관계에서 서로 다른 객체 즉 Food와 Fruit가 어떤 관계를 있는지를 나태는 개념이다.

배열은 공변과 반공변을 지원한다. 배열과 제네릭을 비교하면서 알아보자


<br>

**공변**

자기 자신과 하위 객체로 타입 변환을 허용해주는 것이다.(업캐스팅)

배열

```java
Object[] o = new Number[10]; // Ok.
```

제네릭

```java

List<Object> lint = new ArrayList<Number>(); // Error
```

<br>

**반공변**

자기 자신과 상위 객체로 타입 변환을 허용해주는것이다.(다운캐스팅)

배열

```java
Object[] o = new Number[10];
Number n = o; // Ok.
```

제네릭

```java
List<Number> list = new ArrayList<Object>(); // Error
```

<br>

**무공변**

오로지 자기 타입만 허용하는 것, 제네릭이 바로 무공변 성질이다.

```java
List<Number> list = new ArrayList<Number>(); // Ok.
```

![Untitled](/Java/img/Generic(3).png)

제네릭의 타입 파라미터 간이 상속 관계를 가져도 제네릭 자체는 상속관계를 허용해주지않는다.

이런 경우 발생하는 불편함이 있다.

```java
static void print(List<Object> arr){
	for(Object o : arr){
		System.out.println(o);
	}
}

public static void main(String[] args){
	List<Integer> integers = Arrays.asList(1,2,3);
	print(integers); // 에러
}
```

List<Object> ≠ List<Integer> 제네릭 타입이 맞지 않기 때문에 오류가 발생한다.

이것을 해결하려면 print()의 파라미터를 Integer로 고정시켜 줘야 한다. 하지만  Integer타입만 들어온다는 보장도 없고 필요할 때마다 오버로딩해서 메서드를 추가시켜줘야 한다.

```java
static void print(List<Integer> arr){...}
static void print(List<Double> arr){...}
static void print(List<Number> arr){...}
...
```

이렇게 되면 객체지향을 전혀 이용 못하게 된다.

이를 해결하기 위해 제네릭의 와일드 카드를 사용된다.

**비경계 와일드 카드(Unbounded Wildcard)**

- ?의 형태이며 모든 타입이 들어올수있다.

<br>

**get**

- 비경계 와일드 카드에서 get한 원소는 Object타입이다.

어떤 타입이 와도 읽을수 있도록 모든 타입의 공통 조상인 Oject로 받기 때문이다.

```java
public void get(List<?> list){
	Object object = list.get(0);
	Integer integer = list.get(0); // 오류
}
```

<br>

**add**

- List<?>에는 null만 삽입할수있다.

비경계 와일드카드의 원소가 어떤 타입인지 알 수 없기때문이다.

```java
public static void main(String[] args){
	List<Integer> list = new ArrayList<>();
	addDouble(list); // 오류, Integer타입 리스트에 double값을 넣으려했기때문
}

void addDouble(List<?> list){
	i.add(3.15);
}
```

<br>

**상한 경계 와일드 카드(Upper Bounded Wildcard)**

- ? extends T , T 또는 T의 하위 클래스만 인자로 올 수 있다.
- 값 삽입보다는 조회에 사용

<br>

**get**

- 상한 경계 와일드 카드에서 get한 객체는 T타입이다.

원소들의 최고 공통 조상이 T이기 때문이다.

```java
public void printList(List<? extends Fruit> fruits){
	for(Fruit f : fruits){
		System.out.println(f);
	}
}
```

<br>

**add**

- 상한 경계 와일드 카드에는 null만 삽입 가능하다.

상한 경계 와일드 카드에의 원소가 어떤 타입인지 모르기 때문

```java
List<Apple> apples = new ArrayList<>();
List<? extends Fruit> fruits = apples;
fruit.add(new Banana()); // 오류 apple리스트에 banana가 들어갔기때문
```

<br>

**하한 경계 와일드 카드(Lower Bounded Wildcard)**

- ? super T, T 또는 T의 상위 클래스들만 인자로 올 수 있다.
- 값 조회보다는 삽입에 사용

<br>

**get**

- 하한 경계 와일드카드에서 get한 객체는 Object 타입이다.

원소들의 최고 공통 조상이 Object이기 때문이다.

```java
public void printList(List<? super Fruit> fruits){
	for(Object o : fruits){
		System.out.println(o);
	}
}
```

<br>

**add**

- 하한 경계 와일드카드에는 T또는 T의 하위 클래스를 삽입할수있다.

Fruit는 상위클래스를 담을 수 없기때문이다.

```java
List<? super Fruit> fruits = new ArrayList<>();
fruits.add(new Apple()); // OK. Fruit의 하위 클래스이기때문
fruits.add(new Fruit()); // OK.
fruits.add(new Food());  // error. Fruit의 상위클래스이기 때문 
```


<br><br><br>

### PECS


> PECS(Producer - Extends - Consumer - Super)
> 

외부에서온 데이터를 생산(Producer) 한다면 <? extends T>

외부에서 온 데이터를 소비(Consumer) 한다면 <? super T>

라는 공식이다.

```java
public class FruitBox {
    Fruit[] fruitsBox = new Fruit[10];
    int index = 0;

		// 파라미터로 들어온 컬렉션을 순회하면서 값을 꺼내 요소를 생산
    public void addAll(Collection<? extends Fruit> fruits){ 
        for(Fruit f : fruits){
            fruitsBox[index] = f;
        }
    }

		// 파라미터로 들어온 컬렉션에 기존 요소를 담으면서 요소를 소비
    public void getAll(Collection<? super Fruit> box){
        for(Fruit f : fruitsBox){
            box.add(f);
        }
    }
}
```

<br><br><br>

### Type Erasure


타입 소거(Type Erasure)

제네릭은 JDK 1.5 버전부터 도입된 문법으로 이전 버전의 코드와 호환성을 위해 제네릭 코드는 컴파일되면 제네릭 타입이 사라진다.

그렇기 때문에 클래스 파일에는 제네릭에 대한 정보는 존재하지 않는다.

**타입 소거 과정**

1. 제네릭 타입의 경계를 제거한다
    - <T extends Fruit> 이면 Fruit로 치환
    - <T> 이면 Object로 치환
    
    ```java
    // 소거 전
    class FruitBox<T extends Fruit> {
    	List<T> list = new ArrayList<>();
    	
    	void add(T fruit){
    		list.add(fruit);
    	}
    	
    	T getFruit(int i){
    		return list.get(i);
    	}
    }
    
    // 소거 후
    class FruitBox {
    	List list = new ArrayList<>();
    	
    	void add(Fruit fruit){
    		list.add(fruit);
    	}
    	
    	Fruit getFruit(int i){
    		return list.get(i);
    	}
    }
    
    ```
    <br>

2. 제네릭 타입을 제거한 후 타입이 일치하지 않는 곳은 형변환을 추가
    
    ```java
    // 소거 후
    class FruitBox {
    	List list = new ArrayList<>(); // Object타입
    	
    	void add(Fruit fruit){
    		list.add(fruit);
    	}
    	
    	Fruit getFruit(int i){
    		return (Fruit)list.get(i); // 형변환 추가
    	}
    }
    ```
  
<br> <br>

### Generic의 단점

---

**힙 오염(Heap Pollution)**

JVM의 힙 영역에 저장되어 있는 특성 객체가 불량 데이터를 참조함으로써, 데이터를 사용할 때 얘기치못한 런타임 에러가 발생할수 있는 상태.

힙 오염은 두 가지 상황에서 발생할 수 있다.

<br>

1. 로우 타입과 제네릭 타입을 동시에 사용할 때
    
    ```java
    List<String> stringList = new ArrayList<>();
    List rawlist = stringList; // 로우 타입 사용
    rawlist.add(10); // 힙 오염
    
    String str = stringList.get(0); // 런타임 에러!
    ```
    
    <aside>
    💡 로우 타입(raw type)?  타입 파라미터가 없는 제네릭 타입
    
    </aside>

    <br>

1. 확인되지 않은 형변환이 일어날때
    
    ```java
    List<String> stringList = new ArrayList<>();
    List<?> wildcardList = stringList;
    List<Integer> integerList = (List<Integer>) wildcardList;  // 힙 오염 발생
    
    integerList.add(10);  // 런타임에 ClassCastException 발생
    ```
    <br><br>

**힙 오염 방지**

1. 로우 타입 사용 금지
    
    항상 제네릭 타입을 명시적으로 지정해서 사용한다.
    
2. Collections 클래스의 `checkList()` 사용
    
    의도치 않은 타입의 객체가 들어갔을 때 이를 감지해 예외를 발생시켜준다,
    
    ```java
    List<String> stringList = Collections.checkedList(new ArrayList<>(), String.class);
    ```
    <br><br>

**코드의 가독성**

제네릭을 남용하면 코드의 가독성이 떨어진다.

```java
Map<String, List<Pair<Integer, String>>> map = new HashMap<>();
```

이런식으로 중첩해서 사용할 수 있는데 읽기가 힘들다.


<br><br><br><br><br>


### Reference
[https://inpa.tistory.com/entry/JAVA-☕-제네릭-타입-소거-컴파일-과정-알아보기](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A0%9C%EB%84%A4%EB%A6%AD-%ED%83%80%EC%9E%85-%EC%86%8C%EA%B1%B0-%EC%BB%B4%ED%8C%8C%EC%9D%BC-%EA%B3%BC%EC%A0%95-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0)
[https://asuraiv.tistory.com/16?category=813980](https://asuraiv.tistory.com/16?category=813980)