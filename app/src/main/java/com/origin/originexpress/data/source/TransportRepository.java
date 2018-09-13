package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.utils.GeyCallback;

import org.json.JSONException;
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
 * Created by XunselF on 2018/9/10.
 */

public class TransportRepository implements TransportSource {

    private static final String GET_TRANSPORT_URL = "http://xunself.top/origin_oa/public/index.php/courier/m_get_transport";

    @Override
    public void sendRequest(String username, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .build();

        Request request = new Request.Builder().url(GET_TRANSPORT_URL)
                .post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getTransport(String username, @NonNull final GeyCallback.GetCallback getCallback) {
        sendRequest(username, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Car car = new Car();
                if(responseData != null){car = resolve(responseData);}
                else{car.setResultCode(Car.NOT_DATA);}
                getCallback.onLoaded(car);
            }
        });
    }

    @Override
    public Car resolve(String responseData) {
        Car car = new Car();
        car.setResultCode(Car.NOT_DATA);
        try{

            JSONObject jsonObject = new JSONObject(responseData);
            int resultCode = Car.RESULT_OK;
            String license = jsonObject.getString("license");
            String username = jsonObject.getString("username");
            String driver = jsonObject.getString("driver");
            String phone = jsonObject.getString("phone");
            String longitudeStr = jsonObject.getString("longitude");
            String latitudeStr = jsonObject.getString("latitude");
            String location = jsonObject.getString("location");
            String startDepartment = jsonObject.getString("start_department");
            String 	department = jsonObject.getString("department");
            int status = jsonObject.getInt("status");

            //转换为double
            double longitude = longitudeStr == null? 0:Double.parseDouble(longitudeStr);
            double latitude = latitudeStr == null? 0:Double.parseDouble(latitudeStr);

            car.setResultCode(resultCode);
            car.setLicense(license);
            car.setUsername(username);
            car.setDriver(driver);
            car.setPhone(phone);
            car.setStartDepartment(startDepartment);
            car.setLongitude(longitude);
            car.setLocation(location);
            car.setLatitude(latitude);
            car.setStatus(status);
            car.setDepartment(department);


        }catch(JSONException e){
            e.printStackTrace();
        }
        return car;
    }
}
