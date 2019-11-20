[TOC]

<br>

# Criteria

## 1. Basic Function

*( Make 3 Functions out of 4 ) == 50*

### 1) To-do List

- DB활용하기
- SpeachToText 활용해서 메모 쓰기
- 날짜가 지나면 자동 이일되는 시스템 구현하기
- 필요기능
  1. 투두리추가
     - 날짜 선택, 시간선택 -- 알림기능 연동, Fragment로 날짜와 시간 선택창 들어가기
     - 중요도 선택
     - 음성버튼 구현
  2. 투두리검색
     - 필터 : 날짜, 시간, 중요도
     - 음성으로 검색
  3. 투두리수정
     - fragment 활용하기 -- 검색창과 같은 창에서 수행하기
     - 터치액션리스너 이용하기
     - 삭제기능 같이 구현하기

### 2) News Viewer

- [Related Application : Respresso](https://102smart.tistory.com/entry/레스프레소-Respresso-주식펀드경제뉴스-소식을-받는-어플?category=600890)
- Topic : Stock, (Keyword > related news in 5 different newspaper)
- [Stock] Data 저장 구현해서 1주일간 추이도 가져오면 좋겠다 
- XML parsing해서 리스트뷰로 뉴스타이틀 표출하기 -> 그 이후는 링크이동해도 된다.
- AsyncTask 이용하기
- 신문사는 4개사 이상으로 4개 탭(Topic) 이상으로 
- 광고-free 신문
- 필요기능
  1. Topic별 정렬기능
  2. 핵심 뉴스만 가져오는 기능 --> 자세히보기 --> 링크타고 해당 신문사 링크로 이동
  3. 비교 컬렉션 (각 신문사별 같은 토픽에대한 핵심비교)
  4. 나만의 컬렉션 (DB사용) - 북마크 사용 및 폴더 사용으로 컬렉션 관리

### 3) Alarm Manager (X)

### 4) Game (RatCatcher변형 - 좀비게임2)

- 게임 종류 : 좀비 2탄
- 좀비 종류 : 3가지 (속도, 색깔, HP, 공격력 다름)
- 좀비가 점점 다가오는데, 나를 때리기 전에 좀비를 죽이는 게임

<br>

## 2. Extra Function

*Using ( NFC | Bluetooth ) == 40*

- Beacon 활용해서 일정거리이내에 접근시 데이터 푸시해주기  
  ex ) Starbucks - Siren Order  

  - [Beacon Library](https://github.com/adriancretu/beacons-android)
  - [Can an Android device act as an iBeacon?](https://stackoverflow.com/questions/19602913/can-an-android-device-act-as-an-ibeacon)

- 구글Map 별표기반 --> 걸어갈 수 있는 500m 이내의 레스토랑 서치해주기 --> 및 카페 서치해주기 --> 추가적인 주변 이벤트 서치

- 모니터링 > 제어 > 최적화 > 자율화 의 단계를 거치게된다.

- [IoT Design Strategy](https://brunch.co.kr/@sungi-kim/54)

  1. Easy to install, Easy to control (Required)
  2. Expandable, Integration, All-in-one
  3. Programmable, Self-control
  4. Learning, Recomment, Smart alert
  5. Location-based, Environment-based

- Beacon (Sensor Tag도 Beacon Hardware이다.)

  - 최대 70m 이내 - 알림, 무선결제 등

  - 장치형, 스티커형

  - iBeacon : 실내위치측위기반플랫폼

  - [BLE Scanner Proj](https://gongdol22.tistory.com/12)

  - [Beacon 활용예](https://saurus2.tistory.com/entry/Part-1-비콘이란?category=564008)

  - [UUID 생성하기](https://saurus2.tistory.com/entry/Part-1-비콘이란)

    ```java
    UUID uuid = UUID.randomUUID();
    ```

    

- 쾌적환경조성하기

  - TI Sensor사용

- Make it Punctual  
  : 약속시간, 최적경로, 관련 블로그글, 스케쥴링

- 메모공유시스템

- 여행스케쥴러

  - Flight, Accomodation, Date설정시 



<br>

## 3. Presentation Material

*Presentation == 10*