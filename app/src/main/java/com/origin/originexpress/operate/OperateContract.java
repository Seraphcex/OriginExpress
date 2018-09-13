package com.origin.originexpress.operate;

import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.utils.BasePresenter;
import com.origin.originexpress.utils.BaseView;

/**
 * Created by XunselF on 2018/9/6.
 */

public class OperateContract {

    interface View extends BaseView<Presenter>{

        //初始化
        void init();

        //显示加载中
        void showLoading();

        //成功处理
        void onSuccess(Waybill waybill);

        //提示失败信息
        void showErrorMeg(String error);
    }

    interface Presenter extends BasePresenter{

        //获取数据
        void getWaybill();
    }
}
