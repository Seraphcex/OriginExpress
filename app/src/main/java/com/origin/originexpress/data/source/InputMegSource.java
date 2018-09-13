package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.data.Record;
import com.origin.originexpress.utils.GeyCallback;

import okhttp3.Callback;

/**
 * Created by XunselF on 2018/9/9.
 */

public interface InputMegSource {

    //发出请求
    void sendRequest( okhttp3.Callback callback);

    void sendRequest(Car car, Callback callback);

    void sendRequest(String status, Car car, okhttp3.Callback callback);

    //操作
    void getInputMeg( @NonNull GeyCallback.GetCallback getCallback);

    void submitLocation(Car car, final  GeyCallback.GetCallback getCallback);

    void onTransport(Car car, final GeyCallback.GetCallback getCallback);


    void addTransport(Car car, GeyCallback.GetCallback getCallback);

    //解析json数据
    InputMeg resolve(String responseData);

    Car resolveCar(String responseData);

    void getLocation(Car car);
}
