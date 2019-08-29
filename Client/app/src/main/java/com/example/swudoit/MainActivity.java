package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static final String url = "http://13.125.111.255:3000/";

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

            Intent mainView = new Intent(this, TabActivity.class);

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

        String password = passE.getText().toString();

        String signinURL = url + "user/signin";

        try{

            Log.d("서버", "연결 시도");
            ConnectServer connectServerPost = new ConnectServer();

            connectServerPost.requestPost(idE.getText().toString(), passE.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String id, String password){
            RequestBody body = new FormBody.Builder()
                    .add("id", id)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(url+"user/signin")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    Log.d("Message ", "서버 연결 실패");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("Response ", "Response Body is " + response.body().string());
                    Log.d("Message ", " 서버 연결 성공");
                }
            });
        }
    }

}
