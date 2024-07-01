# Jsp란?


이전 글 [Servlet이란?](Servlet이란.md) 에서 Servlet의 특징과 Servlet만으로 요청과 응답을 처리하는 과정에 대해 알아 보았다. 이번에는 Jsp란 무엇인가에 대해 알아보고 Jsp를 사용해서 요청과 응답을 처리해보자.

<br>

### Jsp란?

---


Java Server Page의 약자로 기존 서블릿만으로 요청과 응답을 처리하던 당시에는 요청이 들어오면 Java 코드로 HTML 화면을 만들어 요청을 해줬다. 


```java
String name = request.getParameter("name");
		
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
```

이렇게 짧은 HTML은 Java 코드로 화면을 출력하는게 어렵지 않지만 대부분의 화면들은 엄청나게 많은 HTML 태그들로 이루어져있다.

모든 태그들을 Java코드로 출력하는건 매우 귀찮을것이다.

그래서 Jsp가 나왔다.

서블릿은 Java코드 내에 HTML을 삽입했다면 Jsp는 HTML 내에 Java 코드를 삽입해 동적인 페이지를 보여준다.

<br>

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String name = request.getParameter("name");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Hello <%=name %></h1>
</body>
</html>
```

`<% %>` 액션 태그를 사용해 HTML 내에 Java 코드를 넣을 수 있다. 

Jsp를 사용하면 개발자는 HTML을 자연스럽게 작성할 수 있고 Java코드를 삽입해 이전에 서블릿으로 만 개발했을 때 보다 효율적으로 개발을 할 수 있게 되었다.

<br>

### Jsp의 실행 과정

![Untitled](/Web/img/jsp(1).png)

(a) : Jsp로 처음 요청이 올 때

1. WAS 내에서 Jsp 엔진이 Jsp파일을 Java코드로 변환한다.(Jsp→Servlet)
2. JVM이 Java 파일을 class 파일로 컴파일한다.
3. JVM에서  class파일을 읽어 인스턴스를 생성한다.
4. 생성된 서블릿으로 요청에 대한 응답을 처리해준다.

<br>

(b) : 이후 같은 Jsp로 요청이 올 때

1. 위의 모든 과정을 생략하고 이미 생성되어있는 서블릿 인스턴스를 사용해 응답한다.


<aside>
💡 서블릿이 싱글톤으로 관리되기 때문에 생략이 가능하다.
</aside>

<br><br>

Jsp가 서블릿으로 컴파일되는 과정을 좀 더 자세히 알아보자

우선 jsp파일을 작성해보자

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%! // 메서드 선언문
@Override
public void init() throws ServletException {
	System.out.println("hello.jsp init()");
}
%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	</head>
	<body>	
	<form action="controller.jsp" method="post">
		<h1>Hello Servlet!</h1>
		<input type="text" name="name"><br>
		<input type="submit" value="Click">
	</form>
	
	</body>
</html>
```

`<%! ~%>` 는  jsp파일내에서 메서드를 선언할수 있게 해준다.

`init()` 를 오버라이드 했다.

출력할 화면은 HTML로 작성해주면 된다.

이제 서버를 실행시켜 hello.jsp로 요청을 주면 WAS가 해당 Jsp파일을 Jsp엔진에게 Java 파일로 컴파일하라는 명령을 보내 Java파일이 생성이 될것이다.

<br><br>

![Untitled](/Web/img/jsp(2).png)

workspace에 컴파일된 Java파일이 보인다.

이제 컴파일된 Java 파일을 열어보면 

```java
public final class hello_jsp extends org.apache.jasper.runtime.HttpJspBase{

...생략

@Override
public void init() throws ServletException {
	System.out.println("hello.jsp init()");
}

...생략

// service()
public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response){

try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("    \r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta charset=\"UTF-8\">\r\n");
      out.write("<title>Insert title here</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("<form action=\"controller.jsp\" method=\"post\">\r\n");
      out.write("	<h1>Hello Servlet!</h1>\r\n");
      out.write("	<input type=\"text\" name=\"name\"><br>\r\n");
      out.write("	<input type=\"submit\" value=\"Click\">\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } 
...생략

}
```

우선 `HttpJspBase` 을 상속을 받고있다. `HttpJspBase` 는 `HttpServlet`의 하위 클래스이다. 즉 이 클래스는 `HttpServlet`을 상속받은 서블릿 클래스라는 것이다.

그리고 재정의한 `init()`도 보인다.

그리고 `service()` 에서 내가 작성한 HTML을 Jsp엔진이 알아서 Java코드로 감싸 출력문을 만들어준다.

이렇게 Jsp를 사용하면 기존에 서블릿으로 매번 HTML코드를 직접 감싸 출력해주던 번거로움을 Jsp엔진이 대신 처리해줘 보다 편하게 개발을 할 수 있다.

<br>

그럼 이제 Jsp만으로 요청을 받아 응답을 처리해보자

hello.jsp

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="welcome.jsp" method="post">
	<h1>Hello Servlet!</h1>
	<input type="text" name="name"><br>
	<input type="submit" value="Click">
</form>

</body>
</html>
```

/hello.jsp로 요청이 들어오면 보여줄 페이지이다.

서버가 실행되서 /hello.jsp로 요청이 들어오면 위에서 봤듯이 서블릿으로 컴파일되고 서블릿의 라이프사이클을 실행시킨다.(init() → service()→ detroy())

![Untitled](/Web/img/jsp(3).png)


form 태그에 name을 입력하고 전송하면  welcome.jsp에게 post방식으로 요청을 보낸다.

<br>

welcome.jsp

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String name = request.getParameter("name");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% if(name.equals("inyeop")) {%>
	<h1>Hello <%=name %></h1>
	<% }else{ %>
	<% response.sendRedirect("/servlet/hello.jsp"); %>
	<% } %>
</body>
</html> 
```

welcome.jsp에서는 요청으로 넘긴 파라미터가 “inyeop”인지를 확인하고 그에 맞는 화면을 출력해준다. 


<br>
name이 inyeop일 경우

![Untitled](/Web/img/jsp(4).png)

name이 inyeop이 아닐 경우에는 다시 hello.jsp로 보내 값을 다시 입력하게 한다.

![Untitled](/Web/img/jsp(5).png)


Jsp만으로 요청과 응답을 처리해봤다.

Jsp로 개발을 해보니 서블릿만으로 개발을 하는것보다 더 편리하고 효율적이었다.
하지만 이런 Jsp만으로 처리를 할 때에도 단점이 있다.

우선 welcome.jsp는 하는일이 2개이다.

1. 비즈니스 로직 처리(name 비교하고 어떤 화면 출력해줄지 선택)
2. 화면 출력(HTML로 화면 출력)

개발을 하다보면 하나의 Jsp에서 더 많은 요청을 처리해야하고 더 많은 HTML을 출력해야 될것이다. 

<br>

그러면 점점 더 복잡해지고 비즈니스 로직이 변경이 되면 HTML도 변경을 해줘야하기때문에 유지보수성도 떨어질것이다.
그리고 프런트와 백엔드가 하나의 Jsp에 섞여있어 분업 또한 용이하지 않아 효율성이 떨어질것이다.

한번 welcome.jsp의 역할 분리해보자!

우선 Jsp의 장점은 HTML코드를 쉽게 출력할수있다는 점이다.
그렇다면 welcome.jsp는 화면 출력만 신경쓰고 비즈니스 로직 처리(name 비교)를 다른 클래스에게 위임하면 해결될것이다.

welcome.jsp

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String name = request.getParameter("name");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<h1>Hello <%=name %></h1>
	
</body>
</html> 
```

welcome.jsp는 이제 이름 비교를 하고 어떤 화면을 출력해줄지에 대한 비즈니스 로직은 없다.
오직 요청으로 들어온 name을 출력해주는 일만 한다. name 또한 어떤것이 들어오는지 모른다.
화면을 출력하는 역할만 할 뿐이다.

그럼 이제 비즈니스 로직을 처리할 jsp를 만들어 보자

controller.jsp

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");	

	String name = request.getParameter("name");

	if(name.equals("inyeop")){
		response.sendRedirect("/servlet/welcom.jsp"+"?name="+name);
	}else{
		response.sendRedirect("/servlet/hello.jsp");
	}

%>
```

controller.jsp는 요청을 받아 name을 비교해 “inyeop”일 경우 welcome.jsp로 name과 함께 이동시켜준다. “inyeop”이 아닐경우에는 hello.jsp로 다시 이동시켜준다.

이렇게 역할을 분리할 수 있다.

하지만 controller.jsp를 보면 오직 Java 코드뿐이다. 화면을 출력하는 역할이 없기 때문에 
어차피 controller.jsp는 컴파일되어 서블릿이 되기 때문에 굳이 jsp로 만들지 않고 처음부터 서블릿으로 만들면 컴파일 과정이 생략될 것이다. 

서블릿을 바꾸자

```java
public class Controller extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/hello.jsp").forward(req, resp);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String name = req.getParameter("name");
		
		if(name.equals("inyeop")) {
			resp.sendRedirect("/welcome.jsp");
		}else {
			resp.sendRedirect("/hello.jsp");
		}
	}
	
	
}

```

controller 라는 서블릿에 Get 요청이 오면 hello.jsp를 보여주고

Post 요청이 오면 비즈니스 로직을 수행하고 어떤 화면을 보여줄지 선택해준다.

작성한 controller를 WAS가 찾아갈수 있게 설정파일에 추가해주자

```xml
	<servlet>
 		<servlet-name>controller</servlet-name>
 		<servlet-class>servlet.Controller</servlet-class>
 	</servlet>
 	
 	<servlet-mapping>
 		<servlet-name>controller</servlet-name>
 		<url-pattern>/hello</url-pattern>
 	</servlet-mapping>
```

이렇게 되면 Jsp를 사용해 화면을 출력하고 서블릿을 사용해 비즈니스 로직을 수행하듯이 역할을 분리하면 유지보수성이 더 좋은 웹개발을 할 수 있다.

비즈니스 로직을 수행해 어떤 화면을 보여줄지 선택해주는 Controller와 화면을 출력하는 View를 분리하여 개발하는 디자인 패턴을 MVC 패턴이라 한다.

<br>

다음은 MVC 패턴에 대해 알아보자

<br><br><br><br><br>

## Reference

[https://ko.wikipedia.org/wiki/파일:JSPLife.png](https://ko.wikipedia.org/wiki/%ED%8C%8C%EC%9D%BC:JSPLife.png)
[https://www.geeksforgeeks.org/difference-between-servlet-and-jsp/?ref=lbp](https://www.geeksforgeeks.org/difference-between-servlet-and-jsp/?ref=lbp)