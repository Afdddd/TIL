# [HTTP] 쿠키, 세션, JWT

<br><br>

기본적으로 웹 브라우저는 HTTP 기반으로 동작하고있다.

HTTP는 무상태(Stateless) 구조로 서버가 클라이언트의 상태를 유지하지 않는다.

그렇기 때문에 서버는 클라이언트의 정보를 저장하지 않아 클라이언트를 구별 할 수 없고 클라이언트는 매번 인증을 진행해야한다.

이러한 문제를 해결하기 위한 방법들이 있다.

<br>

---

## 쿠키(Cookie)

> 웹서버가 생성하는 데이터를 담는 작은파일
> 

![출처:[https://ko.wikipedia.org/wiki/HTTP_쿠키#/media/파일:HTTP_cookie_exchange.svg](https://ko.wikipedia.org/wiki/HTTP_%EC%BF%A0%ED%82%A4#/media/%ED%8C%8C%EC%9D%BC:HTTP_cookie_exchange.svg)](/Web/img/cookie(1).png)


<br>

1. 브라우저(클라이언트)에서 웹서버로 요청을 보낸다.
2. 요청을 받은 서버는 응답 데이터와 쿠키를 함께 응답한다.
3. 응답을 받은 쿠키를 브라우저의 메모리에 저장하고 브라우저에서 서버로 요청을 다시 보낼때 브라우저(클라이언트)의 정보를 가지고있는 쿠키를 서버에 보낸다.

<br>

쿠키에는 클라이언트의 정보를 담고 있으므로 서버는 쿠키에 담긴 정보를 토대로 데이터를 응답한다. 
중간에 서버가 장애가 나도 쿠키에 브라우저의 정보를 담고있으므로 다른 서버에 쿠키를 보내면 이상없이 작업을 이어나갈 수 있다.

이렇게 쿠키는 stateless 구조를 구현하고있다.
쿠키의 사용 예로는  자동로그인, 쇼핑몰 장바구니 등이 있다.

<br>

### 쿠키의 한계

자동로그인 같이 사용자의 개인정보를 쿠키로 저장 할 시 로컬파일에 저장되므로 쉽게 조작되거나 중간에 가로챌 수 있는 위험이 있다.

<br><br><br>


---

## 세션(session)

> 클라이언트가 브라우저의 접속하고 서버와 접속이 종료되기 전의 상태
> 

기본적으로 세션은 쿠키를 기반으로 사용하고있다.

![출처:[https://dongsik93.github.io/til/2020/01/08/til-authorization(1)](https://dongsik93.github.io/til/2020/01/08/til-authorization(1)/)](/Web/img/session(1).png)

<br>

1. 사용자가 로그인을 위해 아이디, 비밀번호를 서버에 요청 (Post)
2. 서버에선 회원 DB에 로그인한 사용자를 조회한다.
3. 사용자가 등록된 회원이라면 세션DB에 회원 정보 세션을 생성한다.
4. 세션 저장소에선 세션ID를 생성해 서버에 반환한다.
5. 반환된 세션ID를 서버에서 쿠키에 담아 사용자에게 응답한다.
6. 사용자가 서버로 부터 응답받은 쿠키(세션ID)를 브라우저에 저장하고, 이후 다른 페이지를 이동하거나 새로이 요청을 한다면 쿠키(세션ID)를 담아 보낸다.
7. 요청을 받은 서버는 쿠키에 담긴 세션ID를 세션DB에서 조회한다.
8. 세션ID에 해당하는 회원 정보를 반환한다.
9. 마지막으로 서버에서 사용자에게 회원정보를 바탕으로 데이터를 응답한다.

<br><br>

### 세션의 장단점

쿠키만 사용했을 때는 보안이 취약하다는 단점이 있었다.

세션에서의 쿠키는 단순히 세션ID를 넘기는 매개체로 역할을 할뿐이다.

모든 회원의 정보는 DB에 저장되어있으므로 보안상 훨씬 안전하다.

단, 세션정보를 조회하는 과정이 있어 많은 요청이 있으면 많은 시간과 자원이 든다.

<br><br>

---

## JWT(Json Web Token)

> Json객체를 이용한 토큰 기반 인증 시스템

<br>

### JWT 구조

![출처: [https://jwt.io/](https://jwt.io/)](/web/img/jwt(1).png)
JWT는 Header, Payload, Signature로 구성되었다.

<br>

**Header**

![출처: [https://jwt.io/](https://jwt.io/)](/web/img/jwt(2).png)

> Header는 Signature를 해싱하기 위한 정보를 담고있다.
> 
- alg  : 암호화 알고리즘
- tpy : 토큰 유형

<br>

**Payload**

![출처: [https://jwt.io/](https://jwt.io/)](/web/img/jwt(3).png)

> 서버와 클라이언트가 주고받는 실제 데이터를 담고있다.

<br>

**Signature**

![출처: [https://jwt.io/](https://jwt.io/)](/web/img/jwt(4).png)

> 토큰의 유효성을 검증을 위한 문자열을 담고있다.


Header 와 Payload를 base64 Url Safe Encode 결과와 서버의 개인키와 합친 후 Header에 정의한 
암호화 알고리즘으로 암호화한다.

<aside>

💡  **base64 Url Safe?**

</aside>

base64는 문자들을 base64형태(ASCII)로 변환하여 전송하고 받는쪽은 다시 변환하여 하는 방법이다 하지만 마지막 62,63번 글자가 “+” 와 “/”이기 때문에 문자가 정상적으로 전송되지 않는 문제가 발생하는데  62,63번 글자를 “-” 와 “_” 로 변경한것을 **base64 Url Safe**이다.

<br><br>

### 토큰 인증 과정

![출처:[https://docs.intersystems.com/irislatest/csp/docbook/DocBook.UI.Page.cls?KEY=GAUTHN_jwt](https://docs.intersystems.com/irislatest/csp/docbook/DocBook.UI.Page.cls?KEY=GAUTHN_jwt)](/web/img/jwt(5).png)

1. 클라이언트가 서버에 로그인 요청(ID, password)
2. 서버측에서 인증 요청이 오면 암호화된 JWT를 생성해 쿠키에 담아 클라이언트에게 응답한다.
3. 클라이언트에서 응답받은 JWT를 저장한다.(localStorage, cookie)
4. 이후 클라이언트는 JWT를 사용하여 서버에 요청한다.
5. 서버는 요청 온 JWT를 유효한 토큰인지 검사를 한다.
6. 만약 토큰이 유효하지 않거나 만료되었다면 오류를 반환한다.
7. 토큰이 유효하다면 클라이언트의 요청 데이터를 응답한다.