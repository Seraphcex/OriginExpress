package com.origin.originexpress.data;

import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.Department;

import java.util.List;

/**
 * Created by XunselF on 2018/9/9.
 */

public class InputMeg {

    private int resultCode;

    private List<Car> carList;

    private List<Department> departmentList;

    public int getResultCode() {
        return resultCode;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
}
