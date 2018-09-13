package com.origin.originexpress.utils;

/**
 * Created by XunselF on 2018/9/4.
 */

public interface GeyCallback {

    interface GetCallback<T>{

        void onLoaded(T t);

        void onDataNotAvailable(String error);

    }
}
