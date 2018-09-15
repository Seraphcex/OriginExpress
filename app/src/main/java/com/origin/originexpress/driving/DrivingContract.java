package com.origin.originexpress.driving;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.utils.BasePresenter;
import com.origin.originexpress.utils.BaseView;

/**
 * Created by XunselF on 2018/9/9.
 */

public class DrivingContract {
    public interface View extends BaseView<Presenter>{

        //初始化
        void init();

        //加载中
        void showLoading();

        void onSuccess();

        //展示失败页面
        void showError(String error);

        //定位
        void getLocation(Car car);

        //展示操作按钮
        void showOperateBt(int actionCode);

    }

    public interface Presenter extends BasePresenter {

        //提交 车辆信息
        void sendMessage(Car car);



    }
}
