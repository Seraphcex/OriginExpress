package com.origin.originexpress.input;

import android.os.Handler;
import android.os.Looper;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.Department;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.utils.GeyCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/9/9.
 */

public class InputCarPresenter implements InputCarContract.Presenter {

    public InputCarContract.View mView;

    private InputMegRepository mInputMegRepository;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public InputCarPresenter(InputCarContract.View view, InputMegRepository inputMegRepository){
        this.mView = view;
        this.mInputMegRepository = inputMegRepository;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        getInputMeg();
    }

    @Override
    public void getInputMeg() {
        mView.showLoading();
        mInputMegRepository.getInputMeg(new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(final Object o) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMeg inputMeg = (InputMeg) o;
                        getMessage(inputMeg);
                    }
                });
            }

            @Override
            public void onDataNotAvailable(String error) {
                mView.showError(error);
            }
        });
    }

    @Override
    public void getMessage(InputMeg inputMeg) {
        List<String> departments = new ArrayList<>();
        List<String> cars = new ArrayList<>();

        List<Department> departmentList = inputMeg.getDepartmentList();
        List<Car> carList = inputMeg.getCarList();

        for(int i = 1; i < departmentList.size(); i++){
            Department department = departmentList.get(i);
            departments.add(department.getName());
        }

        for(int i = 1; i < carList.size(); i++){
            Car car = carList.get(i);
            cars.add(car.getLicense());
        }

        mView.onInputMeg(departments, cars);
    }

    @Override
    public void submitMessage(Car car) {
        mView.showLoading();
        if(car.getLocation() == null){mView.showError("无法定位！");}
        else{
            //发送 添加运送任务 的请求
            mInputMegRepository.addTransport(car, new GeyCallback.GetCallback() {
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
