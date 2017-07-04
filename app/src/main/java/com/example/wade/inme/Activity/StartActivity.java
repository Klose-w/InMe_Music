package com.example.wade.inme.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.GdJsonArray;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(StartActivity.this, LoginOrRegistActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
