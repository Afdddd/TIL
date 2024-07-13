# 자바 컴파일 과정

---

<span style="color : pink;">핵심 키워드</span> : 

1. class loader 실행 방식 <Br>
 (여기서는 class loader의 종류까지는 언급 안합니다. JVM 글에서 언급할거에요)
2.  jvm <br>
(메모리 구조까지는 자세하게 언급 안하고 이해를 위한 예시만. JVM 글에서 언급할 예정)

1. compiler/interpreter
2. 실행 엔진 
3. 심볼릭 레퍼런스, 다이렉트 레퍼런스
4. 컴파일 과정 후에 간단한 예시로 각 단계에서 어떻게 실행되는 지 보여줌
5. 간단한,, 소스코드 vs 컴파일 코드 vs 바이너리 코드 차이점 

---
<Br><Br>

열어분부분  Java는 OS에 독립적인 특징을 가지고 있습니당. <br>
그게 가능한 이유는 JVM(Java Virtuttal Machine) 덕분이죰. 

    - 🧔 죄송하지만... 저는 os에 독립적이라는 게 뭔지 몰라요 글고 그게 왜 JVM 덕인데
    
os에 독립적이라는 것은 어떤 운영체제 (mac, window, linux 등등) 에서 Java 프로그램을 실행시켜도 <br> 다 `똑같이` 실행이 되는 것을 이야기 합니당. 
    이게 JVM 덕분에 가능하다는 것인데…
    
- 각 운영체제(Windows, macOS, Linux 등)에는 해당 운영체제에 맞는 JVM이 설치되어 있어욤.<br>
- JVM은 운영체제와 하드웨어에 맞게 구현된 소프트웨어입니당.
- 바이트코드 파일(밑에 설명 나와요)을 JVM이 실행할 때, JVM은 이 바이트코드를 해당 운영체제와 하드웨어에서 실행 가능한 기계어로 해석하여 실행합니다.
<br><br>



        ❓ 아니 그럼 의문인 게 다른 언어는 그게 안돼 ?
    그렇더라구요. 
    
        1️⃣ os에 독립적인 언어들  
    
        `python , c#, JavaScript, Ruby, Go 등등`
    
        2️⃣ os에 독립적이지 못한 언어들 
        `c/c++, Swift 등등 => 더 있는데 모르는 언어는 쓰기 싫다.`
    
    그럼 얘네는 왜 독립적이지 못하는가 <br>
    ⇒ 주로 언어의 설계 철학, 성능 요구 사항 등에 있당. 
    
        c/c++은 성능과 하드웨어 제어를 최우선을 설계 되었어욤.
        이 언어들은 그래서 시스템 프로그래밍에 주로 사용되고 Java 처럼 
        JVM이 운영체제에 접근하는 게 아니라 언어 자체에서 운영체제나 하드웨어에 
        직접 접근할 수 있게 합니당. 그렇기 때문에 실행속도가 Java 보다 더 빨라요. 
    
    </aside>
    

그렇다면 JVM의 어떤 기능 덕분에 OS에서 독립적으로 실행시킬 수 있는 것인지 <br>자바 컴파일 과정을 통해 알아봅시당. 

![Untitled](/Java/img/JavaCompile(1).png)

![Untitled](/Java/img/JavaCompile(2).png)

<br><br>    

# Java의 컴파일 순서

    1️⃣ 김민제 개발자가 Java 소스 코드를 작성해서 개발을 합니당 (.java) 

    2️⃣ 자바 컴파일러 (Java Compiler인 Javac)가 Java 소스파일을 컴파일 합니당.  

    (이 때 나오는 파일이 자바 바이트 코드인 .class 파일입니당) 

    아직 컴퓨터는 읽을 수 없고 자바 가상 머신만 이해할 수 있는 코드에요.
    바이트 코드의 각 명령어는 1바이트 크기의 Opcode와 추가 피연산자로 이루어져 있습니당. 

<aside>
💡 * 참고 *<br>
⇒ Opcode : 바이트코드의 명령어를 나타내는 1바이트 크기의 코드 ( 덧셈, 곱셈, 메모리 로드, 메모리 저장 등을 지시)<br>
⇒ 피연산자 : Opcode가 필요로 하는 추가적인 데이터 <br><br>
예를들어, Opcode가 두 데이터를 더하라는 거면 피연산자는 더할 두 숫자임.<br>

<Br>
</aside>

    3️⃣ 컴파일된 바이트 코드를 JVM의 클래스 로더(Class Loader)에게 전달합니당 

    ( 아직 .class 파일인거죵) 

    4️⃣ Class Loader는 동적로딩 (Dynamic Loading)을 통해 필요한 클래스들을 로딩 및 링크하여 
    Runtime Data area 즉, JVM의 메모리에 올립니당. 

    (이건 다음에 JVM 글에서 얘기할 건데 JVM의 메모리 구조 안에 Runtime Data area가 있고, 그 외에 stack heap 등등 많아요) 

    ⭐4_1. Class Loader의 세부 동작 
            ㄱ. 로드  :  클래스 파일(.class)을 가져와서 JVM의 메모리에 로드함
            ㄴ. 검증 : 자바 언어 명세 (Java Language Specification) 및 JVM 명세에 
            명시된 대로 구성되어 있는지 검사!
            ㄷ. 준비  : 클래스가 필요로 하는 메모리를 할당 (필드, 메소드, 인터페이스 등등
            ㄹ. 분석  : 클래스의 상수 풀 내 모든 심볼릭 레퍼런스를 (클래스명, 메서드 이름, 필드 이름 등) 
            다이렉트 레퍼런스(객체의 진짜 주소값)로 변경
            ㅁ. 초기화  : 클래스 변수들을 적절한 값으로 초기화 (static 필드)

<aside>
💡 * 참고*  <br>
상수풀 : <br>
다음 JVM에서 말할 거긴 한데 
상수 풀에는 다음과 같은 항목이 있습니당. 

    1️⃣ 상수 값 : 숫자 리터럴, 문자열 리터럴 등등 
    2️⃣ 심볼릭 레퍼런스 : 클래스 이름, 메소드 이름, 필드 이름 등

분석 단계 예를 들자면 
Java 코드에서 
`System.out.println(”Hello, Minje”);` 라는 문장을 치면

- `System`: 클래스 이름
- `out`: `System` 클래스의 정적 필드 이름
- `println`: `out` 객체의 메서드 이름
- `"Hello, Min"`: 문자열 리터럴<br>
⇒ 이렇게 여러가지의 심볼릭 레퍼런스를 가지거덩요 ?<Br>
⇒ 분석 단계에서 이제 이런 심볼릭 레퍼런스를 실제 메모리 주소로 변환 한다는 것이죰
</aside>
<br><br>

    5️⃣ 실행엔진 (Execution Engine)은 JVM 메모리에 올라온 바이트 코드들을 
     명령어 단위로 하나씩 가져와서 실행합니당. 
     ⇒ 이 때 실행엔진은 두 가지 방식으로 변경합니답. 
     
     ⭐5_1. 인터프리터 
        ⇒ 바이트 코드 명령어를 하나씩 읽어서 해석하고 실행함. 
             하나하나의 실행은 빠르지만, 전체적인 실행 속도가 느리다는 단점을 가짐. 

             (왜냐면 이미 읽은 것도 다시 새로 한 줄씩 읽어주기 때문입니당)


    ⭐5_2. JIT 컴파일러 (Just-In-Time Compiler)
        ⇒ 인터프리터의 단점을 보완하기 위해 도입된 방식. 
        ⇒ 바이트 코드 전체를 컴파일해서 바이너리 코드(Binary Code)로 변경하고, 

              이후에는 해당 메소드를 더이상 인터프리팅 하지않고 , 

              바이너리 코드로 직접 실행하는 방식. 

하나씩 인터프리팅하여 실행하는 것이 아니라 

바이트 코드 전체가 컴파일된 바이너리 코드를 실행하는 것이라서 

전체적인 실행속도는 인터프리팅 방식보다 빠름 !! 
<br><br>

# 어떻게.. 과정이 이해가 가시나용.. ?

하나의 예시를 들어볼게욤 

```java
public class Example {
    public static int staticVar = 10;
    public int instanceVar = 20;

    public static void main(String[] args) {
        Example example = new Example();
        System.out.println(example.instanceVar);
    }
}
```

⇒ 이거 있자나요 다음과 같은 단계를 거칩니당 

    1️⃣ 개발자 김민제가 Java 코드로 Example 클래스와 내부 method를 실행시켰다 (.java)

    2️⃣ javac가 소스 파일(.java)을 컴파일 해서 (.class 파일이된다) 바이트 코드로 변환 한다.

    ⇒ 이 과정에서 컴파일러가 (javac) 상수 풀(Constant Pool)을 생성한다. 
- 심볼릭 레퍼런스<br>

`Example(클래스 이름)`, 

`staticVar(정적 필드 이름)`, 

`instanceVar(인스턴스 필드 이름)`, 

`main(메소드 이름)` 

`Example.<init>(생성자 이름)` 

등이 심볼릭 레퍼런스로 저장됨)

- 리터럴 값<br>

`10(정적 필드)` , 

`20(인스턴스 필드)`

    3️⃣ 클래스 로딩 : JVM의 Class Loader가 `Example` 클래스를 로드함. 
    4️⃣ 검증 : 클래스 파일이 자바 언어 명세와 JVM 명세에 맞는지 검증 
    5️⃣ 준비 : 클래스가 필요로 하는 메모리를 할당 
      (^^… 메모리 구조는 다음글에 할 거라고 4번째 쓰는거 같긴한데 )

(위의 예시로는, 

`method 영역`  

- `Example` 클래스 자체의 메타데이터 (클래스 이름, 접근 제한자 등)
- 정적 필드(`staticVar`의 값 10)
- 메서드의 바이트코드 (예: `main` 메서드)

`Heap 영역` 

- 객체 인스턴스 (예: `Example` 객체)
- 인스턴스 필드(`instanceVar`의 값 20)

`stack 영역` 

- 메서드 호출 시 생성되는 스택 프레임

      (예: `main` 메서드 호출 시 생성되는 스택 프레임)

- 지역 변수 (`example` 변수와 `args` 배열)

    
<br>

    6️⃣ 분석 : 클래스의 상수 풀 내 모든 심볼릭 레퍼런스를 실제 메모리 주소나 
      런타임 객체에 대한 다이렉트 레퍼런스로 변환.

    7️⃣ 초기화 : 클래스의 정적 초기화 블록과 정적 필드를 적절한 값으로초기화


##*추가 설명으로 <br>소스코드 vs 자바 바이트 코드 vs 바이너리 코드 해봅니당. <Br><br>

- **소스 코드 (Source Code)**:
    - 인간이 읽을 수 있는 고수준 언어(예: Java, C++)로 작성된 코드
    
    예: 
    
    ```java
    public class HelloWorld { 
       public static void main(String[] args) { 
          System.out.println("Hello, World!"); 
       } 
    }
    ```
    

- **자바 바이트코드 (Java Bytecode)**:
    - 자바 컴파일러(javac)가 소스 코드를 컴파일하여 생성하는 중간 단계의 코드.
    - 예: `.class` 파일에 저장되며, JVM이 이해할 수 있는 명령어로 구성됨.
    - 바이트코드는 플랫폼 독립적이기 때문에, 다양한 운영체제에서 동일하게 실행될 수 있습니다.
    
- **바이너리 코드 (Binary Code)**:
    - 컴퓨터의 CPU가 직접 이해하고 실행할 수 있는 기계어 코드
    - 0과 1로 이루어진 명령어로, 매우 낮은 수준의 코드.
    - 예: `10101000 00000001`

---

[https://gyoogle.dev/blog/computer-language/Java/컴파일 과정.html](https://gyoogle.dev/blog/computer-language/Java/%EC%BB%B4%ED%8C%8C%EC%9D%BC%20%EA%B3%BC%EC%A0%95.html) 

감사합니다감사합니 ㄷ