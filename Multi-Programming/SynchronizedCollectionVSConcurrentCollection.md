# Synchronized Collection VS Concurrent Collection

<br>
Synchronized Collection과 Concurrent Collection의 특징을 살펴보고 차이를 알아보자.

공통적으로 둘 다 컬렉션 프레임워크 스레드 안전성을 제공하기 위해 사용된다.

<br>

### Synchronized Collection

---

`Synchronized` 키워드를 사용한 Collection이다.

내부적으로 `Synchronized` 키워드를 사용한다.
lock을 사용해 원자성을 지키기 때문에 **블로킹(Blocking)** 이다.

대표적으로 아래의 클래스들이 있다.

- Vector
- Hashtable
- Collections.synchronizedXXX

다만 컬렉션 자체가 동기화된 것이 아니라 해당 클래스들의 메서드들이 동기화된것이다.

<br>

Vector의 `addElement` 메서드 

```java
public synchronized void addElement(E obj) {
    modCount++;
    add(obj, elementData, elementCount);
}
```

Synchronized 메서드가 보인다.

<br>

Hashtable의  `size()`메서드

```java
public synchronized int size() {
    return count;
}
```

마찬가지로 Synchronized 메서드가 보인다.

<br>

`Collection.SynchronizedCollection` 의 내부

```java
static class SynchronizedCollection<E> implements Collection<E>, Serializable {
	
	final Object mutex;

	public boolean add(E e) {
	  synchronized (mutex) {return c.add(e);}
	}
	public boolean remove(Object o) {
	  synchronized (mutex) {return c.remove(o);}
	}
}
```

`synchronized`블럭으로 감싸고있다.

`mutex` 락을 하나만 생성해 모든 메서드가 공유한다. 그렇기 때문에 하나의 쓰레드가 synchronized 블럭에 들어가는 순간 mutex를 쥐고 들어가기 때문에 다른 쓰레드들은 해당 클래스의 모든 메서드에 접근하지 못한다.

<br>

`Collection.SynchronizedMap` 

```java
 private static class SynchronizedMap<K,V> implements Map<K,V>, Serializable {
 
  final Object mutex;  
  
	public V get(Object key) {
	  synchronized (mutex) {return m.get(key);}
	}
	
	public V put(K key, V value) {
	  synchronized (mutex) {return m.put(key, value);}
	}
 }
```

마찬가지로 모든 메서드들에 `synchronized` 키워드를 사용하고 있고 하나의 락을 사용한다.

<br><br>

**단점**

모든 메서드에 `synchronized` 를 사용한다. `get()` 과 같이 값을 읽어 들이는 메서드에도 `synchronized`  가 붙어있어 성능상 좋지않다. 

그리고 `SynchronizedCollection` 은 단일락을 사용하기 때문에 하나의 쓰레드가 lock을 쥐고있으면 다른 쓰레드들은 다른 메서드들을 이용하지못하고 **blocking** 상태가되어 성능 저하로 이루어진다.

<br>

<aside>
💡  <b>Blocking 상태</b>
<br> 동기화된 객체에 접근하지 못한 쓰레드가 자신의 할일하지 못하고 기다리고 있는 상태.
</aside>

<br>

이러한 문제들로 sycnchronized collection의 사용을 권장하지 않는다.

Vector나 Hashtable은 코드의 호환성 때문에 남겨놓은거지 자주 사용되지 않는 컬렉션들이다.

자바5 버전부터 추가된 java.util.concurrent 패키지는 보다 더 효율적인 동기화 컬렉션들을 제공한다.

<br><br>

### Concurrent Collection


Concurrent Collection에는 다양한 collection들이 있다.

- CopyOnWriteArrayList
- ConcurrentHashMap
- ConcurrentLinkedQueue
- LinkedBlockingQueue
- LinkedBlockingDeque
- ConcurrentSkipListMap
- ConcurrentSkipListSet

**CopyOnWriteArrayList**

write 작업시(`add()`) 원본 배열에 있는 모든 요소를 복사해 임시 배열을 만들고 임시 배열에 write 작업을 수행하고 임시 배열을 원본 배열에 덮어 씌운다. 동시성을 위해 쓰기 작업에 Lock을 건다.

```java
public void add(int index, E element) {
    synchronized (lock) { // 임계영역
        Object[] es = getArray();
        int len = es.length;
        if (index > len || index < 0)
            throw new IndexOutOfBoundsException(outOfBounds(index, len));
        Object[] newElements; // 임시배열
        int numMoved = len - index;
        if (numMoved == 0)
            newElements = Arrays.copyOf(es, len + 1); // 임시 배열에 요소 복사
        else {
            newElements = new Object[len + 1];
            System.arraycopy(es, 0, newElements, 0, index);
            System.arraycopy(es, index, newElements, index + 1,
                             numMoved);
        }
        newElements[index] = element;
        setArray(newElements); // 덮어 씌우기
    }
}
```

<br>

read(`get()`) 작업은 동시성이 필요하지 않기 때문에 SynchronizedList보다 성능이 좋다. SynchronizedList는 read 작업에도 동기화 처리를 한다.

```java
public E get(int index) {
    return elementAt(getArray(), index);
}
```

<br>

**Concurrent HashMap**

일반 HashMap을 동기화 처리하려면 SynchronizedMap을 사용해 동기화 처리했다. 하지만 SynchronizedMap은 모든 메서드에 동기화 처리를 하기 떄문에 하나의 쓰레드가 SynchronizedMap사용하고 있다면 다른 쓰레드들은 SynchronizedMap의 다른 메서드를 사용하지 못하고 기다리게 된다. 이렇게 되면 성능이 저하된다.

Concurrent HashMap은 Segment라는 배열을 사용한다. 각 Segment는 독립적인 작은 hashmap처럼 작동하고 자체적으로 락을 가지고 있어 여러 쓰레드가 동시에 접근해도 서로 다른 Segment에서 작업하기 때문에 안전하다.

![Untitled](/Multi-Programming/img/currentHashMap.png)

<br>

Concurrent HashMap도 마찬가지로 값을 추가하는 `put()` 에는 동기화 처리가되어있지만 값을 읽어들이는 get()에는 동기화 처리가 안되어있다.

`put()`

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {    
		... 생략  
    synchronized (f) { <--- 동기화
        if (tabAt(tab, i) == f) {
            if (fh >= 0) {
                binCount = 1;
                for (Node<K,V> e = f;; ++binCount) {
                    K ek;
                    if (e.hash == hash &&
                        ((ek = e.key) == key ||
                         (ek != null && key.equals(ek)))) {
                        oldVal = e.val;
                        if (!onlyIfAbsent)
                            e.val = value;
                        break;
                    }
                    Node<K,V> pred = e;
                    if ((e = e.next) == null) {
                        pred.next = new Node<K,V>(hash, key, value);
                        break;
                    }
                }
            }
      ... 생략               
    }

```
<br>

`get()`

```java
public V get(Object key) {
        Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
        int h = spread(key.hashCode());
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (e = tabAt(tab, (n - 1) & h)) != null) {
            if ((eh = e.hash) == h) {
                if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                    return e.val;
            }
            else if (eh < 0)
                return (p = e.find(h, key)) != null ? p.val : null;
            while ((e = e.next) != null) {
                if (e.hash == h &&
                    ((ek = e.key) == key || (ek != null && key.equals(ek))))
                    return e.val;
            }
        }
        return null;
    }
```

Concurrent Collection이든 Synchronized Collection 이든 둘 다 컬렉션 자체가 동기화 된것이 아닌 메서드들이 동기화 된것이다.

<br>

따라서 동기화된 여러 연산들을 묶어 하나의 단일 연산처럼 활용해야할때 문제가 발생한다.

```java

public class SynchronizedCollectionClass{

  Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
  Collection<Integer> conCollection = new CopyOnWriteArrayList<>();


  Runnable syncOperations = () -> {
		for (int i = 0; i < 1000; i++) {
	
      syncCollection.clear();
      syncCollection.add(1);
      syncCollection.add(2);
      syncCollection.add(3);
      syncCollection.add(4);
      syncCollection.remove(1);
      
      
      conCollection.clear();
      conCollection.add(1);
      conCollection.add(2);
      conCollection.add(3);
      conCollection.add(4);
      conCollection.remove(1);
     }
  };

  public static void main(String[] args) throws InterruptedException {
      SynchronizedCollectionClass s = new SynchronizedCollectionClass();

      Thread thread1 = new Thread(s.syncOperations);
      Thread thread2 = new Thread(s.syncOperations);
      

      thread1.start();
      thread2.start();


      thread1.join();
      thread2.join();


      System.out.println(s.syncCollection.size());
  }
}
```

thread1이 clear()하는 순간에 thread2가 add(1)를 할수있다. 이렇게 되면 원하는 값이 나오지 않을 수 있다.

이를 해결하려면 synchronized 블럭으로 감싸 임계영역을 설정해줘야한다.

```java
synchronized(syncCollection){
  syncCollection.clear();
  syncCollection.add(1);
  syncCollection.add(2);
  syncCollection.add(3);
  syncCollection.add(4);
  syncCollection.remove(1);    
}

synchronized (conCollection){
  conCollection.clear();
  conCollection.add(1);
  conCollection.add(2);
  conCollection.add(3);
  conCollection.add(4);
  conCollection.remove(1);    
}
```

이렇게 동기화된 여러 연산들을 묶어 하나의 단일 연산처럼 활용할때는 동기화처리를 해줘야 한다.

<br><br>

### 결론

`Concurrent Collection`이 `Synchronized Collection` 보다 최근에 나왔고 보다 효율성을 위해 나온거라 `Concurrent Collection`을 사용하는게 좋다. 하지만 어떤 상화에서는 `Synchronized Collection`을 사용하는게 더 좋을수도 있다. 예를 들어 값이 수백만개가 들어있는 `ArrayList`를 동기화 처리하려고 한다면 `CopyOnWriteArrayList` 를 사용하면 배열을 복사해야 하기 때문에 메모리도 2배가 필요하고 복사하는데 시간도 오래 걸릴것이다. 이럴 경우에는  `synchronizedCollection()`를 쓰는게 더 좋을것이다. 상황에 따라 적절한 방식을 선택하는 것은 개발자의 몫이다.