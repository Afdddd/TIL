# Builder 패턴

Builder 패턴이란 객체 지향 프로그래밍에서 복잡한 객체를 단계별로 생성하는 디자인 패턴이다.

<br>


### Builder 패턴 배경

일반적으로 객체를 생성할 때는 생성자와 Setter를 사용하여 파라미터를 전달하거나, 객체를 생성한 후 Setter 메서드를 통해 초기화한다.

<br>


**생성자를 통한 객체 생성**

생성자의 경우 필요한 파라미터를 오버로딩하여 필드를 초기화하는 방법을 사용한다. 하지만 필드가 많아지면 생성자의 수가 급격히 증가하고, 올바른 생성자를 호출하기 어렵게 된다.

```java
class User {
    private Long userId;
    private String userName;
    private String password;

    public User() {}
    public User(Long userId) {}
    public User(String userName) {}
    // ...다양한 생성자
}
```

<br>


**Setter를 통한 객체 초기화**

Setter는 파라미터가 없는 생성자를 사용하여 객체를 생성한 후, Setter 메서드를 사용하여 필드를 초기화하는 방법이다. 하지만 이 방법은 개발자가 반드시 직접 초기화 시켜줘야 하므로 실수로 빠뜨려 예외가 발생할 수 있는 일관성 문제와 Setter가 public 으로 열려있기 때문에 협업시 누군가 Setter 메서드를 호출해 값을 조작할 수 있는 불변성 문제가 발생할 수 있다.

```java
class User {
    private Long userId;
    private String userName;
    private String password;

    public User() {}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String password) {
        this.password = password;
    }
}
```

<br><br>


### Buidler 패턴 구현

```java
class UserBuidler{
 private Long userId;
 private String userName;
 private String password;

 
 public UserBuilder userId(Long userId){
	 this.userId = userId;
	 return this;
 }

 public UserBuilder userName(String userName){
	 this.userName= userName;
	 return this;
 }

 public UserBuilder password(String password){
	 this.password= password;
	 return this;
 }
 
 public User builder(){
	 return new User(userId, userName, password);
 }
 
}
```

값을 초기화해주는 메서드를 보면 `this`를 반환한다. 그 이유는 메서드 체이닝을 위해서이다.
빌더 객체 자신을 리턴함으로써 메서드 호출 후 연속적으로 빌더 메서드를 호출 할 수 있게 된다.
마지막으로 `User`객체를 생성하는 `builder`메서드를 만든다.

<br>

```java
User user = new UserBuilder()
            .userId(1L)
            .userName("김인엽")
            .userPassword("0000")
            .builder();
```

메서드 체이니으로 빌더 메서드를 호출해 필드를 초기화해주고 마지막에 `builder`메서드로 `User`인스턴스를 생성해 반환해준다.
Builder 패턴은 GOF의 디자인 패턴과 Effective Java의 Builder 패턴이 있다.

<br>

### Builder 패턴의 장점

- **가독성** : 메서드 체이닝을 통해 객체의 필드를 설정할 수 있어 객체 생성이 직관적이고 읽기 쉽다.
- **유연성** : 필요하지 않은 파라미터는 설정하지 않고도 객체를 생성할 수 있다. 필드의 추가나 변경이 필요할 때도 Builder 클래스만 수정하면 되므로 유지보수가 용이하다.
- **불변성** : Builder 패턴을 사용하면 생성된 객체를 불변(immutable)으로 만들 수 있다.
- **일관성** : 객체 생성 과정에서 모든 필요한 필드가 설정하므로 객체가 일관된 상태로 생성

<br>

### Effective Java의 Builder 패턴

Effective Java의 Builder 패턴은 내부 클래스로 구현된다.



```java
public class User {
    private final Long userId;
    private final String userName;
    private final String password;

    private User(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.password = builder.password;
    }

    public static class Builder {
        private Long userId;
        private String userName;
        private String password;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

```

- 캡슐화 : 내부 클래스로 구현해 세부 사항이 외부에 노출되지 않도록 한다.
- 가독성 : 빌더 클래스는 대상 객체 생성만 을 위한 클래스이므로 두 클래스를 묶어 가독성을 향상시킨다.
- 독립성 : static inner class로 정의하면, Builder 객체를 생성하기 위해 외부 클래스의 인스턴스가 필요하지 않아 독립적으로 Builder를 사용할 수 있게 해준다.

<br>

```java
User user = new User.Builder()
                .userId(1L)
                .userName("Kim In-yeop")
                .password("0000")
                .build();
```

<br>

### GOF의 Builder 패턴

```
💡GoF란?
소프트웨어 공학에서 널리 알려진 네 명의 컴퓨터 과학자를 지칭하는 용어
```


GoF의 Builder 패턴에서는 다음과 같은 구성 요소가 있다.

<br>

1. **Builder 인터페이스**: 객체를 생성하기 위한 단계별 메서드를 정의한다.
2. **ConcreteBuilder 클래스**: Builder 인터페이스를 구현하여 실제 객체 생성을 담당한다.
3. **Director 클래스**: Builder 인터페이스를 이용해 객체를 생성하는 메서드를 정의한다.
4. **Product 클래스**: 생성될 객체.

<br>

**User 클래스**

```java
public class User {
    private Long userId;
    private String userName;
    private String password;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
```

<br>

**Builder 인터페이스 :** 객체 생성에 필요한 단계별 메서드를 정의
```java
public interface UserBuilder {
    void buildUserId(Long userId);
    void buildUserName(String userName);
    void buildPassword(String password);
    User getUser();
}
```

<br>

**ConcreteBuilder 클래스** : Builder 인터페이스를 구현하여 실제 객체 생성을 담당
```java
public class ConcreteUserBuilder implements UserBuilder {
    private User user;

    public ConcreteUserBuilder() {
        this.user = new User();
    }

    @Override
    public void buildUserId(Long userId) {
        user.setUserId(userId);
    }

    @Override
    public void buildUserName(String userName) {
        user.setUserName(userName);
    }

    @Override
    public void buildPassword(String password) {
        user.setPassword(password);
    }

    @Override
    public User getUser() {
        return this.user;
    }
}

```

<br>

**Director 클래스** : Builder 인터페이스를 사용하여 객체를 생성하는 메서드를 정의
```java
public class UserDirector {
    private UserBuilder builder;

    public UserDirector(UserBuilder builder) {
        this.builder = builder;
    }

    public void constructUser(Long userId, String userName, String password) {
        builder.buildUserId(userId);
        builder.buildUserName(userName);
        builder.buildPassword(password);
    }

    public User getUser() {
        return builder.getUser();
    }
}

```

<br>

```java
UserBuilder builder = new ConcreteUserBuilder();
UserDirector director = new UserDirector(builder);

director.constructUser(1L, "Kim In-yeop", "0000");
User user = director.getUser();
```

GOF의 Builder 패턴은 객체 생성 과정을 단게별로 나누기 때문에 복잡한 객체를 생성할 때 유요하다.

<br>

### Lombok @Builder

Lombok의 `@Builder` 어노테이션을 사용하면 Builder 패턴을 매우 간단하게 적용할 수 있다.

Lombok은 Effective Java의 Builder 패턴으로 구현되어있다.

```java
@Builder
public class User {
    private Long userId;
    private String userName;
    private String password;
}
```

클래스 위에 @Builder를 붙여주면 Builder 코드를 생략할 수 있다.

```java
User user = User.builder()
              .userId(1L)
              .userName("Kim In-yeop")
              .password("0000")
              .build();
```