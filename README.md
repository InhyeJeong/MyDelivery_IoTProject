# MyDelivery_RestApi(IoT Project)
- 이 프로젝트는 무인택배함을 수신자 및 배달기사가 **QR코드**로 인식하여 열고 닫히도록 되어있으며, 이를 통해 보안을 강화하는 프로젝트입니다.
- 12월 ~ 진행중

##  이 프로젝트의 **Tech Stack**
```dart
NodeJs, java, python, MySql, Android, Mocha, Supertest, express, body-parser, curl, Sequelize, date-utils
```

## **Sensor**
```
- Raspberry Pi
- Arduino
- Pi Cam
- Reed Switch
- Servo Motor
```

## 기능별 소개

### NodeJs를 활용하여 RestApi 서버 생성
* 서버가 라즈베리파이, 스마트폰 어플과 통신
[사진]
* GET, POST, PUT, DELETE의 형식으로 **Controller** 구현
[코드]
* Model 분리
* **랜덤**으로 QR코드 String으로 생성 후, 발신 및 수신 어플에서 QR코드 구현
[코드]

### 발신자 어플, 수신자어플
* 발신자의 번호를 발신자 어플에서 자동 인식
[사진]
* 발신자의 배달 목록을 **ListView** 를 활용하여 아래의 **우선순위로 정렬**
[사진]
  1. 배달 현황(registration, locked, received)
  2. 접수시간(mysql의 creatAt 데이터 활용)
* 수신완료 항목은 black, 배달완료 항목은 gray background 처리하여 구분
[사진]
* 각 항목 클릭 시, **확대된 QR코드**와 **Sender Open/Close Time, Receiver Open/Close Time** 출력
[사진]
* 서버에서 받은 랜덤 String으로 **QR코드 구현**
[코드]

### 배달 접수 어플
* 아래의 항목을 입력하면 서버로 data 전송
[사진]


