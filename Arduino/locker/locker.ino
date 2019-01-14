#include <Servo.h>
#define reedPin 10
#define servoPin 9
#define servoOpen 20
#define servoClosed 90
#define buzzerPin 4
#define redPin 7
#define greenPin 6
#define bluePin 5

int state; // 0 : closed, 1 : open
Servo myservo;
int reedCount = 0;

void led_red(){
  digitalWrite(redPin, HIGH);
  digitalWrite(bluePin, HIGH);
  digitalWrite(greenPin, HIGH);
}
void led_blue(){
  digitalWrite(redPin, LOW);
  digitalWrite(bluePin, HIGH);
  digitalWrite(greenPin, LOW);
}
void led_green(){ 
  digitalWrite(redPin, LOW);
  digitalWrite(bluePin, LOW);
  digitalWrite(greenPin, HIGH);
}
void led_white(){
  digitalWrite(redPin, HIGH);
  digitalWrite(bluePin, LOW);
  digitalWrite(greenPin, LOW);
}
void led_purple(){
  digitalWrite(redPin, HIGH);
  digitalWrite(bluePin, HIGH);
  digitalWrite(greenPin, LOW);
}
void led_yello(){
  digitalWrite(redPin, HIGH);
  digitalWrite(bluePin, LOW);
  digitalWrite(greenPin, HIGH);
  tone(buzzerPin, 261, 100);
}

void setup() {
  Serial.begin(9600); // 시리얼 통신 시작
  state = 0; // 처음 상태는 closed
  myservo.attach(servoPin); // 서보핀 등록
  myservo.write(servoClosed); // 문 닫기   
  
  pinMode(reedPin, INPUT);
  pinMode(redPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);
  led_white();
}


void loop() {
  if (state == 0){// 문이 닫혀있는 상태 -> 라즈베리파이로부터 문을 열어달라는 신호를 기다림
    char ch = Serial.read();
    if(ch == 'o'){ // o는 open을 의미함
      // 서보모터로 문을 열어줌
      myservo.write(servoOpen);
      led_red();
      tone(buzzerPin, 494, 1000);
      // state를 1(open)로 변경
      state = 1;
    }
  }
  else if (state == 1){ // 문이 열려있는 상태 -> 리드센서를 확인하여 문이 닫히는 순간을 라즈베리파이에게 알려줌
    // 리드스위치 읽기
    int reed_switch = digitalRead(reedPin);
    if(reed_switch == 0){//문이 닫히는 경우
      reedCount++;
      led_yello();
      delay(100);
    }
    else {
      reedCount = 0;
      led_red();
    }

    if(reedCount > 10){ // reed센서가 자격을 특정횟수 이상만큼 연속으로 감지하면 문 잠금
      myservo.write(servoClosed); // 문 닫기 
      Serial.println('c'); // c는 closed를 의미, 라즈베리파이에 문이 닫힘을 알려줌
      reedCount = 0;
      state = 0; // 상태를 closed로 바꿈
      led_white();
    }
  }
}
