# Sequence vs AUTO_INCREMENT

시퀀스(Sequence)와 자동 증가(AUTO_INCREMENT)는 데이터베이스에서 고유한 숫자 값을 생성하는 두 가지 주요 방법이다.

<br>

## 시퀀스(Sequence)

### 특징

1. **범용성**: 시퀀스는 데이터베이스 객체로서 다양한 테이블이나 컬럼에서 재사용이 가능하다.
2. **유연성**: 시퀀스는 시작 값, 증가 값, 최대 값 등을 자유롭게 설정할 수 있다.
3. **독립성**: 시퀀스는 특정 테이블이나 컬럼에 종속되지 않는다. 여러 테이블에서 동일한 시퀀스를 사용할 수 있다.

<br>

### 사용 예

Oracle, PostgreSQL, DB2 등

```sql
CREATE SEQUENCE my_sequence
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

```

<br>

### JPA와의 통합

JPA에서 시퀀스를 사용하여 기본 키를 생성할 수 있다.

```java
@Entity
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "my_sequence", allocationSize = 1)
    private Long id;

    // 기타 필드, 생성자, getter, setter
}

```

<br><br>

## 자동 증가(AUTO_INCREMENT)

### 특징

1. **간단함**: 테이블의 특정 컬럼에 대해 자동으로 숫자 값을 증가시킨다.
2. **일관성**: 각 테이블마다 독립적인 증가 값을 가진다.
3. **제한적 사용**: AUTO_INCREMENT는 특정 테이블의 특정 컬럼에만 적용된다.

<br>

### 사용 예

MySQL, SQL Server 등

```sql
CREATE TABLE my_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

```
<br>

### JPA와의 통합

JPA에서 AUTO_INCREMENT를 사용하여 기본 키를 생성할 수 있다.

```java
@Entity
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기타 필드, 생성자, getter, setter
}

```

<br>

### 요약

| 특성 | 시퀀스(Sequence) | 자동 증가(AUTO_INCREMENT) |
| --- | --- | --- |
| **범용성** | 여러 테이블에서 재사용 가능 | 특정 테이블의 특정 컬럼에만 적용 |
| **유연성** | 시작 값, 증가 값, 최대 값 등을 자유롭게 설정 가능 | 설정이 간단하지만 유연성이 낮음 |
| **성능** | 캐시 사용 시 더 높은 성능 제공 | 간단하고 효율적 |
| **데이터베이스 종속성** | Oracle, PostgreSQL 등에서 사용 | MySQL, SQL Server 등에서 사용 |
| **복잡성** | 다소 복잡할 수 있음 | 설정이 간단하며 관리가 용이함 |