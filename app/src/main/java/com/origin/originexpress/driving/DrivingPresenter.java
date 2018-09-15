package com.origin.originexpress.driving;

import android.os.Handler;
import android.os.Looper;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.utils.GeyCallback;

/**
 * Created by XunselF on 2018/9/10.
 */

public class DrivingPresenter implements DrivingContract.Presenter {

    private InputMegRepository mInputMegRepository;

    private DrivingContract.View mView;

    private Handler mHandler = new Handler(Looper.getMainLooper());



    public DrivingPresenter( DrivingContract.View view, InputMegRepository inputMegRepository){
        this.mInputMegRepository = inputMegRepository;
        this.mView = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void sendMessage(Car car) {
        if(car.getLocation() == null){mView.showError("无法定位！");}
        else{
            mInputMegRepository.onTransport(car, new GeyCallback.GetCallback() {
                @Override
                public void onLoaded(final Object o) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Car car = (Car) o;
                            int resultCode = car.getResultCode();
                            if(resultCode == Car.RESULT_OK){
                                mView.onSuccess();
                            }else{
                                mView.showError("当前车辆已有任务");
                            }
                        }
                    });
                }

                @Override
                public void onDataNotAvailable(final String error) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.showError(error);
                        }
                    });
                }
            });
        }
    }


}
