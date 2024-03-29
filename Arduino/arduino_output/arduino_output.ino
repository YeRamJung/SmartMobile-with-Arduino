#include <SoftwareSerial.h>
#include <Servo.h>
 
#define BT_RXD 13
#define BT_TXD 12
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
Servo servo;

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

int tonepin = 2;

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

//무드등, 에어컨, 보일러 온오프?
bool isOnLight = false;

void setup(){
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(8, OUTPUT);
  servo.attach(4);
  pinMode(tonepin, OUTPUT);
}
 
void loop(){
  int val = bluetooth.read();
  
  /* each value means
   * 1. 곰세마리
   * 2. 학교종
   * 3. 무드등 ->8
   * 4. 모빌 ->4
   */
  
  if (bluetooth.available()) {
    Serial.write(bluetooth.read());
  }
  if (Serial.available()) {
    bluetooth.write(Serial.read());
  }
 delay(10);


//play the music
if(val == 1){
  for (int i = 0; i < 19; i++) {
    int Durations = 1000/tempo_3bears[i];    // calculate sound length
    tone(tonepin, melody_3bears[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
  }
}else if(val==2){
  
  for (int i = 0; i < 24; i++) {
    int Durations = 1000/tempo_school[i];    // calculate sound length
    tone(tonepin, melody_school[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
}
}else if(val==3){//무드등
  if(isOnLight==false){
    digitalWrite(8, HIGH);
    isOnLight=true;
  }else{
    digitalWrite(8, LOW);
    isOnLight=false;
  }
}else if(val==4){
  for(int angle = 0; angle<180; angle+=10){
    servo.write(angle);
    delay(500);
  }
  for(int angle = 180; angle>0; angle-=10){
    servo.write(angle);
    delay(500);}
  }
}
