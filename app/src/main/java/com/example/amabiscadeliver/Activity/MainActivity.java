package com.example.amabiscadeliver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.amabiscadeliver.Connect.GlobalVarLog;
import com.example.amabiscadeliver.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    GlobalVarLog var =   GlobalVarLog.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation();
    }

    private void bottomNavigation(){
        //Navigation Buttons
        NavMenu();

        //Menu Buttons
        Menu();
    }

    private void Menu(){
        ConstraintLayout accountBtn = findViewById(R.id.accountBtn);
        ConstraintLayout orderBtn = findViewById(R.id.storeBtn2);
        ConstraintLayout infoBtn = findViewById(R.id.infoBtn2);

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var.get_codigo() != 0) {
                    startActivity(new Intent(MainActivity.this, EditAccountActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"INICIA SESIÓN PRIMERO",Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var.get_codigo() != 0) {
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"INICIA SESIÓN PRIMERO",Toast.LENGTH_SHORT).show();
                }
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoButton();
            }
        });
    }

    private void NavMenu(){
        LinearLayout callBtn = findViewById(R.id.callBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout infoBtn = findViewById(R.id.infoBtn1);
        LinearLayout orderBtn = findViewById(R.id.storeBtn1);
        FloatingActionButton logBtn = findViewById(R.id.logbtnmain);;

        if(var.get_codigo() == 0){
            logBtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(91,140,90)));
        }else{
            logBtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(219,29,39)));
        }

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var.get_codigo() == 0) {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                    finish();
                }else{
                    var.restart();
                    logBtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(91,140,90)));
                    Toast.makeText(getApplicationContext(),"SESIÓN CERRADA",Toast.LENGTH_SHORT).show();
                }
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoButton();
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var.get_codigo() != 0) {
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"INICIA SESIÓN PRIMERO",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InfoButton(){
        Uri _link = Uri.parse(var.get_URLrepository());
        Intent i = new Intent(Intent.ACTION_VIEW,_link);
        startActivity(i);
    }

    private void CallButton(){
        String phoneNumber = "46463819";
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            String dial = "tel:"+phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallButton();
            }else{
                Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

}