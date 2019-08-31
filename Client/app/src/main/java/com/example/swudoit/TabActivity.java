package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swudoit.fragment.FragmentDiary;
import com.example.swudoit.fragment.FragmentHome;
import com.example.swudoit.fragment.FragmentSetting;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TabActivity extends AppCompatActivity {

    static final String url = "http://13.125.111.255:3000/";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    public static CountDownTimer cdt;

    SharedPreferences prf;
    String userIdx;

    String temp = null;
    String humi = null;
    String gas = null;
    String sound = null;
    String arduinostl = null;  //상태


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        //탭 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("홈"));
        mTabLayout.addTab(mTabLayout.newTab().setText("육아일지"));
        mTabLayout.addTab(mTabLayout.newTab().setText("환경설정"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // userIdx 받아오기
        prf = MainActivity.sharedPreferences;
        userIdx = prf.getString("userIdx", null);

        //ViewPager 생성
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        //Tab이랑 viewpager랑 연결
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // 30초 분기로 15초동안 서버랑 통신을 하여 데이터베이스 속 온도, 가스, 수정하는 구간
        cdt = new CountDownTimer(300 * 1000, 15000) {
            @Override
            public void onTick(long l) {
                // 서버 통신
                Log.d("Message", "아두이노 서버 통신 " + userIdx);
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(userIdx);
            }

            @Override
            public void onFinish() {

            }
        };

        cdt.start();
    }  //end onCreate

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private int tabCount;

        public ViewPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.tabCount = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentHome();
                case 1:
                    return new FragmentDiary();
                case 2:
                    return new FragmentSetting();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        } //실수하면 안됨! 만들어 놓은걸로 바꿔야 함
    }

    public class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String userIdx){
            RequestBody body = new FormBody.Builder()
                    .add("userIdx", userIdx)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"arduino/selectarduino")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    backgroundThreadShortToast(TabActivity.this, "서버통신오류");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        Log.d("Response ", "Response Body is " + stl);

                        if(response.isSuccessful()){
                            temp = stl.substring(stl.indexOf("temp")+9, stl.indexOf("humi")-5);
                            humi = stl.substring(stl.indexOf("humi")+9, stl.indexOf("gas")-5);
                            Log.d("temp", "temp : " + temp + " humi : " + humi);
                            gas = stl.substring(stl.indexOf("gas")+8, stl.indexOf("sound")-5);
                            sound = stl.substring(stl.indexOf("sound")+9, stl.indexOf("stl")-5);
                            arduinostl = stl.substring(stl.indexOf("stl")+6, stl.length() - 3);
                            Log.d("temp", "gas : " + gas + " sound : " + sound + " stl : " + arduinostl);

                        }else{
                            backgroundThreadShortToast(TabActivity.this, "서버 통신 오류");
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

