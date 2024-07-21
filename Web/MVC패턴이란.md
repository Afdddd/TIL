# MVC 패턴

---

### Web 개발의 역사

Java 웹 개발의 역사를 보면 Servlet만으로 개발하던 당시에는 Java 코드로 HTML을 표현해야 하기 때문에 번거로움이 있었다. 이런 번거로움을 해결하기 위해 나온 Jsp는 HTML에 Java 코드를 넣어 동적인 페이지를 좀 더 쉽게 개발할 수 있도록 도와주었다. 하지만 Jsp 내에 화면 출력과 비즈니스 코드 수행이라는 많은 역할을 맡아 유지보수가 힘들어지고 프론트와 백엔드가 혼합되어 현업이 힘들다는 단점이 생겼다. 이런 반복적으로 일어나는 문제를 해결하기 위한 디자인 패턴이 개발되었다.

바로 MVC 패턴이다. 

<br><br>

### MVC 패턴이란?

![Untitled](/Web/img/mvc(1).png)

Model-View-Controller의 약자로 프로그램의 구성요소들을 3개의 관심사를 분리하여 하나의 역할만 수행하도록 하는 디자인 패턴이다.



- Model
    - 데이터와 비즈니스 로직을 수행하는 역할
    - 데이터의 저장, 검색, 수정, 등 데이터 베이스와의 상호작용을 포함한 핵심 기능
    - Entity, DAO, 비즈니스 로직 등
- View
    - 사용자에게 보여줄 UI를 보여주는 역할
    - Controller로 부터 받은 데이터로 사용자의 요청에 맞는 화면을 표시
    
- Controller
    - 애플리케이션의 흐름을 제어하는 중심적인 역할
    - 사용자의 입력을 모델로 전달하고, 모델의 처리 결과를 바탕으로 뷰를 업데이트한다.

<br>

MVC 패턴의 가장 중요한점은 구성요소들이 독립적으로 역할을 수행해야한다는것이다.
구성 요소들이 독립적으로 수행하면 결합도는 낮아지고 응집도는 높아진다.

결합도가 낮아진다? 구성요소들간의 의존도가 낮아진다는 뜻이다. 
구성요소들의 역할을 명확하게 정의해준다면 한 구성요소가 변경되어도 다른 구성요소에 미치는 영향이 줄어든다는 것이다. 예로 View는 Model의 상태에만 의존하고 있어 Model의 상세 구현이 변경되어도 View에는 영향이 없는 것처럼 말이다.

반대로 응집도가 높아진다는 뜻은 책임이 높아진다는 뜻이다.
자신이 부여받은 역할에 맞는 기능만 수행해야한다. 예로 View는 화면을 보여주는 책임에만 집중해야한다.

이렇게 MVC 패턴을 사용해 결합도가 낮고 응집도가 높은 어플리케이션을 만드면 유지보수가 쉬워지고 확장성이 높아진다.

MVC 패턴은 과거에서부터 현재까지 많은 변화를 거쳐왔다.
MVC 패턴의 특징에 대해 알아봤으니 이번엔 MVC 패턴의 발전 과정을 알아보자

<br><br>

### MVC1

![Untitled](/Web/img/mvc(2).png)

MVC1의 아키텍처

MVC1은 이전 글(Jsp란)에서 구현해본 아키텍처이다.

MVC1은 Jsp가 요청을 받아 어떤 페이지를 보여줄지 결정하는 로직(Controller)과 사용자에게 보여줄 화면을 그려(View) 응답해준다.

즉 Jsp가 Controller 역할과 View 역할을 모두 수행한다. 

이렇게 Jsp가 Controller와 View를 모두 수행하면 어플리케이션이 개발되면 될수록 유지보수가 어려워지고 확장성이 떨어질것이다.

그럼 MVC 패턴이 아니지 않나? 엄밀히 따지면 MVC 패턴을 완전히 따르지 않았지만 후에 등장한 MVC2의 기초를 마련한 것으로, MVC 패턴 발전의 역사적 배경에서 MVC라고 불리게 되었다.

<br><br>

### MVC2

MVC1의 단점을 보안하기 위해 MVC2가 개발되었다.

위에서 설명한 MVC 특징은 모두 MVC2의 특징이다.

![Untitled](/Web/img/mvc(1).png)

Mode View Controller로 구성요소를 나누고 독립적으로 실행한다.

- **사용자 요청 (Request):** 사용자가 웹 브라우저에서 특정 URL로 요청을 보낸다.
- **컨트롤러 처리 (Controller):** 컨트롤러가 사용자의 요청을 받아 모델을 호출하여 비즈니스 로직을 처리하고, 그 결과를 뷰에 전달한다.
- **모델 처리 (Model):** 모델이 데이터를 처리하고, 그 결과를 컨트롤러에 반환한다.
- **뷰 렌더링 (View):** 컨트롤러가 뷰에 모델데이터를 전달하고 뷰가 모델 데이터를 받아 화면을 렌더링한다.
- **응답 전송 (Response):** 컨트롤러가 뷰에서 렌더링된 결과물을 사용자에게 응답한다.

<br>

**Front Controller**



모든 controller는 요청에 맞는 뷰를 forward 시켜주는 역할을 한다.
controller가 많아지면 많아질수록 중복 코드가 많아질것이다.

그래서 나온게 Front Controller이다. Front Controller는 하나의 중앙 컨트롤러가 모든 요청을 처리한 후, 해당 요청을 적절한 서브 컨트롤러 또는 JSP로 전달하는 역할을 한다.

![Untitled](/Web/img/mvc(3).png)

<br>

### Spring MVC

Spring MVC는 기존 MVC2의 모델을 그대로 가져왔다.

그리고 MVC의 가장 중요한 개념인 관심사의 분리를 Spring의 IoC라는 개념을 통해 더욱 효과적이게 사용할 수 있게 되었다.

그리고 MVC2는 디자인 모델이기 때문에 개발자가 직접 구현을 해야한다.

하지만 SpringMVC는 Spring Framework에서 제공하기 때문에 가져다 쓸수있다는 장점이있다.

FrontController는 Spring MVC에서 DispatcherServlet으로 구현되어있다.

그리고 Spring MVC에서는 기존에 Jsp로만 개발하던 당시에는 서블릿이 web.xml이라는 설정파일에서 등록된 서블릿을 찾아 요청을 전달했다면 Spring MVC에서는 어노테이션을 사용해 손쉽게 설정할수있게되었다.

그리고 Spring MVC는 보다 컴포넌트들의 관심사를 분리하였는데 요청 URL을 분석하는 핸들러매핑, 화면을 그려주는 뷰리졸버 등 더욱 컴포넌트들을 관심사를 분리하여 독립적으로 시행 할 수 있게 해준다.

![Untitled](/Web/img/mvc(4).png)


<br><br><br><br><br>


## Reference

[https://www.inflearn.com/course/lecture?courseSlug=스프링-mvc-1&unitId=71804&tab=curriculum](https://www.inflearn.com/course/lecture?courseSlug=%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1&unitId=71804&tab=curriculum)
[https://ko.wikipedia.org/wiki/모델-뷰-컨트롤러?source=post_page-----1d74fac6e256--------------------------------#/media/파일:Router-MVC-DB.svg](https://ko.wikipedia.org/wiki/%EB%AA%A8%EB%8D%B8-%EB%B7%B0-%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC?source=post_page-----1d74fac6e256--------------------------------#/media/%ED%8C%8C%EC%9D%BC:Router-MVC-DB.svg)
[https://en.wikipedia.org/wiki/JSP_model_1_architecture](https://en.wikipedia.org/wiki/JSP_model_1_architecture)