package com.example.swudoit.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.swudoit.MainActivity;
import com.example.swudoit.Note;
import com.example.swudoit.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentDiary extends Fragment {
    // 1. Index를 서버를 통해 select하기
    // 2. select된 index대로 title, content 등 data name에 따라 배열에 나누기
    // 3. 나눈 배열을 split해서 다시 배열에 담기
    // 4. 인덱스대로 Item Layout불러오는데, 각각 인덱스에 따른 아규먼트 보내기
    // 귀찮으니까 얘는 그냥 index를 불러오고 서버 통신하는 클래스라 하자^^^^

    protected static SharedPreferences prf;

    // 해당 아이디의 다이어리 갯수를 나타내는 index
    public static int index;
    protected static String userIdx;

    public static LayoutInflater in;


    //public static LinearLayout l;
    public static LinearLayout con;

    public static View view;

    public static ArrayList<String> titleA;
    public static ArrayList<String> contentA;
    public static ArrayList<String> todayA;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);

        //등록 버튼 눌러서 등록 페이지 이동
        ImageButton btnMemoReg = view.findViewById(R.id.btnMemoReg);
        btnMemoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Note.class);
                startActivity(i);
            }
        });

        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);

        in = getLayoutInflater();

        ConnectServer connectServerPost = new ConnectServer();
        connectServerPost.requestPostIndex(userIdx);

        /*l = view.findViewById(R.id.fragmentdiary);

        con = (LinearLayout) in.inflate(R.layout.fragment_item, null);
        LinearLayout conn = (LinearLayout) in.inflate(R.layout.fragment_item, null);

        l.addView(con);
        l.addView(conn);*/
        //TextView title = con.findViewById(R.id.itemTitle);
        //TextView contetn = con.findViewById(R.id.itemContent);
        //TextView today = con.findViewById(R.id.itemToday);

        return view;
    }

    public static void selectIndex(){
        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);

       //ConnectServer connectServerPost = new ConnectServer();
        //connectServerPost.requestPostIndex(userIdx);
    }

    public class ConnectServer{
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

                            ConnectServer connectServerPost = new ConnectServer();
                            connectServerPost.requestPost(userIdx);
                        }else{
                            Log.d("Error", "오류");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        public void requestPost(final String userIdx){
            RequestBody body = new FormBody.Builder()
                    .add("userIdx", userIdx)
                    .add("index", String.valueOf(index))
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/selectdiary")
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
                            String tempTitle = stl.substring(stl.indexOf("title")+11, stl.indexOf("\\\"],\\\"content"));
                            String[] title = tempTitle.split("\\\\\\\",\\\\\\\"");

                            //String tempContent = stl.substring(stl.indexOf("title")+tempTitle.length()+30, stl.indexOf("\\\"],\\\"today"));
                            //Log.d("temp", tempContent);
                            String tempContent = stl.substring(stl.indexOf(tempTitle)+tempTitle.length()+19, stl.indexOf("\\\"],\\\"today"));
                            //Log.d("temp Content", tempContent);
                            String[] content = tempContent.split("\\\\\\\",\\\\\\\"");
                            //Log.d("content ", content[0] + " " + content[1]);

                            String tempToday = stl.substring(stl.indexOf(tempContent)+tempContent.length()+17, stl.indexOf("\\\"],\\\"image_name"));
                            //Log.d("temp Today", tempToday);
                            String[] today = tempToday.split("\\\\\\\",\\\\\\\"");
                            //String[] tempT = tempToday.split("\\\\\\\",\\\\\\\"");

                            for(int i = 0; i < index; i++){
                                addLayout(title[i], content[i], today[i]);
                            }

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

    public void addLayout(final String title, String content, String today){

        LayoutInflater in = getLayoutInflater();

        final LinearLayout l = this.getView().findViewById(R.id.fragmentdiary);
        Log.d("View", title);

        final LinearLayout conn = (LinearLayout) in.inflate(R.layout.fragment_item, null);

        TextView titleT = conn.findViewById(R.id.itemTitle);
        TextView contetT = conn.findViewById(R.id.itemContent);
        TextView todayT = conn.findViewById(R.id.itemToday);

        titleT.setText(title);
        contetT.setText(content);
        todayT.setText(today);

        Message msg = handler.obtainMessage();

        handler.handleMessage(msg);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater in = getLayoutInflater();

                l.addView(conn);

            }
        });
    }

    final Handler handler = new Handler(){
        public void handleMessage(){

        }
    };

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
