package com.example.wade.inme.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.Network.UploadFile;
import com.example.wade.inme.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.data;

public class ChangeAvatarActivity extends Activity{
    InMeApplicacation inMeApplicacation;
	private ImageView img;
    private static final int REQUEST_PICK = 0;
	public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
    final int EVENT_POST_START =1;
    final int EVENT_POST_FAILED =2;
    final int EVENT_POST_SUCCESS =3;
    private static final String actionUrl = "http://115.159.188.113/ListenerMusic/uploadavatar.php";
    /**
     * UI update handler
     */
    private class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_POST_START:
                    Toast.makeText(ChangeAvatarActivity.this,"Started! uploading...",Toast.LENGTH_LONG).show();
                    break;
                case EVENT_POST_FAILED:
                    Toast.makeText(ChangeAvatarActivity.this,"upload failed!",Toast.LENGTH_LONG).show();

                    break;
                case EVENT_POST_SUCCESS:
                    Toast.makeText(ChangeAvatarActivity.this,"upload done!",Toast.LENGTH_LONG).show();

                    break;
            }

            super.handleMessage(msg);
        }
    }

    UpdateHandler updateHandler = new UpdateHandler();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_changeavatar);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
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

		img = (ImageView)findViewById(R.id.photo);
		Glide.with(ChangeAvatarActivity.this).load(inMeApplicacation.getUrl()+inMeApplicacation.getUserBean().getAvatar()).into(img);
		//img.setImageResource(R.drawable.a8x);
		
		Button  bt1 =(Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectClick();
			}
		});
		
		
		

	}
	private void selectClick(){
		Intent intent = new Intent();
		intent.setClass(ChangeAvatarActivity.this , ChangeSelectActivity.class);
        startActivityForResult(intent, REQUEST_PICK);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode==REQUEST_PICK&&resultCode == RESULT_OK) {
			String string = data.getStringExtra(INTENT_SELECTED_PICTURE);

            beginCrop(getImageContentUri(this,string));
		}
		else {
            handleCrop(resultCode, data);
		}
	}

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), inMeApplicacation.getPhone()+".jpg"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Log.d("TTT",Crop.getOutput(result).toString());
            String img_path = getRealFilePath(this,Crop.getOutput(result));
            Glide.with(ChangeAvatarActivity.this).load(img_path).into(img);
            new UploadFile(updateHandler,actionUrl,img_path).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        }
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
