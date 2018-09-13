package com.origin.originexpress.driving;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.origin.originexpress.R;
import com.origin.originexpress.data.Car;
import com.origin.originexpress.data.source.InputMegRepository;
import com.origin.originexpress.utils.ActivityUtils;

public class DrivingActivity extends AppCompatActivity {

    private DrivingContract.Presenter mPresenter;

    private InputMegRepository mInputMegRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        DrivingFragment drivingFragment = (DrivingFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        Car car = (Car)getIntent()
                .getSerializableExtra(Car.EXTRA_CAR_NAME);

        if(drivingFragment == null){
            drivingFragment = DrivingFragment.newInstance(car);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), drivingFragment,
                    R.id.contentFrame);
        }

        mInputMegRepository = new InputMegRepository();

        mPresenter = new DrivingPresenter( drivingFragment, mInputMegRepository);
    }

}
