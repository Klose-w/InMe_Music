package com.example.wade.inme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.R;


public class RegistActivity extends Activity {
	InMeApplicacation inMeApplicacation;
	private EditText etName,etPwd,etPwdAgain;
    private Button btnRegist;
	ImageView Iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		inMeApplicacation=(InMeApplicacation)getApplicationContext();
		initViews();
		initListeners();
	}

	private void initViews() {
		etName = (EditText) findViewById(R.id.regist_et_name);
		etPwd = (EditText) findViewById(R.id.regist_et_pwd);
		//etPwdAgain = (EditText) findViewById(R.id.regist_et_pwd_again);
		btnRegist = (Button) findViewById(R.id.regist_btn_regist);
		Iv_back=(ImageView)findViewById(R.id.iv_back);
		Iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void initListeners() {
		btnRegist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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

				//MyApplication application = MyApplication.getApplication();
				inMeApplicacation.setPhone(name);
				inMeApplicacation.setPasswd(pwd);

				Intent intent = new Intent(RegistActivity.this,RegistNextActivity.class);
				startActivity(intent);
						
			}
		});
		
	}


}
