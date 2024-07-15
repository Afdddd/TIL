# IoC란

---

### IoC란?

Inversion of Control의 약자로 **제어의 역전**이라는 뜻이다.

객체에 대한 생성 및 생명주기 관리 등을 개발자가 아닌 외부 프레임워크가 담당하는것을 제어의 역전이라고 한다.

간단한 예로 커피샵에서 커피를 주문하는 코드이다.

우선 개발자가 객체를 제어할 때의 예이다.

캐셔는 바리스타에게 커피를 요청한다.

<br>

```java
public class Cashier {
    private Barista barista;

    public Cashier(Barista barista) {
        this.barista = barista;
    }

    public void order(String coffee){
        System.out.println("Cashier : "+coffee+" 만들어 주세요.");
        barista.makeCoffee(coffee);
        System.out.println("Cashier : 주문하신 "+coffee +" 나왔습니다.");

    }
}
```

<br><br>

바리스타는 커피를 만든다.

```java
public class Barista {

    public void makeCoffee(String coffee){
        System.out.println("Barista : 넵");
        System.out.println("(커피 만드는 중...)");
    }
}
```

<br><br>

이제 주문을 해보자

```java
public class CoffeeShop {

    public static void main(String[] args) {

        Barista barista = new Barista();
        Cashier cashier = new Cashier(barista);

        cashier.order("아이스 아메리카노");

    }
}
```

손님이 캐셔를 통해 아이스 아메리카노를 주문했다.

<br>

![Untitled](/Spring/img/ioc(1).png)

정상적으로 커피를 주문할수 있다.

여기서 객체의 생성관리를 누가하는지 생각해보자.

커피를 주문하기 위해선 캐셔가 필요하고 캐셔는 커피를 만들수있는 바리스타가 필요하다.

그래서 캐셔와 바리스타가 필요할때 객체를 생성해 주문을 했다.

```java
Barista barista = new Barista();
Cashier cashier = new Cashier(barista);
```

이렇게 내가(개발자) 필요할때 객체를 생성하는 상황은 개발자가 객체를 관리하는것이다.

스프링 프레임워크에서는 객체를 Bean으로 등록을 하고 스프링 컨테이너가 빈(객체)들을의 생명주기를 관리한다.

<br>

## 스프링 컨테이너와 스프링 빈?

---

스프링 컨테이너는 스프링 빈의 메타 데이터를 읽어 인스턴스화, 구성 및 조립을 담당한다.

스프링 빈은 스프링 컨테이너에게 관리 당하는 자바 객체를 말한다.

![Untitled](/Spring/img/ioc(2).png)

**ApplicationContext** 나 **BeanFactory** 를 스프링 컨테이너라 부른다.

- **BeanFactory** : 스프링 컨테이너의 최상위 인터페이스, 스프링 빈을 관리하고 조회하는 역할을 제공한다.
- **ApplicationContext**  : BeanFactory를 모두 상속받아 기능을 제공하고 많은 부가 기능을 제공한다.

ApplicationContext를 많이 사용한다.

<br>

ApplicationContext를 스프링 빈의 메타 데이터를 읽어오는 방법 별로 구현한 객체들이 있다.

- **AnnotaionConfigApplication** : @어노태이션 기반으로 메타 데이터를 읽어온다.
- **GenericXmlApplication** : XML 기반으로 메타 데이터를 읽어온다.

```jsx
ApplicationContext ac = new GenricXmlApplicationContext(appConfgi.xml);
ApplicationContext ac = new AnnotationApplicationContext(appConfig.class);
```

<br><br>

## 스프링 컨테이너의 스프링 빈 등록 과정

---
<br>

1. 스프링 컨테이너에 설정 파일을 매개변수로 넣어 주면 스프링 컨테이너가 스프링 빈의 메타데이터를 읽는다.

![Untitled](/Spring/img/ioc(3).png)




<br><br>

2. 메타 데이터를 읽어온 스프링 컨테이너는 객체를 스프링 빈으로 등록한다.

![Untitled](/Spring/img/ioc(4).png)



스프링 컨테이너는 객체의 메타 데이터를 읽어 스프링 빈으로 등록하고 관리 해준다

<br>

이제 커피샵 예제 코드로 구현해 보자.

빈들을 등록할 설정파일을 만들고 바리스타와 캐셔를 빈으로 등록한다.

<br>

```java
@Configuration
public class config {

    @Bean
    public Barista barista(){
        return new Barista();
    }

    @Bean
    public Cashier cashier(Barista barista){
        return new Cashier(barista);
    }

}
```

- @Configuration : 스프링 컨테이너가 설정파일을 식별할 수 있게 해준다.
- @Bean :  이 메서드가 반환하는 객체를 스프링 컨테이너에 빈으로 등록한다.

이렇게 빈으로 등록을 해주면 스프링 컨테이너가 빈들을 관리해준다.

빈으로 등록된 객체는 기본적으로 싱글톤으로 관리된다. 즉, 한 번 생성된 빈은 스프링 컨테이너에 의해 관리되며 여러 요청에 대해 동일한 인스턴스가 반환된다.


<br>

이제 스프링 컨테이너를 사용해서 주문을 해보자.

```java
public class CoffeeShop {

  public static void main(String[] args) {

      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(config.class);
      Cashier cashier = applicationContext.getBean("cashier",Cashier.class);

      cashier.order("아이스 아메리카노");

  }
}
```

- `AnnotationConfigApplicationContext`: `@Configuration` 어노테이션이 적용된 클래스를 사용하여 스프링 컨테이너를 초기화한다.
- `getBean()`: 스프링 컨테이너에서 관리하는 빈을 가져온다.

![Untitled](/Spring/img/ioc(5).png)

주문이 잘되었다.

<br><br>

### 스프링 컨테이너의 내부 작동 방식


스프링 컨테이너는 설정 파일을 보고 이를 초기화하며, 필요한 객체를 생성하고 의존성을 주입한다. 좀 더 자세히 알아보자

1. **스프링 컨테이너 초기화**

먼저, 스프링 컨테이너를 초기화한다.

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(config.class);
```

<br>

2. **빈 생성 및 등록**

`barista()` 메서드가 호출되어 `Barista` 객체가 생성된다.

```java
@Bean
public Barista barista() {
    return new Barista();
}
```

<br>

3. **빈 의존성 주입**

빈 간의 의존성이 있는 경우, 스프링 컨테이너는 이를 자동으로 주입한다.  `Cashier` 빈은 `Barista` 빈이 필요하다. 스프링 컨테이너는 `cashier()` 메서드를 호출할 때 `barista()` 메서드를 먼저 호출하여 `Barista` 객체를 생성한다. 그 다음, 생성된 `Barista` 객체를 `Cashier` 생성자의 매개변수로 전달하여 `Cashier` 객체를 생성한다.

```java
@Bean
public Cashier cashier(Barista barista) {
    return new Cashier(barista);
}
```

<br>

4. **빈 사용**

`cashier` 빈을 `getBean()` 메서드를 통해 가져와 사용합니다. 이 빈은 이전에 설정 클래스에서 생성되고 등록된 객체이다.

```java
Cashier cashier = applicationContext.getBean("cashier",Cashier.class);
```

이렇게 스프링 컨테이너가 설정 파일을 보고 객체를 생성하고 빈으로 등록하며, 필요할 때 의존성을 주입하는 과정을 **제어 역전(IoC)** 이라고 한다.




<br><br><br><br><br>
### Reference
https://www.inflearn.com/course/lecture?courseSlug=%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8&unitId=55349&tab=curriculum