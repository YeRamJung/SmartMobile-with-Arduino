package com.example.swudoit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static final String url = "http://13.125.111.255:3000/";

    public static SharedPreferences sharedPreferences = null;

    public static String idSession = null;

    EditText idE;

    EditText passE;

    ImageButton login;

    ImageButton sighup;

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idE = (EditText) findViewById(R.id.idEdt);
        passE = (EditText) findViewById(R.id.passEdit);

        login = (ImageButton)findViewById(R.id.login);
        sighup = (ImageButton)findViewById(R.id.signup);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);

//        // 블루투스 활성화하기
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
//
//        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
//            Toast.makeText(this, "블루투스가 지원되지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        else { // 디바이스가 블루투스를 지원 할 때
//
//            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
//                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
//            }
//
//            else { // 블루투스가 비활성화 상태 (기기에 블루투스가 꺼져있음)
//
//                // 블루투스를 활성화 하기 위한 다이얼로그 출력
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//
//                // 선택한 값이 onActivityResult 함수에서 콜백된다.
//                startActivityForResult(intent, REQUEST_ENABLE_BT);
//
//            }
//
//        }


        ImageButton.OnClickListener clickListener = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("버튼", "클릭");
                switch (v.getId()){
                    case R.id.login :
                        logIn(v);
                        break;
                    case R.id.signup :
                        Toast.makeText(MainActivity.this, "회원가입", Toast.LENGTH_SHORT).show();

                        Intent signupView = new Intent(MainActivity.this, SignUp.class);

                        startActivity(signupView);

                        break;
                }
            }
        };

        login.setOnClickListener(clickListener);
        sighup.setOnClickListener(clickListener);

    }
    protected void logIn(View v) {
        String idMg = idE.getText().toString();
        String passMg = passE.getText().toString();
        if(idMg.isEmpty() || passMg.isEmpty()){
            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("Error");
            ab.setMessage("아이디, 비밀번호를 입력하세요!");

            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            ab.create();
            ab.show();
            return;
        }else {
            try{
                Log.d("서버", "연결 시도");
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(idE.getText().toString(), passE.getText().toString());

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        public void requestPost(final String id, String password){
            RequestBody body = new FormBody.Builder()
                    .add("id", id)
                    .add("password", password)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/signin")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    backgroundThreadShortToast(MainActivity.this, "아이디, 비밀번호를 확인하세요.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        Log.d("Response ", "Response Body is " + stl);

                        //String idStl = stl.substring(76, stl.length()-5);
                        //String status = stl.substring(13, 16);

                        if(response.isSuccessful()){

                            int idtemp = stl.indexOf("id");
                            int idxtemp = stl.indexOf("userIdx");

                            String idStl = stl.substring(idtemp+7, idxtemp-5);
                            String idxStl = stl.substring(idxtemp+10, stl.length()-3);

                            Log.d("Idx", idxStl);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("id", idStl);
                            editor.putString("userIdx", idxStl);

                            idSession = sharedPreferences.getString("id", null);

                            editor.commit();


                            backgroundThreadShortToast(MainActivity.this, id + "님 환영합니다.");

                            Intent mainView = new Intent(MainActivity.this, TabActivity.class);
                            startActivity(mainView);
                        }else{
                            backgroundThreadShortToast(MainActivity.this, "아이디, 비밀번호를 확인하세요.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // Toast를 위한 함수
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void logOut(){
        try{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d("Debug1", sharedPreferences.getString("id", null));
            Log.d("Debug2", sharedPreferences.getString("userIdx", null));

            editor.clear();
            editor.commit();
            TabActivity.cdt.cancel();

            Log.d("Logout", "로그아웃, 세션 종료");
        }catch (NullPointerException n){
            n.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//
//            case REQUEST_ENABLE_BT :
//
//                if(requestCode == RESULT_OK) { // '사용'을 눌렀을 때
//                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
//                }
//
//                else { // '취소'를 눌렀을 때
//                    // 여기에 처리 할 코드를 작성하세요.
//                    //TODO
//
//                }
//
//                break;
//
//        }
//
//    }
//    public void selectBluetoothDevice() {
//
//        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
//        devices = bluetoothAdapter.getBondedDevices();
//        // 페어링 된 디바이스의 크기를 저장
//        /**/
//        int pariedDeviceCount = devices.size();
//        // 페어링 되어있는 장치가 없는 경우
//        if(pariedDeviceCount == 0) {
//            // 페어링을 하기위한 함수 호출
//            //TODO
//
//        }
//
//        // 페어링 되어있는 장치가 있는 경우
//        else {
//            // 디바이스를 선택하기 위한 다이얼로그 생성
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
//            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
//            List<String> list = new ArrayList<>();
//            // 모든 디바이스의 이름을 리스트에 추가
//            for(BluetoothDevice bluetoothDevice : devices) {
//                list.add(bluetoothDevice.getName());
//            }
//            list.add("취소");
//
//            // List를 CharSequence 배열로 변경
//            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
//            list.toArray(new CharSequence[list.size()]);
//
//            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
//            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // 해당 디바이스와 연결하는 함수 호출
//                    connectDevice(charSequences[which].toString());
//                }
//            });
//
//
//            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
//            builder.setCancelable(false);
//            // 다이얼로그 생성
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//
//    }
//
//    public void connectDevice(String deviceName) {
//
//        // 페어링 된 디바이스들을 모두 탐색
//        for(BluetoothDevice tempDevice : devices) {
//            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
//            if(deviceName.equals(tempDevice.getName())) {
//                bluetoothDevice = tempDevice;
//                break;
//            }
//        }
//
//        // UUID 생성
//        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//
//        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
//        try {
//
//            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
//            bluetoothSocket.connect();
//            // 데이터 송,수신 스트림을 얻어옵니다.
//            outputStream = bluetoothSocket.getOutputStream();
//            inputStream = bluetoothSocket.getInputStream();
//            // 데이터 수신 함수 호출
//            receiveData();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void receiveData() {
//        final Handler handler = new Handler();
//        // 데이터를 수신하기 위한 버퍼를 생성
//        readBufferPosition = 0;
//        readBuffer = new byte[1024];
//
//        // 데이터를 수신하기 위한 쓰레드 생성
//        workerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(Thread.currentThread().isInterrupted()) {
//                    try {
//                        // 데이터를 수신했는지 확인합니다.
//                        int byteAvailable = inputStream.available();
//                        // 데이터가 수신 된 경우
//                        if(byteAvailable > 0) {
//                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
//                            byte[] bytes = new byte[byteAvailable];
//                            inputStream.read(bytes);
//                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
//                            for(int i = 0; i < byteAvailable; i++) {
//                                byte tempByte = bytes[i];
//                                // 개행문자를 기준으로 받음(한줄)
//                                if(tempByte == '\n') {
//                                    // readBuffer 배열을 encodedBytes로 복사
//                                    byte[] encodedBytes = new byte[readBufferPosition];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    // 인코딩 된 바이트 배열을 문자열로 변환
//                                    final String text = new String(encodedBytes, "US-ASCII");
//                                    readBufferPosition = 0;
//                                    handler.post(new Runnable() {
//
//                                        @Override
//
//                                        public void run() {
//                                            // 텍스트 뷰에 출력
//                                            //textViewReceive.append(text + "\n");
//                                        }
//
//                                    });
//
//                                } // 개행 문자가 아닐 경우
//
//                                else {
//                                    readBuffer[readBufferPosition++] = tempByte;
//                                }
//                            }
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        // 1초마다 받아옴
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        workerThread.start();

//    }


}
