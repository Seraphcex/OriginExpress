package com.origin.originexpress.data;

/**
 * Created by XunselF on 2018/9/9.
 */

public class Department {

    private String name;

    private String longitude;

    private String latitude;

    private String value;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


}
