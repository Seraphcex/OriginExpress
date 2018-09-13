package com.origin.originexpress.main;

import android.content.SharedPreferences;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.utils.BasePresenter;
import com.origin.originexpress.utils.BaseView;

/**
 * Created by XunselF on 2018/9/3.
 */

public class MainContract {




    public interface View extends BaseView<Presenter>{

        //显示加载中
        void showLoading();

        //展示快递员页面
        void onDelivery();

        //展示司机页面
        void onDriver();

        //展示平台页面
        void onStation();

        //管理员页面 TODO
        void onRoot();

        //注销
        void logout();

        //加载相机
        void loadCamera();

        //成功反馈
        void onSuccess();

        //显示运送信息
        void showTransport(Car car);

        //错误信息提示
        void showError(int resultCode);

        void onJumpOperate(String md5waybillnumber);

        void showErrorMeg(String error);


    }

    public interface Presenter extends BasePresenter{

        //分发相应页面
        void isDentity(int dentityCode);

        //取md5运单号
        String getMd5WaybillNumber(String url);

        //设置行为码
        void setmActionCode(int mActionCode);

        int getmActionCode();

        void setLocation( Car car);

        //设置车牌号
        void setmLicense(String mLicense);

        String getmLicense();

        //设置数据
        void setmSharedPreferences(SharedPreferences mSharedPreferences);

        //分发对应的行为
        void onAction(String md5waybillnumber);

        //司机 获取相应运送任务
        void getTransport(String username);

    }
}
