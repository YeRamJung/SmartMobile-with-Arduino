package com.example.swudoit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class ItemAdapter {

    private Context mContext;
    private List<ItemBean> mItemList;

    public ItemAdapter(){ }

    public ItemAdapter(Context context, List<ItemBean> itemList){
        mContext = context;
        mItemList = itemList;
    }

    public void setList(List<ItemBean> itemList) {
        mItemList = itemList;
    }

//    public int getCount() {
//        return mItemList.size();
//    }

    public Object getItem(int i) {
        return mItemList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_item, null);

        ImageView itemImage = view.findViewById(R.id.itemImage);
        GradientDrawable drawable=
                (GradientDrawable) mContext.getDrawable(R.drawable.round_shape_white);
        itemImage.setBackground(drawable);
        itemImage.setClipToOutline(true);

        TextView itemTitle = view.findViewById(R.id.itemTitle);
        TextView itemContent = view.findViewById(R.id.itemContent);
        TextView itemToday = view.findViewById(R.id.itemToday);

        final ItemBean itemBean = mItemList.get(i);

//        // itemImage 이미지를 표시할 때는 원격 서버에 있는 이미지이므로, 비동기로 표시
//        try{
//            if(itemBean.bmpTitle == null) {
//                new DownloadImgTask(mContext, itemImage, mItemList, i).execute(new URL(itemBean.imgUrl));
//            } else {
//                itemImage.setImageBitmap(itemBean.bmpTitle);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

        //ui에 원본 데이터 적용
        itemImage.setImageBitmap(itemBean.bmpTitle);
        itemTitle.setText(itemBean.itemTitle);
        itemContent.setText(itemBean.itemContent);
        itemToday.setText(itemBean.itemToday);
        //StringTokenizer tokens = new StringTokenizer(itemBean.userId);
        //String userId = tokens.nextToken("@");

        //아이템 항목 누르면 수정 페이지로
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoteFix.class);
                intent.putExtra("INDEX", i); //원본데이터의 순번
                intent.putExtra("ITEM", itemBean); //상세표시할 원본 데이터
                intent.putExtra("TITLE", itemBean.itemTitle);
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}