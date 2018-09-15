package com.origin.originexpress.driving;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.origin.originexpress.R;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.utils.CarLocationListener;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by XunselF on 2018/9/9.
 */

public class DrivingFragment extends Fragment implements DrivingContract.View, View.OnClickListener{

    //presenter
    private DrivingContract.Presenter mPresenter;

    //View
    private View mView;

    //car
    private Car mCar;

    private FloatingActionButton fab_unloading_car;

    private FloatingActionButton fab_scan_code;

    private FloatingActionButton fab_transport;

    private FloatingActionButton fab_not_task;

    private TextView tv_transport_status;

    private ZLoadingDialog mZloadingDialog;

    private LocationClient mLocationClient;


    public static DrivingFragment newInstance(Car car){
        DrivingFragment drivingFragment = new DrivingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Car.EXTRA_CAR_NAME, car);
        drivingFragment.setArguments(bundle);
        return drivingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_driving, container, false);
        mView = root;
        Bundle bundle = getArguments();
        mCar = (Car)bundle.getSerializable(Car.EXTRA_CAR_NAME);
        init();
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_return:
                getActivity().finish();
                break;
            case R.id.fab_transport:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("询问")
                        .setMessage("请问该车已装车完毕准备运送了吗？")
                        .setCancelable(true)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCar.setStatus(Car.TRANSPORT_CODE);
                                getLocation(mCar);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                dialog.show();
                break;
            case R.id.fab_unloading_car:
                AlertDialog.Builder dialogUnLoading = new AlertDialog.Builder(getActivity());
                dialogUnLoading.setTitle("询问")
                        .setMessage("请问该车已到达目的地准备卸车？")
                        .setCancelable(true)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCar.setStatus(Car.UNLOADING_CODE);
                                getLocation(mCar);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                dialogUnLoading.show();
                break;
            case R.id.fab_not_task:
                AlertDialog.Builder dialogNotTask = new AlertDialog.Builder(getActivity());
                dialogNotTask.setTitle("询问")
                        .setMessage("请问该车已完成运送任务吗？")
                        .setCancelable(true)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCar.setStatus(Car.NO_TASK_CODE);
                                getLocation(mCar);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                dialogNotTask.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(DrivingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void init() {
        TextView tv_transport_license = mView.findViewById(R.id.tv_transport_license);
        TextView tv_transport_start_department = mView.findViewById(R.id.tv_transport_start_department);
        TextView tv_transport_department = mView.findViewById(R.id.tv_transport_department);
        tv_transport_status = mView.findViewById(R.id.tv_transport_status);
        LinearLayout ll_return = mView.findViewById(R.id.ll_return);
        fab_unloading_car = mView.findViewById(R.id.fab_unloading_car);
        fab_scan_code = mView.findViewById(R.id.fab_scan_code);
        fab_transport = mView.findViewById(R.id.fab_transport);
        fab_not_task = mView.findViewById(R.id.fab_not_task);

        //弹窗
        mZloadingDialog = new ZLoadingDialog(getActivity());
        mZloadingDialog.setLoadingBuilder(Z_TYPE.STAR_LOADING)
                .setLoadingColor(getResources().getColor(R.color.colorThemeBackground))
                .setHintText("Loading...")
                .setHintTextSize(16)
                .setHintTextColor(getResources().getColor(R.color.colorThemeBackground))
                .setDurationTime(0.5);

        tv_transport_license.setText(mCar.getLicense());
        tv_transport_start_department.setText(mCar.getStartDepartment());
        tv_transport_department.setText(mCar.getDepartment());
        String status = "";
        if(mCar.getStatus() == Car.NO_TASK_CODE){
            status = "无任务";
        }else if(mCar.getStatus() == Car.LOADING_CODE){
            status = "装车中";
        }else if(mCar.getStatus() == Car.UNLOADING_CODE){
            status = "卸车中";
        }else{
            status = "运送中";
        }
        showOperateBt(mCar.getStatus());
        tv_transport_status.setText(status);
        ll_return.setOnClickListener(this);
        fab_scan_code.setOnClickListener(this);
        fab_transport.setOnClickListener(this);
        fab_unloading_car.setOnClickListener(this);
        fab_not_task.setOnClickListener(this);
    }

    @Override
    public void onSuccess() {
        mZloadingDialog.dismiss();
        mLocationClient.stop();
        getActivity().finish();

    }

    @Override
    public void showLoading() {
        mZloadingDialog.show();
    }


    @Override
    public void showError(String error) {
        mZloadingDialog.dismiss();
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        mLocationClient.stop();
    }

    @Override
    public void getLocation(Car car) {
        showLoading();
        mLocationClient = new LocationClient(getActivity());
        CarLocationListener myListener = new CarLocationListener();
        myListener.setCar(car);
        myListener.setDrivingPresenter(mPresenter);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void showOperateBt(int actionCode) {
        fab_scan_code.setVisibility(View.GONE);
        fab_unloading_car.setVisibility(View.GONE);
        fab_transport.setVisibility(View.GONE);
        fab_not_task.setVisibility(View.GONE);

        mCar.setStatus(actionCode);

        if(actionCode == Car.NO_TASK_CODE){
            getActivity().finish();
        }else if(actionCode == Car.LOADING_CODE){

            fab_scan_code.setVisibility(View.VISIBLE);
            fab_transport.setVisibility(View.VISIBLE);

        }else if(actionCode == Car.TRANSPORT_CODE){
            fab_unloading_car.setVisibility(View.VISIBLE);

        }else if(actionCode == Car.UNLOADING_CODE){

            fab_scan_code.setVisibility(View.VISIBLE);
            fab_not_task.setVisibility(View.VISIBLE);
        }

        getStatus();
    }

    public void getStatus(){
        String status = "";
        if(mCar.getStatus() == Car.NO_TASK_CODE){
            status = "无任务";
        }else if(mCar.getStatus() == Car.LOADING_CODE){
            status = "装车中";
        }else if(mCar.getStatus() == Car.UNLOADING_CODE){
            status = "卸车中";
        }else{
            status = "运送中，正在定位";
        }
        tv_transport_status.setText(status);
    }
}
