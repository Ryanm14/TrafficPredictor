package me.ryanmiles.trafficpredictor;

import android.app.Application;
import android.util.Log;

import io.paperdb.Paper;
import me.ryanmiles.trafficpredictor.helper.JsonUtil;
import me.ryanmiles.trafficpredictor.helper.SaveData;
import me.ryanmiles.trafficpredictor.model.StationList;


/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class App extends Application {
    private static final String TAG = "App";
    StationList stationList;
    JsonUtil mJsonUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Saving Library
        Paper.init(this);

        //Initalize JsonUtil
        mJsonUtil = new JsonUtil(this);

        //Initialize StationList Singleton
        stationList = StationList.get(this);

        //On app first run
        if (SaveData.loadStations() == null) {
            //Load station data from json
            mJsonUtil.LoadStationsFromJson();

            //Load lat and long data from json and assign to a station
            mJsonUtil.loadLatLongData();

            //Load lat and long data between stations
            //loadPathData();

            //Save stations for faster load time and limit api usage
            SaveData.saveStations(stationList.getStations());
            Log.d(TAG, "Loaded Stations from json file");

        } else {
            //Not first time
            //Get saved data and set the stationList
            stationList.setStationList(SaveData.loadStations());
            Log.d(TAG, "Loaded Stations from memory");
        }

        //load delay json data to stations
        mJsonUtil.loadAllDelayData();
    }
}
