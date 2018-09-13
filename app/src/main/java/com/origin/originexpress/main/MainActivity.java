package com.origin.originexpress.main;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.origin.originexpress.R;
import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.data.source.RecordRepository;
import com.origin.originexpress.data.source.TransportRepository;
import com.origin.originexpress.operate.OperateActivity;
import com.origin.originexpress.utils.ActivityUtils;
import com.yzq.zxinglibrary.common.Constant;

public class MainActivity extends AppCompatActivity {

    private MainContract.Presenter mPresenter;


    private int mDentityCode;

    private MainFragment mMainFragment;


    private static final int REQUEST_CODE_SCAN = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDentityCode = getIntent().getIntExtra(User.IDENTITY_CODE_NAME, User.DATA_NULL);

        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(mMainFragment == null){
            mMainFragment = MainFragment.newInstance(mDentityCode);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mMainFragment, R.id.contentFrame);
        }

        RecordRepository recordRepository = new RecordRepository();

        TransportRepository transportRepository = new TransportRepository();

        InputMegRepository inputMegRepository = new InputMegRepository();

        mPresenter = new MainPresenter(mMainFragment, recordRepository, transportRepository,inputMegRepository, mDentityCode);

    }

    //扫描结果返回处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            String md5WaybillNumbner = mPresenter.getMd5WaybillNumber(content);
            if(md5WaybillNumbner == ""){
                mMainFragment.showErrorMeg("错误的二位码");
            }else{
                Log.d(Waybill.MD5_WAYBILL_NUMBER_NAME, md5WaybillNumbner);
                mPresenter.onAction(md5WaybillNumbner);
            }

        }
    }


}
