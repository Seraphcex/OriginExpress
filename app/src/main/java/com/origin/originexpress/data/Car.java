package com.origin.originexpress.data;

import java.io.Serializable;

/**
 * Created by XunselF on 2018/9/9.
 */

public class Car implements Serializable{

    public static final String EXTRA_CAR_NAME = "EXTRA_CAR_NAME";

    public static final int NOT_DATA = 0;

    public static final int RESULT_OK = 1;

    public static final int NO_TASK_CODE = 0;

    public static final int LOADING_CODE = 1;

    public static final int TRANSPORT_CODE = 2;

    public static final int UNLOADING_CODE = 3;


    private int resultCode;

    private String license;

    private String username;

    private String driver;

    private String phone;

    private double longitude;

    private double latitude;

    private String location;

    private String startDepartment;

    private String 	department;

    private int status;

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getStartDepartment() {
        return startDepartment;
    }

    public void setStartDepartment(String startDepartment) {
        this.startDepartment = startDepartment;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getLicense() {
        return license;
    }

    public int getStatus() {
        return status;
    }

    public String getDepartment() {
        return department;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDriver() {
        return driver;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
