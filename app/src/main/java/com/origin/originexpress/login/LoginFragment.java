package com.origin.originexpress.login;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.origin.originexpress.R;
import com.origin.originexpress.data.User;
import com.origin.originexpress.main.MainActivity;
import com.origin.originexpress.utils.ActivityUtils;
import com.origin.originexpress.utils.EditTextClearTool;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by XunselF on 2018/9/3.
 */

public class LoginFragment extends Fragment implements LoginContract.View{

    //presenter
    private LoginContract.Presenter mLoginPresenter;

    private ZLoadingDialog mZloadingDialog;

    private View mView;

    private boolean mIsRemember = false;

    private boolean mIsAuto = false;

    private Handler handler = new Handler(Looper.getMainLooper());

    private SharedPreferences mSharedPreferences;


    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_login, container, false);
        mView = root;
        init();
        return root;
    }

    //加载presenter
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mLoginPresenter = presenter;
    }

    //初始化控件
    @Override
    public void init() {
        //获取数据
        mSharedPreferences = getActivity().getSharedPreferences("user_config", Context.MODE_PRIVATE);
        String username = mSharedPreferences.getString(User.USERNAME_NAME, "");
        String password = mSharedPreferences.getString(User.PASSWORD_NAME, "");
        int identityCode = mSharedPreferences.getInt(User.IDENTITY_CODE_NAME, User.DATA_NULL);
        mIsRemember = mSharedPreferences.getBoolean(User.IS_REMEMBER_PASSW_NAME, false);
        mIsAuto = mSharedPreferences.getBoolean(User.IS_AUTO_LOGIN_NAME, false);

        //自动登录
        if(mIsAuto && identityCode!=User.DATA_NULL){onJumpMain(identityCode);}


        final LinearLayout la_activity_login = mView.findViewById(R.id.activity_login);
        final EditText et_userName_input = mView.findViewById(R.id.et_userName);
        final EditText et_userPwd_input = mView.findViewById(R.id.et_userPwd);
        ImageView iv_userName_clear = mView.findViewById(R.id.iv_unameClear);
        ImageView iv_userPwd_clear = mView.findViewById(R.id.iv_upwdClear);
        FloatingActionButton btn_login =  mView.findViewById(R.id.btn_login);
        final CheckBox cb_checkboxPwdRem = mView.findViewById(R.id.cb_checkboxPwdRem);
        final CheckBox cb_checkboxAutolog = mView.findViewById(R.id.cb_checkboxAutolog);

        //弹窗
        mZloadingDialog = new ZLoadingDialog(getActivity());
        mZloadingDialog.setLoadingBuilder(Z_TYPE.STAR_LOADING)
                .setLoadingColor(getResources().getColor(R.color.colorThemeBackground))
                .setHintText("Loading...")
                .setHintTextSize(16)
                .setHintTextColor(getResources().getColor(R.color.colorThemeBackground))
                .setDurationTime(0.5);

        //加载用户名或者密码
        et_userName_input.setText(username);
        et_userName_input.setSelection(username.length());
        if(mIsRemember){et_userPwd_input.setText(password);et_userPwd_input.setSelection(password.length());cb_checkboxPwdRem.setChecked(mIsRemember);}

        EditTextClearTool.addClearListener(et_userName_input,iv_userName_clear);
        EditTextClearTool.addClearListener(et_userPwd_input,iv_userPwd_clear);
        la_activity_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.hideKeyboard(getActivity());
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //核验用户信息
                //跳转
                String username = et_userName_input.getText().toString();
                String password = et_userPwd_input.getText().toString();
                if(username.trim().equals("")){
                    Toast.makeText(getContext(), "用户名不得为空", Toast.LENGTH_LONG).show();
                }else if(password.trim().equals("")){
                    Toast.makeText(getContext(), "密码不得为空", Toast.LENGTH_LONG).show();
                }else{
                    mLoginPresenter.verify(username, password);
                    mIsRemember = cb_checkboxPwdRem.isChecked();
                    mIsAuto = cb_checkboxAutolog.isChecked();
                }
            }
        });
    }

    //显示加载中
    @Override
    public void showLoading() {
        mZloadingDialog.show();
    }

    //成功处理
    @Override
    public void onSuccess(final User user) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //关闭弹窗
                mZloadingDialog.dismiss();

                //保存数据
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putString(User.USERNAME_NAME, user.getUsername());
                editor.putString(User.FULLNAME_NAME, user.getFullname());
                editor.putString(User.PHONE_NAME, user.getPhone());
                if(mIsRemember){editor.putString(User.PASSWORD_NAME, user.getPassword());}
                editor.putString(User.DEPARTMENT_NAME, user.getDepartment());
                editor.putInt(User.IDENTITY_CODE_NAME, user.getIdentity_code());
                editor.putBoolean(User.IS_REMEMBER_PASSW_NAME, mIsRemember);
                editor.putBoolean(User.IS_AUTO_LOGIN_NAME, mIsAuto);
                editor.commit();

                onJumpMain(user.getIdentity_code());
            }
        },1000);
    }



    //跳转到main页面
    @Override
    public void onJumpMain(int resultCode) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(User.IDENTITY_CODE_NAME, resultCode);
        startActivity(intent);
        getActivity().finish();
    }





    //显示错误信息
    @Override
    public void showErrorMeg(final int resultCode) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //关闭弹窗
                mZloadingDialog.dismiss();

                if(resultCode == User.USERNAME_IS_EXIST){
                    Toast.makeText(getContext(), "用户名不存在", Toast.LENGTH_LONG).show();
                }else if(resultCode == User.WRONG_PASSWORD){
                    Toast.makeText(getContext(), "用户名密码不正确", Toast.LENGTH_LONG).show();
                }else if(resultCode == User.DATA_IS_EMPTY){
                    Toast.makeText(getContext(), "用户名密码为空", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_LONG).show();
                }
            }
        },1000);
    }
}
