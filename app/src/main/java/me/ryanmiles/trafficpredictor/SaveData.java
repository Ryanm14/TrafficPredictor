package me.ryanmiles.trafficpredictor;

import android.util.Log;

import java.util.ArrayList;

import io.paperdb.Paper;
import me.ryanmiles.trafficpredictor.model.Station;

/**
 * Created by Ryan Miles on 10/31/2016.
 */

public class SaveData {
    public static final String STATIONS_KEY = "stations";
    private static final String TAG = SaveData.class.getCanonicalName();

    public static void saveData() {
    }

    public static void saveStations(ArrayList<Station> stations) {
        Paper.book().write(STATIONS_KEY, stations);
        Log.d(TAG, "Saving Stations");
    }

    public static ArrayList<Station> loadStations() {
        Log.d(TAG, "Loading Stations");
        return Paper.book().read(STATIONS_KEY, null);
    }
}
