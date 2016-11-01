package me.ryanmiles.trafficpredictor.model;

/**
 * Created by Ryan Miles on 11/1/2016.
 */
public class Hour {
    private int hour;

    private double dataDelayMin;
    private double dataDelayMax;

    private double actualDelayMin;
    private double actualDelayMax;

    private double minPercent;
    private double maxPercent;

    private double difference;


    public Hour(int i) {
        hour = i;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getDataDelayMin() {
        return dataDelayMin;
    }

    public void setDataDelayMin(double dataDelayMin) {
        this.dataDelayMin = dataDelayMin;
    }

    public double getDataDelayMax() {
        return dataDelayMax;
    }

    public void setDataDelayMax(double dataDelayMax) {
        this.dataDelayMax = dataDelayMax;
    }

    public double getActualDelayMin() {
        return actualDelayMin;
    }

    public void setActualDelayMin(double actualDelayMin) {
        this.actualDelayMin = actualDelayMin;
    }

    public double getActualDelayMax() {
        return actualDelayMax;
    }

    public void setActualDelayMax(double actualDelayMax) {
        this.actualDelayMax = actualDelayMax;
    }

    public double getMinPercent() {
        return minPercent;
    }

    public void setMinPercent(double minPercent) {
        this.minPercent = minPercent;
    }

    public double getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(double maxPercent) {
        this.maxPercent = maxPercent;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }
}
