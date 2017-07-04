
package com.example.wade.inme.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wade.inme.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChangeSelectActivity extends Activity {

    private static final int TAKE_PICTURE = 520;
    public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
    private Context context;
    private GridView gridview;
    private PictureAdapter adapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();

    /**
     * 具体下载图片，缓存图片，显示图片的具体执行类，它有两个具体的方法displayImage(...)、loadImage(...)，
     */
    private ImageLoader loader;

    /**
     * 用于指导每一个Imageloader根据网络图片的状态（空白、下载错误、正在下载）显示对应的图片，
     * 是否将缓存加载到磁盘上，下载完后对图片进行怎么样的处理。
     */
    private DisplayImageOptions options;

    private ContentResolver mContentResolver;

    //选择按钮和完成按钮
    private FolderAdapter folderAdapter;

    private ImageFloder imageAll, currentImageFolder;

    private String avatarPath = null;
    private String cameraPath = null;

    private int isExisted ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_avatar);

        context = this;
        mContentResolver = getContentResolver();
        loader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)//设置图片在下载期间显示的图片

                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片

                .showImageOnFail(R.mipmap.ic_launcher)//设置图片加载/解码过程中错误时候显示的图片

                .cacheInMemory(true)//设置下载的图片是否缓存在内存中

                .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中

                .considerExifParams(true)//启用EXIF和JPEG图像格式

                .imageScaleType(ImageScaleType.EXACTLY)//图像将完全按比例缩小的目标大小

                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型

                .build();

        initView();
    }


    /**
     * 视图初始化
     */
    private void initView() {
        imageAll = new ImageFloder();
        imageAll.setDir(getResources().getString(R.string.dir_all_pictures));
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);

        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new PictureAdapter();
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    goCamare();
                }
            }
        });

        getThumbnail();
    }


    protected void goCamare() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri imageUri = getOutputMediaFileUri();

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 用于拍照时获取输出的Uri
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && cameraPath != null) {

            avatarPath = cameraPath;
            Intent data2 = new Intent();
            data2.putExtra(INTENT_SELECTED_PICTURE, avatarPath);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }


    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //自定义的一个类用来缓存convertview
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.grid_item_picture2, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == 0) {//拍摄图片
                holder.iv.setImageResource(R.drawable.pickphotos_to_camera_normal);
            } else {
                position = position - 1;
                final ImageItem item = currentImageFolder.images.get(position);
                //显示图片
                loader.displayImage("file://" + item.path, holder.iv, options);

                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Crop.pickImage(ChangeSelectActivity.this);
                        avatarPath = item.path;
                        Intent data = new Intent();
                        data.putExtra(INTENT_SELECTED_PICTURE, item.path);
                        setResult(RESULT_OK, data);
                        Log.d("TTT",avatarPath);
                        Toast.makeText(ChangeSelectActivity.this,avatarPath,Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
            }
            return convertView;
        }
    }

    //自定义的一个类用来缓存convertview
    class ViewHolder {
        ImageView iv;
    }

    /**
     * 所有图片
     */
    class FolderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.list_dir_item, null);
                holder = new FolderViewHolder();
                holder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
                holder.choose = (ImageView) convertView.findViewById(R.id.choose);
                convertView.setTag(holder);
            } else {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFloder item = mDirPaths.get(position);
            loader.displayImage("file://" + item.getFirstImagePath(), holder.id_dir_item_image, options);
            holder.id_dir_item_count.setText(item.images.size() + getResources().getString(R.string.sheet));
            holder.id_dir_item_name.setText(item.getName());

            holder.choose.setVisibility(currentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    //自定义的一个类用来缓存convertview
    class FolderViewHolder {
        ImageView id_dir_item_image;
        ImageView choose;
        TextView id_dir_item_name;
        TextView id_dir_item_count;
    }

    /**
     * 获取缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");

        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {

                // 获取图片的路径
                String path = mCursor.getString(_date);
                imageAll.images.add(new ImageItem(path));

                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }

                ImageFloder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();

                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }

                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        tmpDir = null;
    }

    class ImageFloder {
        /**
         * 图片的文件夹路径
         */
        private String dir;
        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {
        String path;

        public ImageItem(String p) {
            this.path = p;
        }
    }

}
