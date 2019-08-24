void setup()
{
  int melody[] = {262, 294, 330, 349, 392, 440, 492};  
  for(int i = 0; i<7; i++){
    tone(8, melody[i], 500); delay(500);
  }
}

void loop()
{
  digitalWrite(13, HIGH);
  delay(1000); // Wait for 1000 millisecond(s)
  digitalWrite(13, LOW);
  delay(1000); // Wait for 1000 millisecond(s)
}