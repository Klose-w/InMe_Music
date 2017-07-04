package com.example.wade.inme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.R;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;


public class RegistNextActivity extends Activity {

	InMeApplicacation inMeApplicacation;
	ImageView Iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inMeApplicacation=(InMeApplicacation)getApplicationContext();
		setContentView(R.layout.activity_regist_next);
		Iv_back=(ImageView)findViewById(R.id.iv_back);
		Iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final String phone = inMeApplicacation.getPhone();

		Log.i("bmob",phone);

		BmobSMS.initialize(this, "af23fd0984dc21e3d842df6e3c56587a");

		if(phone.length()!=11)
		{
			Toast.makeText(this,"电话号码错误",Toast.LENGTH_LONG).show();
			return;
		}

		BmobSMS.requestSMSCode(RegistNextActivity.this, phone, "senttest", new RequestSMSCodeListener() {
			@Override
			public void done(Integer integer,BmobException e) {
				if(e==null){
					Log.i("bmob", "短信id：" + integer);//用于查询本次短信发送详情
					Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_LONG).show();
				}else{
                    Log.i("bmob","++++++++++++++++++++="+e.getErrorCode());
					Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
		});


		final EditText editText = (EditText) findViewById(R.id.login_et_name);
		Button button = (Button) findViewById(R.id.login_btn_login);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BmobSMS.verifySmsCode(RegistNextActivity.this,phone, editText.getText().toString(), new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						// TODO Auto-generated method stub
						if(ex==null){//短信验证码已验证成功
							Toast.makeText(getApplicationContext(),"验证成功",Toast.LENGTH_LONG).show();
							Intent intent =new Intent(RegistNextActivity.this,NickNameActivity.class);
							startActivity(intent);
						}else{
							Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
						}
					}
				});
			}
		});

	}
}
