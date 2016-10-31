package me.ryanmiles.trafficpredictor.model;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class Point {
    ArrayList<Double> DayAvgSpeeds;
    ArrayList<Double> TimeAvgSpeeds;

    public Point(ArrayList<Double> dayAvgSpeeds, ArrayList<Double> timeAvgSpeeds) {
        DayAvgSpeeds = dayAvgSpeeds;
        TimeAvgSpeeds = timeAvgSpeeds;
    }

    public ArrayList<Double> getDayAvgSpeeds() {
        return DayAvgSpeeds;
    }

    public void setDayAvgSpeeds(ArrayList<Double> dayAvgSpeeds) {
        DayAvgSpeeds = dayAvgSpeeds;
    }

    public ArrayList<Double> getTimeAvgSpeeds() {
        return TimeAvgSpeeds;
    }

    public void setTimeAvgSpeeds(ArrayList<Double> timeAvgSpeeds) {
        TimeAvgSpeeds = timeAvgSpeeds;
    }
    @Override
    public String toString() {
        return "Point{" +
                "DayAvgSpeeds=" + DayAvgSpeeds +
                ", TimeAvgSpeeds=" + TimeAvgSpeeds +
                '}';
    }
}
