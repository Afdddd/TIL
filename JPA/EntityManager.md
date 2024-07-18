# [JPA] 영속성 컨텍스트

---

<br>

## 엔티티 매니저 팩토리와 엔티티 매니저

![Untitled](/JPA/img/EntityManager(1).png)


```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
```

요청이 들어올때 마다 엔티티 매니저 팩토리에서 엔티티 매니저를 생성한다.

생성된 엔티티 매니저는 커넥션 풀에 있는 커넥션을 사용해 DB에 연결한다.

<br><br>

## 영속성 컨텍스트란?

> ***“엔티티를 영구 저장하는 환경”***
> 
- 영속성 컨텍스트는 논리적인 개념으로 눈에 보이지 않는다.
- EntityManager를 통해 영속성 컨텍스트에 접근할 수 있다.


<br><br>


## 엔티티의 생명 주기

<br>

### 비영속

> **영속성 컨텍스트와 전혀 관계가 없는 새로운 상태**
> 

![Untitled](/JPA/img/EntityManager(2).png)

```java
// 객체를 생성만 한 상태(비영속)
Member member = new Member(); 
member.setId(1L);
member.setName("hi");
```

<br>

### 영속

> **영속성 컨텍스트에 관리되는 상태**
> 

![Untitled](/jpa/img/EntityManager(3).png)

**em.persist(Entity)** : Entiy를 영속성 컨텍스트에 관리

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();

tx.begin(); // 트랜잭션 시작

Member member = new Member(1L,"hello");

System.out.println("=== BEFORE ===");
**em.persist(member);**
System.out.println("=== AFTER ===");

tx.commit(); // 커밋
```

```java
// 결과
=== BEFORE ===
=== AFTER  ===
Hibernate: 
    /* insert hellojpa.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
```

우리는 `persist()`를 하면 DB에 쿼리를 보내는줄 알았지만 사실은 그렇지 않다.

결과를 보면 `persist()` 시점에 쿼리를 보내지 않는다.

`persist()` 는 엔티티를 영속성 컨텍스트로 관리를 한다.

`commit()` 시점에서 DB에 쿼리를 보낸다.

<br>

### 준영속

> **영속상태의 엔티티가 영속 컨텍스트에서 분리된 상태**

<br>

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();

tx.begin(); // 트랜잭션 시작

Member findMember = em.find(Member.class, 1L); // 영속
em.detach(findMember);  // 준영속
findMember.setName("hi"); // 반영 안됨

tx.commit(); // 커밋
```

```java
// 결과
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_ 
    from
        Member member0_ 
    where
        member0_.id=?
```

`em.detach()` 이전인 `em.find()` 는 영속상태이기 때문에 select 쿼리를 호출했고
`em.detach()` 이후인 `setName()` 는 준영속상태이기 때문에 update쿼리가 호출이 되지 않았다.

- em.detach(entity) : 특정 엔티티만 준영속 상태로 전환
- em.clear() : 영속성 컨텍스트를 완전히 초기화
- em.close() : 영속성 컨텍스트를 종료

<br>

## 영속성 컨텍스트의 이점

1. 1차 캐시
2. 동일성 보장
3. 트랜잭션을 지원하는 쓰기 지연
4. 변경 감지(Dirty Checking)
5. 지연 로딩(Lazy Loading)

<br>

### 1차 캐시

> **엔티티를 보관하는 저장소**


<aside>
💡 1차캐시를 영속성 컨텍스트라고 생각하면 편하다.
</aside>

<br>

![Untitled](/jpa/img/EntityManager(4).png)

```java
tx.begin(); // 트랜잭션 시작

Member member1 = new Member("member1"); 

em.persist(member1); 1.
Member findMember = em.find(Member.class,"member1"); 2.

tx.commit(); // 커밋
```

```java
// 결과
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_ 
    from
        Member member0_ 
    where
        member0_.id=?
```

`em.persist()`로 1번 `em.find()`로 2번 select 쿼리를 보냈지만 결과는 select 쿼리가 1번만 실행됬다. 처음 `em.persist(member1)` 로 `member1`이 **1차캐시**에 저장이 되고 `em.find()`는 1차 캐시에 저장된 `member1` 을 조회한 것 이기 때문에 쿼리가 1개만 전송된것이다.

<br>

**데이터베이스에서 조회**

```java
tx.begin(); // 트랜잭션 시작

// member2가 1차 캐시에 저장이 안됐을 경우
Member findMember = em.find(Member.class,"member2");

tx.commit(); // 커밋
```

![Untitled](/jpa/img/EntityManager(5).png)

`member2`가 없으면 DB에서 가져와 1차캐시에 저장하고 또 `member2`가 호출되면 1차캐시에 저장되어있는 `member2`를 반환한다.

<br>

### 동일성 보장

같은 트랜잭션안이라면 1차캐시를 통해 동일한 인스턴스를 반환한다.
 

```java
Member findMember1 = em.find(Member.class, 1L);
Member findMember2 = em.find(Member.class, 1L);
System.out.println("result = "+(findMember1 == findMember2)); // true
```

<br>

### 트랜잭션을 지원하는 쓰기 지연

영속성 컨텍스트에 변경이 발생했을 때, 바로 데이터베이스로 쿼리를 보내지 않고 SQL 쿼리를 SQL저장소에 모아놨다가, 영속성 컨텍스트가 flush 하는 시점에 모아둔 SQL 쿼리를 데이터베이스로 보내는 기능이다.


![Untitled](/jpa/img/EntityManager(6).png)

![Untitled](/jpa/img/EntityManager(7).png)

```java
tx.begin(); // 트랜잭션 시작

Member memberA = new Member(1L, "A");
Member memberB = new Member(2L, "B");
            
em.persist(memberA); // 영속
System.out.println(">=================<");
em.persist(memberB); // 영속
System.out.println(">=================<");

tx.commit(); // 커밋
```
<br>

```java
// 결과
>=================<
>=================<
Hibernate: 
    /* insert hellojpa.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
Hibernate: 
    /* insert hellojpa.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
```

`persist()`할때마다 쿼리를 보내지않고 모아서 보낸다.

<br>

META-INF/persistence.xml 에서 모을 SQL수를 정할수있다.

```java
<property name="hibernate.jdbc.batch_size" value="10"/>
```



<br>

### 변경 감지(Dirty Checking)

JPA에서는 트랜잭션이 끝나는 시점에 **변화가 있는 모든 엔티티 객체**를 데이터베이스에 자동으로 반영하는 기능이다.

<br> 

```java
tx.begin(); // 트랜잭션 시작

Member findMember = em.find(Member.class, 1L); //조회
findMember.setName("Hello"); 

tx.commit(); // 커밋
```

```java
// 결과
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_ 
    from
        Member member0_ 
    where
        member0_.id=?
Hibernate: 
    /* update
        hellojpa.Member */ update
            Member 
        set
            name=? 
        where
            id=?
```

조회한 엔티티의 필드만 `setXXX()`로 변경해준것뿐이지만 update쿼리가 보내졌다.
**마치 컬렉션에서 객체의 필드를 변경하는 것처럼 간단하게 가능하다.**

어떻게 가능한것일까?


![Untitled](/JPA/img/EntityManager(8).png)

JPA는 엔티티의 최초 조회 상태를 1차 캐시에 저장 해놓는데 이것을 **스냅샷**이라 한다.

트랜잭션 커밋 시점에 엔티티와 스냅샷을 비교한다.

**이때 다른점이 있다면 update쿼리를 생성한다.**

준영속/비영속 상태의 엔티티는 Dirty Checking 대상에 포함되지 않는다.



<br><br><br><br><br>

### Reference
https://www.inflearn.com/course/ORM-JPA-Basic/dashboard

