# 네트워크 기초

- 네트워크 개념
- 네트워크 처리량
- 트래픽
- 대역폭
- RTT

<br>

# 1. 네트워크란 ?

<aside>
💡 노드(node)와 링크(link)가 서로 연결되어 있으며 리소스를 공유하는 집합을 의미! 
<Br>
노드 : 서버, 라우터, 스위치 등 네트워크 장치 <Br>
링크 : 유선 또는 무선과 같은 연결매체 (와이파이나 LAN)
<br>
</aside>
<br>

![Untitled](/Network/img/Node.png)

⇒ 다음과 같은 사진이 있다면 

     저희가 직접 www.naver.com을 검색하면  Naver 사이트로 연결을 

     해주잖아요. 그 때 서로 연결된 주체들이 `node`인 거고 , 

      그 두 개의 node를 연결해주는 게 `link` 인 거에요 !

<Br>

# 2. 트래픽이란 ?

<aside>
💡 => 특정 시점에 `link` 내의 흐르는 데이터의 양
       즉, 서버를 통해 최종 사용자에게 전달된 데이터의 양을 말함.

        ex) 서버에 저장한 파일(문서, 이미지, 동영상) 등을 

              클라이언트(사용자)가 다운로드 시 발생되는 데이터의 누적량.

⚠️ 트래픽과 처리량을 헷갈릴 수 있는데

- 트래픽이 많아졌다 = 흐르는 데이터가 많아졌다.
- 처리량이 많아졌다 = 처리되는 트래픽이 많아졌다.

로 이해하시면 됩니당. 
<br><br>

✅ 단위 : bps를 쓴당 (bits per second)(초당 전송 또는 수신되는 비트 수)

✅ 트래픽 계산 예시 

❓ 10MB 짜리 영상을 10명이 다운로드 받았다면 

⇒  `트래픽`은 (10MB * 10 해서) `100MB`

✅트래픽 급증을 대비하기 위해 사용할 수 있는 전략 

- 로드 밸런싱
 ⇒ 트래픽을 여러 서버에 분산시켜 부하를 줄임

- 오토 스케일링
⇒ 클라우드 환경에서 트래픽 변화에 따라 
     서버 인스턴스를 자동으로 늘리거나 줄임
- 캐싱 
⇒ 자주 조회되는 데이터를 캐시하여 서버 부하를 줄임

”위의 개념들에 대해서는 추후 다루겠음”
</aside>

<Br><Br>

# 3. 처리량(throughput)

<aside>
💡 => 링크 내에서 성공적으로 전달된 데이터의 양, 
       보통 얼만큼의 트래픽을 처리했는지를 나타냄.

`많은 트래픽을 처리한다 == 많은 처리량을가진다.`

✅ 단위 : bps를 씀 (bits per second) 
(초당 전송 또는 수신되는 비트 수)

처리량은 사용자들이 많이 접속할 때 마다 커지는 
트래픽(특정 시점에 link내에 흐르는 데이터 양)이에용.

〽️ 처리량에 영향을 주는 것들.
- 네트워크 장치 간의 대역폭, 
- 네트워크 중간에 발생하는 에러, 
- 장치의 하드웨어 스팩에 영향을 받음.

</aside>

<Br><Br>

# 4. 대역폭

<aside>
💡 => 초당 처리할 수 있는 데이터의 양. 
       즉,  주어진 시간 동안 네트워크 연결을 통해 흐를 수 있는 최대 비트 수 (최대 트래픽)<br><Br>
       !!트래픽과 다르게 시간 개념이 추가됐어욤.

![https://postfiles.pstatic.net/MjAyNDA2MTVfODEg/MDAxNzE4NDIzODQzMjgx.ssINQzuVhGjRaiP>H01FZghwJhz26_yEdMbLW7lNb8Gog.C_ncGN_ixuFB28_LtYqX9AyrUl1HH9UOpdxM2r7gx6wg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfODEg/MDAxNzE4NDIzODQzMjgx.ssINQzuVhGjRaiPH01FZghwJhz26_yEdMbLW7lNb8Gog.C_ncGN_ixuFB28_LtYqX9AyrUl1HH9UOpdxM2r7gx6wg.PNG/image.png?type=w580)

예를 들어 이런 고속도로가 있다고 하면

왼쪽은 동시에 2대 처리, 오른쪽은 동시에 4대 처리가 가능하잖아요

그렇게 주어진 시간 동안 흐를 수 있는 최대 트래픽을 말하는 거죠.

대역폭이 높을수록 사용자에게 빠른 서비스를 제공할 수 있어욤.

대략적인 최대 동시 접속자 수 유추의 척도가 되구욤

<br>
✅ 단위  

⇒ 단위는 bps로 (bit per second) 

     즉, 초당 bit 단위의 데이터 처리량을 말합니당. 

```jsx
대역폭 = (용량 * 사용자수 * 8) / 처리 시간 = bps 
*참고로 8을 곱하는 이유는 Bytes에서 bit로 변환하기 위함*.
```

✅그럼 최대 동접자수 계산해볼게욤, 

Q. 100Mbps라는 대역폭을 가진 서버가있고  한 사용자당 100kbps로 동영상 파일을 요청한다면 최대 동접자수는 ?!

```jsx
먼저, 단위를 맞추기 위해 Mbps를 kbps로 변환 
1Mbps = 1000kbps 이므로, 100Mbps는 100 * 1000 = 100,000kbps임. 

따라서, 100,000kbps / 100kbps 하면 
```

=> 약 1000명의 최대 동접자수를 가짐.

![https://postfiles.pstatic.net/MjAyNDA2MTVfNzIg/MDAxNzE4NDI0MDQ3NzIw.THcuSQghUVMvDwccjygWleqGZGxKe2x6WSMvFZtq7Zcg.sWYzC76r5BzhKforyr0H8TmUXok7ZO82nepoRZelR7sg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfNzIg/MDAxNzE4NDI0MDQ3NzIw.THcuSQghUVMvDwccjygWleqGZGxKe2x6WSMvFZtq7Zcg.sWYzC76r5BzhKforyr0H8TmUXok7ZO82nepoRZelR7sg.PNG/image.png?type=w580)

</aside>

<Br><br>
# 5. RTT (Round Trip Time)

<aside>
💡 `왕복 지연시간`은 신호를 전송하고 해당신호의 수신확인에 걸린 시간을 걸린 값이자

어떤 메시지가 두 장치 사이를 왕복하는 데 걸린 시간 !

⇒ ***네트워크 연결의 속도와 안전성을 진단*** 할 때 주로 사용합니당.

✅ RTT에 영향을 주는 요소들 

⇒ 무선, 유선이냐<Br>
⇒ 근거리, 원거리냐<br>
⇒ 트래픽 양 

등등

 <br>
 RTT값 확인법 :

![https://postfiles.pstatic.net/MjAyNDA2MTVfNzYg/MDAxNzE4NDI0MTQyNDcy.HDsmzVvp6e5JJosELeuS-FJwnITsvIMHAilmqEHYkMgg.jGEE04TVsgs5vkxunX75zclaGK_O0eFaNCDHmP6H5Esg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfNzYg/MDAxNzE4NDI0MTQyNDcy.HDsmzVvp6e5JJosELeuS-FJwnITsvIMHAilmqEHYkMgg.jGEE04TVsgs5vkxunX75zclaGK_O0eFaNCDHmP6H5Esg.PNG/image.png?type=w580)

cmd 창에 ping google.com 쳐보면

![https://postfiles.pstatic.net/MjAyNDA2MTVfODYg/MDAxNzE4NDI0MjE0NjI0.MxBurjjej0z5u0eCKrE_jwCHX9C2pP7Yxm-dOEHSDScg.fDcjNZ1uxpNiT3UBv5YW7XXtfdHPqYSWwd19CNWea7wg.PNG/image.png?type=w580](https://postfiles.pstatic.net/MjAyNDA2MTVfODYg/MDAxNzE4NDI0MjE0NjI0.MxBurjjej0z5u0eCKrE_jwCHX9C2pP7Yxm-dOEHSDScg.fDcjNZ1uxpNiT3UBv5YW7XXtfdHPqYSWwd19CNWea7wg.PNG/image.png?type=w580)

rtt값을 볼 수 있어용

</aside>