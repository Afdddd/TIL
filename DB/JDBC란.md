# JDBC란?
---



JDBC란 (Java DataBase Connectivity) 자바에서 데이터베이스에 접속할수있도록 하는 자바 API이다.

수십가지의 데이터베이스 마다 접속방법, SQL전달 방법 그리고 결과를 응답받는 방법이 모두 다르기 때문에 JDBC라는 자바 표준이 나온다.

<br><br>

![Untitled](/DB/img/jdbc(1).png)


## JDBC 드라이버

![h2 데이터베이스의 JDBC 드라이버](/DB/img/jdbc(2).png)
h2 데이터베이스의 JDBC 드라이버
인터페이스만 있다고 기능이 동작하지 않는다.

각각의 DB회사에서 자신의 DB에 맞도록 구현해서 라이브러리로 제공하는데, 
이것을 **JDBC 드라이버**라 한다. 

예를들어 MySQL DB에 접근할 수 있는 것은 MySQL JDBC 드라이버라 하고, 
Oracle DB에 접근할 수 있는 것은 Oracle JDBC 드라이버라 한다.

<br><br>

JDBC는 대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서 제공한다.

- java.sql.Connection
- java.sql.Statement
- java.sql.ResultSet


<br>

## Connection이란?

---

Connection이란 JDBC드라이버를 통해 데이터베이스와 연결정보를 담고있는 객체

<br>

#### JDBC드라이버로 연결

```java
public abstract class ConnectionConst{
	public static final String URL = "jdbc:h2:tcp://localhost/~/test2";
	public static final String USAERNAME = "SA";
	public static final String PASSWORD = "";
}
```

데이터베이스에 접속하는데 필요한 정보들을 편리하게 사용할수있도록 상수로 만들어놓았다.
<br><br>




```java

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class ={}",connection,connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }
}
```

데이터베이스에 연결하려면 JDBC에서 제공하는 `DriverManager.getConnection(,,,)`을 사용하면 된다.
이렇게 하면 라이브러리에 있는 데이터베이스 드라이버를 찾아서 해당 드라이버가 제공하는 커넥션을반환해준다. 
여기서는 H2 데이터베이스 드라이버가 작동해서 실제 데이터베이스와 커넥션을 맺고 그결과를 반환해준다.

<br>

간단하게 테스트 코드를 작성해보자.

```java
public class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }
}
```

<br>

connection변수에 객체가 담겼는지 확인하는 코드이다. 실행결과는 이렇게 나온다.

```java
22:36:40.501 [Test worker] INFO hello.jdbc.connection.DBConnectionUtil - get connection=conn0: url=jdbc:h2:tcp://localhost/~/test2 user=SA, class =class org.h2.jdbc.JdbcConnection
BUILD SUCCESSFUL in 6s
```

실행결과를 보면 class=class org.h2.jdbc.JdbcConnection 부분을 확인할 수 있다. 이것이 바로
H2 데이터베이스 드라이버가 제공하는 H2 전용 커넥션이다. 물론 이 커넥션은 JDBC 표준 커넥션
인터페이스인 java.sql.Connection 인터페이스를 구현하고 있다.

<br><br>

## Statement란?
---

Statement란 Connection을 통해 연결된 DB에 SQL문을 전송하고 처리된 결과를 반환하는 객체이다.

<br>

#### Statement와 PreparedStatement

Statement와 PreparedStatement의 가장 큰 차이점은 캐시(cache)를 사용한다는 점이다.

1. 쿼리 문장 분석
2. 컴파일
3. 실행

```java
//Statement
String sql = "select from member where member_id = memberId";
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

Statement를 사용하면 매번 쿼리를 실행할때마다 1~3단계를 거치게 된다.

<br>

Statement는 `executeQuery()` 나 `executeUpdate()` 를 실행하는 시점에 SQL문을 전송하는데 무슨 SQL문장이 전송되는지 한눈에 알수있지만 매번 컴파일하기 때문에 성능면에서 떨어진다.

```java
//PreparedStatement
String sql = "select from member where member_id = ?;
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setString(1,memberId);
ResultSet rs = pstmt.executeQuery();
```

PreparedStatement는 1~3단계를 처음 한번만 실행하고 캐시에 담아 재사용한다.

컴파일이 미리 준비되어있기 때문에 Statement보다 성능면에서 뛰어나고 ?를 `setXXX()` 를 통해 
치환해주며 재사용이 가능하다.

Statement'는 매개변수 없이 정적 SQL 쿼리를 실행하는 데 더 간단하고 적합하지만, 'PreparedStatement'는 보안상의 이점과 잠재적인 성능 향상으로 인해 매개변수화된 SQL 쿼리를 실행하는 데 선호됩니다. 

<br>

#### `ExecuteQuery()`

```java
String sql = "select from member where member_id=?";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setString(1,memberId);

ResultSet rs = pstmt.executeQuery();

```

1. Select 구문을 수행할때 사용하는 함수이다.
2. 결과값으로 ResultSet을 반환한다.

<br><br>

#### `ExecuteUpdate()`

```java
String sql = "insert into member(member_id,money) values(?,?)";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setString(1,memberId);
pstmt.setInt(2,money);

int row = pstmt.executeUpdate(); 
```

1. Select구문을 제외한 다른 구문을 수행할때 사용하는함수이다.
2. 결과값으로 Int를 반환하는데 영향받은 DB row수를 반환한다.

<br><br>

## ResultSet이란?
---

ResultSet이란 SQL요청에 필요한 데이터들을 행과 열로 저장하는 객체이다.

ResultSet은 내부에있는 커서가 이동해 다음 데이터를 조회할 수 있다. 

<br>

![Untitled](/DB/img/jdbc(3).png)

`ResultSet.next()`: 내부에 있는 커서를 이동해서 다음 데이터를 조회할 수 있다. 참고로 최초의 커서는 데이터를 가리키고 있지 않기 때문에 `ResultSet.next()` 를 최초 한번은 호출해야 데이터를 조회할 수 있다.



- `ResultSet.next()` 의 결과가 `true`면 커서가 가르키는곳에 데이터가 있다는 뜻이다.
- `ResultSet.next()의` 결과가 `false`면 더이상 커서가 가르키는 데이터가 없다는 뜻이다.

<br>

`ResultSet.getXXX(”YYY”)`: ResultSet의 get메서드는 현재 커서가 가르키는 행의 원하는 타입의 데이터를 가져올수있는 메서드이다. 예를들어 `ResultSet.getString(”member_Id”)` 이라면 결과는 “member1”을 반환한다.

<br>

```java
String sql = "select * from member where member_id = ?";
try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1,memberId);

      rs = pstmt.executeQuery();
	      if (rs.next()) {
		      Member member = new Member();
		      member.setMemberId(rs.getString("member_id"));
		      member.setMoney(rs.getInt("money"));
			    return member;
      }else {
          throw new NoSuchElementException("member not found memberId=" + memberId);
}
```

memberId를 통해 member를 하나 조회하는 코드이다. `rs.next()` 가 `true`면 member객체가 존재하는것이 때문에 찾은 member를 반환하고 `false`면 예외를 터트리는 코드이다.
<br>
<aside>
💡 조회할 데이터가 1개라면 if문을 사용하고 조회할 데이터가 많다면 while문을 사용하자.

</aside>

<br><br><br>

## JDBC 전체 동작 과정
---


```java
public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(); // 1.
            pstmt = con.prepareStatement(sql); // 2-a.
            pstmt.setString(1,memberId); // 2-b.

            rs = pstmt.executeQuery(); // 2-c.
            if (rs.next()) { // 3.
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("error",e);
            throw e;
        }
        finally {
            close(con,pstmt,rs); // 4.
        }
    }
```

1. Connection - 커넥션을 획득한다.
2. Statement
    1. SQL문을 분석한다.
    2. SQL문의 인자값을 치환한다.
    3. SQL문을 실행한다.
3. ResultSet - 조회된 결과를 저장한다.
4. 리소스들을 닫아준다.





<br><br><br><br><br>
### Reference
https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-1/dashboard