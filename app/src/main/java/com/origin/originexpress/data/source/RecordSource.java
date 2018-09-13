package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;

import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.utils.GeyCallback;

/**
 * Created by XunselF on 2018/9/8.
 */

public interface RecordSource {

    //发出请求
    void sendRequest(Record record,  okhttp3.Callback callback);

    //操作
    void operateWaybill(Record record, @NonNull GeyCallback.GetCallback getCallback);

    //解析json数据
    Record resolve(String responseData);

}
