package com.example.amabiscadeliver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import com.example.amabiscadeliver.R;

public class IntroActivity extends AppCompatActivity {
    private ConstraintLayout startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        startBtn=findViewById(R.id.startbtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}