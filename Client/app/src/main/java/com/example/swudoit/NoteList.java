package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

public class NoteList extends AppCompatActivity {

    // 1. Index를 서버를 통해 select하기
    // 2. select된 index대로 title, content 등 data name에 따라 배열에 나누기
    // 3. 나눈 배열을 split해서 다시 배열에 담기
    // 4. 인덱스대로 Item Layout불러오는데, 각각 인덱스에 따른 아규먼트 보내기

    protected static SharedPreferences prf;

    // 해당 아이디의 다이어리 갯수를 나타내는 index
    static int index;
    protected static String userIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);
    }

    public static void selectIndex(){
        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);

        ConnectServer connectServerPost = new ConnectServer();
        connectServerPost.requestPostIndex(userIdx);
    }

    public static class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        String url = "http://13.125.111.255:3000/";

        public void requestPostIndex(final String userIdx){
            RequestBody body = new FormBody.Builder()
                    .add("userIdx", userIdx)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/selectIdx")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        Log.d("Response ", "Response Body is " + stl);

                        if(response.isSuccessful()){
                            String temp = stl.substring(stl.length()-3, stl.length()-2);
                            // Index
                            index = Integer.parseInt(temp);
                        }else{
                            Log.d("Error", "오류");
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
