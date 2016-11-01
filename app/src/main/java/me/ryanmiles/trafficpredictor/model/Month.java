package me.ryanmiles.trafficpredictor.model;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 11/1/2016.
 */
public class Month {
    private String name;
    private ArrayList<DayOfTheWeek> mDays;

    public Month(String name) {
        this.name = name;
        mDays = getDefaultValues();
    }

    private ArrayList<DayOfTheWeek> getDefaultValues() {
        ArrayList<DayOfTheWeek> defaultDays = new ArrayList<>();
        defaultDays.add(new DayOfTheWeek("Sunday"));
        defaultDays.add(new DayOfTheWeek("Monday"));
        defaultDays.add(new DayOfTheWeek("Tuesday"));
        defaultDays.add(new DayOfTheWeek("Wednesday"));
        defaultDays.add(new DayOfTheWeek("Thursday"));
        defaultDays.add(new DayOfTheWeek("Friday"));
        defaultDays.add(new DayOfTheWeek("Saturday"));
        return defaultDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<DayOfTheWeek> getDays() {
        return mDays;
    }

    public void setDays(ArrayList<DayOfTheWeek> days) {
        mDays = days;
    }
}
