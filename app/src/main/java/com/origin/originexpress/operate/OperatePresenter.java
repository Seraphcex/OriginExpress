package com.origin.originexpress.operate;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.data.source.WaybillSource;
import com.origin.originexpress.utils.GeyCallback;

/**
 * Created by XunselF on 2018/9/6.
 */

public class OperatePresenter implements OperateContract.Presenter {

    private OperateContract.View mOperateView;

    private WaybillSource mWaybillRepository;

    private String mMd5WaybillNumber;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public OperatePresenter(String md5Waybillnumber, OperateContract.View operateView, WaybillSource waybillRepository){
        this.mMd5WaybillNumber = md5Waybillnumber;
        this.mOperateView = operateView;
        this.mWaybillRepository = waybillRepository;
        mOperateView.setPresenter(this);
    }

    @Override
    public void start() {
        getWaybill();
    }

    @Override
    public void getWaybill() {

        mOperateView.showLoading();
        mWaybillRepository.getWaybill(mMd5WaybillNumber, new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(final Object o) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Waybill waybill = (Waybill) o;
                        mOperateView.onSuccess(waybill);
                    }
                });

            }

            @Override
            public void onDataNotAvailable(final String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mOperateView.showErrorMeg(error);
                    }
                });
            }
        });
    }
}
