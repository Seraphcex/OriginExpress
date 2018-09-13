package com.origin.originexpress.data;

import java.util.List;

/**
 * Created by XunselF on 2018/9/6.n'h
 */

public class Waybill {

    public static final String MD5_WAYBILL_NUMBER_NAME = "md5_WAYBILL_NUMBER_NAME";

    public static final int ACTION_RECEIVE = 0;

    public static final int ACTION_DELIVERY = 1;

    public static final int ACTION_SIGNING = 2;

    public static final int ACTION_ENTER_WAREHOUSE = 3;

    public static final int ACTION_OUT_OF_WAREHOUSE = 4;

    public static final int ACTION_LOADING_CAR = 5;

    public static final int ACTION_UNLOADING_CAR = 6;

    public static final int ACTION_NOT_RECEIVE = 7;

    public static final int ACTION_SEARCH = 8;

    //结果码
    private int resultCode;
    //运单号
    private String waybillNumber;
    private String md5WaybillNumber;
    //寄件人信息
    private String sName;
    private String sPhone;
    private String sAddress;
    //收件人信息
    private String rName;
    private String rPhone;
    private String rAddress;
    //时间
    private String time;
    //包裹状态
    private String status;
    //所有记录
    private List<Record> recordList;

    public String getTime() {
        return time;
    }

    public int getResultCode() {
        return resultCode;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public String getMd5WaybillNumber() {
        return md5WaybillNumber;
    }

    public String getrAddress() {
        return rAddress;
    }

    public String getrName() {
        return rName;
    }

    public String getrPhone() {
        return rPhone;
    }

    public String getsAddress() {
        return sAddress;
    }

    public String getsName() {
        return sName;
    }

    public String getsPhone() {
        return sPhone;
    }

    public String getStatus() {
        return status;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMd5WaybillNumber(String md5WaybillNumber) {
        this.md5WaybillNumber = md5WaybillNumber;
    }

    public void setrAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public void setrPhone(String rPhone) {
        this.rPhone = rPhone;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }
}
