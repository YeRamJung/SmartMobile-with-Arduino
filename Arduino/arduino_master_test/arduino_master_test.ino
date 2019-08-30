#include <Adafruit_Sensor.h>   // dht 라이브러리 오류 방지용
#include <DHT.h> // DHT라이브러리 포함
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <Servo.h>

#define BT_RXD 7
#define BT_TXD 8
SoftwareSerial bluetooth(BT_RXD, BT_TXD);

Servo servo;

int GasPin = A1;  // MQ7 일산화탄소 센서 데이터 핀 A1 에 연결
int dhtPin = A0; // DHT11 온습도 센서 데이터 핀 A0 에 연결
int SoundPin = A2;  // LM393 소리감지 센서 데이터 핀 A2 에 연결

DHT dht(dhtPin,DHT11);

const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

int temp_alarm = 0;   // 온도 경고 변수
int humi_alarm = 0;   // 습도 경고 변수
int gas_threshold = 65;   // 가스 경고 판단 변수

bool isDangerGas = false;  // 가스 판단?
bool isBabyCry = false;  // 소리 감지 울음 판단?


//Speaker
//음계 설정
#define NOTE_C5  523   //do
#define NOTE_D5  587   //re
#define NOTE_E5  659   //mi
#define NOTE_F5  698   //pa
#define NOTE_G5  784   //sol
#define NOTE_A5  880   //la
#define NOTE_B5  988   //si
#define NOTE_C6  1047  //do

int tonepin = 12;

//3bears
int melody_3bears[] = {
NOTE_C5,NOTE_C5,NOTE_C5,NOTE_C5,NOTE_C5,               //?????
NOTE_E5,NOTE_G5,NOTE_G5,NOTE_E5,NOTE_C5,               //?????
NOTE_G5,NOTE_G5,NOTE_E5,NOTE_G5,NOTE_G5,NOTE_E5,       //??????
NOTE_C5,NOTE_C5,NOTE_C5,                               //???
NOTE_G5,NOTE_G5,NOTE_E5,NOTE_C5,                       //????
NOTE_G5,NOTE_G5,NOTE_G5,                               //???
NOTE_G5,NOTE_G5,NOTE_E5,NOTE_C5,                       //????
NOTE_G5,NOTE_G5,NOTE_G5,                               //???
NOTE_G5,NOTE_G5,NOTE_E5,NOTE_C5,                       //????
NOTE_G5,NOTE_G5,NOTE_G5,NOTE_A5,NOTE_G5,               //?????
NOTE_C6,NOTE_G5,NOTE_C6,NOTE_G5,                       //????
NOTE_E5,NOTE_D5,NOTE_C5                                //???
};
int tempo_3bears[]={
4,8,8,4,4,
4,8,8,4,4,
8,8,4,8,8,4,
4,4,2,
4,4,4,4,
4,4,2,
4,4,4,4,
4,4,2,
4,4,4,4,
8,8,8,8,2,
4,4,4,4,
4,4,2
};

//school bell
int melody_school[]={
NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5, NOTE_G5, NOTE_E5,   //???????
NOTE_G5, NOTE_G5, NOTE_E5, NOTE_E5, NOTE_D5,                     //?????
NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5, NOTE_G5, NOTE_E5,   //???????
NOTE_G5, NOTE_E5, NOTE_D5, NOTE_E5,NOTE_C5                      //?????
};

int tempo_school[]={
  4, 4, 4, 4, 4, 4, 4,
  4, 4, 4, 4, 4,
  4, 4, 4, 4, 4, 4, 4,
  4, 4, 4, 4, 4
  };


//무드등, 에어컨, 가습기 온오프?
bool isOnLight = false;
bool isOnAir = false;
bool isOnHumi = false;


void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  dht.begin();
  pinMode(6, OUTPUT);
  servo.attach(10);
  pinMode(tonepin, OUTPUT);

  pinMode(GasPin, INPUT);
  pinMode(SoundPin, INPUT);
  lcd.begin(16, 2); 
}


void loop() {
  int val = bluetooth.read();

  int temp = dht.readTemperature();  // 온도값 저장
  int humi = dht.readHumidity();  // 습도값 저장
  int gas = analogRead(GasPin);  // 가스값 저장
  int sound = analogRead(SoundPin);
  
  /* each value means
   * 1. 곰세마리
   * 2. 학교종
   * 3. 무드등 ->8
   * 4. 모빌 ->4    모터는 언제 끌건지 결정해야 합니다
   * 5. LCD 
   */

  if (bluetooth.available()) {
    Serial.write(bluetooth.read());
  }
  if (Serial.available()) {
    bluetooth.write(Serial.read());
  }
 delay(10);
 
//play the music
if(val == 1){ //곰세마리 음악 재생
  for (int i = 0; i < 19; i++) {
    int Durations = 1000/tempo_3bears[i];    // calculate sound length
    tone(tonepin, melody_3bears[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
  }
}
else if(val==2){  //학교 종이 땡땡땡 음악 재생
  for (int i = 0; i < 24; i++) {
    int Durations = 1000/tempo_school[i];    // calculate sound length
    tone(tonepin, melody_school[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
  }
}
else if(val==3){//무드등
  if(isOnLight==false){
    digitalWrite(8, HIGH);
    isOnLight=true;
  }
  else{
    digitalWrite(8, LOW);
    isOnLight=false;
  }
}else if(val==4){ //모빌
  for(int angle = 0; angle<180; angle+=10){
    servo.write(angle);
    delay(500);
  }
  for(int angle = 180; angle>0; angle-=10){
    servo.write(angle);
    delay(500);
  }
}
else if(val==5){//에어컨
  lcd.setCursor(0,0);
  if(isOnAir==false){
    lcd.print("Aircon ON");
    isOnAir=true;
  }
  else{
    lcd.print("Aircon OFF");
    isOnAir=false;
  }
}
else if(val==6){//가습기
  lcd.setCursor(0,1);
  if(isOnHumi==false){
    lcd.print("Humi ON");
    isOnHumi=true;
  }
  else{
    lcd.print("Humi OFF");
    isOnHumi=false;
  }
}

  // 센서 (온습도, 가스, 사운드) 측정
  Serial.print("temp: ");
  Serial.print(temp);
  Serial.print(" , ");
  Serial.print(" humi: ");
  Serial.print(humi);
  Serial.print(" gas: ");
  Serial.print(gas);
  Serial.print(" , ");
  Serial.print(" sound: ");
  Serial.println(sound);
  


  // 온습도 판별 value 값 의미
  /* 1. 적정
   * 2. 주의
   * 3. 위험
   */
   
  // 온도 판별
  if(temp<22 || temp>30 ){  // [온도] 22 미만 30 초과  : 3. 위험
    temp_alarm=3;
    }
    else if(temp<24 || temp>28){   // [온도]  22~24 || 28~30  : 2. 주의 
    temp_alarm=2;
    }
    else{   // [온도]  24~28 : 1. 적정 
      temp_alarm=1;
      }
   Serial.print(" temp_alarm:");
   Serial.println(temp_alarm);

  // 습도 판별
  if(humi<45 || humi>70){  // [습도] 45 미만 70 초과  : 3.위험
    humi_alarm=3;
    }
    else if(humi<50 || humi>60){   // [습도] 45~50 || 60~70  : 2.주의
      humi_alarm=2;
    }
    else{   // [습도] 50~60  : 1.적정
      humi_alarm=1;
      }
   Serial.print("humi_alarm: ");
   Serial.println(humi_alarm);
      
  // 가스 판별
  if(gas > gas_threshold){
    isDangerGas = true;
    Serial.println("In Danger Of GAS!!!!");  // 위험
  }
  else{
    isDangerGas = false;
    Serial.println("In Safe of GAS~");   // 안전
  }

  // 소리 감지

  if (sound > 35){
    isBabyCry = true;
    Serial.println("Baby Crying!");
  }
  else{
    isBabyCry = false;
    Serial.println("Baby Fine!");
  }

  // LCD
  
  lcd.setCursor(0,0);
  lcd.write("Hello");
  
}
