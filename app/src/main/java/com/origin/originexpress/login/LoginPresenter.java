package com.origin.originexpress.login;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.origin.originexpress.data.User;
import com.origin.originexpress.data.source.UserRepository;
import com.origin.originexpress.utils.GeyCallback;


/**
 * Created by XunselF on 2018/9/3.
 */

public class LoginPresenter implements LoginContract.Presenter {

    //视图
    public LoginContract.View mLoginView;

    //模型
    public UserRepository mUserRepository;

    private android.os.Handler mHandler = new Handler(Looper.getMainLooper());


    public LoginPresenter(@NonNull UserRepository mUserRespository, @NonNull LoginContract.View loginView){
        this.mLoginView = loginView;
        this.mUserRepository = mUserRespository;
        this.mLoginView.setPresenter(this);
    }

    //加载数据
    @Override
    public void start() {

    }

    //判断是否登录
    @Override
    public void isLogin() {

    }

    //验证用户信息
    @Override
    public void verify(String username, String password) {
        //用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //展示加载中
        mLoginView.showLoading();
        //发送请求
        mUserRepository.verifyUser(user, new GeyCallback.GetCallback() {
            @Override
            public void onLoaded(final Object o) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        User user = (User) o;

                        //结果码
                        int resultCode = user.getResult_code();
                        Log.d("resultcode", "username:" + user.getUsername() + ",identity:" + user.getIdentity_code() + ",code:" + resultCode);
                        if(resultCode == User.RESULT_OK){
                            //登录成功操作
                            mLoginView.onSuccess(user);
                        }else{
                            mLoginView.showErrorMeg(resultCode);
                        }
                    }
                });
            }

            @Override
            public void onDataNotAvailable(final String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("user", "Error:" + error);
                        mLoginView.showErrorMeg(User.DATA_NULL);
                    }
                });
            }
        });
    }
}
