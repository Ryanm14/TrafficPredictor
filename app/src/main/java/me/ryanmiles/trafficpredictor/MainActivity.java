package me.ryanmiles.trafficpredictor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aigestudio.wheelpicker.WheelPicker;

import org.greenrobot.eventbus.EventBus;

import me.ryanmiles.trafficpredictor.event.UpdateDoftwEvent;
import me.ryanmiles.trafficpredictor.event.UpdateTimesEvent;
import me.ryanmiles.trafficpredictor.helper.Util;

public class MainActivity extends AppCompatActivity implements WheelPicker.OnItemSelectedListener {

    WheelPicker mWheelPickerMonth;
    WheelPicker mWheelPickerDotw;
    WheelPicker mWheelPickerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWheelPickerMonth = (WheelPicker) findViewById(R.id.wheel_month);
        mWheelPickerDotw = (WheelPicker) findViewById(R.id.wheel_dotw);
        mWheelPickerTime = (WheelPicker) findViewById(R.id.wheel_times);

        mWheelPickerMonth.setData(Util.getMonths());
        mWheelPickerMonth.setOnItemSelectedListener(this);
        mWheelPickerDotw.setData(Util.getDotw());
        mWheelPickerDotw.setOnItemSelectedListener(this);
        mWheelPickerTime.setData(Util.getTimes());
        mWheelPickerTime.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.wheel_dotw:
                EventBus.getDefault().post(new UpdateDoftwEvent(position));
                break;
            case R.id.wheel_times:
                EventBus.getDefault().post(new UpdateTimesEvent(position));
                break;
        }
    }
}
