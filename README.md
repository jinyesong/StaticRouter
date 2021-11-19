# StaticRouter

<br>

## 💻 ip 설정 방법

1. window 검색창에 네트워크 보기 검색
2. ethernet() 더블클릭
3. 속성 선택
4. 인터넷 프로토콜 버전 4(TCP/IPv4) 더블클릭
5. 다음 IP 주소 사용 클릭 후 원하는 IP 주소, 서브넷 마스크, 기본 게이트웨이 입력
   <br>

## ▶ 동작 방법

- Routing Table을 검색해서 Next Hop IP를 찾는다. 이때, 최종 목적지 Host network에 대한 정보가 Routing Table에 등록되어 있지 않으면 패킷을 Drop.
- Next Hop Ip 에 대한 MAC address를 ARP Table에서 찾는다. ARP Table에 Entry가 없으면 ARP Request를 보내서 MAC 주소를 알아온다.
- Router는 수신된 패킷의 목적지가 자신의 Interface LAN에 속한 경우 패킷 직접 전달(우리가 구현할 것)

### h1과 h2 통신

1. h1의 Routing table 검색
2. Route가 있는지 확인(routing 조건)
3. ARP Cache Table 확인 (MAC 주소 있으면 바로 Router1로 패킷 전달, 없으면 Request)  
   -> ARP Request를 보낼 때, 길을 아는 라우터 IP를 destination으로 넣어서 보낸다.
   -> ARP Request를 받은 라우터는 본인의 MAC주소를 넣어서 Reply.
4. h1은 Router1로 패킷 전달
5. Router 1은 받은 패킷의 IP destination 확인
6. Router 1의 Routing Table을 확인해서 routing 조건 확인 후 route 찾기
7. PC 0 에 대한 ARP Table 확인 후 MAC 주소 있으면 바로 PC 0으로 패킷 전달하고 없으면 Request를 통해서 구한다.

### Routing Table step

1. 원하는 host address가 있는지 찾는다
2. 원하는 network address가 있는지 찾는다
3. default entry가 있는지 찾는다.

### Routing Table field

1. Destination
2. Net mask  
   -class C 255.255.255.0
3. Gateway  
   -Interface를 통해 패킷이 가야 할 목적지 혹은 대상  
   -자신의 NIC 주소, 호스트의 주소, 로컬 서브넷의 라우터 주소
4. Metric
   -Destination까지의 hop 수(연결된 링크 상에 거쳐 가야 할 라우터 수)
5. Flags  
   -U(UP): 라우터 동작 여부  
   -G(Gateway) : 목적지가 다른 네트워크에 존재  
   -H(Host-specific) : 목적지가 Host. H가 체크되어 있지 않으면 route를 네트워크로, 목적지는 network id & subnet id 로 설정되어야한다.

### Router에서 패킷 수신 시 동작
