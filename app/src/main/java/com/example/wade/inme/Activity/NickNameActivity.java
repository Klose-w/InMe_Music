package com.example.wade.inme.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.Network.HttpThread;
import com.example.wade.inme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NickNameActivity extends AppCompatActivity {
    private InMeApplicacation inMeApplicacation;
    private EditText et_nickname;
    private Button btn_reg;
    private JSONObject jsonObject;
    private String register_url;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("json");
            Log.d("TTT","___________________"+result);
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject.getString("message").equals("success")){
                    Intent intent = new Intent(NickNameActivity.this,LoginOrRegistActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        register_url= inMeApplicacation.getUrl() +"signup.php";
        setContentView(R.layout.activity_nick_name);

        et_nickname = (EditText) findViewById(R.id.nicheng_et_name);
        btn_reg = (Button) findViewById(R.id.nicheng_btn_start);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> register=new HashMap<String,String>();
                register.put("phone", inMeApplicacation.getPhone());
                register.put("passwd",inMeApplicacation.getPasswd());
                register.put("nickname",et_nickname.getText().toString());
                JSONObject params=new JSONObject(register);

                Log.d("TTT","_________________________________"+params);
                new HttpThread(handler,register_url,params.toString()).start();
            }
        });

    }


}
