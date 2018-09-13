package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;

import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.utils.GeyCallback;

/**
 * Created by XunselF on 2018/9/6.
 */

public interface WaybillSource {

    //发出请求
    void sendRequest(String md5WaybillNumber, okhttp3.Callback callback);

    //验证用户信息
    void getWaybill(String md5WaybillNumber,@NonNull GeyCallback.GetCallback getCallback);

    //解析json数据
    Waybill resolve(String responseData);
}
