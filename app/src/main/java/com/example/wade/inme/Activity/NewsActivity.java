package com.example.wade.inme.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wade.inme.JavaBean.NewsBean;
import com.example.wade.inme.R;

public class NewsActivity extends AppCompatActivity {

    TextView Tv_title_newsA;
    TextView Tv_time_news;
    TextView Tv_zhengwen_news;
    TextView Tv_news_t;
    NewsBean newsBean;
    WebView view;
    ImageButton Ib_backmain_news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle b=getIntent().getExtras();
        newsBean=(NewsBean)b.getSerializable("news");
        Tv_title_newsA=(TextView)findViewById(R.id.tv_title_newsA);
        Tv_time_news=(TextView)findViewById(R.id.tv_time_news);
        Tv_zhengwen_news=(TextView)findViewById(R.id.tv_zhengwen_news);
        Tv_news_t=(TextView)findViewById(R.id.tv_news_tt);
        Ib_backmain_news=(ImageButton)findViewById(R.id.ib_backmain_news);
        Ib_backmain_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Tv_title_newsA.setText(newsBean.getTitle());
        Tv_time_news.setText(newsBean.getPubDate()+"  "+newsBean.getComeform());
        Tv_zhengwen_news.setText("");
        Tv_news_t.setText(newsBean.getChannelName());
        view=(WebView)findViewById(R.id.wb_news);
        view.loadDataWithBaseURL("about:blank",newsBean.getDesc(),"text/html","utf-8",null);
    }
}
