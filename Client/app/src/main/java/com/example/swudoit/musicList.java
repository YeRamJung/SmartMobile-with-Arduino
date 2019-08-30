package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.swudoit.fragment.FragmentHome;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class musicList extends AppCompatActivity {
    private ImageButton music1;
    private ImageButton music2;
    private int num1, num2 = 0;

    private BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        /*블루투스*/
        bt = new BluetoothSPP(this); //Initializing

//        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
//            Toast.makeText(getApplicationContext()
//                    , "Bluetooth is not available"
//                    , Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
//            public void onDataReceived(byte[] data, String message) {
//                Toast.makeText(musicList.this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
//            public void onDeviceConnected(String name, String address) {
//                Toast.makeText(getApplicationContext()
//                        , "Connected to " + name + "\n" + address
//                        , Toast.LENGTH_SHORT).show();
//            }
//
//            public void onDeviceDisconnected() { //연결해제
//                Toast.makeText(getApplicationContext()
//                        , "Connection lost", Toast.LENGTH_SHORT).show();
//            }
//
//            public void onDeviceConnectionFailed() { //연결실패
//                Toast.makeText(getApplicationContext()
//                        , "Unable to connect", Toast.LENGTH_SHORT).show();
//            }
//        }); //end 블루투스


        music1 = (ImageButton)findViewById(R.id.musicPlayer);
        music2 = (ImageButton)findViewById(R.id.musicPlayer2);

        //곰세마리
        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num1==0){
                    Toast.makeText(musicList.this, "음악 재생" , Toast.LENGTH_LONG).show();
                    music1.setSelected(true);
                    num1=1;
                }
                else{
                    Toast.makeText(musicList.this, "음악 정지" , Toast.LENGTH_LONG).show();
                    music1.setSelected(false);
                    num1=0;
                }
            }
        });

        //학교종이 땡땡땡
        music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num2==0){
                    Toast.makeText(musicList.this, "음악 재생" , Toast.LENGTH_LONG).show();
                    music2.setSelected(true);
                    num2=1;
                }
                else{
                    Toast.makeText(musicList.this, "음악 정지" , Toast.LENGTH_LONG).show();
                    music2.setSelected(false);
                    num2=0;
                }
            }
        });

    }//end onCreate

//    public void onDestroy() {
//        super.onDestroy();
//        bt.stopService(); //블루투스 중지
//    }
//
//    public void onStart() {
//        super.onStart();
//        if (!bt.isBluetoothEnabled()) { //
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
//        } else {
//            if (!bt.isServiceAvailable()) {
//                bt.setupService();
//                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
//                setup();
//            }
//        }
//    }


    public void setup() {
        //곰세마리
        ImageButton musicPlayer = findViewById(R.id.musicPlayer); //데이터 전송
        musicPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("1", true);
            }
        });

        //학교종
        ImageButton musicPlayer2 = findViewById(R.id.musicPlayer2); //데이터 전송
        musicPlayer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("2", true);
            }
        });
    }
}
