package com.origin.originexpress.data.source;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.origin.originexpress.data.User;
import com.origin.originexpress.utils.GeyCallback;


import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by XunselF on 2018/9/3.
 */

public class UserRepository implements UserDataSource{

    //登录验证后台
    private static final String LOGIN_VERIFY_URL = "http://xunself.top/origin_oa/public/index.php/login/m_verifyuser";




    //发送请求
    @Override
    public void sendRequest(User user, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username",user.getUsername())
                .add("password",user.getPassword())
                .build();

        Request request = new Request.Builder().url(LOGIN_VERIFY_URL)
                .post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    //验证用户信息
    @Override
    public void verifyUser(User user,@NonNull final GeyCallback.GetCallback getUserCallback) {
        //发送请求
        sendRequest(user,  new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //返回错误信息
                getUserCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final User user = resolve(result);
                //返回用户数据
                getUserCallback.onLoaded(user);
            }
        });

    }

    //解析json数据
    public User resolve(String responseData){
        User user = new User();
        try{
            //jsonobject解析
            JSONObject jsonObject = new JSONObject(responseData);
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String fullname = jsonObject.getString("fullname");
            String phone = jsonObject.getString("phone");
            String department = jsonObject.getString("department");
            int identityCode = jsonObject.getInt("identity_code");
            int resultCode = jsonObject.getInt("result_code");

            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setPhone(phone);
            user.setDepartment(department);
            user.setIdentity_code(identityCode);
            user.setResult_code(resultCode);

        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }



}
