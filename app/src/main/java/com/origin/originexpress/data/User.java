package com.origin.originexpress.data;

/**
 * Created by XunselF on 2018/9/3.
 */

public class User {

    //验证回调结果参数
    public static final int USERNAME_IS_EXIST = 0;

    public static final int RESULT_OK = 1;

    public static final int WRONG_PASSWORD = 2;

    public static final int DATA_IS_EMPTY = 3;

    //身份参数
    public static final int USER_IDENTITY_ROOT = 0;

    public static final int USER_IDENTITY_DELIVERY = 1;

    public static final int USER_IDENTITY_DRIVER = 2;

    public static final int USER_IDENTITY_STATION = 3;

    public static final int DATA_NULL = 9;

    public static final String IDENTITY_CODE_NAME = "IDENTITY_CODE_NAME";

    public static final String USERNAME_NAME = "USERNAME_NAME";

    public static final String PASSWORD_NAME = "PASSWORD_NAME";

    public static final String FULLNAME_NAME = "FULLNAME_NAME";

    public static final String PHONE_NAME = "PHONE_NAME";

    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";

    public static final String IS_REMEMBER_PASSW_NAME = "IS_REMEMBER_PASSW_NAME";

    public static final String IS_AUTO_LOGIN_NAME = "IS_AUTO_LOGIN_NAME";

    private String username;

    private String password;

    private String fullname;

    private String phone;

    private String department;

    private int result_code;

    private int identity_code;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdentity_code() {
        return identity_code;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setIdentity_code(int identity_code) {
        this.identity_code = identity_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
