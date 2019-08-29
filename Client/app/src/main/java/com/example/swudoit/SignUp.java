package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    boolean isUser;

    EditText idE;

    EditText passE;

    EditText passConfirmE;

    ImageButton sighup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idE = (EditText) findViewById(R.id.idEdt);
        passE = (EditText) findViewById(R.id.passEdit);
        passConfirmE = (EditText) findViewById(R.id.passEditConfirm);

        sighup = (ImageButton)findViewById(R.id.signup);

        ImageButton.OnClickListener clickListener = new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("버튼", "클릭");
                Toast.makeText(SignUp.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                Intent signupView = new Intent(SignUp.this, MainActivity.class);

                startActivity(signupView);
            }

        };

        sighup.setOnClickListener(clickListener);

    }

}
