# 직렬화와 역직렬화

**직렬화(Serialization)** 란 객체를 바이트 스트림형태로 연속적인(Serial) 데이터로 변환하는 포맷 변환기술을 말한다. 
**역직렬화(deserialization)** 는 반대로 바이트 스트림에서 객체로 복원하는 과정이다.

JVM Heap 영역이나 Stack 영역에 있는 객체 데이터를 직렬화를 통해 바이트 형태로 변환하여 데이터베이스나 파일로 변환해 외부에 저장해두고, 다른 컴퓨터에서 이 파일을 가져와 역직렬화를 통해 자바 객체로 변환해서 JVM 메모리에 적재하는것이다.

```
💡 바이트 스트림이란?
 Byte(8비트)로 이루어진 입출력을 위한 통로를 말한다.
```

<br><br>

### 직렬화의 사용
- 파일 저장: 객체를 파일에 저장하여 애플리케이션 종료 후에도 객체의 상태를 유지할 수 있다.
- 네트워크 전송: 객체를 직렬화하여 네트워크를 통해 다른 시스템으로 전송할 수 있다. RMI(Remote Method Invocation)나 분산 시스템에서 사용된다.
- 세션 데이터: 웹 애플리케이션에서 사용자의 세션 데이터를 직렬화하여 저장하거나 전송할 수 있다.


```
💡 RMI란?
 Java에서 제공하는 분산 시스템 통신을 위한 API로, 네트워크를 통해 다른 JVM에서 실행 중인 객체의 메서드를 호출할 수 있는 기능을 제공.
 RMI는 자바의 객체지향 개념을 네트워크로 확장하여, 로컬 환경과 동일한 방식으로 원격 객체에 접근할 수 있게 해준다.
```

<br><br>

### 사용법

직렬화를 사용하기 위해서는 `Serializable` 인터페이스를 `Implements` 해야한다.

`Serializable` 인터페이스는 구현할 메서드가 없는 마커 인터페이스로 JVM이 직렬화를 고려하여 작성한 클래스인지를 판단하는 기준으로 사용된다.

만약 `Serializable` 을 `Implements`하지 않으면 `NotSerializableException`이 발생하게 된다.

```java
public class User implements Serializable {

	private String name;
  private int age;
}
```
<br>

객체를 직렬화할때는 `ObjectOutputStream`을 사용한다.

```java
public class SerializationExample {
    public static void main(String[] args) {
        // 직렬화할 객체 생성
        User user = new User("Jae", 30);

        // 객체를 직렬화하여 파일에 저장
        try (FileOutputStream fileOut = new FileOutputStream("user.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(user);  // 객체 직렬화
            System.out.println("객체가 직렬화되어 파일에 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

- `FileOutputStream` : **파일에 데이터를 바이트 단위로 쓰는** 역할
- `ObjectOutputStream` : **객체를 바이트 스트림으로 직렬화**하는 역할
- `writeObject()` : `user` 객체를 바이트로 변환(직렬화)하고, 이를 `FileOutputStream`을 통해 지정된 파일

<br>

반대로 역직렬화는 `ObjectInputStream`을 사용한다.

```java
public class DeserializationExample {
    public static void main(String[] args) {
        // 파일에서 객체를 역직렬화하여 복원
        try (FileInputStream fileIn = new FileInputStream("user.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            User user = (User) in.readObject();  // 객체 역직렬화
            System.out.println("객체가 역직렬화되었습니다: " + user);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

- `FileInputStream` : 파일로부터 바이트 데이터를 읽어들이는 스트림
`user.ser` 파일을 열어, 그 안에 있는 직렬화된 데이터를 읽을 준비를 한다.
<br>

- `ObjectInputStream` : 직렬화된 **객체 데이터를 읽어서 객체로 변환**하는 역할
`FileInputStream`에서 읽어들인 바이트 데이터를 **객체로 역직렬화**할 준비를 한다.
<br>

- `readObject()` : 직렬화된 데이터를 읽고 이를 **역직렬화하여 원래 객체**로 변환하는 메서드
파일로부터 읽은 직렬화된 데이터를 **`User` 객체로 복원한다.**

<br><br>

### serialVersionUID

Java의 직렬화에서는 객체를 직렬화하여 저장하거나 네트워크를 통해 전송한 후, 다시 객체로 복원(역직렬화)할 때 **클래스의 일관성**이 매우 중요하다. 객체가 직렬화된 후 해당 클래스가 변경되었다면, 역직렬화 시 문제가 발생할 수 있다.

이 문제를 방지하기 위해 `serialVersionUID`를 사용하여 클래스의 버전을 관리한다.

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;  // 클래스의 버전을 명시적으로 지정
    private String name;
    private int age;
}
```

<br>

`serialVersionUID`는 직렬화된 객체가 저장된 상태와, 역직렬화 시점의 클래스가 **같은 버전**인지 확인하는 데 사용된다.  직렬화 시점과 역직렬화 시점에서 클래스의 `serialVersionUID`가 일치해야만 객체가 정상적으로 역직렬화될 수 있다.

만약 클래스에 `serialVersionUID`를 명시하지 않으면, 컴파일러가 **자동으로** 생성한다. 하지만 클래스가 변경될 때마다 `serialVersionUID`가 새로 생성되기 때문에 **환되지 않는 문제**가 발생할 수 있다.

그래서 수동으로 설정하는 것이 일반적이다.

<br><br>

### 주의할점

직렬화할 클래스에 **참조 변수**가 있다면, 그 참조하는 객체도 반드시 **`Serializable` 인터페이스를 구현**해야 한다. 직렬화 과정에서는 **객체 그래프**(객체가 참조하는 다른 모든 객체들)도 함께 직렬화되기 때문다.

만약 참조하는 객체가 직렬화할 수 없는 클래스라면 **`NotSerializableException`** 예외가 발생한다.

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;  // 클래스의 버전을 명시적으로 지정
    private String name;
    private int age;
    private Team team; // 참조 변수
}

public class Team implements Serializable { // Team도 Serializabel을 구현
	private static final long serialVersionUID = 1L; 
	private String name;
}
```

만약 **참조 변수**가 많거나 객체가 복잡할 경우, **직렬화 비용**이 커질 수 있다. 모든 참조된 객체를 직렬화하려면 객체 그래프가 매우 커지고, 그로 인해 **CPU 사용량** 및 **메모리 사용량**이 크게 증가할 수 있다.

또한 직렬화가 불필요한 객체까지 직렬화해야 한다면, 불필요한 리소스 사용이 발생하게 된다.

그런상황에서는 `transient` **키워드**를 사용하면 특정 필드를 **직렬화에서 제외**할 수 있다.

**`transient`** 키워드를 사용하면 **직렬화에서 특정 필드를 제외**할 수 있다.  `User` 클래스에서 `Team` 필드를 직렬화하지 않도록 설정하려면, `transient` 키워드를 추가하면 된다.

```java
transient private Team team;  // 직렬화에서 제외할 필드
```

이렇게 하면 `User` 객체를 직렬화할 때, `team` 필드는 **직렬화되지 않고**, 직렬화된 결과에서 **무시**된다. 직렬화된 데이터를 **역직렬화**할 때는 `team` 필드가 **기본값**으로 설정된다.  `Team` 객체는 **null**로 설정된다.

또 다른 방법으로는 `writeObject()`와 `readObject()` 메서드를 오버라이드해서 원하는 필드만 선택적으로 직렬화하거나, 특정 필드를 직렬화에서 제외하는 방법이 있다.