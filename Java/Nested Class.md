# Nested 클래스



## Nested 클래스란?


> 클래스 안의 클래스


<br><br>

## Nested 클래스의 종류



1. Static nested 클래스
2. Inner 클래스 (내부 클래스)
3. anonymous 클래스 (익명 클래스)

<br><br>

## Nested 클래스를 사용하는 이유



- 한곳에서만 사용되는 클래스를 논리적으로 묶어서 처리할 필요가 있을 때
- 캡슐화가 필요할때 (내부 구현을 감추고 싶을때)
- 소스의 가독성과 유지보수성을 높이고 싶을떄

<br><br>

## Static Nested 클래스



클래스안에 static 클래스를 선언

```java
public class OuterOfStatic {
		
    int outerValue = 0;

    static class StaticNested{ // Static Nested 클래스 선언
        private int value = 0;

        public int getValue(){
            return value;
        }

        public void setValue(int value){
            this.value = value;
        }
    }
}

```

내부 클래스는 감싸고있는 외부 클래스의 어떤 변수도 접근 가능하다. private으로 선언된 변수까지도 가능하다.
하지만 Static Nested 클래스는 static으로 선언 되었기 때문에 불가능하다.

<br>

객체 생성

```java
OuterOfStatic.StaticNested staticNested = new OuterOfStatic.StaticNested();
```

감싸고있는 클래스 이름 뒤에 .(점)을 찍고 객체를 생성할수있다.

객체를 생성한 후에는 일반 객체를 사용하는 방법과 같다.

<br>

### Static Nested 클래스 특징

- Static Nested 클래스에서 외부 클래스의 변수에 접근할 수 없다.

```java
OuterOfStatic.StaticNested staticNested = new OuterOfStatic.StaticNested();
staticNested.outerValue; // 불가능
```

<br>

- 외부 클래스에서 Static Nested 클래스의 변수에 접근할 수 없다.

```java
OuterOfStatic outerOfStatic = new OuterOfStatic();
outerOfStatic.setValue(3); // 불가능

```

<br><br>

## 내부 클래스



내부 클래스와 Static Nested 클래스의 차이는 static을 붙이는냐 마느냐의 차이만 있다.

```java
public class OuterOfInner {
    class Inner{
        private int value = 0;

        public int getValue(){
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}

```

<br>

하지만 객체를 생성한 다음 사용하는 방법에는 차이가 있다.

```java
OuterOfInner outer = new OuterOfInner();
OuterOfInner.Inner inner = outer.new Inner();
inner.setValue(3);
```

Inner 클래스의 객체를 생성하기 전에 먼저 Inner 클래스를 감싸는 OuterOfInner 클래스의 객체를 먼저 생성하고 생성된 객체를 통해 Inner클래스의 객체를 생성해야한다.

하나의 클래스에서 어떤 공통적인 작업을 수행하는 클래스가 필요한데 다른 클래스에서는 그 클래스가 전혀 필요없을 때 내부클래스를 사용하여 캡슐화한다.

```
💡 GUI에서 가장 많이 사용된다. 어떤 버튼을 눌렀을때 발생하는 이벤트들은 모두 상이하기 때문에 별도의 클래스를 만드는 것보다는
 내부 클래스를 만드는것이 훨씬 편하다.
```

<br><br>

## 익명 클래스



익명을 Anonymous라 한다. 자바에서의 익명 클래스는 이름이 없는 클래스이다.

GUI 에서는 내부 클래스보다 익명 클래스를 사용하면 더욱 편리하다.

```java
public class MagicButton {
    public MagicButton(){}
    
    private EventListener listener;
    
    public void setListener(EventListener listener){
        this.listener = listener;
    }
    
    public void onClickProcess(){
        if(listener != null){
            listener.onClick();
        }
    }    
}

```


<br>

`EventListener`라는 것을 매개변수로 받아 인스턴스 변수에 저장한다.

```java
public interface EventListener {
    public void onClick();
}
```

<br>

`EventListener` 인터페이스이다.

```java
public class MagicButtonListener implements EventListener{

    @Override
    public void onClick() {
        System.out.println("Magic Button Clicked!");
    }
}
```

<br>

`EventListener`를 구현한 `MagicButtonListener`

```java
public class AnonymousSample {
    public static void main(String[] args) {
        AnonymousSample anonymousSample = new AnonymousSample();
        anonymousSample.setButtonListener();
    }

    public void setButtonListener(){
        MagicButton magicButton = new MagicButton();
        **MagicButtonListener magicButtonListener = new MagicButtonListener();**
        magicButton.setListener(magicButtonListener);
        magicButton.onClickProcess();
    }
}    
```

<br>

일반적으로 `setListener`메서드를 실행하기 위해선 `EventListener`를 구현한 객체를 생성해 매개변수로 넘겨주었다. 여기서 익명 클래스를 사용하면 굳이 `EventListener`를 구현한 `MagicButtonListener` 를 생성하지 않아도 된다.

```java
public class AnonymousSample {
    public static void main(String[] args) {
        AnonymousSample anonymousSample = new AnonymousSample();
        anonymousSample.setButtonListener();
    }

    public void setButtonListener(){
        MagicButton magicButton = new MagicButton();
        magicButton.setListener(new EventListener() {
            @Override
            public void onClick() {
                System.out.println("Magic Button Clicked!");
            }
        });
        magicButton.onClickProcess();
    }
}

```

`setListener()`를 보면 `new EventListener()` 생성자를 호출한 후 바로 `onClick()`를 구현하였다.

이렇게 구현하것이 익명 클래스이다.

<br>

하지만 이렇게 구현했을 때에는 클래스 이름도 없고, 객체 이름도 없기 때문에 재사용 하려면 다음과 같이 객체를 생성하면 된다.

```java
public void setButtonListener(){
    MagicButton magicButton = new MagicButton();

    EventListener listener = new EventListener() {
        @Override
        public void onClick() {
            System.out.println("Magic Button Clicked!");
        }
    };
    
    magicButton.setListener(listener);
    magicButton.onClickProcess();
}
```

<br><br>

## Nested 클래스의 특징

```java
public class NestedValueReference {
    public int publicInt = 0;
    protected int protectedInt = 1;
    int defaultInt = 2;
    private int privateInt = 3;
    static int staticInt = 4;
    
    static class staticNested { // Static Nested 클래스
        public void setValue(){
            staticInt = 14;
        }
    }
    
    class Inner{  // 내부 클래스
        public void setValue(){
            publicInt = 20;
            protectedInt = 21;
            defaultInt = 22;
            protectedInt = 23;
            staticInt = 24;
        }
    }
    
    public void setValue(){
        EventListener listener = new EventListener() { // 익명 클래스
            @Override
            public void onClick() {
                publicInt = 30;
                protectedInt = 31;
                defaultInt = 32;
                protectedInt = 33;
                staticInt = 34;
            }
        };
    }
}

```

클래스 별 참조 가능한 변수들이다.

<br>

Static Nested 클래스는 감싸고 있는 클래스의 static 변수만 참조가능하다.
내부 클래스와 익명 클래스는 감싸고있는 클래스의 어떤 변수라도 참조할수있다.

```java
public class ReferenceAtNested {
    static class StaticNested{
        private int staticNestedInt = 99;
    }
    
    class Inner{
	      private int innerValue = 100;
    }
    
    public void setValue(int value){
        StaticNested nested = new StaticNested();
        nested.staticNestedInt = value;
        Inner inner = new Inner();
        inner.innerValue = value;
    }
}
```

반대로 감싸고있는 클래스는 이렇게 각 클래스의 객체를 생성한 후 참조 하는 것이 가능하다.
그 값이 private이라 할지라도 모두 가능하다.