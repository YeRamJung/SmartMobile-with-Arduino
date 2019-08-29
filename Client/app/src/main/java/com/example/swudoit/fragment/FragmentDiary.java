package com.example.swudoit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.swudoit.Note;
import com.example.swudoit.R;
import com.example.swudoit.SettingInfoActivity;

public class FragmentDiary extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

//        //등록 버튼 눌러서 등록 페이지 이동
//        Button btnMemoReg = view.findViewById(R.id.btnMemoReg);
//        btnMemoReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), Note.class);
//                startActivity(i);
//            }
//        });


        return view;

    }
}
