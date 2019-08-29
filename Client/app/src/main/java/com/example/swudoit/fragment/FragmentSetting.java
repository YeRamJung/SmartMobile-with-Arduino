package com.example.swudoit.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.swudoit.MainActivity;
import com.example.swudoit.R;
import com.example.swudoit.SettingInfoActivity;
import com.example.swudoit.SignUp;

public class FragmentSetting extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        //회원 정보 레이아웃 눌러 페이지 이동
        LinearLayout infoLayout = view.findViewById(R.id.infoLayout);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SettingInfoActivity.class);
                startActivity(i);
            }
        });


        //로그아웃 레이아웃 눌러 다이얼로그 띄우기
        LinearLayout logoutLayout = view.findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());

                ab.setTitle("로그아웃 확인");
                ab.setMessage("로그아웃 하시겠습니까?");

                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), SignUp.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                ab.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getActivity(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                ab.create().show();
            }
        });

        //탈퇴 레이아웃 눌러 다이얼로그 띄우기
        LinearLayout signoutLayout = view.findViewById(R.id.signoutLayout);
        signoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                final EditText edittext = new EditText(getActivity());

                ab.setTitle("탈퇴하시겠습니까?");
                ab.setMessage("비밀번호 확인");
                ab.setView(edittext);

                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    /*입력받은 비밀번호랑 실제 회원 비밀번호랑 같은지 확인하기*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                ab.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getActivity(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                ab.create().show();
            }
        });

        return view;
    }
}