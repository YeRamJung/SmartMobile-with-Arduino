package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.swudoit.fragment.FragmentDiary;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class item extends AppCompatActivity {

    TextView itemTitle;
    TextView itemContent;
    TextView itemToday;
    ImageView itemImage;

    List<ItemBean> listView;

    String title;
    String content;
    String today;
    String image_name;
    String image_copied;

    Adapter adapter;

    Diary d = new Diary();
    FragmentDiary fd = new FragmentDiary();
    ItemAdapter iad = new ItemAdapter();
    int index = fd.index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemTitle = (TextView)findViewById(R.id.itemTitle);
        itemContent = (TextView)findViewById(R.id.itemContent);
        itemToday = (TextView)findViewById(R.id.itemToday);
        itemImage = (ImageView)findViewById(R.id.itemImage);

        iad.setList(listView);

    }

//    public void addView(TextView title, TextView content, TextView today, ImageView image){
//        itemTitle = title;
//        itemContent = content;
//        itemToday = today;
//        itemImage = image;
//    }

    public static void dataTest(String title, String content, String today, String image_name, String image_copied){
        Log.d("Message", "title : " + title);
        Log.d("Message", " content : " + content);
        Log.d("Message", " today : " + today);
        Log.d("Message", " image_name : " + image_name);
        Log.d("Message", " image_copied : " + image_copied);
    }

}
