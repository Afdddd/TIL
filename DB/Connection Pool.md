# Connection Pool
---
<br>

## 데이터베이스 커넥션 획득 과정
<br>

![Untitled](/DB/img/connection_pool(1).png)

1. 애플리케이션 로직은 DB드라이버를 통해 커넥션을 조회한다.
2. DB드라이버는 DB와 TCP/IP 커넥션을 연결한다. 이과정에서 3 way handShake같은 TCP/IP 연결을 위한 네트워크 동작이 이루어 진다.(syn → syn/ack → ack)
3. 연결된 커넥션에 ID,PW와 부가 정보 전달한다.
4. DB는 넘어온 ID,PW로 내부인증을 하고 내부에 DB 세션을 생성한다.
5. DB는 커넥션 생성완료 되었다는 응답을 보낸다.
6. DB드라이버는 커넥션 객체를 생성해 클라이언트에 반환한다.
7. 이후 작업을 완료한 커넥션은 `cloas()` 를 사용해 끊어주어야한다. 이과정에서도 4 way handShake 동작이 이루어진다.(fin → ack → fin → ack)

<br>

매번 Connection을 연결하고 끊어줄 때 위의 과정을 거친다면 시간적인 비용이 발생할것이다. 결국 성능이 안좋아질것이다.

<br>

그래서 이를 보안하기 위해 **Connection Pool**이 나왔다.

<br><br>

## Connection Pool이란?

---
Connection Pool은 미리 연결해놓은 Connection들을 pool에 넣어놓고 요청이 오면 pool에서 커넥션을 꺼내 전달해준다. 이 후 작업이 끝난 커넥션은 끊어지지 않고 다시 pool로 돌아간다. 커넥션을 재사용 하기 때문에 좋은 성능을 발휘할 수 있다.

<br>

![Untitled](/DB/img/connection_pool(2).png)

- 애플리케이션을 시작하는 시점에 커넥션 풀은 필요한 만큼 커넥션을 미리 확보해서 풀에 보관한다.
- 커넥션 풀을 통해 이미 생성되어 있는 커넥션을 객체 참조로 그냥 가져다 쓰기만 하면 된다.
- 커넥션을 모두 사용하고 나면 이제는 커넥션을 종료하는 것이 아니라, 다음에 다시 사용할 수 있도록 해당 커넥션을 그대로 커넥션 풀에 반환하면 된다.
- 대표적인 커넥션 풀 오픈소스는 commons-dbcp2 , tomcat-jdbc pool , **HikariCP** 등이 있다.

<br>

Spring에서는 HikariCP를 기본 CP로 사용하고 있다.

```xml
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

Jdbc 또는 Jpa의 의존성을 추가하면 내부 라이브러리에 HikariCP가 들어있다.


![Untitled](/DB/img/connection_pool(3).png)

<br>

<i>HikariDataSource.java</i>
```java
public class HikariDataSource extends HikariConfig implements DataSource, Closeable{
...
}
```

<br>

❓ Spring은 왜 HikariCP를 선택했을까?

<br>

![Untitled](/DB/img/connection_pool(4).png)

위의 지표는  각 커넥션 풀 구현체가 단위 시간당 처리할 수 있는 "커넥션 사이클" 수를 나타내는 지표와 각 커넥션 풀 구현체가 단위 시간당 처리할 수 있는 "스테이트먼트 사이클" 수를 나타내는 지표이다. HikariCP가 다른 구현체들의 비해 커넥션을 얻고 반환하는 작업과 SQL 쿼리를 실행하는 작업의 처리 속도가 훨씬 빠르다고 나타낸다.

<br>

그리고 HikariCp는 130KB의 경량화 CP라이브러리이고 Java의 JIT에 최적화되어 높은 성능을 보여준다. 더 자세한건 ([https://github.com/brettwooldridge/HikariCP?tab=readme-ov-file](https://github.com/brettwooldridge/HikariCP?tab=readme-ov-file))

이런 이유를 바탕으로 Spring은 HikariCP를 선택했다.

<br><br>

## DataSource

커넥션을 획득하는 방법을 추상화한 인터페이스이다.

순수 JDBC로 개발할때 Connection을 맺으려면 매번 `DriverManger`의 `getConnection()`으로 Connection을 생성했어야 했다.

```java
public static final String URL = "jdbc:mysql://127.0.0.1:3306/connect_test";
public static final String USERNAME = "root";
public static final String PASSWORD = "0000";

@Test
@DisplayName("DriverManger로 connection 획득")
void driverManger() throws SQLException {
    Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con3 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con4 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con5 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con6 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    
    
    log.info("connection={}",con1);
    log.info("connection={}",con2);
    log.info("connection={}",con3);
    log.info("connection={}",con4);
    log.info("connection={}",con5);
    log.info("connection={}",con6);
}
```

<br>

*실행 결과*

```java
15:05:02.502 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@15515c51
15:05:02.508 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@673bb956
15:05:02.508 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@cd7f1ae
15:05:02.508 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@60e949e1
15:05:02.508 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@3c4bc9fc
15:05:02.508 [Test worker] INFO  c.s.ConnectionPool.ConnectionTest -- connection=com.mysql.cj.jdbc.ConnectionImpl@680362a
```
<br>
그리고 항상 `getConnection()` 의 파라미터에 DB와 연결에 필요한 정보들을 넘겨줬어야 했다.

- `Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);`

<br>

그래서 Java는 `DataSource`라는 인터페이스로 좀 더 편하게 Connection을 획득할 수 있도록 개선했다.

어떻게 개선했는지 보자

<br>

SpringFramework에서는 `DataSource`를  `DriverMangerDataSoruce` 로 구현했다.

```java
@Test // DB 설정만
void dataSourceDriverManger() throws SQLException {
    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    useDataSource(driverManagerDataSource);
}

// Connection 획득만
private void useDataSource(DataSource dataSource) throws SQLException {
    Connection con1 = dataSource.getConnection();
    Connection con2 = dataSource.getConnection();
    Connection con3 = dataSource.getConnection();
    Connection con4 = dataSource.getConnection();
    Connection con5 = dataSource.getConnection();
    Connection con6 = dataSource.getConnection();

    log.info("connection={}",con1);
    log.info("connection={}",con2);
    log.info("connection={}",con3);
    log.info("connection={}",con4);
    log.info("connection={}",con5);
    log.info("connection={}",con6);
}
```

`DataSource`에 DB 연결에 필요한 데이터를 넘겨주고 `useDataSource()` 라는 메서드를 사용해 Connection을 획득한다. 

처음 한번  DB 연결에 필요한 데이터를 넘겨주면 이 후에는 편리하게 Connection을 획득할 수 있다.
추상화를 통해 DB설정과 Connection 획득이라는 역할을 나눴다.
그리고 Connection Pool로 변경도 코드의 수정 없이 가능하다.

<br>

HikariCp로 변경해보겠다.

HikariCp 생성

```java
@Test
@DisplayName("HikariCP로 connection 획득")
void HikariCP() throws SQLException, InterruptedException {
    HikariDataSource hikariDataSource = new HikariDataSource(); 
    hikariDataSource.setJdbcUrl(URL);
    hikariDataSource.setUsername(USERNAME);
    hikariDataSource.setPassword(PASSWORD);
    hikariDataSource.setMaximumPoolSize(10); //10개의 커넥션을 미리 생성해놓는다.
    hikariDataSource.setPoolName("myPool");

    useDataSource(hikariDataSource);
    Thread.sleep(1000);
}
```

HikariCp는 Spring 내에서 `DataSource`를 `implements`하고 있다.

다형성을 이용해 `useDataSource()`로 Connection을 획득할 수 있다.

```java
private void useDataSource(DataSource dataSource) throws SQLException {
    Connection con1 = dataSource.getConnection();
    Connection con2 = dataSource.getConnection();
    Connection con3 = dataSource.getConnection();
    Connection con4 = dataSource.getConnection();
    Connection con5 = dataSource.getConnection();
    Connection con6 = dataSource.getConnection();
    Connection con7 = dataSource.getConnection();
    Connection con8 = dataSource.getConnection();
    Connection con9 = dataSource.getConnection();
    Connection con10 = dataSource.getConnection();

    log.info("connection={}",con1);
    log.info("connection={}",con2);
    log.info("connection={}",con3);
    log.info("connection={}",con4);
    log.info("connection={}",con5);
    log.info("connection={}",con6);
    log.info("connection={}",con7);
    log.info("connection={}",con8);
    log.info("connection={}",con9);
    log.info("connection={}",con10);
}
```

실행결과를 보면

![Untitled](/DB/img/connection_pool(5).png)

connection pool의 설정 정보들과

<br>

![Untitled](/DB/img/connection_pool(6).png)

connection pool에 담겨진 connection들의 상태가 보인다.

로그를 보면 [Test worker]와 [myPool connection adder] 보이는데 이건 실행한 쓰레드를 나타낸다.
[Test worker]는 Test를 실행한 쓰레드이고 [myPool connection adder]은 커넥션풀에 커넥션을 채우는 쓰레드이다. 커넥션 풀에 커넥션을 채우는 일은 오래걸리는 일이기 때문에 별도의 쓰레드를 사용해서 커넥션 풀을 채워야 어플리케이션 실행 시간에 영향을 주지 않는다.

<br><br>

### Connetion의 상태

```java
Connection not added, stats (total=10, active=6, idle=4, waiting=0)
```

- total : connection pool에 있는 connection들의 수
- active : DB와 연결되어 사용중인 connection의 수
- idle : DB와 연결되어있지만 사용되지않고 놀고 있는 connecion의 수
- waiting : 대기중인 연결 요청의 수

<br><br>

### close()를 사용하면 어떻게 될까?

close()를 사용해서 커넥션이 pool로 돌아가는지 확인해 보자.

```java

@Test
@DisplayName("HikariCP로 connection 획득")
void HikariCP() throws SQLException, InterruptedException {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(URL);
    hikariDataSource.setUsername(USERNAME);
    hikariDataSource.setPassword(PASSWORD);
    hikariDataSource.setPoolName("myPool");
    hikariDataSource.setMaximumPoolSize(10);

    useDataSource(hikariDataSource);
    Thread.sleep(1000);
}

private void useDataSource(DataSource dataSource) {

    Connection con1 = null;
    Connection con2 = null;

    try{
        con1 = dataSource.getConnection();
        con2 = dataSource.getConnection();

        log.info("connection= {}",con1);
        log.info("connection= {}",con2);

    }catch (SQLException e){
        e.printStackTrace();
    }finally {
        try {
            con1.close();
            con2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

con1과 con2를 사용하고 finally를 통해 close()를 호출해줬다.

![Untitled](/DB/img/connection_pool(7).png)

맨 밑의 로그를 보면 total = 10 으로 close()를 사용해도 커넥션이 다시 pool로 돌아간게 보인다.

<br><br>

### Connection Pool 설정

DB와 Connetcion Pool의 구현체에 커넥션 설정을 해줄수있다.
<br>

**DB서버 설정(MySql 기준)**

사용하는 DB에 따라 설정 이름도 다르고 명령어도 다르다.

![Untitled](/DB/img/connection_pool(8).png)

- `max_connections` : client와 맺을 수 있는 최대 connection수, mysql은 기본 151개 이다.

- `wait_timeout`:  connection이 inactive(비활성)할 때 다시 요청이 오기 까지 얼마의 시간을 기다린 뒤에 close할 것인지를 결정하는 수

<br><br>

**HikariCp 설정**

```java
@Test
@DisplayName("HikariCP로 connection 획득")
voidHikariCP()throwsSQLException, InterruptedException {
    HikariDataSource hikariDataSource =newHikariDataSource();
    
    hikariDataSource.setJdbcUrl(URL); // JDBC URL 설정
    hikariDataSource.setUsername(USERNAME); // 데이터베이스 사용자 이름 설정
    hikariDataSource.setPassword(PASSWORD); // 데이터베이스 비밀번호 설정
    hikariDataSource.setPoolName("myPool"); // 커넥션 풀 이름 설정
    hikariDataSource.setMaximumPoolSize(10); // 커넥션  풀의 최대 크기 설정
    hikariDataSource.setMaxLifetime(30000);  // 커넥션의 최대 수명 설정 (30초)
    hikariDataSource.setConnectionTimeout(30000); // 커넥션 획득을 위한 최대 대기 시간 설정 (30초)
    hikariDataSource.setMinimumIdle(10); // 최소 유휴 연결 수 설정 (10개)

    useDataSource(hikariDataSource);
    Thread.sleep(1000);
}
```

- `maximumPoolSize` : pool이 가질 수 있는 최대 connection 수, idle과 active connection을 합친 최대수
- `maxLifetime`: pool에서 connection의 최대 수명, `maxLifetime`을 넘기면 idle인 connection들은 pool에서 제거하고 active인 경우에는 작업이 끝나고 pool로 반환되고 제거된다.
- `connectionTimeout` : pool에서 connection을 받기 위한 대기시간, 이시간을 넘기면 예외를 발생시킨다.
- `minimumIdle`:  pool이 가질 수 있는 최소 connection의 수

<br><br>

HikariCP에서는 `maximumPoolSize` 와 `minimumIdle`을 같은값으로 설정하는걸 권장한다.(기본으로 둘다 10으로 설정되어있다.)

만약 `maximumPoolSize` 가 10이고 `minimumIdle` 가 5라면 connection pool에는 최소수를 맞추기 위해 5개의 커넥션이 있을텐데 10개의 요청이 오면 추가로 5개의 커넥션을 획득해야 하기 때문에 커넥션 풀을 사용하는 이유가 흐려진다. 그래서`maximumPoolSize` 와 `minimumIdle` 의 크기를 맞추는걸 권장한다. 

<br>

Connection Pool의 설정들은 동시 접속 수가 많은 대규모 웹 애플리케이션의 성능 튜닝할때에 사용된다. 접소 수 가 많아지면 DB의 `max_connections`를 충분히 높게 설정하여 모든 사용자가 필요한 만큼의 연결을 할당받을 수 있도록 하고 동시에, `maximumPoolSize` 값을 적절히 조절하여 HikariCP의 커넥션 풀에서 관리되는 커넥션 수를 최적화할 수 있다.


<br><br>
<br><br>
<br>

### Reference

[https://www.inflearn.com/course/lecture?courseSlug=스프링-db-1&unitId=110073&tab=curriculum](https://www.inflearn.com/course/lecture?courseSlug=%EC%8A%A4%ED%94%84%EB%A7%81-db-1&unitId=110073&tab=curriculum)
[https://www.youtube.com/watch?v=zowzVqx3MQ4&t=1881s](https://www.youtube.com/watch?v=zowzVqx3MQ4&t=1881s)