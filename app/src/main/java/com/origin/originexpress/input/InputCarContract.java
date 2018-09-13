package com.origin.originexpress.input;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.utils.BasePresenter;
import com.origin.originexpress.utils.BaseView;

import java.util.List;

/**
 * Created by XunselF on 2018/9/9.
 */

public class InputCarContract {

    public interface View extends BaseView<InputCarContract.Presenter> {

        //加载中
        void showLoading();

        //展示输入数据页面
        void onInputMeg(List<String> department, List<String> license);

        //展示失败页面
        void showError(String error);

        //展示成功页面
        void onSuccess();
    }

    public interface Presenter extends BasePresenter {

        //获取输入页面的数据
        void getInputMeg();

        //获取数据
        void getMessage(InputMeg inputMeg);

        //上传数据
        void submitMessage(Car car);


    }
}
