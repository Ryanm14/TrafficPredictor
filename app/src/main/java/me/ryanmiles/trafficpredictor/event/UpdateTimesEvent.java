package me.ryanmiles.trafficpredictor.event;

/**
 * Created by Ryan Miles on 11/12/2016.
 */
public class UpdateTimesEvent {
    private int pos;

    public UpdateTimesEvent(int position) {
        pos = position;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
