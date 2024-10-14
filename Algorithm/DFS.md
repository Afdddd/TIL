# DFS


깊이 우선 탐색(Depth-First-Search)은 그래프 완전 탐색 기법 중 하나이다. **깊이 우선 탐색**은 한 경로를 가능한 깊이까지 탐색한 후, 더 이상 갈 곳이 없으면 다시 돌아가면서 다른 경로를 탐색하는 방식이다.

재귀함수와 스택 자료구조를 이용해서 구현 할 수 있다. 시간 복잡도는 

$$
O(V+E)
$$

<i>V : 노드 수 , E : 에지 수</i>

<br>

재귀 함수를 이용하므로 스택 오버플로에 유의해야 한다.

<br><br>

### DFS의 핵심 이론

<br>


DFS는 한 번 방문한 노드를 다시 방문하면 안된다. 방문 여부를 체크할 배열이 필요하며, 그래프는 인접 리스트로 표현한다. 

인접리스트란?

![image.png](/img/dfs(1).png)

**인접 리스트(Adjacency List)** 는 그래프를 표현하는 방법 중 하나로, 각 노드(정점)와 그 노드에 연결된 다른 노드들을 리스트 형태로 저장하는 방식이다.


<br><br>

### 1. DFS를 시작할 노드를 정한 후 사용할 자료구조 초기화

DFS를 위해 필요한 초기 작업은 인접 리스트로 그래프 표현하기, 방문 배열 초기화하기, 시작 노드 스택에 삽입이다. 스택에 시작 노드를 1로 삽입할 때 해당 위치의 방문 배열을 체크하면 T,F,F,F,F,F가 된다.

![Untitled](/img/dfs(2).png)

<br><br>

### 2. 스택에서 노드를 꺼낸 후 꺼낸 노드의 인전 노드를 다시 스택에 삽입하기

이제 `pop`을 수행하여 노드를 꺼낸다. 꺼낸 노드를 탐색 순서에 기입하고 인전 리스트의 인접노드를 스택에 삽입하며 방문 배열을 체크한다. 방문배열은 T,T,T,F,F,F가 된다.

![Untitled](/img/dfs(3).png)

<br><br>

### 3. 스택 자료구조에 값이 없을 때까지 반복한다.

앞선 과정을 스택 자료구조에 값이 없을 떄까지 반복한다. **이때 이미 다녀간 노드는 방문 배열을 바탕으로 재삽입하지 않는것이 핵심**이다.

![Untitled](/img/dfs(4).png)

```
1 -> 3 -> 4 -> 6 -> 2 -> 5
```

<br>

스택에서 3을 꺼내며 탐색 순서에 기록하고 인접 노드 4를 스택에 삽입하며 방문 배열에 체크한다. 4를 꺼내며 탐색 순서에 기록하고 6을 삽입하며 방문 배열에 체크한다. 6을 꺼내며 참색 순서에 기록하고 6과 인접한 노드는 없으므로 추가 삽입은 없다. 계속해서 스택에서 2를 꺼내며 탐색 순서를 기록하고 2와 인접한 5,6을 삽입하기 위해 본다. 이 때 6은 방문 배열에 T로 체크되어있으므로 5만 삽입한다. 이 과정을 스택이 빌 떄까지 진행한다.

<br><br>

### Java로 DFS 구현

이제 Java로 인접리스트와 스택을 사용해서 DFS를 구현해보자.

```java
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DFS {

  static class Graph{
    private final int v; // 노드 개수
    private final List<List<Integer>> adjList; // 인접 리스트

    // 그래프 생성자
    public Graph(int v){ 
      this.v = v+1; // 노드가 1번 부터 시작하므로 +1
      adjList = new ArrayList<>();
      for(int i=0; i<this.v; i++){
        adjList.add(new LinkedList<>());
      }
    }

    // 간선 추가
    void addEdge(int v, int w){
      adjList.get(v).add(w); // v->w의 간선 추가
      adjList.get(w).add(v); // 무방향 그래프라면 w->v 의 간선 추가
    }

    // DFS(깊이 우선 탐색) - 스택 사용
    public void DFS(int start){
      boolean[] visited = new boolean[v]; // 방문 배열
      Stack<Integer> stack = new Stack<>();

      stack.push(start); // 시작 노드를 스택에 넣음

      // 스택이 비어있지 않다면
      while(!stack.isEmpty()){ 
        int node = stack.pop(); // 스택에서 노드를 꺼냄

        // 노드를 방문하지 않았다면
        if(!visited[node]){
          System.out.print(node + " "); // 노드 방문 (출력)
          visited[node] = true; // 방문 처리

          // 현재 노드에 인접한 모든 노드를 스택에 넣음
          for(int neighbor: adjList.get(node)){
            if(!visited[neighnor]){ // 방문하지 않은 노드만 push
              stack.push(neighbor);
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    // 5개의 노드를 가진 그래프 생성
    Graph graph = new Graph(6);


    graph.addEdge(1, 2);
    graph.addEdge(1, 3);
    graph.addEdge(2, 5);
    graph.addEdge(2, 6);
    graph.addEdge(3, 4);
    graph.addEdge(4, 6);

    graph.DFS(1); // 출력 결과 : 1 3 4 6 2 5
  }
}

```

**Graph 클래스** : 

- `V`: 노드(정점)의 개수
- `adjList`: 각 노드에 연결된 인접 노드를 저장하는 인접 리스트
- `addEdge`: 두 노드 간의 간선을 추가하는 메소드로, 무방향 그래프이므로 양방향으로 간선을 추가

**DFS 메서드**:

- `visited[]`: 이미 방문한 노드를 체크하기 위한 배열
- `stack`: 스택을 사용하여 깊이 우선으로 노드를 탐색
- 탐색을 시작한 후, 스택이 빌 때까지 노드를 꺼내고 그 노드와 연결된 다른 노드를 계속 스택에 넣는다.

<br><br><br><br>
출처 : Doit!알고리즘코딩테스트withJAVA