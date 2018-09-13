package com.origin.originexpress.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.data.source.RecordRepository;
import com.origin.originexpress.data.source.TransportRepository;
import com.origin.originexpress.operate.OperateActivity;
import com.origin.originexpress.utils.GeyCallback;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

/**
 * Created by XunselF on 2018/9/3.
 */

public class MainPresenter implements MainContract.Presenter {

    //视图
    private MainContract.View mView;

    //身份参数
    private int mDentityCode;

    //行为码
    private int mActionCode;

    //车牌
    private String mLicense;

    private SharedPreferences mSharedPreferences;

    private RecordRepository mRecordRepository;

    private TransportRepository mTransportRepository;

    private InputMegRepository mInputMegRepository;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MainPresenter(MainContract.View mainView, RecordRepository recordRepository,TransportRepository transportRepository,InputMegRepository inputMegRepository, int dentityCode){
        this.mView = mainView;
        this.mDentityCode = dentityCode;
        this.mRecordRepository = recordRepository;
        this.mInputMegRepository = inputMegRepository;
        this.mTransportRepository = transportRepository;
        this.mView.setPresenter(this);
    }

    public void setmSharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    //展示页面
    @Override
    public void start() {
        //分发页面
        isDentity(mDentityCode);
        if(mDentityCode == User.USER_IDENTITY_DRIVER){getTransport(mSharedPreferences.getString(User.USERNAME_NAME, ""));}
    }

    public void setmActionCode(int mActionCode) {
        this.mActionCode = mActionCode;
    }

    //设置车牌号
    public void setmLicense(String mLicense) {
        this.mLicense = mLicense;
    }

    //获取车牌号
    public String getmLicense() {
        return mLicense;
    }

    @Override
    public void onAction(String md5waybillnumber) {
        if(mActionCode == Waybill.ACTION_SEARCH){
            mView.onJumpOperate(md5waybillnumber);
        }else{
            operate(md5waybillnumber);
        }
    }

    @Override
    public void getTransport(String username) {
        mView.showLoading();
        mTransportRepository.getTransport(username, new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(final Object o) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Car car = (Car) o;
                        mView.showTransport(car);
                    }
                });
            }

            @Override
            public void onDataNotAvailable(final String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showErrorMeg(error);
                    }
                });
            }
        });
    }

    public int getmActionCode() {
        return mActionCode;
    }

    @Override
    public void setLocation(Car car) {
        mInputMegRepository.submitLocation(car, new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(Object o) {
                Car car = (Car) o;
                Log.d("位置成功更新", car.getStatus() + "");
            }

            @Override
            public void onDataNotAvailable(String error) {

                Log.d("位置更新失败", error);
            }
        });
    }

    //进行收件操作
    public void operate(String md5waybillnumber){
        mView.showLoading();
        Record record = new Record();
        record.setMd5waybillnumber(md5waybillnumber);
        record.setManager(mSharedPreferences.getString(User.FULLNAME_NAME,""));
        record.setPhone(mSharedPreferences.getString(User.PHONE_NAME, ""));
        record.setLicense(mLicense);
        record.setActionCode(mActionCode);
        record.setLocation(mSharedPreferences.getString(User.DEPARTMENT_NAME, ""));

        mRecordRepository.operateWaybill(record, new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(final Object o) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Record record = (Record) o;
                        int resultCode = record.getResultCode();
                        if(resultCode == Record.RESULT_OK){
                            mView.onSuccess();
                        }else{
                            mView.showError(resultCode);
                        }
                    }
                });

            }

            @Override
            public void onDataNotAvailable(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showError(999);
                    }
                });
            }
        });
    }

    //根据身份分发相对应页面
    @Override
    public void isDentity(int dentityCode) {
        //分发页面 根据用户身份码
        if(dentityCode == User.USER_IDENTITY_DELIVERY){
            mView.onDelivery();
        }else if(dentityCode == User.USER_IDENTITY_DRIVER){
            mView.onDriver();
        }else if(dentityCode == User.USER_IDENTITY_STATION){
            mView.onStation();
        }
    }

    public String getMd5WaybillNumber(String url){
        String md5WaybillNumber = "";
        if (url.indexOf("?") != -1 && url.indexOf("=") != -1){
            String params = url.substring(url.indexOf("?") + 1, url.length());
            md5WaybillNumber = params.substring(params.indexOf("=") + 1, params.length());
        }
        return md5WaybillNumber;
    }



}
