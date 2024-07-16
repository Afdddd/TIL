# DI란
<br>

>DI란?

Dependency Injection의 약자로 의존성 주입이라는 뜻이다.

객체가 자신의 의존성을 직접 생성하지 않고 외부에서 주입받도록 하는 방식이다.

>의존성?

의존성은 한 객체가 다른 객체를 참조하고 사용할 때 발생한다. 예를 들어, 클래스 `A`가 클래스 `B`를 참조하고 사용한다면, `A`는 `B`에 의존한다고 한다.

```java
public class A{
	B b;
	
	public A(B b){
		b = new B();
	}	
	
	public void doSomething(){
		b.doSomething();
	}
}
```

위의 코드에서 클래스 `A`는 `B` 객체를 직접 생성하고 있다. `A`가 `B`에 강하게 결합되어 있음을 의미한다.

Spring에서는 이런 결합도를 낮추게 하기 위해 스프링 컨테이너가 객체들의 생명주기를 관리한다.

그리고 의존관계를 식별해서 필요한 경우 의존성을 주입해준다.

스프링에서는 `@Autowired`라는 어노테이션을 사용해서 자동으로 의존 관계를 식별하고 주입해준다.

`@Autowired`는 다양한곳에 붙여 사용할 수 있다.

<br><br>

>### 필드 주입

```java
@Component
public class A {
    @Autowired private B b;

    public void doSomething() {
        b.doSomething();
    }
}
```

>💡@Component란?

개발자가 작성한 클래스를 스프링 빈으로 등록하는 데 사용된다. @Component 어노테이션을 사용하면 해당 클래스가 자동으로 스프링 컨테이너에 의해 인식되고 관리한다.

<br>

매우 간단하고 직관적이지만 지양하는 방법이다. 실제로 IntelliJ에서는 필드 주입을 사용하면 "Field Injection is not recommended"라는 경고문을 띄운다.

- final을 사용하지 못함
    
    필드 을 사용하면 `final` 제어자를 사용하지 못한다. **`b`** 객체가 중간에 변경이 될수도있다.
    
- 테스트가 어렵다
    
    순수 자바 테스트 코드는 @Autowired가 동작하지 않는다.그렇기 때문에 순수 자바 테스트코드를 작성할때 의존 관계 주입 자체를 하지 못한다.(`private`으로 막혀있고, 생성자나 `setter`가 없기 때문)
    
<br><br>

>### 수정자 주입

```java
@Component
public class A {
	  private B b;
	  
	  @Autowired
	  public void setB(B b){
		  this.b = b;
	  }

    public void doSomething() {
        b.doSomething();
    }
}
```

`public` 으로 열려있으므로 **선택,변경** 가능성이 있는 의존관계에 사용한다.

필드 주입과 마찬가지로 `final`을 사용하지 못한다.

<br><br>

>### 생성자 주입

```java
@Component
public class A {
	  final private B b;
	  
	  @Autowired
	  public A(B b){
		  this.b = b;
	  }

    public void doSomething() {
        b.doSomething();
    }
}
```

가장 많이 사용되는 주입 방식이다.

생성자 주입은 스프링 빈으로 등록과 동시에 의존 관계를 주입한다. 
그리고 생성자 호출 시점때 딱 1번 실행되기 때문에 **불변**이다.또한 생성자 주입을 사용하면 필드에 `final`키워드를 사용할 수 있다. 

그렇게 때문에 객체 생성 시 값이 **누락**되면 오류를 컴파일 시점에서 막아준다.