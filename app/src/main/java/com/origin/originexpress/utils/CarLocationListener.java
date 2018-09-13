package com.origin.originexpress.utils;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.driving.DrivingContract;
import com.origin.originexpress.driving.DrivingPresenter;
import com.origin.originexpress.input.InputCarContract;
import com.origin.originexpress.main.MainContract;

/**
 * Created by XunselF on 2018/9/10.
 */

public class CarLocationListener extends BDAbstractLocationListener {

    //presenter
    private InputCarContract.Presenter inputCarPresenter;

    private DrivingContract.Presenter drivingPresenter;

    private MainContract.Presenter mainPresenter;

    private int actionCode;

    private Car car;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();    //获取纬度信息
        double longitude = bdLocation.getLongitude();    //获取经度信息
        String addr = bdLocation.getAddrStr();    //获取详细地址信息

        //加载 经纬度、详细地址 数据
        car.setLatitude(latitude);
        car.setLongitude(longitude);
        car.setLocation(addr);

        //添加 运送任务
        if(inputCarPresenter != null){
            inputCarPresenter.submitMessage(car);
        }else if(drivingPresenter != null){
                drivingPresenter.sendMessage(car);
        }else if(mainPresenter != null){
            mainPresenter.setLocation(car);
        }

    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public void setInputCarPresenter(InputCarContract.Presenter inputCarPresenter) {
        this.inputCarPresenter = inputCarPresenter;
    }

    public void setDrivingPresenter(DrivingContract.Presenter drivingPresenter) {
        this.drivingPresenter = drivingPresenter;
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }
}
