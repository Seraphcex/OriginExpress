package com.origin.originexpress.data.source;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.utils.GeyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XunselF on 2018/9/6.
 */

public class WaybillRepository implements WaybillSource {

    private String get_waybill_url = "http://xunself.top/origin_oa/public/index.php/main/m_get_waybills";

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper());
    @Override
    public void sendRequest(String md5WaybillNumber, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("md5waybillnumber",md5WaybillNumber)
                .build();

        Request request = new Request.Builder().url(get_waybill_url)
                .post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getWaybill(String md5WaybillNumber, @NonNull final GeyCallback.GetCallback getCallback) {
        sendRequest(md5WaybillNumber, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //返回错误信息
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final Waybill waybill = resolve(result);
                getCallback.onLoaded(waybill);
            }
        });
    }

    @Override
    public Waybill resolve(String responseData) {
        Waybill waybill = new Waybill();
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            waybill.setResultCode(jsonObject.getInt("result_code"));
            waybill.setWaybillNumber(jsonObject.getString("waybillnumber"));
            waybill.setMd5WaybillNumber(jsonObject.getString("md5waybillnumber"));
            waybill.setsName(jsonObject.getString("sname"));
            waybill.setsPhone(jsonObject.getString("sphone"));
            waybill.setsAddress(jsonObject.getString("saddress"));
            waybill.setrName(jsonObject.getString("rname"));
            waybill.setrPhone(jsonObject.getString("rphone"));
            waybill.setrAddress(jsonObject.getString("raddress"));
            waybill.setTime(jsonObject.getString("time"));
            waybill.setStatus(jsonObject.getString("status"));
            JSONArray jsonArray = jsonObject.getJSONArray("records");
            List<Record> records = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject recordObject = jsonArray.getJSONObject(i);
                Record record = new Record();
                record.setWaybillnumber(recordObject.getString("waybillnumber"));
                record.setLicense(recordObject.getString("license"));
                record.setLocation(recordObject.getString("location"));
                record.setManager(recordObject.getString("manager"));
                record.setPhone(recordObject.getString("phone"));
                record.setStatus(recordObject.getInt("status"));
                record.setTolocal(recordObject.getString("tolocal"));
                record.setTime(recordObject.getString("time"));
                records.add(record);
            }
            waybill.setRecordList(records);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waybill;
    }
}
