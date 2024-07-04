# ArrayList 자동확장

# ArrayList의 공간 확장 메커니즘

>오늘은 Java의 ArrayList가 <b style="color : yellow">자동으로 공간 확장을 어떻게 하는지</b>, <Br>
>그리고 그러한 자동확장 상황에서 <b style="color : yellow">메모리가 부족하지 않으며, 
너무 자주 확장이 되지 않도록 하는 방법에 무엇이 있는가</b><br>
에 대해서 다뤄볼 예정입니당. 

<Br>
Java의 `ArrayList`는 List 인터페이스를 구현하고 <Br> 배열에 요소를 저장합니당. <br> (사진 참고)

![Untitled](/Java/img/ArrayList(1).png)

대표적인 기능으로 

![Untitled](/Java/img/ArrayList(2).png)

사진과 같은 <Br>
- add()    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⇒ 배열에 요소를 추가해줌 <br>
- remove() ⇒ 삭제, <Br>
- get()    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⇒ 가져오기 등등의 기능을 제공합니당. 

ArrayList의 가장 큰 특징으로 `동적 확장` 이 있는데, <Br>
다양한 수의 요소를 수용하기 위해 필요에 따라 <Br>
<b>내부 배열의 크기를 자동으로 조정</b>할 수 있는 것 입니당.
 

```jsx
  private static final int DEFAULT_CAPACITY = 10;
```

다음과 같이 만약 ArrayList 배열의 초기 값을 설정 하지 않아도   <br>
class 내부에 기본 용량인 10으로 배열을 생성합니다.
<Br><Br>

>설명하기 전에 간단하게 브리핑을 하자면, 
`ArrayList` 는 <Br> 공간이 부족하면 새로운 배열을 생성하고, 
>기존 데이터를 <br>새로운 배열로 복사한 뒤, 참조를 새 배열로 변경하며<Br>
>이 과정에서 배열의 크기는 기존 크기의 1.5배로 증가합니당. <br>
>이제 그 과정을 자세하게 말씀 드릴게욥 

### JDK 8기준 ArrayList 자동 확장

<mark><b>step 1️⃣ : ArrayList에 12개의 요소 추가 상황</b></mark> 
<br>
기본 배열 크기인 10을 넘어가는 상태

```java
import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
    
        //elementData는 10개의 null 값으로 채워진 배열 생성
        ArrayList<String> list = new ArrayList<>();
        
        // 처음 반복문에서 
        // elementData: ["Element 0", null, null, null, null, null, null, null, null, null]
				// size: 1
        for (int i = 0; i < 12; i++) {
            list.add("Element " + i);
        }
        System.out.println("ArrayList size: " + list.size());
        System.out.println("ArrayList elements: " + list);
    }
}
```

⇒ 이 때 기본 용량이 10으로 설정된 `ArrayList`가 생성됨. <br> 
⇒ 10번째 요소까지 추가했을 때는 자동확장이 일어나지 않음. <br>
⇒ 11번째 요소를 추가할 때 어떤 일이 일어나냐 ! <br>

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // 용량 확인 및 필요시 확장
    elementData[size++] = e;  // 요소 추가 및 크기 증가
    return true;
}
```

⇒ add method로 값을 추가 하잖아요. <br>
=>  `ensureCapacityInternal()` 를 통해 [이 과정은 JDK7에서 추가] <br>내부 배열의 용량이 충분한지 확인하고 필요시 확장합니당. <Br>
현재까지 size + 1은 11이겠죵 <br>
그 11값을 매개변수로 가지고  `ensureCapacityInternal()` 메소드를 호출해요.


```java
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}
```

minCapacity : 11일거고 
DEFAULTCAPACITY_EMPTY_ELEMENTDATA는 

```java
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
```

보시는 바와 같이 빈 배열이기 때문에 elementData 가 빈배열이라면 

if문을 타는 거라서 지금은 안타용 

따라서, `ensureExplicitCapacity(minCapacity);` 를 타게될 거거든요 <br>[이 과정도 JDK7에서 추가]

```java
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

* 참고  
`modCount`는 `ArrayList`가 구조적으로 변경될 때마다 증가하는 변수.
⇒ `ArrayList`의 동작 중 구조적 변경<br>(예: 요소 추가, 삭제 등)이 발생했는지 여부를 추적하는 데 사용됨.

⇒ 이 상태에서 `minCapacity`은 11이고

⇒ `elementData.length` 은 10이니까 둘의 차가 
0보다 크죠. 

 그럼 이제 `grow(minCapacity);` 가 호출이 됩니당. 

```java
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);  // 기존 용량의 1.5배
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
        // 새로운 용량이 최소 필요 용량보다 작다면 최소 필요 용량으로 설정
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        // 새로운 용량이 최대 배열 크기(MAX_ARRAY_SIZE)를 초과하면, 
        // hugeCapacity 메소드를 호출하여 적절한 용량을 설정.
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

`minCapacity`  : 11

`oldCapacity` : 10

`newCapacity` : 15<br>
(oldCapacity 를 비트 시프트 연산자(>>말하는 것)를 통해서 <br>1비트를 오른쪽으로 이동시킴. (>>가 1비트 오른쪽 이동임)<Br>
(10은 2진수로 1010이고 이걸 비트시프트 연산하면 오른쪽으로 <br>1비트 만큼 움직이고 맨 앞은 양음의 수로 두어 0101이 되기 때문에 <br>
(원래 10이던 수가 (0101을 10진수로바꾸면 5) 5로 바뀌어 기존용량의 1.5배 만큼 늘어납니당. 

`MAX_ARRAY_SIZE`

```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```

`Integer.MAX_VALUE`는 자바에서 표현할 수 있는 최대 정수 값으로, 약 21억임. <br>이 값을 직접 사용하지 않고 <b>-8</b>을 하는 이유는 <Br>일부 JVM 구현에서 배열 크기에 대한 추가적인 오버헤드를 처리하기 위해서, <Br>(왜 굳이 -9,-7도 아니고 -8인지는 못찾음)

그래서 `MAX_ARRAY_SIZE` 가 만약에 필요한 `newCapacity` 보다 <br>크게 되면 오버헤드를 피하기 위해 
`hugeCapacity` 를 호출하는 것이지용 <br>
[이 과정은 JDK8에서 추가]

```java
private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
}
```

1. 일단 `minCapacity` 가 음수인지 확인하고 
음수인 경우는 배열의 크기가 너무 커져서 음수로 오버플로우한 경우로 간주함.<br>
이 경우, `OutOfMemoryError`를 던져서 메모리 할당 실패를 명시적으로 알립니다.

2. 그 다음, minCapacity 가 MAX_ARRAY_SIZE를 초과하는지 확인한 후, 만약 `minCapacity`가 `MAX_ARRAY_SIZE`보다 크면,<Br> `Integer.MAX_VALUE`를 반환 <Br>
⇒ 이는 자바에서 배열의 최대 크기를 나타냄 (약 21억 정수)

3. 그렇지 않으면 `MAX_ARRAY_SIZE`를 반환.

### 주요 차이점 요약을 해보자면

1. **JDK 6**:
    - `ensureCapacity` 메소드가 직접 용량 확인과 확장을 처리.
    - 배열 크기를 1.5배로 확장.
2. **JDK 7**:
    - `ensureCapacityInternal` 메소드가 추가되어 초기 용량을 설정.
    - `ensureExplicitCapacity` 메소드가 실제 용량 확인과 확장을 처리.
    - 배열 크기를 1.5배로 확장하는 로직은 동일.
3. **JDK 8**:
    - `ensureCapacityInternal` 및 `ensureExplicitCapacity` 메소드가 유지.
    - `hugeCapacity` 메소드가 추가되어 매우 큰 용량을 안전하게 처리.
    - 배열 크기를 1.5배로 확장하며, `MAX_ARRAY_SIZE`를 넘는 경우를 처리.

이러한 변화는 각 버전에서의 <b>성능 최적화와 안정성</b>을 위한 것! <br>
특히 JDK 8에서는 매우 큰 배열을 처리하는 경우를 위해 <Br>
추가적인 로직이 포함되었다 (huge method).

<Br><br>

# 메모리 부족 및 과도한 확장 방지 방법

<mark><b>1️⃣ **초기 용량 설정**: </b></mark>

`ArrayList`를 생성할 때 예상되는 최대 크기를 고려하여 <br>
초기 용량을 설정하기. => 이는 빈번한 크기 변경을 줄이는 데 도움이 됩니당.  

⇒ 특히 , 예상되는 요소의 수가 많은 경우 유용.

```java
ArrayList<Integer> list = new ArrayList<>(100);
```

⇒ ArrayList는 내부적으로 `elementData` 라는 배열을 사용하여 요소를 저장하는데요<br>
⇒ 넘겨준 크기의 값이 양수일 경우 그 값으로 배열 크기를 지정합니당. <Br>
⇒ 이렇게 하면 초기 용량을 설정하여 불필요한 메모리 할당과 해제를 줄일 수 있어욤!


<mark><b>2️⃣ trimToSize() 메소드 사용</b></mark> <Br>
⇒ 작업이 완료된 후 실제로 사용된 요소의 수에 맞게 배열의 크기를 줄여<br> 메모리를 효율적으로 사용합니당. 메모리 낭비를 줄이는 데 유용해욤

```java
list.trimToSize();
```

```java
public void trimToSize() {
    modCount++;
    if (size < elementData.length) {
        elementData = Arrays.copyOf(elementData, size);
    }
}
```


⇒if 문에서 

`size` : ArrayList에 실제로 저장된 요소의 개수 

`elementData.length` : 내부 배열의 현재 크기 
`size`가 `elementData.length`보다 작은 경우에만 배열 크기를 조정합니다. <Br> 즉, 배열에 여유 공간이 있는 경우에만 작동.

⇒  새로운 배열(elementData )은 기존 배열의 요소들을 복사하되, 크기가 `size`와 동일.

⇒ `elementData` 참조를 새로운 배열로 변경하여, 불필요한 메모리를 해제!!하는거죠!!


<mark><b>3️⃣ 대안 자료 구조 사용</b></mark>

**LinkedList**: 빈번한 삽입 및 삭제가 필요한 경우 `LinkedList`가 더 효율적입니다.

<aside>
💡 **LinkedList**: 노드 기반의 자료 구조로, 각 요소는 노드로 저장되며, 
삽입 및 삭제 시 연결된 노드의 포인터만 변경됩니다.

</aside>

Linked List는 Array List와는 다르게 엘리먼트와 엘리먼트 간의 연결(link)을 이용해서 리스트를 구현

ArrayList는 값의 삽입, 삭제가 빈번하면 뒤의 자료구조 위치를 전부 이동해야 하지만,

LinkedList는 서로 연결된 next 값만 변경해주면 돼서 훨씬 빠르다.

![Untitled](ArrayList%20%E1%84%8C%E1%85%A1%E1%84%83%E1%85%A9%E1%86%BC%E1%84%92%E1%85%AA%E1%86%A8%E1%84%8C%E1%85%A1%E1%86%BC%20f2b0dbe8ab4547698ee874ed5f686790/Untitled%202.png)

**HashMap / HashSet**: 요소의 고유성을 유지하면서 빠른 삽입 및 검색이 필요한 경우 `HashMap`이나 `HashSet`이 더 효율적입니다.

<aside>
💡 **HashMap / HashSet**: 해시 테이블 기반의 자료 구조로, 
빠른 검색과 삽입을 위해 요소가 해시 테이블에 저장됩니다.

</aside>

1. 
2. 
3. **적절한 크기 증가율 설정**: 기본적으로 1.5배로 증가하지만, 용량 증가율을 보다 세밀하게 조정할 수 있습니다. 예를 들어, 직접 커스텀한 `ArrayList`를 구현하여 필요에 따라 다른 증가 전략을 사용할 수 있습니다.