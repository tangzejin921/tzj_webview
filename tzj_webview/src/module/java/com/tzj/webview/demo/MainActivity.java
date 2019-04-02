package com.tzj.webview.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tzj.webview.R;
import com.tzj.webview.TzjWebViewActivity;
import com.tzj.webview.entity.UserInfo;
import com.tzj.webview.js.JSUserInfo;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText url,idNo,name,phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        url = findViewById(R.id.url);
        idNo = findViewById(R.id.idNo);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        JSUserInfo.login = new JSUserInfo.IUserLogin() {
            @Override
            public boolean isLogin() {
                return false;
            }

            @Override
            public UserInfo userInfo() {
                UserInfo userInfo = new UserInfo();
                userInfo.setIdNo(idNo.getText().toString());
                userInfo.setName(name.getText().toString());
                userInfo.setPhone(phone.getText().toString());
                userInfo.setcIdNo(idNo.getText().toString());
                userInfo.setcName(name.getText().toString());
                userInfo.setcPhone(phone.getText().toString());
                return userInfo;
            }

            @Override
            public void toLogin(Context ctx) {
                Toast.makeText(ctx,"请求登录",Toast.LENGTH_LONG).show();
            }

            @Override
            public void loginResult(boolean b) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        TzjWebViewActivity.start(this,url.getText().toString());
    }
}
