package com.example.wade.inme.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wade.inme.R;

public class WebActivity extends AppCompatActivity {

    WebView Wb_online;
    TextView Tv_news_web;
    ImageButton Ib_backmain_web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Wb_online=(WebView)findViewById(R.id.wb_online);
        Tv_news_web=(TextView)findViewById(R.id.tv_news_web);
        Ib_backmain_web=(ImageButton)findViewById(R.id.ib_backmain_web);
        Ib_backmain_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Wb_online.destroy();
            }
        });
        String url=getIntent().getStringExtra("url");
       // url="http://music.163.com/outchain/player?type=0&id=382240905";
        Tv_news_web.setText(getIntent().getStringExtra("name"));
        Wb_online.getSettings().setJavaScriptEnabled(true);
        Wb_online.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        Wb_online.setWebChromeClient(new WebChromeClient());
        Wb_online.loadUrl(url);

    }
}
