package com.origin.originexpress.login;

import com.origin.originexpress.data.User;
import com.origin.originexpress.utils.BasePresenter;
import com.origin.originexpress.utils.BaseView;

/**
 * Created by XunselF on 2018/9/3.
 */

public class LoginContract {

    public interface View extends BaseView<Presenter> {
        //初始化
        void init();

        //显示加载中
        void showLoading();

        //成功处理
        void onSuccess(User user);

        //跳转到首页
        void  onJumpMain(int identityCode);

        //提示失败信息
        void showErrorMeg(int resultCode);

    }

    public interface Presenter extends BasePresenter{
        //判断是否已登录
        void isLogin();

        //验证登录信息
        void verify(String username, String password);
    }
}
