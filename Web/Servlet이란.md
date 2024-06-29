# Servlet이란?

### Servlet이란

---

Java를 사용하여 웹 페이지를 동적으로 생성하는 클래스이다.

클라이언트로부터 요청이 오면 WAS가 해당 요청을 처리할수있는 서블릿을 찾는다.

요청을 처리할 서블릿의 라이프 사이클에 따라 요청의 타입(get, post)에 맞는 응답을 보여준다.

jsp가 없던 시기에는 서블릿에서 응답페이지(HTML)을 만들어 응답했었다.
<br>

우선 서블릿이 어떤 라이프 사이클을 갖는지 부터 알아보자
<br>
<br>

### Servlet Life Cycle

---

모든 서블릿은 HttpServlet을 상속받고 있다.

```java
public class myClass extends HttpServlet{
}
```
<br>

그리고 서블릿은 싱글톤 객체로 관리된다.

같은 요청이 반복될때마다 서블릿의 인스턴스를 매번 생성하는것은 효율적이지 않기 때문이다.

그래서 WAS는 요청을 처리할 수 있는 서블릿 인스턴스가 있는지 확인하고 없으면 인스턴스를 생성한다.
<br>

이렇게 생성된 서블릿은 모든 서블릿은 동일한 라이프 사이클을 가진다. 
`init` → `service` → `destroy` 
<br>

`init()`

- 서블릿을 초기화하고 서블릿이 처음 로드되고 인스턴스화될 때 한 번만 호출된다.
- 리소스 할당, DB 연결 등 설정 작업을 수행할 때 사용된다.

```java
public void init() throws ServletException{
}
```

<br>


`service()`

- 서블릿에 대한 요청을 처리

```java
public void service(HttpServletReqeust request, HttpServlerResponse response) throws ServletException{
	 if (req.getMethod().equalsIgnoreCase("GET")) {
        doGet(req, res);
    } else if (req.getMethod().equalsIgnoreCase("POST")) {
        doPost(req, res);
    }
}

protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<html><body>");
    out.println("<h1>GET request handled</h1>");
    out.println("</body></html>");
}

protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("<html><body>");
      out.println("<h1>POST request handled</h1>");
      out.println("</body></html>");
  }
```

- service()는 Http메서드에 따라 doGet(), doPost()등을 실행시킨다.
    - `doGet(HttpServletReqeust request, HttpServlerResponse response)` : get 방식으로 요청이 올때 처리할 메서드
    - `doPost(HttpServletReqeust request, HttpServlerResponse response)` : Post 방식으로 요청이 올 때 처리할 메서드
    - `HttpServletRequest` : 서블릿 컨테이너에 의해 제공되며 요청에 대한 정보를 포함
    - `HttpServletResponse` : 서블릿 컨테이너에 의해 제공되며 응답을 보낼 수 있도록 함
    

<br>

`destory`

- 서블릿이 삭제되기 전에 리소스를 정리
- 할당 받은 리소스, DB 연결 닫기 등

```java
public void destroy(){ }
```


<br>
이렇게 서블릿은 싱글톤으로 관리해 메모리 절약할 수 있고 라이프 사이클을 통해 요청을 처리할 수 있다.

그럼 이제 서블릿만으로 요청과 응답을 처리해 보고 어떤게 불편해서 Jsp가 만들었는지를 느껴보자
<br>
<br>

### 구현 해보기

---

우선 서블릿을 만들자

`HttpServlet`을 상속받아야한다.

```java
public class HelloServlet extends HttpServlet {
}
```

<br>

그리고 서블릿의 라이프 사이클 메서드들을 Override하자

```java
@Override
public void init() throws ServletException{
	System.out.println("hello 서블릿 init()");
}

@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	System.out.println("hello 서블릿 doGet()");
}

@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	System.out.println("hello 서블릿 doPost()");
}

@Override
public void destroy() {
	System.out.println("hello 서블릿 destroy()");
}
```

실제 서블릿의 라이프 사이클을 확인하기 위해 프린트문을 작성했다.

그리고 `service()`는 `HttpServlet` 에서 HTTP 메서드를 처리하는 기본 로직을 구현하고 있어 `doGet()`과 `doPost()` 만 작성해주면 된다.

<br>

Get 요청이 올 때 응답할 페이지를 작성해보자.

```java
@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("hello 서블릿 doGet()");
    
    // 응답의 컨텐츠 타입과 인코딩 설정
    response.setContentType("text/html");
    
    PrintWriter out = response.getWriter();
		
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<meta charset=\"UTF-8\">");
    out.println("<title>Insert title here</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Hello Servlet!</h1>");
    out.println("<form action=\"hello\" method=\"post\">");
    out.println("<input type=\"text\" name=\"name\"><br>");
    out.println("<input type=\"submit\" value=\"Click\">");
    out.println("</form>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
	}
	
```

PrintWriter 라는 객체를 사용해 모든 HTML 태그를 감싸 출력하고 있다.
이미 이것만 봐도 서블릿으로만 응답하는건 매우매우 귀찮아 보인다.

form 태그를 보면

```html
<form action="hello" method="post">
	<input type="text" name="name"><br>
	<input type="submit" value="Click">
</form>
```

라고 작성이 되어있는데 HTML form 태그는 클라이언트에서 서버로 데이터를 전송하는 방식을 정의한다. 여기서 action 속성은 데이터를 보낼 URL을 지정하고, method 속성은 데이터를 전송하는 HTTP 메서드를 지정한다. 
<br>

사용자가 이름을 입력하고 "Click" 버튼을 클릭하면 “/hello” URL로 데이터를 `POST` 메서드로 전송된다.
여기서 WAS가 “/hello”라는 URL로 응답해줄 서블릿을 어떻게 찾아가서 요청을 하지? 라는 생각이 들것이다.
WAS는 설정파일에서 등록된 서블릿을 보고 찾아간다. “/hello”는 등록된 서블릿의 URL이다.

우리가 만든 서블릿도 등록해보자

설정파일은 WEB-INF/web.xml에 작성한다.

```java
<!--서블릿 등록  -->
<servlet>
	<servlet-name>helloServlet</servlet-name>
	<servlet-class>servlet.HelloServlet</servlet-class>
</servlet>
 	
<!-- 경로 등록 -->
<servlet-mapping>
	<servlet-name>helloServlet</servlet-name>
	<url-pattern>/hello</url-pattern>
</servlet-mapping>
```

<br>

이제 다시 돌아와 Post 요청이 올 때  응답할 페이지를 작성해보자.

```java
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("hello 서블릿 doPost()");
	
    // 한글 처리를 위한 인코딩 설정
    request.setCharacterEncoding("UTF-8");
    
    // 클라이언트에서 전송된 파라미터 'name' 가져오기
    String name = request.getParameter("name");
    
    // 응답의 컨텐츠 타입과 인코딩 설정
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
	
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<meta charset=\"UTF-8\">");
    out.println("<title>Insert title here</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Hello "+name+"</h1>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
}
```

doGet()과 마찬가지로 PrintWriter 객체로 HTML 태그를 출력하고 있다.
HttpServletRequest 객체로 요청으로 온 파라미터 데이터를 꺼낼수있다.

이제 실행해 보자

(http://localhost:8088/servlet/hello) 우리가 설정파일에 등록해준 서블릿으로 요청을 보내보자
우리가 요청 (http://localhost:8088/servlet/hello)을 하면 WAS가 설정 파일에 등록된 서블릿을 찾는다.  
기본적으로 url로 접근하면 Get방식이다.
그럼 WAS가 우리가 등록한 HelloServlet의 인스턴스를 생성하고 라이프 사이클이 동작할것이다.

<br>

![Untitled](/Web/img/Servlet(1).png)
서블릿이 생성되었으니 init()이 호출되었고 url로 접근했으니 doGet()이 호출되어 화면을 출력할것이다.
<br>


![Untitled](/Web/img/Servlet(2).png)
정상적으로 화면이 출력되었다.
데이터를 입력하고 Click을 눌러 데이터를 전송하면 
<br>


![Untitled](/Web/img/Servlet(3).png)
WAS가 form태그의 action 속성과 method 타입 확인해 서블릿을 찾아 응답한다.
post로 지정해줬기 때문에 doPost()가 호출된다.
<br>


![Untitled](/Web/img/Servlet(4).png)
마찬가지로 정상적으로 요청을 처리했다.
<br>


![Untitled](/Web/img/Servlet(5).png)
마지막으로 서버를 끄면 라이프사이클의 마지막인 detroy()가 호출된다.

<br>

이렇게 서블릿만으로 요청과 응답을 해봤다.
자바코드만으로 화면을 출력하기에는 너무 귀찮은 일이 많다.
지금 예제에서는 간단한 HTML 코드인데도 너무 길어진다.
그래서 나온 Jsp는 HTML에 자바코드를 넣어 보다 쉽게 화면을 그릴수있게 해준다.

다음은 Jsp를 사용해 요청과 응답을 해보겠다.