# Java 병렬 처리

Java에서 병렬 처리(parallel processing)를 수행하는 두 가지가 있다. 

1. **스레드를 사용하는 방법 (Thread-based parallel processing)**:
    - 이 방식은 Java의 기본적인 스레드(Thread)를 사용하여 병렬 처리를 구현하는 방법. 개발자가 직접 스레드를 생성하고, 각 스레드에 작업을 할당한 후, 이 스레드들이 병렬로 실행되도록 한다.
    - Java의 기본 `Thread` 클래스, `Runnable` 인터페이스, 그리고 `ExecutorService`를 활용한 병렬 처리가 포함된다.
2. **Fork/Join Framework를 사용하는 방법 (Fork/Join-based parallel processing)**:
    - 이 방식은 Java 7에 도입된 **Fork/Join Framework**를 사용하여 병렬 처리를 구현하는 방식. 작업을 작은 단위로 나누고, 이를 병렬로 처리한 후 다시 합치는 구조이다.
    - **Fork/Join Framework**는 특히 큰 작업을 작은 작업으로 쪼개서 병렬로 처리하고, 이를 다시 합칠 때 유용한 방식하다.

---

## 1. 스레드를 사용한 병렬 처리 (Thread-based Parallel Processing)

Java에서 스레드를 사용한 병렬 처리는 개발자가 스레드를 직접 관리하고, 스레드에 특정 작업을 할당하여 동시에 여러 작업을 처리하는 방식이다. Java는 `Thread` 클래스와 `Runnable` 인터페이스를 통해 멀티스레딩을 지원하며, Java 5에서는 **`ExecutorService`**가 추가되어 스레드 풀을 통한 병렬 처리를 더욱 간편하게 할 수 있게 되었다.

### 구현 방법

### 1.1. `Thread`를 사용한 병렬 처리

```java
public class ThreadExample {
    public static void main(String[] args) {
        // Thread 클래스를 상속받은 클래스를 정의
        Thread task1 = new Thread(() -> {
            System.out.println("Task 1 실행 중...");
        });

        Thread task2 = new Thread(() -> {
            System.out.println("Task 2 실행 중...");
        });

        // 병렬로 실행
        task1.start();
        task2.start();
    }
}

```

### 1.2. `ExecutorService`를 사용한 병렬 처리

`ExecutorService`는 스레드 풀을 관리하여 여러 작업을 병렬로 실행하는 데 유용하다.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3개의 스레드 풀 생성

        // 스레드 풀에 작업 제출
        executor.submit(() -> System.out.println("Task 1 실행 중..."));
        executor.submit(() -> System.out.println("Task 2 실행 중..."));
        executor.submit(() -> System.out.println("Task 3 실행 중..."));

        executor.shutdown(); // 작업이 완료되면 스레드 풀 종료
    }
}

```

### 장점

- **직접적인 제어**: 개발자가 스레드를 직접 관리하고, 스레드의 생명 주기와 실행 방법을 제어할 수 있다.
- **다양한 방식으로 구현 가능**: 기본적인 `Thread`, `Runnable`, `ExecutorService` 등을 활용하여 다양한 병렬 처리 방식을 구현할 수 있다.

### 단점

- **복잡성**: 스레드를 직접 관리해야 하므로, 코드가 복잡해질 수 있고 스레드 수에 대한 적절한 조절이 필요하다.
- **자원 관리**: 직접적인 스레드 관리는 자원을 잘못 관리할 경우 성능 문제가 발생할 수 있다.

---

## 2. Fork/Join Framework를 사용한 병렬 처리 (Fork/Join-based Parallel Processing)

### 기본 개념

**Fork/Join Framework**는 Java 7에서 도입된 병렬 처리 프레임워크로, **작업을 작은 단위로 분할(Fork)**한 후, **병렬로 처리하고 결과를 다시 병합(Join)**하는 방식을 사용한다. 이 프레임워크는 큰 작업을 더 작은 작업으로 나누어 병렬 처리하고, 다시 그 결과를 결합하여 최종 결과를 생성하는 방식으로 동작한다.

- **Fork**: 큰 작업을 작은 단위로 나누는 단계.
- **Join**: 병렬로 나뉜 작업들의 결과를 다시 합치는 단계.

### 구현 방법

Fork/Join Framework를 사용하려면 `RecursiveTask` 또는 `RecursiveAction` 클래스를 상속받아 작업을 나누고 병합하는 방식으로 병렬 처리를 구현한다.

```java
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class SumTask extends RecursiveTask<Integer> {
    private final int[] arr;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10;

    public SumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            // 작은 작업일 경우 직접 계산
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        } else {
            // 큰 작업일 경우 두 개로 나눠서 병렬 처리
            int mid = (start + end) / 2;
            SumTask leftTask = new SumTask(arr, start, mid);
            SumTask rightTask = new SumTask(arr, mid, end);
            leftTask.fork(); // 비동기로 실행
            return rightTask.compute() + leftTask.join(); // 결과 병합
        }
    }
}

public class ForkJoinExample {
    public static void main(String[] args) {
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1; // 1부터 1000까지 숫자 배열
        }

        ForkJoinPool pool = new ForkJoinPool();
        SumTask task = new SumTask(array, 0, array.length);

        int result = pool.invoke(task); // 병렬로 작업 실행 및 결과 병합
        System.out.println("합계: " + result);
    }
}

```

위 코드에서는 배열의 합을 구하는 작업을 `SumTask`라는 클래스로 분할하고, 이 작업을 병렬로 처리한 후 최종 결과를 합치는 방식을 사용한다.

### 장점

- **큰 작업을 작은 단위로 효율적으로 처리**: 작업을 자동으로 분할하고 병합하는 기능을 통해 큰 작업을 빠르게 병렬 처리할 수 있다.
- **성능 최적화**: 내부적으로 Java의 **Fork/Join Pool**을 사용하여 작업을 효율적으로 관리한다.

### 단점

- **복잡한 작업 흐름**: 작업의 분할과 병합에 대한 구체적인 구조를 직접 관리해야 하므로, 작업이 복잡할 경우 코드도 복잡해질 수 있다.

`Stream API`는 Java 8에서 도입된 데이터 처리 도구로, 컬렉션이나 배열 등의 데이터를 선언적으로 처리할 수 있는 강력한 기능을 제공한다. `Stream`은 **일반 스트림**과 **병렬 스트림(parallel stream)**으로 나눌 수 있는데, 병렬 스트림을 사용하면 데이터를 자동으로 여러 스레드를 사용해 병렬로 처리할 수 있다.

### 병렬 스트림(parallel stream)

병렬 스트림은 **내부적으로 Fork/Join Framework**를 사용하여 데이터를 병렬로 처리한다. 개발자는 스레드 관리나 작업 분할에 대해 신경 쓰지 않고, 스트림에 `parallel()` 메서드를 호출하는 것만으로 병렬 처리를 쉽게 구현할 수 있다.

### 병렬 스트림을 사용하는 방법

병렬 스트림을 사용하려면, `Stream`에 대해 `parallelStream()`을 호출하거나, 일반 스트림에서 `parallel()` 메서드를 호출하면 된다.

### 1. `parallelStream()`을 사용하는 방법

```java
import java.util.Arrays;

public class ParallelStreamExample {
    public static void main(String[] args) {
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        // 병렬 스트림을 사용하여 배열의 합 계산
        int sum = Arrays.stream(array)
                        .parallel()  // 병렬 스트림으로 전환
                        .sum();

        System.out.println("합계: " + sum);  // 출력: 500500
    }
}

```

### 2. 일반 스트림에서 `parallel()` 호출

```java
import java.util.List;
import java.util.Arrays;

public class ParallelStreamExample2 {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date");

        // 병렬로 처리하여 각 단어를 대문자로 변환
        words.stream()
             .parallel()  // 병렬 스트림으로 전환
             .map(String::toUpperCase)
             .forEach(System.out::println);
    }
}

```

위 코드에서는 `stream()`으로 일반 스트림을 생성한 후 `parallel()`을 호출해 병렬로 작업을 처리한다. `parallel()`을 호출하면 내부적으로 데이터는 여러 스레드에 나눠 처리되며, 각 단어를 병렬로 대문자로 변환한다.

### 병렬 스트림의 특징

1. **Fork/Join Framework 사용**:
    - 병렬 스트림은 내부적으로 **Fork/Join Framework**를 사용한다. 데이터를 자동으로 작은 조각으로 분할하고, 여러 스레드에서 병렬로 처리한 후 결과를 한다.
2. **자동 병렬화**:
    - `parallelStream()` 또는 `parallel()`을 호출하면 Java가 자동으로 데이터를 병렬로 처리한다. 개발자가 직접 스레드를 관리하거나 동기화 문제를 처리할 필요가 없다.
3. **효율성**:
    - 데이터의 크기가 크거나 시간이 많이 걸리는 작업에 대해 병렬 스트림을 사용하면 성능이 크게 향상될 수 있다. 하지만 데이터 크기가 작거나 처리 시간이 짧은 경우, 병렬 스트림을 사용하는 것이 오히려 성능을 저하시킬 수 있다. 병렬화의 오버헤드가 있기 때문이다.
4. **순서 보장**:
    - 일반적으로 병렬 스트림에서는 작업의 **순서가 보장되지 않기 때문에**, 데이터의 순서가 중요한 경우 병렬 스트림을 사용할 때 주의가 필요하다. 예를 들어, 순서를 유지하려면 `forEachOrdered()`를 사용할 수 있다.

```java
// 순서를 유지하면서 병렬로 처리
words.stream()
     .parallel()
     .map(String::toUpperCase)
     .forEachOrdered(System.out::println);  // 순서 보장

```

1. **비동기 처리**:
    - 병렬 스트림은 데이터를 병렬로 처리하면서 여러 스레드에서 작업을 동시에 수행하므로, 작업을 빠르게 처리할 수 있다. 하지만 복잡한 데이터 흐름에서는 스레드 동기화 이슈가 발생할 수 있으므로 주의가 필요하다.
    

### 병렬 스트림의 성능 고려 사항

- **CPU 코어 수**: 병렬 스트림의 성능은 CPU 코어 수에 따라 달라진다. 병렬 작업을 처리할 스레드의 수가 코어 수보다 많아지면 오히려 성능이 저하될 수 있다.
- **데이터 크기**: 작은 데이터에 병렬 스트림을 사용하는 것은 오히려 성능 저하를 유발할 수 있으므로, 데이터 크기에 맞게 병렬 처리를 사용하는 것이 중요하다.
- **메모리 오버헤드**: 병렬 처리로 인해 여러 스레드가 동시에 작업을 처리하면서 메모리 사용량이 증가할 수 있다.