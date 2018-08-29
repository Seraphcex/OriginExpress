package com.origin.originexpress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

public class OperateActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCAN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        //绑定控件
        ImageView iv_scan_icon = findViewById(R.id.iv_scan_icon);

        //扫描点击事件
        iv_scan_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //检查是否有相机权限，有则加载相机，没有则申请
                if(ContextCompat.checkSelfPermission(OperateActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(OperateActivity.this,
                            new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
                }
                else{
                    loadCamera();
                }

            }
        });

    }

    //申请权限返回结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA_PERMISSION: {
                //
                if(grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"申请权限失败", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadCamera();
                }
                break;
            }
        }
    }

    //扫描结果返回处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK){
            if(data != null){
                //获取扫描结果
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(this,content,Toast.LENGTH_LONG).show();
            }
        }
    }

    //相机跳转
    public void loadCamera(){
        Intent intent = new Intent(OperateActivity.this, CaptureActivity.class);
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);//是否播放提示音
        config.setShake(true);//是否震动
        config.setShowAlbum(false);//是否显示相册
        config.setShowFlashLight(true);//是否显示闪光灯
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

}
