#include <SoftwareSerial.h>

//bluetooth
#define BT_RXD 8
#define BT_TXD 7
SoftwareSerial bluetooth(BT_RXD, BT_TXD);


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

//무드등, 에어컨, 보일러 온오프
bool isOnLight = false;
bool isOnAir = false;
bool isOnBoil = false;

void setup(){
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(2, OUTPUT);
  pinMode(13, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(tonepin, OUTPUT);
}
 
void loop(){
  //블루투스로부터 읽어온 데이터를 저장 할 변수 val
  //안드로이드에서 선택한 기능에 따라 int형 변수로 저장
  int val = bluetooth.read();
  
  /* each value means
   * 1. 곰세마리
   * 2. 학교종
   * 3. 무드등->2
   * 4. 에어컨->13
   * 5. 보일러->4
   */

  //블루투스 통신
  if (bluetooth.available()) {
    Serial.write(bluetooth.read());
  }
  if (Serial.available()) {
    bluetooth.write(Serial.read());
  }
 delay(10);


if(val == 1){ //곰세마리 음악 재생
  for (int i = 0; i < 19; i++) {
    int Durations = 1000/tempo_3bears[i];    // calculate sound length
    tone(tonepin, melody_3bears[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
  }
}else if(val==2){//학교종 음악 재생
  for (int i = 0; i < 24; i++) {
    int Durations = 1000/tempo_school[i];    // calculate sound length
    tone(tonepin, melody_school[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
}
}else if(val==3){//무드등
  if(isOnLight==false){
    digitalWrite(2, HIGH);
    isOnLight=true;
  }else{
    digitalWrite(2, LOW);
    isOnLight=false;
  }
}else if(val==4){//에어컨
  if(isOnAir==false){
    digitalWrite(13, HIGH);
    isOnAir=true;
  }else{
    digitalWrite(13, LOW);
    isOnAir=false;
  }
}else if(val==5){//보일러
  if(isOnBoil==false){
    digitalWrite(4, HIGH);
    isOnBoil=true;
  }else{
    digitalWrite(4, LOW);
    isOnBoil=false;
  }
}
}
