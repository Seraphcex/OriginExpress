package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.utils.GeyCallback;

/**
 * Created by XunselF on 2018/9/10.
 */

public interface TransportSource {


    //发出请求
    void sendRequest(String username, okhttp3.Callback callback);

    //操作
    void getTransport(String username, @NonNull GeyCallback.GetCallback getCallback);

    //解析json数据
    Car resolve(String responseData);
}
