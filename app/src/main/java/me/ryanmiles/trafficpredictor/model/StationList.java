package me.ryanmiles.trafficpredictor.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 10/31/2016.
 */

public class StationList {
    private static StationList sStationList;

    private Context mContext;
    private ArrayList<Station> stations;

    private StationList(Context context) {
        mContext = context.getApplicationContext();
        stations = new ArrayList<>();
    }

    public static StationList get(Context context) {
        if (sStationList == null) {
            sStationList = new StationList(context);
        }
        return sStationList;
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public void setStationList(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public ArrayList<Station> getStations() {
        return stations;

    }

    public Station getStation(int ID) {
        for (Station station : stations) {
            if (station.getID() == ID) {
                return station;
            }
        }
        return null;
    }

    public Station getStationFromPostion(int i) {
        return stations.get(i);
    }

    public Station getStationFromCaPm(String capm) {
        for (Station station : stations) {
            if (station.getCA_PM().equals(capm)) {
                return station;
            }
        }
        return null;
    }

    public void updateStation(Station station) {
        int stationID = station.getID();
        stations.set(getStationPostion(stationID), station);
    }

    public int getStationPostion(int ID) {
        for (Station station1 : stations) {
            if (station1.getID() == ID) {
                return stations.indexOf(station1);
            }
        }
        return -1;
    }


}
