package com.origin.originexpress.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.origin.originexpress.AboutActivity;
import com.origin.originexpress.R;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.User;
import com.origin.originexpress.data.Waybill;
import com.origin.originexpress.driving.DrivingActivity;
import com.origin.originexpress.input.InputCarActivity;
import com.origin.originexpress.login.LoginActivity;
import com.origin.originexpress.operate.OperateActivity;
import com.origin.originexpress.utils.CarLocationListener;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by XunselF on 2018/9/3.
 */

public class MainFragment extends Fragment implements MainContract.View,View.OnClickListener{

    private MainContract.Presenter mPresenter;

    private View mView;

    private DrawerLayout mDrawerLayout;

    private SharedPreferences mSharedPreferences;

    private int mIdentity;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCAN = 111;

    private int mActionCode;

    private ZLoadingDialog mZloadingDialog;

    private LocationClient mLocationClient;

    private CarLocationListener myListener;




    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public static MainFragment newInstance(int identity){
        //传值
        Bundle bundle = new Bundle();
        bundle.putInt(User.IDENTITY_CODE_NAME, identity);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIdentity = getArguments().getInt(User.IDENTITY_CODE_NAME);
        int layout = R.layout.activity_main;
        //分发页面
        if(mIdentity == User.USER_IDENTITY_ROOT){
            layout = R.layout.activity_main;
        }else if(mIdentity == User.USER_IDENTITY_DELIVERY){
            layout = R.layout.activity_main_delivery;
        }else if(mIdentity == User.USER_IDENTITY_DRIVER){
            layout = R.layout.activity_main_driver;
        }else if(mIdentity == User.USER_IDENTITY_STATION){
            layout = R.layout.activity_main_station;
        }
        View root = inflater.inflate(layout, container, false);
        mView = root;
        init();
        return root;
    }

    public void init(){
        mSharedPreferences = getActivity().getSharedPreferences("user_config", Context.MODE_PRIVATE);
        mDrawerLayout = mView.findViewById(R.id.ll_activity_main_delivery);
        mLocationClient = new LocationClient(getActivity());
        myListener = new CarLocationListener();

        mPresenter.setmSharedPreferences(mSharedPreferences);

        //弹窗菜单
        LinearLayout ll_open_menu = mView.findViewById(R.id.ll_open_menu);
        LinearLayout ll_logout = mView.findViewById(R.id.ll_logout);
        LinearLayout ll_search_waybill = mView.findViewById(R.id.ll_search_waybill);
        LinearLayout ll_about = mView.findViewById(R.id.ll_about);
        LinearLayout ll_personal_terms = mView.findViewById(R.id.ll_personal_terms);
        LinearLayout ll_open_setting = mView.findViewById(R.id.ll_open_setting);
        TextView tv_user_name = mView.findViewById(R.id.tv_user_name);
        TextView tv_user_department = mView.findViewById(R.id.tv_user_department);
        TextView tv_user_identity = mView.findViewById(R.id.tv_user_identity);

        mIdentity = mSharedPreferences.getInt(User.IDENTITY_CODE_NAME, User.DATA_NULL);

        String identity = "";
        if(mIdentity == 0){identity = "管理员";}
        else if(mIdentity == 1){identity = "快递员";}
        else if(mIdentity == 2){identity = "司机";}
        else if(mIdentity == 3){identity = "站点";}

        //弹窗
        mZloadingDialog = new ZLoadingDialog(getActivity());
        mZloadingDialog.setLoadingBuilder(Z_TYPE.STAIRS_RECT)
                .setLoadingColor(getResources().getColor(R.color.colorThemeBackground))
                .setHintText("Loading...")
                .setHintTextSize(16)
                .setHintTextColor(getResources().getColor(R.color.colorThemeBackground))
                .setDurationTime(0.5);

        //加载用户名
        tv_user_name.setText(mSharedPreferences.getString(User.FULLNAME_NAME, "--"));
        tv_user_department.setText(mSharedPreferences.getString(User.DEPARTMENT_NAME, "--"));
        tv_user_identity.setText(identity);



        ll_open_menu.setOnClickListener(this);
        ll_search_waybill.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_personal_terms.setOnClickListener(this);
        ll_open_setting.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void getLocation(int status, Car car){
        if(!mLocationClient.isStarted()){
            if(status == Car.TRANSPORT_CODE){
                myListener.setCar(car);
                myListener.setMainPresenter(mPresenter);
                mLocationClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                //十分钟定位
                option.setScanSpan(10000);//600000
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                option.setOpenGps(true);
                option.setIsNeedAddress(true);
                mLocationClient.setLocOption(option);
                mLocationClient.start();
            }
        }else{
            if(status != Car.TRANSPORT_CODE){
                mLocationClient.stop();
            }
        }
    }


    //处理中加载页面
    @Override
    public void showLoading() {
        mZloadingDialog.show();
    }

    //展示快递员界面
    @Override
    public void onDelivery() {

        //收件按钮
        FloatingActionButton bt_add_package = mView.findViewById(R.id.bt_add_package);
        //派送按钮
        FloatingActionButton bt_delivery = mView.findViewById(R.id.bt_delivery);
        //收件按钮
        FloatingActionButton bt_sign_up = mView.findViewById(R.id.bt_sign_up);



        bt_add_package.setOnClickListener(this);
        bt_delivery.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
    }

    //展示司机界面
    @Override
    public void onDriver() {
        FloatingActionButton bt_transport = mView.findViewById(R.id.bt_transport);


        bt_transport.setOnClickListener(this);
    }

    public void onJumpOperate(String md5waybillnumber){
        Intent intent = new Intent(getActivity(), OperateActivity.class);
        intent.putExtra(Waybill.MD5_WAYBILL_NUMBER_NAME, md5waybillnumber);
        startActivity(intent);
    }

    @Override
    public void showErrorMeg(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    //展示平台界面
    @Override
    public void onStation() {
        FloatingActionButton bt_enter_warehouse = mView.findViewById(R.id.bt_enter_warehouse);

        FloatingActionButton bt_out_of_warehouse = mView.findViewById(R.id.bt_out_of_warehouse);

        bt_enter_warehouse.setOnClickListener(this);
        bt_out_of_warehouse.setOnClickListener(this);
    }

    //展示管理员页面
    @Override
    public void onRoot() {

    }

    //注销
    @Override
    public void logout() {
        String userName = mSharedPreferences.getString(User.USERNAME_NAME, "");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.putString(User.USERNAME_NAME, userName);
        editor.commit();



        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
        }
        else{
            loadCamera();
        }
    }

    public void loadCamera() {
            //检查是否有相机权限，有则加载相机，没有则申请

            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
             * 也可以不传这个参数
             * 不传的话  默认都为默认不震动  其他都为true
             * */

            ZxingConfig config = new ZxingConfig();
            config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
            config.setPlayBeep(true);//是否播放提示音
            config.setShake(true);//是否震动
            config.setShowAlbum(true);//是否显示相册
            config.setShowFlashLight(true);//是否显示闪光灯
            intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
            startActivityForResult(intent, REQUEST_CODE_SCAN);

    }

    //成功信息
    @Override
    public void onSuccess() {
        mZloadingDialog.dismiss();
        int actionCode = mPresenter.getmActionCode();
        String message = "";
        if(actionCode == Waybill.ACTION_RECEIVE){
            message = "成功扫码，已收件！";
        }else if(actionCode == Waybill.ACTION_DELIVERY){
            message = "成功扫码，正在派送！";
        }else if(actionCode == Waybill.ACTION_SIGNING){
            message = "成功扫码，已被签收！";
        }else if(actionCode == Waybill.ACTION_ENTER_WAREHOUSE){
            message = "成功扫码，已入库！";
        }else if(actionCode == Waybill.ACTION_OUT_OF_WAREHOUSE){
            message = "成功扫码，已出库！";
        }
        Snackbar.make(mView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTransport(final Car car) {
        int resultCode = car.getResultCode();
        LinearLayout ll_driver_bt = mView.findViewById(R.id.ll_driver_bt);
        LinearLayout ll_transport = mView.findViewById(R.id.ll_transport);
        if(resultCode == Car.NOT_DATA){
            ll_driver_bt.setVisibility(View.VISIBLE);
            ll_transport.setVisibility(View.GONE);
        }else{
            ll_driver_bt.setVisibility(View.GONE);
            ll_transport.setVisibility(View.VISIBLE);

            TextView tv_transport_status = mView.findViewById(R.id.tv_transport_status);
            TextView tv_transport_license = mView.findViewById(R.id.tv_transport_license);
            TextView tv_transport_location = mView.findViewById(R.id.tv_transport_location);
            TextView tv_transport_tolocal = mView.findViewById(R.id.tv_transport_tolocal);

            tv_transport_license.setText(car.getLicense());
            tv_transport_location.setText(car.getStartDepartment());
            tv_transport_tolocal.setText(car.getDepartment());

            int status = car.getStatus();
            String message = "";
            if(status == Car.LOADING_CODE){
                message = "装车中";
            }else if(status == Car.UNLOADING_CODE){
                message = "卸车中";
            }else if(status == Car.TRANSPORT_CODE){
                message = "车辆运送中，正在定位";
            }

            tv_transport_status.setText(message);

            ll_transport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DrivingActivity.class);
                    intent.putExtra(Car.EXTRA_CAR_NAME, car);
                    startActivity(intent);
                }
            });
            getLocation(car.getStatus(), car);
        }
        mZloadingDialog.dismiss();
    }

    //错误反馈
    @Override
    public void showError(int resultCode) {
        mZloadingDialog.dismiss();
        if(resultCode == Record.NOT_WAYBILL){
            Toast.makeText(getActivity(), "不存在该运单号", Toast.LENGTH_LONG).show();
        }else if(resultCode == Record.WAYBILL_STATUS_ERROR){
            int actionCode = mPresenter.getmActionCode();
            String message = "";
            if(actionCode == Waybill.ACTION_RECEIVE){
                message = "该包裹已被收件";
            }else if(actionCode == Waybill.ACTION_DELIVERY){
                message = "该包裹未出库，无法进行操作";
            }else if(actionCode == Waybill.ACTION_SIGNING){
                message = "该包裹未派送，无法进行操作";
            }else if(actionCode == Waybill.ACTION_ENTER_WAREHOUSE){
                message = "该包裹状态不能进行入库操作";
            }else if(actionCode == Waybill.ACTION_OUT_OF_WAREHOUSE){
                message = "该包裹未入库，无法进行操作";
            }
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }else if(resultCode == Record.NOT_SAME_DEPARTMENT){
            Toast.makeText(getActivity(), "该包裹不在该站点收件", Toast.LENGTH_LONG).show();
        }else if(resultCode == Record.RESULT_ERROR){
            Toast.makeText(getActivity(), "未知错误，请重新扫码", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "网络错误 0x00000", Toast.LENGTH_LONG).show();
        }
    }

    //申请权限返回结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA_PERMISSION: {
                //
                if(grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"申请权限失败", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadCamera();
                }
                break;
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_add_package:
                mPresenter.setmActionCode(Waybill.ACTION_RECEIVE);
                checkPermission();
                break;
            case R.id.bt_delivery:
                mPresenter.setmActionCode(Waybill.ACTION_DELIVERY);
                checkPermission();
                break;
            case R.id.bt_sign_up:
                mPresenter.setmActionCode(Waybill.ACTION_SIGNING);
                checkPermission();
                break;
            case R.id.ll_open_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_open_setting:
                break;
            case R.id.ll_logout:
                logout();
                break;
            case R.id.ll_search_waybill:
                mPresenter.setmActionCode(Waybill.ACTION_SEARCH);
                checkPermission();
                break;
            case R.id.ll_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_personal_terms:
                break;
            case R.id.bt_transport:
                Intent intentA = new Intent(getActivity(), InputCarActivity.class);
                startActivity(intentA);
                break;
            case R.id.bt_enter_warehouse:
                mPresenter.setmActionCode(Waybill.ACTION_ENTER_WAREHOUSE);
                checkPermission();
                break;
            case R.id.bt_out_of_warehouse:
                mPresenter.setmActionCode(Waybill.ACTION_OUT_OF_WAREHOUSE);
                checkPermission();
                break;
            default:
                break;
        }
    }


}
