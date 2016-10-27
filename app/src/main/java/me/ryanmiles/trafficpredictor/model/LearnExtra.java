package me.ryanmiles.trafficpredictor.model;

/**
 * Created by Ryan Miles on 10/27/2016.
 */

public class LearnExtra {
    private double a;
    private double b;
    private double diff;

    public LearnExtra(double a, double b, double diff) {
        this.a = a;
        this.b = b;
        this.diff = diff;
    }

    public double getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    @Override
    public String toString() {
        return "LearnExtra{" +
                "a=" + a +
                ", b=" + b +
                ", diff=" + diff +
                '}';
    }
}
