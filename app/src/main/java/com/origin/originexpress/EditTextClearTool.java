package com.origin.originexpress;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditTextClearTool {
    public static void addClearListener(final EditText editText, final ImageView imageView){
        //EditText文本监听器
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //当EditText文本长度不为0时，显示清空图标
                String str = s + "";
                if(s.length() > 0){
                    imageView.setVisibility(View.VISIBLE);
                }
                else{
                    imageView.setVisibility(View.INVISIBLE);
                }

            }
        });

        //清空图标点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(null);
            }
        });

    }
}
