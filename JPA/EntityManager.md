# [JPA] 영속성 컨텍스트

<span>
Keyword <br>
- EntityManager <br>
- Entity 생명주기  <br>
- 1차 캐시 <br>
- 쓰기 지연 <br>
- Dirty checking <br>
- flush <br>

---

</span>

<br>

## 엔티티 매니저 팩토리(EntityManagerFactory)와 엔티티 매니저(Entity Manager)

![Untitled](/JPA/img/EntityManager(1).png)


```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
```

요청이 들어올때 마다 엔티티 매니저 팩토리에서 엔티티 매니저를 생성한다.

생성된 엔티티 매니저는 커넥션 풀에 있는 커넥션을 사용해 DB에 연결한다.

- EntityManagerFactory : Entity Manager를 만들고 구성하는 법을 제공하는 interface임. 
- EntityManager : DB 테이블과 mapping된 객체인 Entity에 대한 CRUD 작업을 수행하기 위한 method들을 제공하며 Entity의 라이프 사이클과 영속성 관리등을 담당함. 

<br><br>

## 영속성 컨텍스트란?

> ***“엔티티를 영구 저장하는 환경”***
> 
- 영속성 컨텍스트는 논리적인 개념으로 눈에 보이지 않는다.
- EntityManager를 통해 영속성 컨텍스트에 접근할 수 있다.


<br><br>


## 엔티티의 생명 주기

<br>

### 엔티티의 생명주기란 
=> 엔티티가 생성되어 DB에 저장되고 필요에 따라 수정되거나 삭제되는 일련의 과정을 말함. <br>
=> 엔티티의 상태에 따라 생명주기를 정의합니담. <Br>
`비영속 , 영속, 준영속, 삭제` 의 4가지 생명주기가 있다.

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
=> 이 상태는 당연하지만, DB에 영향을 미치지 않고 저장도 되지않음.
<br>

### 영속

> **영속성 컨텍스트에 관리되는 상태**
> 

![Untitled](/JPA/img/EntityManager(3).png)

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

> **영속상태의 엔티티가 영속 컨텍스트에서 분리(Detached)된 상태**

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


### 삭제

> **엔티티를 영속성 컨텍스트와 DB에서 삭제한 상태**

```java
 em.remove(member);
```
=> remove method를 통해서 영속성컨텍스트에서 엔티티를 삭제함.<br> 
=> 이 상태에서는 영속성 컨텍스트에서 removed 상태로 표시가 되어있고, 실제로 삭제된 것은 아님. <br>
=> 트랜잭션이 커밋될 때 JPA는 영속성 컨텍스트에서 removed 상태로 표시된 모든 엔티티에 대해 DB에서 DELETE 쿼리를 실행함 <br>
=> 그래서 DB에서 커밋되는 시점에 사라짐 <br>
=> 커밋된 시점부터는 영속성 컨텍스트에 존재하지 않는다. 

## ??그럼 연관관계를 갖는 엔티티는용 

연관관걔를 갖는 엔티티의 경우, 부모 엔티티가 삭제될 때 자식 엔티티도 함께 삭제되기를 원할 수 있다. 이를 위해 <br>
`CasecadeType.REMOVE` 아니면 `orphanRemoval = true` 라는 옵션을 사용 가능함. 

``` java
@Entity
public class Parent {
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Child> children = new ArrayList<>();
}

@Entity
public class Child {
    @ManyToOne
    private Parent parent;
}
```

✅ 참고!
```java

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    // 기본 생성자, getter 및 setter 생략

    @PreRemove
    public void logUserDeletion() {
        System.out.println("User Id =" + id + " and username " + username + "이 삭제됩니다.");
        // 여기서 다른 삭제 전에 수행해야 할 작업들을 추가 가능. 
    }
}
```

JPA 엔티티의 삭제가 이루어지기 전에 호출되는 콜백 메소드를 정의할 때 사용됩니당. 

이 콜백 메소드를 사용하여 `엔티티가 삭제되기 전에 특정 작업을 수행 가능합니당.` 

ex) 엔티티 삭제 전 log 남기기, 다른 연관된 작업 

⇒ 이와 같이 코드를 기술하면

```java
EntityManager em = ...;
em.getTransaction().begin();

User user = em.find(User.class, 1L); // ID가 1인 User 엔티티 조회
if (user != null) {
    em.remove(user); // 이 시점에서 @PreRemove 메서드가 호출됨
}

em.getTransaction().commit();
```
⇒ 이렇게 remove 할 때, PreRemove 메소드가 호출됩니당. 



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

![Untitled](/JPA/img/EntityManager(4).png)

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

![Untitled](/JPA/img/EntityManager(5).png)

`member2`가 없으면 DB에서 가져와 1차캐시에 저장하고 또 `member2`가 호출되면 1차캐시에 저장되어있는 `member2`를 반환한다.

여기서 @Id는 DB의 식별값 Id 이다. 

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


![Untitled](/JPA/img/EntityManager(6).png)

![Untitled](/JPA/img/EntityManager(7).png)

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

<br><br>

### Flush 플러시 
트랜잭션 커밋을 실행하면 변경 내용을 데이터베이스에 반영하게 된다. <Br>
트랜잭션 커밋이 일어날 때 플러시도 함께 발생하여 데이터베이스에 반영할 수 있는 것이다. <Br>
즉, 플러시는 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하는 것이다. 

✅ 플러시 발생 시 

- 변경 감지 (더티체킹)
- 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
- 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록, 수정, 삭제 쿼리)

✅ 영속성 컨텍스트를 플러시 하는 방법

- em.flush() - 직접 호출(테스트에 사용)

```java
Member member = new Member(200L, "newName");
em.persist(member);

em.flush(); // 강제 호출
System.out.println("------------");

tx.commit();
```

flush()는 변경을 감지하여 데이터베이스에 반영하는 역할을 한다. 

따라서 이후에 commit()이 발생해도 쿼리를 다시 실행하지는 않는다. 

또한, flush()를 한다고 해서 1차 캐시의 내용이 사라지지 않는다. 

- tx.commit() - 트랜잭션 커밋을 통한 자동 호출
- JPQL 쿼리 실행 - 플러시 자동 호출

⇒ 자동호출 되는 이유
```java
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);

//중간에 JPQL 실행
query = em.createQuery("select m from Member m", Member.class);
List<Member> members= query.getResultList();
```

⇒ 이 코드처럼 persist()를 실행한 뒤 JPQL로 쿼리를 보내면 members에는 데이터베이스로부터 결과를 얻을 수 없을 것이다. 

⇒ 쿼리 발생 이전에 데이터베이스에 반영하는 flush()가 호출되어야 하기 때문이다. 

⇒ 직접 플러시를 호출하거나, 쿼리 이전에 commit()을 해야 한다. 

⇒ 이러한 문제를 방지하기 위해 JPQL이 실행하게 되면 자동으로 플러시를 호출하여 JPQL 쿼리를 반영할 수 있도록 하는 것이다.

## 플러시 모드 옵션
- flushModeType.AUTO(기본 값) 커밋이나 쿼리를 실행할 때 플러시
- flushModeType.COMMIT : 커밋할 때만 플러시

```java
em.setFlushMode(FlushModeType.COMMIT)
```
⇒ 커밋이나 JPQL 쿼리 등을 실행할 때 자동으로 플러시 되는 것을 방지하기 위해 플러시 모드를 설정할 수 있다. 

⇒ 변경된 것과는 아무런 관련이 없는 데이터베이스 테이블에 쿼리를 보내고자 할 때 종종 사용된다.

## 플러시에 대한 오해
- 플러시는 영속성 컨텍스트를 비우지 않는다.
- 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화하는 역할이다.
- 따라서, 트랜잭션은 끝나지 않는 것이다.
- 플러시의 개념은 트랜잭션이라는 작업 단위에 중요 → 커밋 직전에만 동기화하면 된다.


<br><br><br><br><br>

### Reference
https://www.inflearn.com/course/ORM-JPA-Basic/dashboard

