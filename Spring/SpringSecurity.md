# Spring Security

### Spring Security란?

스프링 프레임워크를 기반으로 어플리케이션의 **인증**과 **인가**를 위한 강력한 기능을 제공하는 프레임 워크이다. REST API의 보안을 담당하며, 사용자 인증, 권한 부여 같은 다양한 보안 문제를 처리하는 데 사용된다.

<br>

Spring Security에는 3가지 중요한 개념이 있다.

- **인증(Authentication)**
- **인가(Authorization)**
- **서블릿 필터(Servlet Filter)**


<br><br>

### 인증이란?

**Authentication**

**인증**은 사용자가 누구인지 확인하는 과정이다. 
Spring Security에서는 사용자가 입력한 자격 증명(username, password)을 시스템에 저장된 자격 증명과 비교해 인증을 수행한다.

<br><br>

### 인가란?

**Authorization**

**인가**는 사용자가 특정 리소스에 접근할 수 있는 권한이 있는지를 결정하는 과정이다.
Spring Security는 사용자에게 할당된 역할(Role)이나 권한(Authority)을 기반으로 인가를 수행한다. 예를 들어, 관리자만 접근할 수 있는 페이지가 있는 경우, Spring Security는 사용자가 관리자 권한을 가지고 있는지를 확인하고 접근을 허용하거나 차단한다.

<br><br>

### 서블릿 필터

Servlet Filter

**서블릿 필터(Servlet Filter)** 는 자바 기반 웹 애플리케이션에서 서블릿에 요청이 도달하기 전이나 응답이 클라이언트로 전송되기 전에 요청과 응답을 가로채고 조작할 수 있는 컴포넌트이다.

```
💡서블릿 필터가 중요한 이유?
서블릿 필터가 Spring MVC 패턴에서 보안 로직을 효과적으로 처리할 수 있는 위치를 제공해주기 때문이다.

Spring mvc 패턴에서 요청이 들어오면 `dispatcherServlet`이 요청을 받고 알맞은 `controller`로 라우팅 한다. 
이 흐름 사이에 보안 관련된 코드를 넣을곳이 없기 때문인다. `dispatcherServlet`은 요청 처리, 뷰 선택, 응답 
생성 등의 MVC 관련 작업에 집중하는 것이 목적이고, `controller`는 실제 요청을 처리하는 부분이기 때문에 
`controller`에 보안관련 코드는 적합하지 않다. 또한 보안 로직이 각 컨트롤러에 중복될 수 있다. 그렇기 때문에 
서블릿에 요청이 도달하기 전에 보안 로직을 수행할 수 있는 서블릿 필터가 중요한 것이다.
```

<br><br>

### FilterChain

Filter Chain은 클라이언트의 HTTP 요청을 처리하기 위해 구성된 **필터** 집합을 말한다.

![image.png](/Spring/img/springSecurity(1).png)

Spring Security는 인증과 인가를 서블릿 필터를 통해 수행한다. 하지만 서블릿 필터와 Spring Security는 라이프사이클 불일치가 발생한다.

<br><br>

### Servlet Filter와 Spring Security의 불일치
<br>

**Servlet Filter**는 **Servlet Containe**r에 의해 관리가 된다.

- 초기화 : 서블릿 컨테이너가 시작될 때  `web.xml` 에 정의한 필터가 초기화 된다.
- 요청 처리 :  HTTP 요청이 들어올 때마다 서블릿 컨테이너는 요청을 필터 체인을 통해 처리한다.
- 종료 : 서블릿 컨테이너가 종료될 때 필터가 종료된다.

<br>

**Spring Security**는 **ApplicationContext**에 의해 관리가 된다.

- **컨텍스트 초기화** : Spring 애플리케이션이 시작될 때 `ApplicationContext`가 초기화되며, Spring 컨텍스트에 정의된 Bean들이 생성되고 초기화된다.
- **Bean 초기화 및 관리** : `ApplicationContext`는 Bean의 라이프사이클을 관리하며, Bean들이 의존성 주입을 통해 설정된다.
- **컨텍스트 종료 :** 애플리케이션이 종료될 때, `ApplicationContext`는 모든 Bean을 종료하고 자원을 정리한다.

<br>

Servlet Filter 와 Spring Security의 라이프사이클 불일치로 초기화 문제가 발생할 수 있다.
서블릿 필터는 서블릿 컨테이너가 시작될 때 바로 초기화되지만, Spring `ApplicationContext`는 보통 서블릿 컨테이너가 시작된 후 `ContextLoaderListener`나 Spring Boot의 자동 설정에 의해 초기화된다. 즉, 필터가 초기화될 때 `ApplicationContext`가 아직 준비되지 않은 상태일 수 있다.

이러한 문제를 `DelegatingFilterProxy` 로 해결할 수 있다.

<br><br>

### DelegatingFilterProxy

![image.png](/Spring/img/springSecurity(2).png)

`DelegatingFilterProxy`는 서블릿 컨테이너의 필터와 Spring의 `ApplicationContext` 간의 연결 역할을 한다. 서블릿 필터로서 서블릿 컨테이너에 등록되지만 이 필터는 실제 필터 작업을 Spring `ApplicationContext` 내의 특정 Bean에게 위임(delegate)한다. 

`DelegatingFilterProxy`는 지연로딩을 지원하기 때문에 `ApplicationContext` 초기화가 완료된 후에 필터 Bean을 사용한다. 이로 인해 필터 인스턴스를 등록할 때 발생할 수 있는 초기화 문제를 방지할 수 있다.

또한 필터가 Spring `ApplicationContext`에서 관리되므로, Spring의 의존성 주입을 활용할 수 있게 된다.

<br><br>

### FilterChainProxy

`DelegatingFilterProxy`가 특정 Bean에게  위임한다고 했는데 그 특정 필터 Bean이 Spring Security의 `FilterChainProxy` 이다.

![image.png](/Spring/img/springSecurity(3).png)

Spring Security의 **중앙 보안 필터**로, Spring Security의 모든 보안 필터를 관리하고 실행하는 역할을 한다. HTTP 요청이 들어올 때, 요청을 적절한 `SecurityFilterChain`으로 전달하여 각 체인에 정의된 필터들을 실행한다.

<br><br>

### SecurityFilterChain

![image.png](/Spring/img/springSecurity(4).png)

FilterChainProxy가 요청에 맞는 SecurityFilterChain을 선택하면 선택된 SecurityFilterChain 내에 정의된 보안 필터들이 순서대로 실행된다. 만약 요청이 여러 `SecurityFilterChain`에 매칭되더라도, 가장 먼저 매칭된 하나의 `SecurityFilterChain`만 실행된다.