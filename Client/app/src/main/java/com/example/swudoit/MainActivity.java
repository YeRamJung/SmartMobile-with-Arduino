package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import java.io.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;
import android.os.AsyncTask;
import java.io.OutputStream;
import java.net.URL;
import java.io.BufferedReader;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    boolean isUser;

    EditText idE;

    EditText passE;

    ImageButton login;

    ImageButton sighup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idE = (EditText) findViewById(R.id.idEdt);
        passE = (EditText) findViewById(R.id.passEdit);

        login = (ImageButton)findViewById(R.id.login);
        sighup = (ImageButton)findViewById(R.id.signup);

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

    private void logIn(View v){

        String id = idE.getText().toString();

        String pass = passE.getText().toString();

        isUser = signIn(id, pass);

        if (isUser) {
            Toast.makeText(MainActivity.this, id + "님 환영합니다!!", Toast.LENGTH_SHORT).show();

            Intent mainView = new Intent(this, swudoit_main.class);

            startActivity(mainView);
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("Error");
            ab.setMessage("아이디, 비밀번호를 확인하세요!");

            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            ab.create();
            ab.show();
        }

    }

    private boolean signIn(String id, String pass) {
        if(id != null){
            return true;
        }else {
            return false;
        }
    }

    protected void mClick(View v) {

        String id = idE.getText().toString();

        String pass = passE.getText().toString();


        new JSONTask().execute("https://13.125.153.65:3000/user/signin", id, pass);
    }


    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            Log.d("서버", "통신 시작");

            String result = null;

            try{
                String url = params[0];
                String body = params[1];

                URL urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();

                // 10 초동안 서버로부터 반응없으면 에러
                conn.setReadTimeout(100000);
                // 접속하는 커넥션 타임 15초동안 접속안되면 안되는 것으로 간주(ms)
                conn.setConnectTimeout(150000);

                // Get인지 Post인지
                conn.setRequestMethod("POST");
                // character set UTF-u로 선언
                conn.setRequestProperty("Accetp-Charset", "UTF-8");
                // 서버로부터 보내는 패킷이 어떤 타입인지 선언
                conn.setRequestProperty("Content-Type", "applecation/x-www-form-urlencoded"); // 폼테그 형식

                // 안드로이드가 서버로부터 받는 것, 보내는 것을 트로
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream outStream = conn.getOutputStream();
                // body는 서버로 보낼 스트링 값 설정
                outStream.write(body.getBytes("utf-8"));

                int resCode = conn.getResponseCode(); // connect, send http reuqest, receive htttp request
                System.out.println ("code = "+ resCode);


                InputStreamReader InputStream = new InputStreamReader(conn.getInputStream(), "UTF-8");//InputStreamReader는 서버로부터 안드로이드로 받아오는 데이터 흐름을 읽어주는 클래스
                BufferedReader Reader = new BufferedReader(InputStream);
                StringBuilder Builder = new StringBuilder();//스트링을 만들어주는데 유용하게쓰이는 클래스
                String ResultStr; //저장할 공간

                while ((ResultStr = Reader.readLine()) != null) {//(중요)서버로부터 한줄씩 읽어서 문자가 없을때까지 넣어줌
                    Builder.append(ResultStr + "\n"); //읽어준 스트링값을 더해준다.
                }

                //result = Builder.toString();//빌더를 차곡차곡쌓아서 result에 넣는다.
                //Toast.makeText(this.Parent, Body.toString(), Toast.LENGTH_LONG).show();

                result = Builder.toString();


            }catch(Exception e){
                e.printStackTrace();
            }

            return result;
        }
    }

}
