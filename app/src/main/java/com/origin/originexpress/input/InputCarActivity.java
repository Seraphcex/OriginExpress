package com.origin.originexpress.input;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.origin.originexpress.R;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.utils.ActivityUtils;

public class InputCarActivity extends AppCompatActivity {

    private InputCarContract.Presenter mPresenter;

    private InputMegRepository mInputMegRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_car);

        InputCarFragment inputCarFragment = (InputCarFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(inputCarFragment == null){
            inputCarFragment = InputCarFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), inputCarFragment, R.id.contentFrame);
        }

        mInputMegRepository = new InputMegRepository();

        mPresenter = new InputCarPresenter(inputCarFragment, mInputMegRepository);

    }
}
