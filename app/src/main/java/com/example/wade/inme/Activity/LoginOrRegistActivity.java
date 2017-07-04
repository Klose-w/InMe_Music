package com.example.wade.inme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.R;


public class LoginOrRegistActivity extends Activity {

	private Button btnstartlogin,btnstartregist;
	InMeApplicacation inMeApplicacation;
	SharedPreferences sp;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}

		setContentView(R.layout.activity_loginorregist);
		inMeApplicacation=(InMeApplicacation)getApplicationContext();
		sp=getSharedPreferences("person",MODE_WORLD_READABLE);
		if(sp.getString("plogin","否").equals("是")){
			String id=sp.getString("pphonenum","0");
			inMeApplicacation.setPhone(id);
			Intent intent = new Intent(LoginOrRegistActivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}
		initViews();
		initListeners();
	}

	

	private void initViews() {
		btnstartlogin = (Button) findViewById(R.id.start_btn_login);
		btnstartregist = (Button) findViewById(R.id.start_btn_regist);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==2){
			Log.e("hhhhh","dfdfds");
			this.finish();
		}
	}

	private void initListeners() {
		btnstartlogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentLogin = new Intent(LoginOrRegistActivity.this,LoginActivity.class);
				startActivityForResult(intentLogin,117);
			}
		});
		
		btnstartregist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentRegist = new Intent(LoginOrRegistActivity.this,RegistActivity.class);
				startActivity(intentRegist);
			}
		});
		
	}


}
