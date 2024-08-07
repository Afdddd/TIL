# HTTP 메서드



## GET

> 리소스 조회
> 
- `GET` 요청은 특정 URI에 있는 리소스를 조회할 때 사용한다.
- 서버에게 리소스를 요청하고 해당 리소스의 상태를 받아오는 데 사용
- 데이터를 전달할때는 주로 쿼리 스트링을 사용해서 전달한다.
- HTTP Body를 통해 데이터를 전송할 수도 있지만, 이 방식은 지원하지 않을수도 있기 때문에 권장하지 않는다.

<br>

## POST

> 요청 데이터 처리, 주로 등록에 사용
> 
- `POST` 요청은 클라이언트가 서버에 데이터 처리를 요청할 때 사용된다.(일반적으로 새로운 리소스를 등록하거나, 데이터 처리 작업을 위해 사용)
- 클라이언트는 `POST` 요청을 특정 URI에 보내어 Http body 를 통해 데이터를 전송한다.

<br>

## PUT

> 리소스를 대체, 해당 리소스가 없으면 생성
> 
- `PUT` 요청은 주어진 URI에 대한 리소스를 완전히 교체하거나, 새로 생성할 때 사용된다.(덮어씌우기)
- 클라이언트는 목표 URI와 함께 교체할 리소스를 전달해야한다.
- members/100 라고 위치를 지정해주었다면 100의 회원이 있으면 새로운 리소스로 덮어씌우고 없으면 새로 생성한다.

<br>

## PATCH

> 리소스 부분 변경
> 
- 일반적으로 리소스의 전체를 교체하는 `PUT`과 달리, `PATCH`는 리소스의 일부만을 수정하는 데 적합하다.
- 만약 리소스가 없을 경우에는 404 Not Found를 반환한다.


<br>

## DELETE

> 리소스 삭제
> 
- `DELETE` 요청은 지정된 URI의 리소스를 삭제할 때 사용된다.
- 서버는 요청된 URI의 리소스를 제거하고, 삭제가 성공적으로 이루어졌는지에 대한 적절한 HTTP 상태 코드를 반환한다.
- `DELETE` 요청은 한 번 수행되면 복구할 수 없으므로 주의해야한다.

<br>


## PRG 패턴

<br>

Post Redirect Get 패턴으로 Post로 Form을 제출 후 사용자가 페이지를 새로고침하여 발생할 수 있는 중복 제출 문제를 해결하기 위해 사용된다.

예를 들어 사용자가 상품을 결제하려고 결제 정보(카드 정보)를 Form에 입력하고 결제하기를 클릭하면 카드의 정보가 `Post` 요청으로 전달될 텐데 만약 사용자가 새로고침을 누르면 같은 Form이 전달되기 때문에 중복 결제가 될수있는 위험이있다.

서버는 POST 요청을 처리한 후에는 클라이언트를 새로운 URL로 리디렉션하여 GET 요청을 보내도록 유도한다.

클라이언트는 리다이렉트를 따라 새로운 URL로 `Get` 요청을 보낸다. 이 후 새로 고침해도 `Get`으로요청을 보내기 때문에 중복 결제를 방지할 수 있다.

<br><br>

일반적으로 사용되는 HTTP 메서드(GET, POST, PUT, PATCH, DELETE) 외에도 HTTP/1.1 표준에 지정된 몇 가지 다른 메서드가 있다.

### HEAD

- `GET`과 비슷하지만 HTTP body 없이 응답의 Header와 상태줄만 조회
- 리소스의 데타데이터를 확인하고 싶을 때 사용 ( 예 : 파일크기, 수정날짜)
- 리소스를 다운로드 하기 전에 존재 여부를 확인할 때 
<br>

### OPTION

- 본 요청을 하기 전에  안전한지 미리 검사하는 메서드
- CORS 설정 확인에 주로 사용

<br>

### Trace

- 웹서버로 가는 네트워크 경로를 체크하는 메서드
- 웹서버와 클라이언트 사이에 중간서버(프록시)의 존재를 확인할때 사용
- 디버깅 목적 + 요청 경로를 추적하고 중간에 어떤 프록시나 게이트웨이가 있는지 확인할 때 사용

<br><br>

## 메서드 속성

![Untitled](/Web/img/httpMethod_property.png)

<br>

### 안전 Safe

>호출해도 리소스를 변경되지 않는 성질

|  | Sage |
| --- | --- |
| GET | ✅ |
| POST | ❌ |
| PUT | ❌ |
| PATCH | ❌ |
| DELETE | ❌ |

요청이 서버의 리소스 상태를 변경하지 않고 데이터를 요청만 하는지를 의미한다.

`GET` 을 제외하고는 모두 데이터가 변하는 메서드이다.

<br>


### 멱등 Idempotent

>같은 메서드를 한 번 호출하든 두 번 호출하든 100번 호출하든 결과가 똑같은 성질

|  | Sage |
| --- | --- |
| GET | ✅ |
| POST | ❌ |
| PUT | ✅ |
| PATCH | ❌ |
| DELETE | ✅ |

<br>


`GET` : ✅

서버의 리소스를 조회하는 용도이기 때문에 여러번 보내더라도 같은 응답을 받는다.

또 멱등은 외부 요인으로 중간에 리소스가 변경되느것 까지는 고려하지 않는다. 

예를 들어 

1. A 가 GET /members/100 통해 리소스를 조회
2. B 가 PUT /members/100 통해 리소스를 변경
3. A 가 GET /members/100 통해 리소스를 다시 조회

이런 경우에도 GET은 멱등성이 깨지 않는다. 

<br>

`POST`:  ❌

새로운 리소스를 생성하기 때문에 같은 요청을 여러 번 보내면 각각 다른 리소스를 생성할 수있다. 
`POST` 같은 경우에는 중복 결제같은 심각한 문제가 발생할 수 있다.

<br>

`PUT` : ✅

 특정 리소스를 완전히 교체하거나 생성하기 떄문에 같은 데이터로 여러번 요청해도 같은 결과를 얻을 수 있다.


<br>

`PATCH` : ❌ or ✅
일반적으로 `PATCH` 메서드는 멱등하지 않다. 

왜냐하면 `PATCH` 요청이 여러 번 보내지면 리소스의 상태가 변경될 수 있기 때문이다. 예를 들어, 리소스의 값을 증가시키는 경우 (`i++`와 같은 계산)에는 각 요청마다 리소스의 상태가 달라질 수 있다. 하지만 특정 경우에 따라 `PATCH` 요청이 멱등할 수 있다. 예를 들어, 리소스의 특정 필드를 특정 값으로 설정하는 경우에는 멱등성을 보장할 수 있다.

`DELETE` : ✅

이미 삭제된 리소스를 다시 삭제 요청해도 삭제된 결과는 동일하다.
자동 복구 메커니즘에 사용된다. 예를 들어 사용자가 삭제 요청 시 서버에 오류가 났을 때 삭제 요청은 서버에서 재실행을 시켜도 가능하다.

<br><br>

### 캐시 가능 Casheable

>응답 결과 리소스를 캐시해서 사용 유무

|  | Sage |
| --- | --- |
| GET | ✅ |
| POST | ✅(직접 구현) |
| PUT | ❌ |
| PATCH | ✅(직접 구현) |
| DELETE | ❌ |

- GET, HEAD, POST, PATCH 캐시가능하다.
- 실제로는 GET, HEAD 정도만 캐시로 사용한다.
- POST, PATCH는 본문 내용까지 캐시 키로 고려해야 하는데, 구현이 쉽지 않다.

캐시는 값을 저장하는것인데 `POST`,`PATCH`같은 경우에는 데이터 변경이 되는 메서드이기 때문에 데이터 불일치 문제가 발생할 수 있기 때문이다. 이를 구현하기가 쉽지 않다는 말이다.

<br><br>

## HTTP 상태 코드

>HTTP 프로토콜에서 클라이언트가 요청한 작업(HTTP Method)의 처리 결과를 나타내는 숫자 코드

<br>

### **1xx (Informational)**: 요청을 받았고 처리 중임을 나타낸다.

- `100 Continue` : 진행중임을 의미
- `101 Switching Protocols`: 서버가 클라이언트의 프로토콜 변경 요청을 수락했음을 의미
- `102 Processing`: 이 코드는 서버가 요청을 수신하였으며 이를 처리하고 있지만, 아직 제대로 된 응답을 알려줄 수 없음을 의미

<br>

### **2xx (Success)**: 요청이 성공적으로 처리되었음을 나타낸다.

- `200 OK` : 요청이 성공적으로 되었다는 의미. HTTP 메서드의 따라 의미가 달라진다.
- `201 Created`**:** 요청이 성공적으로 처리되었으며, 자원이 생성되었음을 의미
- `202 Accepted`: 요청이 수신되었지만 아직 처리되지 않았음을 의미
- `204 No Content`: 서버가 요청을 성공적으로 처리했지만 응답으로 내용이 없음을 의미

<br>

### **3xx (Redirection)**: 요청을 완료하려면 추가 동작이 필요함을 나타낸다.

- `300 Multiple Choices`: 리소스에 대해 여러 선택지가 있음을 의미
- `301 Moved Permanently`: 리소스가 새 위치로 영구적으로 이동되었음을 의미
- `302 Found`: 리소스가 일시적으로 다른 위치에 있음을 의미. (보통 303 또는 307를 사용하는 것이 더 권장된다.)
- `304 Not Modified`: 클라이언트가 마지막 요청 이후 리소스가 수정되지 않았음을 의미
- `307 Temporary Redirect`: 리소스가 일시적으로 다른 위치에 있음을 의미

<br>

### **4xx (Client Error)**: 클라이언트 오류를 나타낸다. [클라이언트와 관련돼서 중요함]

- `400 Bad Request`: 클라이언트의 잘못된 문법으로 서버가 이해 못함. 요청이 잘못되었음을 의미
- `401 Unauthorized`: 요청을 위한 권한 인증이 필요함을 의미
- `403 Forbidden`: 인증은 처리 되었으나, 해당 자원에 대한 인가를 거치지 않은 경우에 대한 응답. 거부처리. 
- `404 Not Found`: 요청한 URI 리소스를 찾을 수 없음을 의미
- `405 Method Not Allowed`: 요청된 HTTP 메서드가 허용되지 않음을 의미
- `409 Conflict`: 요청이 현재 상태와 충돌하여 처리할 수 없음을 의미
- `429 Too Many Requests`: 클라이언트가 주어진 시간 내에 너무 많은 요청을 보냈음을 의미

<br>

### **5xx (Server Error)**: 서버 오류를 나타낸다.

- `500 Internal Server Error`: 서버에서 처리 과정 중에 오류가 발생했음을 나타냅니다.
- `501 Not Implemented`: 서버가 요청된 기능을 지원하지 않음을 나타냅니다.
- `502 Bad Gateway`: 게이트웨이나 프록시에서 유효한 응답을 받을 수 없음을 나타냅니다.
- `503 Service Unavailable`: 서버가 일시적으로 과부하 상태이거나 유지 보수 중임을 나타냅니다.
- `504 Gateway Timeout`: 게이트웨이나 프록시에서 시간 초과로 인해 요청을 처리할 수 없음을 나타냅니다.

<br>

꼭 HTTP 메서드를 속성에 맞게 개발하거나 상황에 맞는 응답코드를 보내는 것은 아니다. 권장사항 정도이므로 개발자마다, 회사마다 API의 설계가 다르다. 따라서 API 명세서를 작성해놓고 공유하자.

<br><br><br><br><br>

### Reference

https://inpa.tistory.com/entry/WEB-%F0%9F%8C%90-HTTP%EC%9D%98-%EB%A9%B1%EB%93%B1%EC%84%B1-%C2%B7-%EC%95%88%EC%A0%95%EC%84%B1-%C2%B7-%EC%BA%90%EC%8B%9C%EC%84%B1-%F0%9F%92%AF-%EC%99%84%EB%B2%BD-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0
[https://developer.mozilla.org/ko/docs/Web/HTTP/Status](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)
