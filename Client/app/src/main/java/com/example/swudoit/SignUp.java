package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    static final String url = "http://13.125.111.255:3000/";

    EditText idE;

    EditText passE;

    EditText passConfirmE;

    ImageButton sighup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idE = (EditText) findViewById(R.id.idEdtForSignup);
        passE = (EditText) findViewById(R.id.passEditForSignup);
        passConfirmE = (EditText) findViewById(R.id.passEditConfirm);

        sighup = (ImageButton)findViewById(R.id.signup);

        ImageButton.OnClickListener clickListener = new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("버튼", "클릭");
                // if Log나 Pass에 데이터 없으면 알람창 뜨고 return 아니면 서버 통신하기
                if(idE.getText().toString().isEmpty() || passE.getText().toString().isEmpty() || passConfirmE.getText().toString().isEmpty()){
                    AlertDialog.Builder ab = new AlertDialog.Builder(SignUp.this);

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
                }

                String pass = passE.getText().toString();
                String confirm = passConfirmE.getText().toString();

                // 비밀번호 일치 하지 않음
                if(pass != confirm){
                    Log.d("Pass ", pass);
                    Log.d("Confirm ", confirm);

                    /*AlertDialog.Builder ab = new AlertDialog.Builder(SignUp.this);

                    ab.setTitle("Error");
                    ab.setMessage("비밀번호가 일치하지 않습니다.");

                    ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    ab.create();
                    ab.show();*/

                    Toast.makeText(SignUp.this, "비밀번호 일치하지 않음", Toast.LENGTH_SHORT).show();

                    //return;
                }else{

                    try{
                        Log.d("서버", "연결 시도");
                        SignUp.ConnectServer connectServerPost = new SignUp.ConnectServer();
                        connectServerPost.requestPost(idE.getText().toString(), passE.getText().toString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        };

        sighup.setOnClickListener(clickListener);
    }

    public class ConnectServer {
        OkHttpClient client = new OkHttpClient();

        public void requestPost(final String id, String password) {
            RequestBody body = new FormBody.Builder()
                    .add("id", id)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(url + "user/signup")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    backgroundThreadShortToast(SignUp.this, "아이디, 비밀번호를 확인하세요.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("Response ", "Response Body is " + response.body().string());
                    try{
                        Gson gson = new Gson();
                        String stl = gson.toJson(response.body());

                        Log.d("Message ", stl);

                        if(stl.contains("62")){ // 62 : 성공

                            backgroundThreadShortToast(SignUp.this, id + "님 가입을 축하합니다.");

                            Intent signupView = new Intent(SignUp.this, MainActivity.class);
                            startActivity(signupView);
                        }else{ // 66 : 중복됨
                            backgroundThreadShortToast(SignUp.this, "중복된 아이디가 존재합니다.");
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
}
