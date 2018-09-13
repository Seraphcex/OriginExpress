package com.origin.originexpress.input;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.origin.originexpress.R;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.User;
import com.origin.originexpress.driving.DrivingContract;
import com.origin.originexpress.utils.CarLocationListener;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/9/9.
 */

public class InputCarFragment extends Fragment implements InputCarContract.View, View.OnClickListener {

    private InputCarContract.Presenter mPresenter;

    private ZLoadingDialog mZloadingDialog;

    private View mView;

    private String mStartDepartment;

    private String mDepartment;

    private String mLicense;

    private SharedPreferences mSharedPreferences;

    public LocationClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_input_car, container, false);
        mView = root;
        init();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void init(){
        LinearLayout ll_return = mView.findViewById(R.id.ll_return);
        FloatingActionButton fab_submit = mView.findViewById(R.id.fab_submit);
        mSharedPreferences = getActivity().getSharedPreferences("user_config", Context.MODE_PRIVATE);
        //弹窗
        mZloadingDialog = new ZLoadingDialog(getActivity());
        mZloadingDialog.setLoadingBuilder(Z_TYPE.CIRCLE_CLOCK)
                .setLoadingColor(getResources().getColor(R.color.colorThemeBackground))
                .setHintText("Loading...")
                .setHintTextSize(16)
                .setHintTextColor(getResources().getColor(R.color.colorThemeBackground))
                .setDurationTime(0.5);



        ll_return.setOnClickListener(this);
        fab_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_return:
                getActivity().finish();
                break;
            case R.id.fab_submit:
                if(mStartDepartment.equals(mDepartment)){
                    Toast.makeText(getActivity(), "起始站点与最终站点不得相同", Toast.LENGTH_LONG).show();
                }else if(mStartDepartment.equals("") || mDepartment.equals("") || mLicense.equals("")){
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
                }else{
                   Car car = new Car();
                   car.setStatus(Car.LOADING_CODE);
                   car.setLicense(mLicense);
                   car.setStartDepartment(mStartDepartment);
                   car.setDepartment(mDepartment);
                   car.setPhone(mSharedPreferences.getString(User.PHONE_NAME, ""));
                   car.setUsername(mSharedPreferences.getString(User.USERNAME_NAME, ""));
                   car.setDriver(mSharedPreferences.getString(User.FULLNAME_NAME, ""));
                   //获取数据
                   getCarLocation(car);
                }
                break;
        }
    }

    //定位
    public void getCarLocation(Car car){
        mLocationClient = new LocationClient(getActivity());
        CarLocationListener myListener = new CarLocationListener();
        myListener.setCar(car);
        myListener.setInputCarPresenter(mPresenter);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public static InputCarFragment newInstance(){
        return new InputCarFragment();
    }
    @Override
    public void setPresenter(InputCarContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mZloadingDialog.show();
    }

    @Override
    public void onInputMeg(final List<String> department, final List<String> license) {
        mZloadingDialog.dismiss();
        Spinner sp_location = mView.findViewById(R.id.sp_location);
        Spinner sp_tolocal = mView.findViewById(R.id.sp_tolocal);
        Spinner sp_license = mView.findViewById(R.id.sp_license);
        mDepartment = department.get(0);
        mStartDepartment = department.get(0);
        mLicense = license.get(0);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, department);

        ArrayAdapter<String> licenseAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, license);

        sp_location.setAdapter(departmentAdapter);
        sp_tolocal.setAdapter(departmentAdapter);
        sp_license.setAdapter(licenseAdapter);


        sp_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStartDepartment = department.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_tolocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mDepartment = department.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_license.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mLicense = license.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onSuccess() {
        mZloadingDialog.dismiss();
        Snackbar.make(mView, "添加任务成功！", Snackbar.LENGTH_LONG).show();
        getActivity().finish();
        mLocationClient.stop();
    }

    @Override
    public void showError(String error) {
        mZloadingDialog.dismiss();
        mLocationClient.stop();
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
