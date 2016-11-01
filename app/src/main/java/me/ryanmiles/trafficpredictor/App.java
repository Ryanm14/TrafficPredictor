package me.ryanmiles.trafficpredictor;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import io.paperdb.Paper;
import me.ryanmiles.trafficpredictor.model.Station;
import me.ryanmiles.trafficpredictor.model.StationList;


/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class App extends Application {

    private static final String TAG = "App";
    StationList stationList;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        stationList = StationList.get(this);
        if (SaveData.loadStations() == null) {
            AllStations();
        } else {
            stationList.setStationList(SaveData.loadStations());
        }

    }

    private void AllStations() {
        String json = loadJSONFromAsset("I5N_Stations.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Station station = new Station();
                    station.setFwy(jObj.getString("Fwy"));
                    station.setDistrict(jObj.getInt("District"));
                    station.setCounty(jObj.getString("County"));
                    station.setCity(jObj.getString("City"));
                    station.setCA_PM(jObj.getString("CA PM"));
                    station.setAbs_PM(jObj.getDouble("Abs PM"));
                    station.setLength(jObj.getDouble("Length"));
                    station.setID(jObj.getInt("ID"));
                    station.setName(jObj.getString("Name"));
                    station.setLanes(jObj.getInt("Lanes"));
                    station.setType(jObj.getString("Type"));
                    station.setSensorType(jObj.getString("Sensor Type"));
                    station.setHOV(jObj.getString("HOV"));
                    station.setMS_ID(jObj.getString("MS ID"));
                    stationList.addStation(station);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        SaveData.saveStations(stationList.getStations());
    }

    public String loadJSONFromAsset(String jsonfile) {
        String json = null;
        try {

            InputStream is = getAssets().open(jsonfile);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

//USE HIGH AND LOW FOR DELAY FOR WHOLE ROUTE FOR HOUR / DAY OR DAYY OF WEEK AND RUN TESTS WITH THOSES
