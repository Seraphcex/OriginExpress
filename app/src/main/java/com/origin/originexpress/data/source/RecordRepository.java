package com.origin.originexpress.data.source;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.utils.GeyCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XunselF on 2018/9/8.
 */

public class RecordRepository implements RecordSource {

    private static final String RECORD_OPERATE_URL = "http://xunself.top/origin_oa/public/index.php/Courier/m_add_package";


    @Override
    public void sendRequest(Record record, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("action_code",record.getActionCode() + "")
                .add("md5waybillnumber",record.getMd5waybillnumber())
                .add("manager", record.getManager())
                .add("manager_department", record.getLocation())
                .add("phone", record.getPhone())
                .build();

        Request request = new Request.Builder().url(RECORD_OPERATE_URL)
                .post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void operateWaybill(Record record, @NonNull final GeyCallback.GetCallback getCallback) {
        sendRequest(record, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                final Record record = resolve(responseData);
                Log.d("test", responseData);
                getCallback.onLoaded(record);
            }
        });
    }

    @Override
    public Record resolve(String responseData) {
        Record record = new Record();
        try{
            //jsonobject解析
            JSONObject jsonObject = new JSONObject(responseData);
            int resultCode = jsonObject.getInt("result_code");
            String waybillnumber = jsonObject.getString("waybillnumber");
            String manager = jsonObject.getString("manager");
            String phone = jsonObject.getString("phone");
            String license = jsonObject.getString("license");
            String location = jsonObject.getString("location");
            String tolocal = jsonObject.getString("tolocal");
            int status = jsonObject.getInt("status");

            record.setResultCode(resultCode);
            record.setWaybillnumber(waybillnumber);
            record.setManager(manager);
            record.setPhone(phone);
            record.setLicense(license);
            record.setLocation(location);
            record.setTolocal(tolocal);
            record.setStatus(status);

        }catch(Exception e){
            e.printStackTrace();
        }

        return record;
    }
}
