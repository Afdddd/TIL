# JPA Entity Listener

JPA 엔티티 리스너(Entity Listener)는 엔티티의 상태 변화에 반응하여 특정 로직을 자동으로 실행할 수 있도록 해주는 기능이다.

엔티티의 **생성**, **수정**, **삭제** 등의 이벤트가 발생할 때마다 자동으로 메서드를 실행할 수 있게 해준다.

- **`@PrePersist`**: 엔티티가 처음 **저장**되기 전에 실행.
- **`@PostPersist`**: 엔티티가 처음 **저장**된 후에 실행.
- **`@PreUpdate`**: 엔티티가 **업데이트**되기 전에 실행.
- **`@PostUpdate`**: 엔티티가 **업데이트**된 후에 실행.
- **`@PreRemove`**: 엔티티가 **삭제**되기 전에 실행.
- **`@PostRemove`**: 엔티티가 **삭제**된 후에 실행.
- **`@PostLoad`**: 엔티티가 **조회**된 후에 실행.

<br>

```java
  public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private LocalDateTime lastUpdate;

    @Builder
    public Inventory(Product product, Integer quantity){
        this.product = product;
        this.quantity = quantity;
    }

    @PrePersist
    @PreUpdate
    void setLastUpdate(){
        this.lastUpdate = LocalDateTime.now();
    }
}
```

<br>

재고를 처음 등록한 날짜나 최근 업데이트 된 날짜를 저장하는 필드 `lastUpdate` 가 있다.

DB에 처음 등록할 때와 업데이트 될때 마다 값이 변하므로 `@PrePersist` , `@PreUpdate` 를 사용해 자동으로 값을 변경하도록 해주는것이 좋다.

특히나 JPA의 **더티 체킹** 으로 값을 변경할 경우 엔티티가 메모리 상에서 변경될 때마다 바로 데이터베이스에 저장되지 않고, 트랜잭션이 커밋될 때 그 변경 사항이 실제로 데이터베이스에 반영되기 때문에 `lastUpdate`와 같은 필드를 업데이트하는 시점은 **메모리에서 값이 변경될 때마다**가 아니라, **데이터베이스에 실제로 저장되기 직전**에 하는 것이 더 효율적이다.