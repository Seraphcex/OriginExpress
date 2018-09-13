package com.origin.originexpress.operate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.origin.originexpress.R;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.data.source.WaybillRepository;
import com.origin.originexpress.utils.ActivityUtils;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

public class OperateActivity extends AppCompatActivity {

    private OperateContract.Presenter mPresenter;

    private WaybillRepository mWaybillRepository;

    private String mMd5WaybillNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        mMd5WaybillNumber = getIntent().getStringExtra(Waybill.MD5_WAYBILL_NUMBER_NAME);

        OperateFragment operateFragment = (OperateFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(operateFragment == null){
            operateFragment = OperateFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), operateFragment, R.id.contentFrame);
        }

        mWaybillRepository = new WaybillRepository();

        mPresenter = new OperatePresenter(mMd5WaybillNumber, operateFragment, mWaybillRepository);


    }

}
