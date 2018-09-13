package com.origin.originexpress.operate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.origin.originexpress.R;
import com.origin.originexpress.data.Record;
import com.origin.originexpress.data.Waybill;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/9/6.
 */

public class OperateFragment extends Fragment implements OperateContract.View, View.OnClickListener{


    private OperateContract.Presenter mOperatePresenter;

    private TextView tv_waybill_number;
    private TextView tv_waybill_status;

    private TextView tv_waybill_sname;
    private TextView tv_waybill_sphone;
    private TextView tv_waybill_saddress;

    private TextView tv_waybill_rname;
    private TextView tv_waybill_rphone;
    private TextView tv_waybill_raddress;

    private View mView;

    private RecordAdapter mRecordAdapter;

    private ZLoadingDialog mZloadingDialog;

    private Handler mHandler = new Handler(Looper.getMainLooper());



    public static OperateFragment newInstance(){
        OperateFragment operateFragment = new OperateFragment();
        return operateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_operate, container, false);
        mView = root;
        init();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mOperatePresenter.start();
    }

    @Override
    public void setPresenter(OperateContract.Presenter presenter) {
        mOperatePresenter = presenter;
    }

    @Override
    public void init() {
        LinearLayout ll_return = mView.findViewById(R.id.ll_return);
        tv_waybill_number = mView.findViewById(R.id.tv_waybill_number);
        tv_waybill_status = mView.findViewById(R.id.tv_waybill_status);

        tv_waybill_sname = mView.findViewById(R.id.tv_waybill_sname);
        tv_waybill_sphone = mView.findViewById(R.id.tv_waybill_sphone);
        tv_waybill_saddress = mView.findViewById(R.id.tv_waybill_saddress);
        tv_waybill_rname = mView.findViewById(R.id.tv_waybill_rname);
        tv_waybill_rphone = mView.findViewById(R.id.tv_waybill_rphone);
        tv_waybill_raddress = mView.findViewById(R.id.tv_waybill_raddress);

        //弹窗
        mZloadingDialog = new ZLoadingDialog(getActivity());
        mZloadingDialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)
                .setLoadingColor(getResources().getColor(R.color.colorThemeBackground))
                .setHintText("Loading...")
                .setHintTextSize(16)
                .setHintTextColor(getResources().getColor(R.color.colorThemeBackground))
                .setDurationTime(0.5);


        RecyclerView recyclerView = mView.findViewById(R.id.rv_records);
        mRecordAdapter = new RecordAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mRecordAdapter);

        ll_return.setOnClickListener(this);
    }

    @Override
    public void showLoading() {
        mZloadingDialog.show();
    }

    @Override
    public void onSuccess(final Waybill waybill) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (waybill.getMd5WaybillNumber() == null){
                    showErrorMeg("无效的二维码");
                }else{

                    mZloadingDialog.dismiss();
                    tv_waybill_number.setText(waybill.getWaybillNumber());
                    tv_waybill_sname.setText(waybill.getsName());
                    tv_waybill_sphone.setText(waybill.getsPhone());
                    tv_waybill_saddress.setText(waybill.getsAddress());

                    tv_waybill_rname.setText(waybill.getrName());
                    tv_waybill_rphone.setText(waybill.getrPhone());
                    tv_waybill_raddress.setText(waybill.getrAddress());

                    List<Record> records = waybill.getRecordList();

                    if(waybill.getStatus().equals("0")){
                        tv_waybill_status.setText("已收件");
                    }else if(waybill.getStatus().equals("1")){
                        tv_waybill_status.setText("派送中");
                    }else if(waybill.getStatus().equals("2")){
                        tv_waybill_status.setText("已签收");
                    }else if(waybill.getStatus().equals("7")){
                        tv_waybill_status.setText("已下单");
                    }else{
                        tv_waybill_status.setText("运输中");
                    }

                    if(records.size() == 0){
                        Record record = new Record();
                        record.setStatus(7);
                        record.setTime(waybill.getTime());
                        records.add(record);
                    }
                    mRecordAdapter.setRecords(records);
                    mRecordAdapter.notifyDataSetChanged();
                }

            }
        }, 1000);
    }

    @Override
    public void showErrorMeg(String error) {
        mZloadingDialog.dismiss();
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_return:
                getActivity().finish();
                break;
        }
    }
}
