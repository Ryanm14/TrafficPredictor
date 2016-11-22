package me.ryanmiles.trafficpredictor.helper;

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

    public static void saveStations(final ArrayList<Station> stations) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Saving Stations");
                Paper.book().write(STATIONS_KEY, stations);
                Log.i(TAG, "Saved Stations");
            }
        }).start();
    }

    public static ArrayList<Station> loadStations() {
        Log.i(TAG, "Loading Stations");
        return Paper.book().read(STATIONS_KEY, null);
    }

    public static void delete() {
        Paper.book().destroy();
    }
}
