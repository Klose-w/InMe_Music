package com.example.wade.inme.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.wade.inme.R;


public class ForgetPwdActivity extends Activity {

	private EditText etName,etPwd,etPwdAgain;
    private Button btnNext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		initViews();
		initListeners();
	}

	private void initViews() {
		etName = (EditText) findViewById(R.id.forget_et_name);
		etPwd = (EditText) findViewById(R.id.forget_et_pwd);
		//etPwdAgain = (EditText) findViewById(R.id.regist_et_pwd_again);
		btnNext = (Button) findViewById(R.id.forget_btn_next);
		
		
	}

	private void initListeners() {
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("user.xml", Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				String name = etName.getText().toString();
				String pwd = etPwd.getText().toString();
			
				
				if("".equals(name)){
					etName.setError("请输入账号");
					return;
				}
				if("".equals(pwd)){
					etPwd.setError("请输入密码");
					return;
				}
				
				editor.putString("name", name);
				editor.putString("pwd", name);
				editor.commit();
				finish();
				Intent intent = new Intent(ForgetPwdActivity.this,RegistNextActivity.class);
				startActivity(intent);
						
			}
		});
		
	}

}
