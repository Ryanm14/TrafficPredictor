package me.ryanmiles.trafficpredictor.model;

/**
 * Created by Ryan Miles on 11/1/2016.
 */
public class Hour {
    private int hour;
    private double delay;


    public Hour(int i) {
        hour = i;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
