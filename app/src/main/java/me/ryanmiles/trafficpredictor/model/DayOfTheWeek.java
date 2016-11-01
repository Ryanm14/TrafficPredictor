package me.ryanmiles.trafficpredictor.model;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 11/1/2016.
 */
public class DayOfTheWeek {
    private ArrayList<Hour> mHours;
    private String mName;

    public DayOfTheWeek(String name) {
        mName = name;
        mHours = getDefaultHours();
    }

    private ArrayList<Hour> getDefaultHours() {
        ArrayList<Hour> defaultHours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            defaultHours.add(new Hour(i));
        }
        return defaultHours;
    }

    public ArrayList<Hour> getHours() {
        return mHours;
    }

    public void setHours(ArrayList<Hour> hours) {
        mHours = hours;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
