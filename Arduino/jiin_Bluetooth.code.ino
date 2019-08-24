#include <SoftwareSerial.h>

//? ?? ??
#define PIN_TX 3
#define PIN_RX 2

//????? ??? ?? ??
SoftwareSerial btSerial(PIN_TX, PIN_RX);

void setup()
{
	Serial.begin(9600);
	btSerial.begin(9600);
}

void loop()
{
	if(Serial.available()){
		btSerial.write(Serial.read());
	}
	if(btSerial.available()){
		Serial.write(btSerial.read());
	}
}