package com.example.swudoit;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class ItemBean implements Serializable {

    public String imgUrl;

    public String itemTitle;  //제목
    public String itemContent;  //내용
    public String itemToday;  //날짜
    public String itemImage;  //이미지

    public transient Bitmap bmpTitle;

}