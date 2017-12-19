package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ds on 2017/12/3.
 */

public class ContactListActivity extends Activity{
    private ListView lv_contact;
    protected static final String tag = "ContactListActivity";
    private String[] mPermission;
    private List<HashMap<String,String>> contactList = new ArrayList<HashMap<String,String>>();
    private Cursor indextCursor;
    private MyAdapter mAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //8填充数据适配器
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }

    /**
     *  //获取系统联系人数据的方法
     */
    private void initData() {
        //读取联系人可能是个耗时操作，放到子线程中处理
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                //1获取内容解析器的对象
                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, ConstantValue.REQUEST_CODE_ASK_PERMISSIONS);
                    return;   }
                ContentResolver contentResolver = getContentResolver();


                //2查询系统联系人数据库表的过程（读取联系人权限）
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
               //集合在使用之前首先要清空，否则会数据重复
                contactList.clear();
                //3循环游标，直到没有数据为止
                while (cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Log.i(tag,"id=" +id);
                    //4根据用户唯一性id值查询data表和mimetype表生成的视图，获取data以及mimetype字段
                    indextCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id=?", new String[]{id}, null);
                    //5循环获取每一个联系人的电话号码及姓名及数据类型。
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    while (indextCursor.moveToNext()){
                        String data = indextCursor.getString(0);
                        String type = indextCursor.getString(1);
                      /*  Log.i(tag,"data ="+indextCursor.getString(0));
                        Log.i(tag,"mimetype ="+);*/
                      //6区分类型去给hashMap填充数据
                        if (type.equals("vnd.android.cursor.item/phone_v2")){
                            //数据非空的判断
                            if (!TextUtils.isEmpty(data))
                            hashMap.put("phone",data);
                        }else if (type.equals("vnd.android.cursor.item/name")){
                            if (!TextUtils.isEmpty(data))
                            hashMap.put("name",data);
                        }
                    }
                    indextCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //7,消息机制，发送一个空的消息，告知主线程可以去使用子线程已经填好的数据集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initUI() {
        lv_contact =(ListView)findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1获取点中条目的索引指向集合中的对象
                if (mAdapter!=null){
                    HashMap<String, String> hashMap = mAdapter.getItem(position);
                    //2获取当前条目指向集合对应的电话号码
                    String phone = hashMap.get("phone");
                    //3此电话号码需要给第三个导航界面使用

                    //4在结束此界面回到前一个导航界面的时候，需要将数据返回过去
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);
                    finish();
                }

            }

            });
        }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);
           TextView tv_name =(TextView) view.findViewById(R.id.tv_name);
           TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
           tv_name.setText(getItem(position).get("name"));
           tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }
}
