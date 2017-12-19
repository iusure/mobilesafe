package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.StreamUtil;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    /**
     * 更新新版本状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面 状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * URL地址出错状态码
     */
    private static final int URL_ERROR = 102;
    /**
     * IO
     */
    private static final int IO_ERROR = 103 ;
    private static final int JSON_ERROR = 104 ;
    private TextView tv_version_name;
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;
    private int mLocalVersionCode;
    protected static final String tag = "SplashActivity";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对话框，提示更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                   //进入应用程序主界面，Activity跳转过程
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(SplashActivity.this,"读取异常");
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this,"url异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this,"json解析异常");
                    enterHome();
                    break;
            }
        }
    };


    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框是依赖于Activity存在的
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setIcon(R.drawable.ic_launcher);
        bulider.setTitle("版本更新");
        //设置描述内容
        bulider.setMessage(mVersionDes);
        //积极按钮,立即更新
        bulider.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk，apk链接地址downUrl
                downloadApk();
            }
        });
        bulider.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
            }
        });
        //点击取消事件监听
        bulider.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //即使用户点击取消，也需要让其进入我们主界面
                enterHome();
                dialog.dismiss();
            }
        });
        bulider.show();
    }

    private void downloadApk() {
        //apk链接地址，放置apk的所在路径

        //1，判断sd卡是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2,获取sd卡的路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
                    File.separator+"mobilesafe.apk";
            //3,发送请求，获取apk，并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4,发送请求，传递参数（下载地址，下载应用放置的位置）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载过后的放在sd卡中的apk)
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                }
                //刚刚开始下载方法

                @Override
                public void onStart() {
                    Log.i(tag,"刚刚开始下载");
                    super.onStart();
                }
                //下载过程中的方法(下载apk总大小，当前下载位置，是否正在下载)

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag,"下载中。。。");
                    Log.i(tag,"total"+total);
                    Log.i(tag,"current"+current);
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应apk
     * @param file 安装文件
     */
    private void installApk(File file) {
        //系统应用界面，源码，安装apk的入口
       Intent intent = new Intent("android.intent.action.VIEW" );
       intent.addCategory("android.intent.category.DEFAULT");
/*       //文件作为数据源
       intent.setData(Uri.fromFile(file));
       //设置安装的类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
       // startActivity(intent);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面后将导航界面关闭（导航界面只可见一次）
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉当前Activity头title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
        //初始化数据库
        initDB();

    }

    private void initDB() {
        //1归属地的数据库的拷贝过程
        initAddressDB("address.db");
    }

    /**
     * 拷贝数据库值到files文件夹下
     * @param dbName 数据库名称
     */
    private void initAddressDB(String dbName) {

       /* getCacheDir();
        Environment.getExternalStorageDirectory().getAbsolutePath();*/
        //1，files文件夹下创建同名数据库文件的过程
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()){
            return;
        }
        InputStream stream = null;
        FileOutputStream fos = null;
        //2,输入流读取第三方资产目录下的文件
        try {

             stream = getAssets().open(dbName);
            //3将读取到的内容写入到指定文件夹文件中去
             fos = new FileOutputStream(file);
            //4每次读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp=stream.read(bs))!=-1){
                fos.write(bs,0,temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (stream!=null && fos != null){
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *添加淡入初始化动画
     */
    private void initAnimation() {
       AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取数据方法
     */
    private void initData() {
        //1,应用版本名称
        tv_version_name.setText("版本名称:"+getVersionName());
        //检测（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载（member）
        //2,获取本地版本号
        mLocalVersionCode = getVersionCode();
        //3,获取服务器版本号（客户端发请求，服务端给响应（json，xml））
        //http://www.ooxx.com//update.json?key=value 返回200 请求成功，流的方式将数据读取下来
        //json中内容包含
        /**
         * 更新版本的版本名称
         * 新版本的描述信息
         * 服务器版本号
         *新版本apk的下载地址
         */
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }else {
            //直接进入主界面
            //enterHome();
            //消息机制
            //mHandler.sendMessageDelayed(msg,4000);
            //发送消息4s后去处理当前ENTER_HOME状态码所对应的消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }


    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread(){
            public void run(){
                //发送请求获取数据 参数则为请求json的链接地址
                //http://169.254.79.234:8080/update.json  测试阶段不是最优
                //http://10.0.2.2:8080/update.json
                //仅限于模拟器访问电脑tomcat
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1,封装url地址
                    URL url = new URL("http://10.0.2.2:8080/update.json ");
                    //2,开启一个链接

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                     //3,设置常见请求参数（请求头）
                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //默认是get请求方式
                  //  connection.setRequestMethod("");
                    //4,获取请求成功响应码
                    if (connection.getResponseCode() == 200){
                        //5,以流的形式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6,将流转换成字符串（工具类封装），
                       String json =  StreamUtil.stream2String(is);
                        Log.i(tag,json);
                        //7.json解析
                       JSONObject jsonObject = new JSONObject(json);
                       String versionName =jsonObject.getString("versionName");
                       mVersionDes = jsonObject.getString("versionDes");
                       String versionCode = jsonObject.getString("versionCode");
                       mDownloadUrl = jsonObject.getString("downloadUrl");
                        //debug
                       Log.i(tag,versionName);
                       Log.i(tag,mVersionDes);
                       Log.i(tag,versionCode);
                       Log.i(tag,mDownloadUrl);

                       //8,比对版本号(服务器版本号大于本地版本号，提示更新)
                        if (mLocalVersionCode < Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框（UI），消息机制
                            msg.what = UPDATE_VERSION;
                        }else {
                            //进入应用程序主界面
                            msg.what = ENTER_HOME;
                        }

                    }
                    } catch ( MalformedURLException e) {
                        e.printStackTrace();
                        msg.what = URL_ERROR;
                    } catch ( IOException e) {
                    msg.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    //指定睡眠时间，请求网络的时长超过4s则不做处理
                    //请求网络的时长小于4s，强制睡眠让其满4s
                    long endTime = System.currentTimeMillis();
                    if (endTime-startTime < 4000){
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }

            }

        }.start();
 /*       new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }) ;*/
    }

    /**
     *返回版本号
     * @return
     * 非0 代表获取成功
     */
    private int getVersionCode() {
        //1,包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取版本名称:清单文件中
     * @return	应用版本名称	返回null代表异常
     */
    private String getVersionName() {
        //1,包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI方法
     */
    private void initUI() {
        //noinspection deprecation
        tv_version_name =  findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout)findViewById(R.id.rl_root);
    }
}
