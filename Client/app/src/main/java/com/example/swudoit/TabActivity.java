package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.swudoit.fragment.FragmentDiary;
import com.example.swudoit.fragment.FragmentHome;
import com.example.swudoit.fragment.FragmentSetting;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class TabActivity extends AppCompatActivity {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    TextView userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_home, null);

        userId = (TextView)view.findViewById(R.id.txtPerson);

        //탭 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("홈"));
        mTabLayout.addTab(mTabLayout.newTab().setText("육아일지"));
        mTabLayout.addTab(mTabLayout.newTab().setText("환경설정"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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

    public void uploadUserID(){
    }
}

