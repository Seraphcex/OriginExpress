package com.origin.originexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化及绑定控件
        init();
        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //核验用户信息


                //跳转
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void init(){

        EditText et_userName_input = findViewById(R.id.et_userName);
        EditText et_userPwd_input = findViewById(R.id.et_userPwd);
        ImageView iv_userName_clear = findViewById(R.id.iv_unameClear);
        ImageView iv_userPwd_clear = findViewById(R.id.iv_upwdClear);

        EditTextClearTool.addClearListener(et_userName_input,iv_userName_clear);
        EditTextClearTool.addClearListener(et_userPwd_input,iv_userPwd_clear);

    }

}
