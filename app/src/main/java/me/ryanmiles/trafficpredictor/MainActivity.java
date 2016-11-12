package me.ryanmiles.trafficpredictor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aigestudio.wheelpicker.WheelPicker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.ryanmiles.trafficpredictor.event.UpdateDoftwEvent;
import me.ryanmiles.trafficpredictor.event.UpdateMonthEvent;
import me.ryanmiles.trafficpredictor.event.UpdateTimesEvent;

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

        mWheelPickerMonth.setData(getMonths());
        mWheelPickerMonth.setOnItemSelectedListener(this);
        mWheelPickerDotw.setData(getDotw());
        mWheelPickerDotw.setOnItemSelectedListener(this);
        mWheelPickerTime.setData(getTimes());
        mWheelPickerTime.setOnItemSelectedListener(this);

    }


    public List getMonths() {
        List<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return months;
    }

    public List getDotw() {
        List<String> doftw = new ArrayList<>();
        doftw.add("Sunday");
        doftw.add("Monday");
        doftw.add("Tuesday");
        doftw.add("Wednesday");
        doftw.add("Thursday");
        doftw.add("Friday");
        doftw.add("Saturday");
        return doftw;
    }

    public List getTimes() {
        List<String> times = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            times.add(i + ":00 am");
        }
        for (int i = 1; i <= 12; i++) {
            times.add(i + ":00 pm");
        }
        return times;
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {

        String text = "";
        switch (picker.getId()) {
            case R.id.wheel_month:
                EventBus.getDefault().post(new UpdateMonthEvent(position));
                break;
            case R.id.wheel_dotw:
                EventBus.getDefault().post(new UpdateDoftwEvent(position));
                break;
            case R.id.wheel_times:
                EventBus.getDefault().post(new UpdateTimesEvent(position));
                break;
        }
    }
}
