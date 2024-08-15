# [JPA] JPA란?

---
<br>

## JPA란?

> **J**ava **P**ersistance **A**PI.
- 자바 진영의 **ORM**기술 표준이다.
- 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 **인터페이스**이다.
- JPA의 구현체로는 **Hibernate**, EclipseLink, DataNucleus, OpenJPA, TopLink Essentials 들이 있다.

<br><br>

## ORM이란?

> **O**bject **R**elational **M**apper.
- 객체는 객체대로, 관계형 데이터베이스는 관계형 데이터베이스 대로 설계할수있게 중간에서 매핑해주는 프레임 워크이다.



<br><br>



## JPA 배경

<br>

### SQL 중심적인 개발의 문제점

```java
public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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

        } catch (SQLException e) {
            log.error("error",e);
            throw e;
        }
        finally {
            close(con,pstmt,rs); 
        }
    }
```

기존의 JDBC에 직접 연결해 데이터를 조회하는 방법은 이렇다.

1. DriverManager를 통해 커넥션을 휙득한다.
2. Statement을 통해 쿼리문을 전송한다.
3. Resultset으로 데이터를 저장해 필요한 데이터를 꺼낸다.
4. 리소스들을 정리해준다.

조회 한번하는데 복잡하고 긴 코드를 작성해야한다. 만약 테이블이 수십개라면 그만큼 작성해야 하는 코드도 늘어나는 것이다. 물론 JdbcTemplate 이나 MyBatis 같은 SQLMapper를 통해 커넥션을 획득하고 쿼리문 처리, 결과 바인딩, 리소스 정리 등 반복되는 코드들을 줄여주지만 SQL만은 개발자가 직접 작성해야만 한다. 이렇게 **SQL에 의존적인 개발은 피할수없다.**

<br><br>

### 패러다임의 불일치

![패러다임 불일치](https://github.com/user-attachments/assets/db2cfae0-954e-4f34-9dde-04f1f5b53e63)


“객체 지향 프로그래밍”은 추상화, 캡슐화, 상속, 다형성 등 을 사용하고 <br>
”관계형 데이터베이스”는 row, table, column, attribute 등을 사용하기 때문에 시스템 간의 차이가 발생한다.<br>
 이 두 가지가 각각 다른 목표와 구조를 가지고 있기 때문에 여러 가지 문제가 발생한다.

<br>

1. **상속**

    ![Untitled](/JPA/img/JPA(1).png)

    객체에는 상속관계가 있지만 관계형 데이터베이스에는 상속관계가 존재하지않는다.

    Item을 상속받은 Album을 DB에 저장하려면 객체를 쪼개 insert item, insert Album 이렇게 두번 작성해야한다. 

    Album을 조회하기 위해선 Item테이블과 Album테이블을 조인하고 각각의 객체를 생성해 받아줘야한다.

    이렇게 객체 지향적으로 코드를 짜면 SQL처리가 힘들어진다.

    <br><br>

    **만약 DB가 아니라 컬렉션에 저장한다면?** 

    ```java
    list.add(album); // 저장
    list.add(movie);

    Album album = list.get(album); // 조회(다형성)
    ```

    굉장히 심플해진다. 다형성도 활용이 가능해진다.

<br><br>

2. **연관관계**

    > 연관관계란 두 객체가 서로를 참조할 수 있는 관계를 말한다.


    1. 단방향 연관관계 : A 객체가 B 객체를 참조하지만 B 객체는 A 객체를 참조하지 않는 경우
    2. 양방향 연관관계 : A 객체와 B 객체가 서로를 참조하는 경우

    <br>

    객체는 **참조**를 사용해 다른 객체와 연관관계를 가지고, 연관된 객체를 조회한다.
    관계형 데이터베이스는 **외래키**를 사용해 다른 테이블과 연관관계를 가지고, join을 통해 연관된 테이블을 조회한다.
    객체는 단방향으로 조회가 가능하고, 관계형 데이터베이스는 양방향으로 조회가 가능하다.

    <br>

    **객체를 테이블에 맞춰 모델링**

    ```java
    class Member {
        String id;       // MEMBER_ID 컬럼 사용
        Long teamId;     // TEAM_ID (PK)컬럼 사용
        String username; // USERNAME 컬럼 사용
    }

    class Team {
        Long id;       // TEA_ID (PK)컬럼 사용
        String name;   // NAME 컬럼 사용
    }

    // 쿼리문
    INSERT INTO MEMBER(MEMBER_ID, TEAM_ID, USERNAME) values ...
    ```

    관계형 데이터베이스에 맞춰 모델링하면 `Member`객체와 연관된 `Team`객체를 참조할 수 없게된다.

    <br>

    **객체지향 모델링**

    ```java
    class Member {
        String id;       // MEMBER_ID 컬럼 사용
        Team team;       // 참조로 연관관계를 맺는다.
        String username; // USERNAME 컬럼 사용

        Team getTeam() {
            return team;
        }
    }

    class Team {
        Long id;       // TEA_ID (PK)컬럼 사용
        String name;   // NAME 컬럼 사용
    }

    // 쿼리문 TEAM_ID = member.getTeam.getId() 변환
    INSERT INTO MEMBER(MEMBER_ID, TEAM_ID, USERNAME) values ...
    ```

    객체지향 모델링을 사용하면 테이블에 저장하거나 조회하기 쉽지않다.
    개발자가 직접 변환해줘야한다.



<br><br>

3. **객체 그래프 탐색**

    ![Untitled](/JPA/img/JPA(2).png)

    객체에서 회원이 소속된 팀을 조회할 때 참조를 사용해서 연관된 팀을 찾으면 되는데 이것을 
    객체 그래프 탐색이라 한다. 객체는 자유롭게 객체 그래프를 탐색해야한다.

    ```java
    member.getOrder().getOrderItem()... // 자유로운 객체그래프 탐색
    ```
    <br>
        

    **하지만 처음 실행하는 SQL에 따라 탐색 범위가 결정된다.**

    ```java
    SELECT M.*, T.*
        FROM MEMBER M
        JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID 

    member.getTeam(); //OK
    member.getOrder(); //null
    ```
        
<br><br>


4. **엔티티 신뢰 문제**
    
    ```java
    class MemberService {
    	...
    	public void process() {
    		Member member = memberDAO.find(memberId);
    		member.getTeam();  // ???
    		member.getOrder().getDelivery(); // ???
    	}
    }
    ```
    
    - `memberDAO`를 열어 SQL을 열어 보기전까진 조회를 할 수 없다.
    - service 로직에서 개발하는데 DAO단으로 넘어가서 코드를 확인해봐야

<br><br>

5. **비교**
    - 데이터베이스 : 기본키의 값으로 각 row를 구분
    - 객체 : (`==`) 객체의 주소값을 비교.
              (`equal()`) 객체 내부값을 비교.
    
    ```java
    class MemberDAO {
     
     public Member getMember(String memberId) {
     String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID = ?";
     ...
     //JDBC API, SQL 실행
     return new Member(...);
     }
    }
    
    // 비교
    String memberId = "100";
    Member member1 = memberDAO.getMember(memberId);
    Member member2 = memberDAO.getMember(memberId);
    member1 == member2; //다르다.
    ```
    
    `member1`과 `member2`는 같은 데이터베이스 로우에서 조회했지만, 객체 측면에서 볼 때 둘은 다른 인스턴스이다.
    
    `MemberDAO.getMember()`를 호출할 때마다 `new Member()`로 인스턴스가 새로 생성되기 때문이다.
    
    <br>

    **만약 컬렉션에서 비교한다면?**
    
    ```java
    String memberId = "100";
    Member member1 = list.get(memberId);
    Member member2 = list.get(memberId);
    member1 == member2; //같다.
    ```
    
    같은 주소값을 비교하므로 같다.
    
    <br><br>

> ***객체답게 모델링 할수록 매핑 작업이 늘어난다.
객체를 자바 컬렉션에 저장하듯이 DB에 저장할 수 없을까?***
 

<br>

---

<br><br>

## JPA 동작

![Untitled](/JPA/img/JPA(3).png)

JPA는 애플리케이션과 JDBC사이에서 동작한다.

<br>

### 저장

```java
jpa.persist(member);
```
<br>

![Untitled](/JPA/img/JPA(4).png)

1. 애플리케이션이 객체를 JPA에 넘긴다.
2. JPA는 객체를 분석한다.
3. JPA가 적절한 INSERT SQL 생성 
4. JPA가 JDBC API를 사용해 DB에 SQL을 실행

<br>

### 조회

```java
Member member = jpa.find(memberId);
```

![Untitled](/JPA/img/JPA(5).png)

1. 애플리케이션에서 PK를 JPA에 넘긴다.
2. JPA는 적절한 SELECT SQL을 생성한다.
3. JPA가 JDBC API를 사용해 DB에 SQL을 실행한다.
4. JPA가 반환된 결과를 ResultSet 매핑한다.


<br><br>

---

## JPA의 장점

<br>

### 생산성

```java
jpa.persist(member); // 저장
Member member = jpa.find(memberId); // 조회
member.setName("변경할 이름"); // 수정
jpa.remove(member) // 삭제
```

- **객체를 컬레션에 저장**하듯 JPA에게 저장할객체 전달
- 반복적인 SQL 처리
- **SQL 중심적인 개발에서 객체 중심으로 개발**

<br>

### 유지보수

- 기존에는 필드 변경시 모든 SQL을 수정해야만 했다.
- SQL은 JPA가 처리하기 때문에 필드만 추가해주면 된다.

<br>

### JPA와 패러다임의 불일치 해결

1. **상속**
    <br>

    저장

    ```java
    // 개발자가 할일
    jpa.persist(album);

    // 나머진 JPA가 처리
    INSERT INTO ITEM...
    INSERT INTO ALBUM...
    ```

    조회

    ```java
    // 개발자가 할일
    Album album = jpa.find(Album.class, albumId);

    // 나머진 JPA가 처리
    SELECT I.*,A.*
        FROM ITEM I
        JOIN ALBUM A ON I.ITEM_ID = A.ITEM_ID;
    ```

    개발자는 데이터베이스의 구조에 신경을 쓰지 않아도 된다.

<br>

2. **연관관계, 객체 그래프 탐색**

    ```java
    // 연관관계 저장
    member.setTeam(team); 
    jpa.persist(member);

    // 객체 그래프 탐색
    Member member = jpa.find(Member.class, memberId);
    Team team = member.getTeam();
    ```

    자바 컬렉션처럼 연관관계를 설정할 수 있고, 객체 그래프를 자유롭게 탐색할 수 있다.

<br>

3. **신뢰할 수 있는 엔티티, 계층**

    ```java
    class MemberService {
        ...
        public void process() {
            Member member = memberDAO.find(memberId);
            member.getTeam();  // 자유로운 객체 그래프 탐색
            member.getOrder().getDelivery(); // 데이터가 있다는 가정 하에 가능
        }
    }
    ```

    - `memberDAO` 가 JPA를 통해 조회한것이라면 신뢰가 보장되어있기 때문에 조회가 가능해진다.
    - JPA는 **지연 로딩**이라는 기능으로 자유로운 객체 그래프 탐색이 가능하다.

<br>

4. **비교하기**

    ```java
    String memberId = "100";
    Member member1 = jpa.find(Member.class, memberId); 
    Member member2 = jpa.find(Member.class, memberId);
    member1 == member2; //같다.
    ```

    - JPA는 동일한 트렌잭션에서 조회한 엔티티는 같음을 보장한다.



### JPA의 성능 최적화

<br>

1. **1차 캐시와 동일성 보장**
    
    ```java
    String memberId = "100";
    Member member1 = jpa.find(Member.class, memberId); // SQL
    Member member2 = jpa.find(Member.class, memberId); // 캐시
    member1 == member2;
    ```
    
    - 같은 트랜잭션 안에서는 같은 엔티티를 반환한다.

    <br>
    

1. **트랜잭션을 지원하는 쓰기 지연 - INSERT**
    
    ```java
    transaction.begin(); // 트랜잭션 시작
    
    em.persist(memberA);
    em.persist(memberB);
    em.persist(memberC);
    // 여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
    
    // 커밋하는 순간 데이터베이스에 INSERT SQL을 모아서 보낸다.
    transaction.commit(); //트랜잭션 커밋
    ```
    
    - 트랜잭션을 커밋할때 까지 INSERT SQL을 모은다.
    - JDBC BATCH SQL 기능을 사용해서 한번에 SQL 전송.

    <br>
    

1. **지연 로딩과 즉시 로딩**
    - 지연 로딩 : 객체가 실제로 사용될때 로딩한다.
    - 즉시 로딩 : JOIN SQL로 한번에 연관된 객체까지 미리 조회한다.
    
    ```java
    // 지연로딩
    Member member = memberDAO.find(memberId);  // => SELECT * FROM MEMBER
    Team team = member.getTeam; 
    String teamName = team.getName;            // => SELECT * FROM TEAM
    
    // 즉시로딩
    Member member = memberDAO.find(memberId);  // => SELECT M.*, T.*
    Team team = member.getTeam;                      FROM MEMBER    
    String teamName = team.getName;                  JOIN TEAM...
    ```

<br><br><br><br><br>

### Reference
https://www.inflearn.com/course/ORM-JPA-Basic/dashboard
