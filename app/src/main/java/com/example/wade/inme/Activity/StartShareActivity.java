package com.example.wade.inme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.Network.HttpThread;
import com.example.wade.inme.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartShareActivity extends Activity {

    private GridView gridview;

    private EditText editText ;

    private GridAdapter gridAdapter;

    private Context context ;

    private static final int REQUEST_PICK = 0;

    private TextView tv_submit;

    private ImageView img_back;

    //存放所有选择的照片
    private ArrayList<String> allSelectedPicture = new ArrayList<String>();

    //存放从选择界面选择的照片
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    InMeApplicacation inMeApplicacation;
    private JSONObject jsonObject;
    private String startshare_url ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("json");
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject.getString("message").equals("success")){
                    Toast.makeText(StartShareActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_startshare);
        context = this ;
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        startshare_url= inMeApplicacation.getUrl() +"start_share.php";
        //设置在activity启动的时候输入法默认是不开启的
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置当前线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5对UIL进行加密命名
                .diskCacheSize(100 * 1024 * 1024)//50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(300)// 可以缓存的文件数量
                .tasksProcessingOrder(QueueProcessingType.LIFO)//后进先出
                .build();
        //初始化操作
        ImageLoader.getInstance().init(config);

        initView();
    }

    /**
     * 初始化视图
     */
    private void initView(){

        img_back = (ImageView) findViewById(R.id.iv_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText = (EditText) findViewById(R.id.editText);

        tv_submit = (TextView) findViewById(R.id.send_tv);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert
                Map<String,String> startsharemap=new HashMap<String,String>();
                startsharemap.put("user_id",inMeApplicacation.getUserBean().getId());
                startsharemap.put("share_type","1");
                startsharemap.put("content_text",editText.getText().toString());
                JSONObject params=new JSONObject(startsharemap);

                new HttpThread(handler,startshare_url,params.toString()).start();
            }
        });
        //显示图片
        gridview = (GridView) findViewById(R.id.gridview);

        gridAdapter = new GridAdapter();

        gridview.setAdapter(gridAdapter);
    }

    /**
     * 展示图片的GridView的适配器
     */
    class GridAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater = LayoutInflater.from(context);
        @Override
        public int getCount() {
            return allSelectedPicture.size()+ 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null ;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.childgrid_item, null);

                holder.image = (ImageView) convertView.findViewById(R.id.child_iv);
                holder.btn = (Button) convertView.findViewById(R.id.child_delete);

                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == allSelectedPicture.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.icon_addpic));
                holder.btn.setVisibility(View.GONE);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectClick();
                    }
                });
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                ImageLoader.getInstance().displayImage("file://" + allSelectedPicture.get(position),
                        holder.image);

                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        allSelectedPicture.remove(position);

                        //更新UI
                        gridview.setAdapter(gridAdapter);
                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }

    private void selectClick(){
        Intent intent = new Intent();
        intent.setClass(StartShareActivity.this , SelectPictureActivity.class);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("allSelectedPicture", allSelectedPicture);
        intent.putExtras(bundle);

        if(allSelectedPicture.size()< 9){
            startActivityForResult(intent, REQUEST_PICK);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            for(String str : selectedPicture){
                if(!allSelectedPicture.contains(str)){
                    allSelectedPicture.add(str);

                    gridview.setAdapter(gridAdapter);
                }
            }
        }
    }
}
