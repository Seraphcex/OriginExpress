package com.origin.originexpress.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.origin.originexpress.data.source.UserRepository;
import com.origin.originexpress.utils.ActivityUtils;
import com.origin.originexpress.R;

public class LoginActivity extends AppCompatActivity {

    //presenter
    private LoginContract.Presenter mPresenter;

    //model模型
    private UserRepository mUserRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化fragment
        LoginFragment loginFragment = (LoginFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        //连接fragment 与 activity
        if(loginFragment == null){
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.contentFrame);
        }

        //初始化
        mUserRepository = new UserRepository();
        mPresenter = new LoginPresenter(mUserRepository, loginFragment);
    }


}
