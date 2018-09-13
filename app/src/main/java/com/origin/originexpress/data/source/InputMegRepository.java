package com.origin.originexpress.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.Department;
import com.origin.originexpress.data.InputMeg;
import com.origin.originexpress.data.Record;
import com.origin.originexpress.utils.GeyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XunselF on 2018/9/9.
 */

public class InputMegRepository implements InputMegSource {

    private static final String GET_INPUT_MEG_URL = "http://xunself.top/origin_oa/public/index.php/courier/m_get_input_message";

    private static final String ADD_TRANSPORT_URL = "http://xunself.top/origin_oa/public/index.php/courier/m_add_transport";

    private static final String SUBMIT_LOCATION_URL = "http://xunself.top/origin_oa/public/index.php/courier/m_set_location";

    @Override
    public void sendRequest(Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();


        Request request = new Request.Builder().url(GET_INPUT_MEG_URL)
               .build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void sendRequest(String status, Car car, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("status", status)
                .add("license", car.getLicense())
                .add("username", car.getUsername())
                .add("driver", car.getDriver())
                .add("phone", car.getPhone())
                .add("longitude", String.valueOf(car.getLongitude()))
                .add("latitude", String.valueOf(car.getLatitude()))
                .add("location", car.getLocation())
                .add("start_department", car.getStartDepartment())
                .add("department", car.getDepartment()).build();


        Request request = new Request.Builder().url(ADD_TRANSPORT_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void addTransport(Car car, final GeyCallback.GetCallback getCallback) {
        sendRequest(String.valueOf(Car.LOADING_CODE), car, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Car car = resolveCar(responseData);
                getCallback.onLoaded(car);
            }
        });
    }

    @Override
    public void onTransport(Car car, final GeyCallback.GetCallback getCallback) {
        sendRequest(String.valueOf(car.getStatus()), car, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Car car = resolveCar(responseData);
                getCallback.onLoaded(car);
            }
        });
    }


    @Override
    public void sendRequest(Car car, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("status", String.valueOf(car.getStatus()))
                .add("license", car.getLicense())
                .add("username", car.getUsername())
                .add("driver", car.getDriver())
                .add("phone", car.getPhone())
                .add("longitude", String.valueOf(car.getLongitude()))
                .add("latitude", String.valueOf(car.getLatitude()))
                .add("location", car.getLocation())
                .add("start_department", car.getStartDepartment())
                .add("department", car.getDepartment()).build();


        Request request = new Request.Builder().url(SUBMIT_LOCATION_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void submitLocation(Car car, final GeyCallback.GetCallback getCallback) {
        sendRequest(car, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Car car = resolveCar(responseData);
                getCallback.onLoaded(car);
            }
        });
    }

    @Override
    public void getLocation(Car car) {

    }

    @Override
    public void getInputMeg(@NonNull final GeyCallback.GetCallback getCallback) {
        sendRequest(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCallback.onDataNotAvailable(e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                InputMeg inputMeg = resolve(responseData);
                getCallback.onLoaded(inputMeg);
            }
        });
    }

    @Override
    public Car resolveCar(String responseData) {
        Car car = new Car();
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            car.setResultCode(jsonObject.getInt("result_code"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public InputMeg resolve(String responseData) {
        InputMeg inputMeg = new InputMeg();
        try{
            //jsonobject解析
            JSONObject jsonObject = new JSONObject(responseData);


            JSONArray departments = jsonObject.getJSONArray("departments");
            List<Department> departmentList = new ArrayList<>();
            for(int i = 0; i < departments.length(); i++){
                Department department = new Department();
                JSONObject departmentObject = departments.getJSONObject(i);
                department.setName(departmentObject.getString("name"));
                department.setLatitude(departmentObject.getString("latitude"));
                department.setLatitude(departmentObject.getString("longitude"));
                department.setValue(departmentObject.getString("value"));
                departmentList.add(department);
            }
            inputMeg.setDepartmentList(departmentList);

            JSONArray cars = jsonObject.getJSONArray("cars");
            List<Car> carList = new ArrayList<>();
            for(int i = 0; i < cars.length(); i++){
                Car car = new Car();
                JSONObject carObject = cars.getJSONObject(i);
                car.setLicense(carObject.getString("license"));
                carList.add(car);
            }
            inputMeg.setCarList(carList);



        }catch (JSONException e){
            e.printStackTrace();
        }
        return inputMeg;
    }


}
