# Immutable 객체 생성 방법

# 1️⃣ 불변객체란(Immutable Object) ?

<aside>
💡 객체 생성 이후 내부의 상태가 변하지 않는 객체. 
특징부터 먼저 나열해보자면, <br>
<br>
 1) 불변객체는  read-only 메소드만을 제공. <br>
 2) 객체의 내부 상태를 제공하는 메소드 제공 x  or 방어적 복사를 통해 제공 <br>
 3) Java 대표 불변 객체는 String 

</aside>
<br>
예를 들어 

```java
String str = "hello";
String upperStr = str.toUpperCase(); // "HELLO" 반환, str은 여전히 "hello"
char firstChar = str.charAt(0); // 'h' 반환, str은 여전히 "hello"
```

⇒ String class에서 제공하는 toUpperCase나charAt 메소드처럼 <br>
     상태값을 변경하는 method가 아닌 읽기만 가능한 메소드를 
     제공한다는 말이당. <br>(이게 read only method)

```java
String name = "Old";
name.toCharArray()[0] = 'E';
System.out.println(name);
```

⇒ 이렇게 하면 old라는 값 그대로 출력이됩니당. <br>
⇒  Java의 String은 불변 클래스이기 때문에 위와 같이<br>
    String 내부의 char형 배열을 얻어 수정하여도 <br>
    반영이 되지 않는것을 볼 수 있죵

 

⇒  Java에서는 배열이나 객체 등의 참조(Reference)를 전달하기 <br>
   때문에 참조를 통해 값을 수정하면 내부의 상태가 변합니다. <br>
  따라서,  내부를 복사하여 전달하고 있는데, 이를 <br>
  <mark>방어적 복사  (defensive-copy) </mark>라고 한다. 
     

대표적인 예로 string의 toCharArray가있어용

```jsx
public char[] toCharArray() {
    char result[] = new char[value.length];
    System.arraycopy(value, 0, result, 0, value.length);
    return result;
}
```

 1) result라는 새로운 char배열을 생성. <br>
 2) System.arraycopy를 통해 value 배열의 모든 요소를 result로 복사 <Br>

>value :  원본 배열 (String 객체의 내부 문자 배열) <br>
>0 : 원본 배열에서 복사를 시작할 인덱스 <br>
>result : 대상 배열(새로 생성된 배열) <br>
>0 : 대상 배열에서 데이터를 저장하기 시작할 인덱스 <br>
>value.length : 복사할 요소의 수 (원본 배열의 전체 길이만큼 복사) <br>
>한 후 result를 return 해줍니당. 그럼 예를 들어, 

```java
String str = "hello";
char[] charArray = str.toCharArray();
charArray[0] = 'H';
System.out.println(str); // 여전히 "hello" 출력
```

charArray에는 str을 배열로 나눈 새로운 char 배열이 담기나, <br>
그 값을 변경해도 여전히 str은 hello 값 그대로인 것을 알 수 있습니당. 


# 2️⃣ 불변객체 장점

<mark><b>2_1. Thread-safe하여 프로그래밍에 유용하며, 동기화를 고려하지 않아도 됨.</b> </mark>

<aside>
💡 멀티 쓰레드 환경에서 동기화 문제가 발생하는 이유는 <br>
공유 자원에 동시에 쓰기(Write) 때문입니당. <br>
하지만 만약 공유 자원이 불변이라면 더 이상 동기화를 <br>
고려하지 않아도 되죠. <br>
왜냐하면 항상 동일한 값을 반환할 것이기 때문에!! <br>
이는 안정성을 보장할 뿐만 아니라 동기화를 하지 않음으로써 <br>
성능상의 이점도 가져다줍니당.

</aside>
<br>

⇒ 보통 이럴 경우에 여러 쓰레드가 동시에 메소드나 블록에 접근하려고 하면 <br>
`synchronized`를 통해서 제어를 하는데요 

이는 `모니터 락 획득을 위해서 성능 악화를 초래`한다는 단점이 있습니다. 

- 모니터락이 뭔가
    
    <aside>
    💡 만약 아래와 같은 class가 있다고 가정합니당.<br>
    모니터 락은 자바 객체마다 하나씩 존재해요. <br>
    근데 한 쓰레드가 `increment` 메소드를 호출하게 된다면, <br> 
    이 쓰레드는 Conter 객체의 모니터 락을 획득하게 됩니당. <br> <br>
    
    다른 쓰레드가 increment나 getCount 메소드를 호출하려고 하면 현재 락을<br> 소유한 쓰레드가 락을 해제할 때 까지 대기해야 해요. <Br>
    
    메소드 실행이 완료 되면 락이 해제되고, 대기 중인 쓰레드 중 하나가 락을 <Br> 획득하여 메소드를 실행합니당.<Br>
    
    </aside>
    
    ```java
    public class Counter {
        private int count = 0;
    
        public synchronized void increment() {
            count++;
        }
    
        public synchronized int getCount() {
            return count;
        }
    }
    
    ```
    

<mark><b>2_2. GC 성능 높일 수 있음.</b></mark>

<aside>
💡 1. 우선, 불변 객체를 참조하고 있는 컨테이너 객체에 의해서 
불변객체가 GC의 대상이 되지 않음.

</aside>

```java
public class ImmutableHolder {
    private final Object value;

    public ImmutableHolder(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}

public class Main {
    public static void main(String[] args) {
        // Object 타입의 value 객체 생성
        String value = "Hello, World!";
        
        // ImmutableHolder 객체 생성 및 value 참조
        ImmutableHolder holder = new ImmutableHolder(value);

        // holder 객체가 살아 있는 동안 value 객체는 GC의 대상이 되지 않음
        System.out.println(holder.getValue());  // "Hello, World!"
    }
}

```

⇒ 위 코드에서 `ImmutableHolder의 객체 holder`가 생성되면 

`value` 필드에 “Hello Word” 문자열 객체에 대한 참조가 저장된당.
holder 객체가 살아있는 동안 value 필드가 참조하는 문자열 객체는 <br>
가비지 컬렉션의 대상이 되지 않음. <br>

따라서, `ImmutableHolder` 객체와 `value` 필드가 동일한 생명주기를 가진다는 것이고,<br> GC가 `ImmutableHolder` 객체를 스캔할 때, <br>
value 필드가 참조하는 객체를 스캔할 필요가 없게 되어 GC 성능에 기여를 한다는 것이당. 
<br><br>

# 3️⃣ 불변객체 만드는 방법

# 1. final 키워드 제공함.

우선 final의 의미를 먼저 주자면

<aside>
💡 1. 변수 : <Br>
 변수에 final이 붙으면, 그 변수는 한 번 초기화된 이후 다시 값을 변경할 수 없음.<br> 
2. 메소드 : <br>
메소드에 final이 붙으면, 그 메소드는 서브 클래스에서 오버라이드 불가능 <br>
3. 클래스 : <br>
클래스에 final이 붙으면 그 클래스는 상속될 수 없음<br><Br>

</aside>

```jsx
final String name = "Old";
name = "New";  // 컴파일 에러
```

![Untitled](/Java/img/Immutabel(1).png)

⇒ Java에서는 final이 붙은 변수의 값을 변경하려고 하면 컴파일 에러가 발생한다.<br>

Java에서는 불변성을 확보할 수 있도록 final 키워드를 제공하고 있어용.<br> Java에서 변수들은 기본적으로 가변적인데, 변수에 final 키워드를 붙이면 <br>
참조값을 변경 못하도록 하여 불변성을 확보할 수 있습니당.<br>

하지만 <b>final 키워드가 내부의 객체 상태를 변경하지 못하도록 하는 것은 아니다.</b> <Br>
예를 들어 아래와 같이 final로 선언된 List에는 새로운 객체가 더해져도(상태가 변해도) 문제가 없다.

```jsx
final List<String> list = new ArrayList<>();
list.add("a");
```
>참고로 써놓는데 위에 같은 경우에 final이 하는 역할은 아래의 코드처럼
```
list = new ArrayList<>();
```
>새로이 list에 참조하는 객체를 생성할 수 없다는 얘기다

- 혹시 왜 list는 가변이고 String은 불변인지 궁금한사람 ??
    
    클래스가 그렇게 설계되어있다. 
    
    ```java
    public final class String {
        // ...
        toUpperCase()
        substring()
        ...
    }
    
    ```
    
    ⇒ 그렇다 자바는 이렇게 final로 메소드가 선언이 되어있어 상속이 불가능하며,<br> 상태값을 변경하는 method가 존재하지 않는다. (read-only method)
    
    그에반해 list는 
    
    ```java
    public interface List<E> extends Collection<E> {
        boolean add(E e);
        E set(int index, E element);
        E remove(int index);
        // ...
    }
    
    ```
    
    ⇒ 이렇게 interface 즉, 상속받아 상태값을 유동적으로 변경시킬 수 있게 되어있으며, <br>
    set, remove 등 내부 상태를 변경할 수 있는 method를 제공한당. 
    

⇒ 그래서 ?  Java에서는 참조에 의해 값이 변경될 수 있는 점들을 <br>
   유의해야 하는데, 이를 방지하려면 불변 클래스로 만들어야 한다.

```
Java에서 불변 객체를 생성하기 위해서는 다음과 같은 규칙에 따라서 클래스를 생성해야 한다.

1. 클래스를 final로 선언하라
2. 모든 클래스 변수를 private와 final로 선언하라
3. 객체를 생성하기 위한 생성자 또는 정적 팩토리 메소드를 추가하라
4. 참조에 의해 변경가능성이 있는 경우 방어적 복사를 이용하여 전달하라
```

# 1. 불변 클래스 : 클래스를 final로 선언

```jsx
public final class ImmutableClass {
    // Fields and methods
}
```

⇒ 상속을 통해 객체의 상태가 변경되는 것을 방지함. <span style="color : red;">이 클래스를 상속 불가능. </span>

=> 다른 클래스가 이 클래스를 확장하여 새로운 메소드를 추가하거나 <br>
기존 메소드를 재정의하는 것을 막을 수 있다.

=> 만약 클래스가 상속 가능하다면, 하위 클래스에서 객체의 상태를 <br>
변경할 수 있는 메소드를 추가할 가능성이 생김. 

# **2. 모든 필드를 `private`로 선언하고 `final`로 선언하기**

- 객체의 필드가 외부에서 변경되지 않도록 보장합니다.

```java
public final class ImmutableClass {
    private final int field1;
    private final String field2;
    // Additional fields
}
```

⇒> private로 선언하면 외부 클래스가 직접 필드에 접근할 수 없고 <br>
    외부에서 직접 필드를 변경하는 것을 막음

⇒  final로 선언하면 필드를 초기화한 후에는 그 값을 변경할 수 없고 <br>
    한 번 설정된 값은 변하지 않음. 

# **3. 생성자를 통해 모든 필드를 초기화하기**

- 모든 필드는 객체 생성 시에만 설정될 수 있도록 합니다.

```java
public final class ImmutableClass {
    private final int field1;
    private final String field2;

    public ImmutableClass(int field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
}
```

모든 필드를 초기화하는 생성자를 제공하여 객체가 생성될 때 모든 필드가 초기화 되도록함. <Br>
이를 통해 객체 생성 후에 필드가 변경되지 않도록 함. 

# **4. 필드에 대한 getter 메서드만 제공하기**

- setter 메서드를 제공하지 않아 필드의 변경을 방지합니다.

```java
java코드 복사
public final class ImmutableClass {
    private final int field1;
    private final String field2;

    public ImmutableClass(int field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public int getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }
}
```

⇒ getter 메소드는 필드의 값을 반환하지만 필드의 값을 변경하지 않는다.  <br>
   setter 메서드를 제공하지 않으면 외부에서 객체의 필드를 변경할 수 없다. 
   따라서, 이를 통해 객체의 불변성을 유지할 수 있다. 

# **5. mutable 객체를 사용하지 않거나, 꼭 사용해야 한다면 방어적 복사를 이용하기**

- mutable 객체를 필드로 사용할 경우, 외부에서 객체의 상태를 변경할 수 있으므로 <Br>방어적 복사를 사용하여 안전성을 확보해야한당.

```java
java코드 복사
import java.util.Date;

public final class ImmutableClass {
    private final Date mutableField;

    public ImmutableClass(Date mutableField) {
        this.mutableField = new Date(mutableField.getTime());
    }

    public Date getMutableField() {
        return new Date(mutableField.getTime());
    }
}
=> 이렇게 하면 mutableFiled의 값은 변하지 않으니까용...
```

⇒ mutable 객체 Date, List 등 내부 상태가 변경될 수 있기 때문에 불변 객체를 유지하기 어렵다. <br>
이런 mutable 객체를 필드로 사용해야 한다면, 방어적 복사를 통해 <br>
외부에서 내부 상태를 변경할 수 없도록 해야 함. 

<mark><b>이 방법이 중요한 이유가</b></mark> : <br>
생성자에서 필드에 할당할 때와, getter 메서드를 통해 필드를 반환할 때 <br>
각각 새로운 객체를 생성하여 반환하고, 이를 통해 외부에서 필드에 접근하거나 <Br> 반환된 객체를 통해 내부 상태를 변경할 수 없도록 함. 

```jsx
Date date = new Date();
date.setTime(1622548800000L);  // 날짜와 시간을 변경

Date date = new Date();
date.setYear(2022 - 1900);  // 연도를 변경

Date date = new Date();
date.setMonth(6 - 1);  // 월을 변경 (0부터 시작하므로 6월은 5로 설정)

Date date = new Date();
date.setDate(15);  // 일을 변경

```

Date 객체 내부 클래스인데 월, 년, 일 변경하는 method를 제공하고있졍. 
이래서 가변이라는 것입니당. 