/*
    Speaker
*/

#define NOTE_C5  523   //do
#define NOTE_D5  587   //re
#define NOTE_E5  659   //mi
#define NOTE_F5  698   //pa
#define NOTE_G5  784   //sol
#define NOTE_A5  880   //la
#define NOTE_B5  988   //si
#define NOTE_C6  1047  //do

int tonepin = 8;

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
NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5, NOTE_G5, NOTE_E5   //???????
NOTE_G5, NOTE_G5, NOTE_E5, NOTE_E5, NOTE_D5                     //?????
NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5, NOTE_G5, NOTE_E5   //???????
NOTE_G5, NOTE_E5, NOTE_D5, NOTE_E5,NOTE_C5                      //?????
}



void setup() { 
} 
void loop() {

int music[];
int tempo[];

//choose the music to play()data from android
switch

//play the music
  for (int i = 0; i < sizeof(music); i++) {
    int Durations = 1000/tmepo[i];    // calculate sound length
    tone(tonepin, music[i], Durations);    
    int pauseBetweenNotes = Durations *1.3 ;
    delay(pauseBetweenNotes);
    noTone(tonepin);
  }

}
