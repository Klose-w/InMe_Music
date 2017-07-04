package com.example.wade.inme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.Network.HttpThread;
import com.example.wade.inme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
	InMeApplicacation inMeApplicacation;
	private EditText etName,etPwd;
    private Button btnLogin;
	private TextView tvForgetPwd,tvLogin;
	private JSONObject jsonObject;
	private String login_url ;
	SharedPreferences sp;
	ImageView Iv_back;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String result = bundle.getString("json");
			try {
				jsonObject = new JSONObject(result);
				if(jsonObject.getString("message").equals("success")){
					inMeApplicacation.setPhone(etName.getText().toString());
					SharedPreferences.Editor editor=sp.edit();
					editor.putString("plogin","是");
					editor.putString("pphonenum",etName.getText().toString());
					editor.putString("ppass",etPwd.getText().toString());
					editor.commit();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					setResult(2);
					finish();
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
		setContentView(R.layout.activity_login);
		inMeApplicacation=(InMeApplicacation)getApplicationContext();
		login_url=inMeApplicacation.getUrl() +"login.php";
		sp=getSharedPreferences("person",MODE_WORLD_READABLE);
		if(sp.getString("plogin","否").equals("是")){
			String id=sp.getString("pphonenum","0");
			inMeApplicacation.setPhone(id);
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			setResult(2);
			finish();
		}
		initViews();
		initListeners();


	}

	private void initViews() {
		etName = (EditText) findViewById(R.id.login_et_name);
		etPwd = (EditText) findViewById(R.id.login_et_pwd);
        etName.setText(sp.getString("pphonenum",""));
        etPwd.setText(sp.getString("ppass",""));
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		tvForgetPwd = (TextView) findViewById(R.id.login_tv_forget);
		Iv_back=(ImageView)findViewById(R.id.iv_back);
		Iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initListeners() {
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = etName.getText().toString();
				String pwd = etPwd.getText().toString();

				Map<String,String> login=new HashMap<String,String>();
				login.put("phone", name);
				login.put("passwd",pwd);
				JSONObject params=new JSONObject(login);

				new HttpThread(handler,login_url,params.toString()).start();
			}
		});
		tvForgetPwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,ForgetPwdActivity.class));
			}
		});
		
	}


}
