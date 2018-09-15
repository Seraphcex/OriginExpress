package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;

import com.origin.originexpress.data.User;
import com.origin.originexpress.utils.GeyCallback;


/**
 * Created by XunselF on 2018/9/3.
 */

public interface UserDataSource {


    //发出请求
    void sendRequest(User user, okhttp3.Callback callback);

    //验证用户信息
    void verifyUser(User user,@NonNull GeyCallback.GetCallback getCallback);

    //解析json数据
    User resolve(String responseData);

}
