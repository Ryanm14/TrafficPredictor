package me.ryanmiles.trafficpredictor.helper;

import android.util.Log;

import java.util.ArrayList;

import io.paperdb.Paper;
import me.ryanmiles.trafficpredictor.model.Station;

import static io.paperdb.Paper.book;

/**
 * Created by Ryan Miles on 10/31/2016.
 */

public class SaveData {
    public static final String STATIONS_KEY = "stations";
    public static final String API_CALLS_KEY = "api_calls";
    private static final String TAG = SaveData.class.getCanonicalName();

    public static void saveData() {
    }

    public static void saveStations(final ArrayList<Station> stations) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Saving Stations");
                book().write(STATIONS_KEY, stations);
                Log.i(TAG, "Saved Stations");
            }
        }).start();
    }

    public static ArrayList<Station> loadStations() {
        Log.i(TAG, "Loading Stations");
        return book().read(STATIONS_KEY, null);
    }

    public static void delete() {
        book().destroy();
    }

    public static int getCalls() {
        Log.i(TAG, "Loading Calls");
        return Paper.book().read(API_CALLS_KEY, 0);
    }

    public static void saveCalls(int apiCalls) {
        Log.i(TAG, "Save Calls");
        book().write(API_CALLS_KEY, apiCalls);
    }
}
