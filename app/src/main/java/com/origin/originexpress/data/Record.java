package com.origin.originexpress.data;

/**
 * Created by XunselF on 2018/9/6.
 */

public class Record {

    public static final int NOT_WAYBILL = 0;

    public static final int RESULT_OK = 1;

    public static final int WAYBILL_STATUS_ERROR = 2;

    public static final int NOT_SAME_DEPARTMENT = 3;

    public static final int RESULT_ERROR = 4;


    //运单号
    private String md5waybillnumber;

    private String waybillnumber;
    //管理者
    private String manager;
    //手机号
    private String phone;
    //车牌（司机）
    private String license;
    //所在站点
    private String location;
    //寄件位置
    private String tolocal;
    //时间
    private String time;
    //状态
    private int status;

    private int actionCode;

    private int resultCode;

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public String getMd5waybillnumber() {
        return md5waybillnumber;
    }

    public void setMd5waybillnumber(String md5waybillnumber) {
        this.md5waybillnumber = md5waybillnumber;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getPhone() {
        return phone;
    }

    public int getStatus() {
        return status;
    }

    public String getLicense() {
        return license;
    }

    public String getLocation() {
        return location;
    }

    public String getManager() {
        return manager;
    }

    public String getTime() {
        return time;
    }

    public String getTolocal() {
        return tolocal;
    }

    public String getWaybillnumber() {
        return waybillnumber;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTolocal(String tolocal) {
        this.tolocal = tolocal;
    }

    public void setWaybillnumber(String waybillnumber) {
        this.waybillnumber = waybillnumber;
    }
}
