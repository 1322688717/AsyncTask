package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn_one;
    TextView tv_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_one =findViewById(R.id.button);
        tv_one = findViewById(R.id.tv_one);

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化一个MyAsynTask
                MyAsynTask task = new MyAsynTask();
                //执行task   或者说是  启动这个异步请求
                task.execute("http://wthrcdn.etouch.cn/weather_mini?city");

            }
        });

    }
    class MyAsynTask extends AsyncTask<String,Void,String>{

        /**
         * 这个方法可以要可以不要   是在doin之前的准备方法
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("TAG","这里执行了onPreExecute");
        }

        /**
         * 请求服务器
         * 相当于在线程中get请求
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            Log.e("TAG","这里执行了doInBackground");
            try {
                //实例化一个URL对象
                URL url = new URL(strings[0]);
                try {
                    //获取HttpURLConnection实例
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    //设置和请求相关的属性
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(6000);
                    //获取响应码并获取响应数据
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        //实例化一个响应流
                        InputStream in = conn.getInputStream();
                        //实例化一个数组
                        byte[] b = new byte[1024];
                        //int一个长度
                        int len = 0;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //将字节数组里面的内容写入缓存流
                        while((len = in.read(b))>-1){
                            //参数一：待写入的数组   参数二：起点    参数三：长度
                            baos.write(b,0,len);
                        }
                        //在控制台上显示出获取的数据
                        String msg = new String(baos.toByteArray());
                        Log.e("TAG",msg+"==========");
                        return msg;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 最后要显示在UI上的方法
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("TAG","这里执行了onPostExecute");
            if (s!=null){
                tv_one.setText(s);
            }
        }
    }
}