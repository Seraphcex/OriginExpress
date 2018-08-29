package com.origin.originexpress;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //替换toolbar以及设置
        Toolbar toolbar = findViewById(R.id.tb_activity_setting);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_36);
        toolbar.setTitle(R.string.app_name);

        //绑定控件
        LinearLayout ll_item_license = findViewById(R.id.ll_driver_info_block);
        LinearLayout ll_item_about = findViewById(R.id.ll_about_block);
        Button btn_logout = findViewById(R.id.btn_logout);


        //toolbar导航按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //车牌菜单项的点击事件以及弹窗
        ll_item_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingActivity.this);

                dialogBuilder.setIcon(R.drawable.license);
                dialogBuilder.setTitle("请输入车牌号码");

                View licenseView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.alertdialog_license,null);
                dialogBuilder.setView(licenseView);

                //从服务器获取用户对应的车牌号
                EditText et_license_input = licenseView.findViewById(R.id.et_license);
                et_license_input.setText("");

                //保存点击事件
                dialogBuilder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //取消点击事件
                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.show();
            }
        });

        //关于跳转
        ll_item_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        //登出跳转
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
