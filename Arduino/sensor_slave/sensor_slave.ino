#include <DHT.h>
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <Servo.h>

int dhtPin = A0;
int GasPin = A1;
int SoundPin = A2;

DHT dht(dhtPin,DHT11);

void setup() {
  Serial.begin(9600);
  dht.begin();
  
  pinMode(GasPin, INPUT);
  pinMode(SoundPin, INPUT);
}

void loop() {

  int temp = dht.readTemperature();  // 온도값 저장
  int humi = dht.readHumidity();  // 습도값 저장
  int gas = analogRead(GasPin);  // 가스값 저장 
  int sound = analogRead(SoundPin);

  Serial.print(temp);
  Serial.print(humi);
  Serial.print(gas);
  Serial.println(sound);

  
  delay(5000);

}
