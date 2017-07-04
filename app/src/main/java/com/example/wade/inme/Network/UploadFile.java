package com.example.wade.inme.Network;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lenovo on 2017/6/28.
 */

public class UploadFile extends Thread {
    final int EVENT_POST_START =1;
    final int EVENT_POST_FAILED =2;
    final int EVENT_POST_SUCCESS =3;
    private Handler mUpdateHandler;
    private String uploadUrl;
    private String srcPath;

    public UploadFile(Handler handler,String uploadUrl,String srcPath){
        mUpdateHandler = handler;
        this.uploadUrl = uploadUrl;
        this.srcPath = srcPath;
    }
    @Override
    public void run() {
        super.run();
        mUpdateHandler.sendEmptyMessage(EVENT_POST_START);
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                    + "\""
                    + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Log.d("dxjia", result);

            mUpdateHandler.sendEmptyMessage(EVENT_POST_SUCCESS);

            dos.close();
            is.close();

        } catch (Exception e) {
            mUpdateHandler.sendEmptyMessage(EVENT_POST_FAILED);
            e.printStackTrace();
        }
    }
}
