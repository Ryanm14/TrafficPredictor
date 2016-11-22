package me.ryanmiles.trafficpredictor.helper;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import me.ryanmiles.trafficpredictor.async.GetDirectionsAsync;
import me.ryanmiles.trafficpredictor.model.Station;
import me.ryanmiles.trafficpredictor.model.StationList;

/**
 * Created by Ryan Miles on 11/21/2016.
 */

public class JsonUtil {
    private StationList mStationList;
    private Context mContext;

    public JsonUtil(Context context) {
        mContext = context;
        mStationList = StationList.get(context);
    }

    public String loadJSONFromAsset(String jsonfile) {
        //Get json string from json file in the asset folder
        String json;
        try {
            InputStream is = mContext.getAssets().open(jsonfile);
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

    public void LoadStationsFromJson() {
        //Load json station data to Java Objects
        String json = loadJSONFromAsset("I5N_Stations.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                jObj = items.getJSONObject(i);
                Station station = new Station();
                station.setFwy(jObj.getString("Fwy"));
                station.setDistrict(jObj.getInt("District"));
                station.setCounty(jObj.getString("County"));
                station.setCity(jObj.getString("City"));
                station.setCA_PM(jObj.getString("CA PM"));
                station.setAbs_PM(jObj.getDouble("Abs PM"));
                if (!jObj.isNull("Length")) {
                    station.setLength(jObj.getDouble("Length"));
                }
                station.setID(jObj.getInt("ID"));
                station.setName(jObj.getString("Name"));
                station.setLanes(jObj.getInt("Lanes"));
                station.setType(jObj.getString("Type"));
                station.setSensorType(jObj.getString("Sensor Type"));
                station.setHOV(jObj.getString("HOV"));
                station.setMS_ID(jObj.getString("MS ID"));
                mStationList.addStation(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadDelayDataToStations(String jsonFileName, int month, int doftw) {
        String json = loadJSONFromAsset(jsonFileName);
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj;
                jObj = items.getJSONObject(i);
                Station station = mStationList.getStation(jObj.getInt("VDS"));
                for (int time = 0; time < 24; time++) {
                    double value = jObj.getDouble(String.valueOf(time));
                    station.setDataMax(month, doftw, time, value);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void loadLatLongData() {
        String json = loadJSONFromAsset("I5N_lat_lng.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                jObj = items.getJSONObject(i);
                try {
                    Station station = mStationList.getStationFromCaPm(jObj.getString("ca_pm"));
                    //Station station = mStationList.getStationFromCaPm(jObj.getString("name"));
                    station.setLat(jObj.getString("lat"));
                    station.setLng(jObj.getString("lng"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadAllDelayData() {
        //Set default delay values for each station for all months, doftw, and 24 hr cycle
        for (Station station : mStationList.getStations()) {
            station.setDefaultMonthValues();
        }

        //Loop through each month and Doftw and load delay json data
        for (int i = 0; i < 1; i++) {
            //month
            for (int j = 0; j < 7; j++) {
                //DOFTW
                String load = "I5N_" + Util.getMonth(i) + "_" + Util.getDay(j) + ".json";
                loadDelayDataToStations(load, i, j);
            }
        }
    }

    public void loadPathData() {
        for (int i = 0; i < mStationList.getStations().size() - 1; i++) {
            String sourcelat = mStationList.getStationFromPostion(i).getLat();
            String sourcelong = mStationList.getStationFromPostion(i).getLng();
            String destlat = mStationList.getStationFromPostion(i + 1).getLat();
            String destlong = mStationList.getStationFromPostion(i + 1).getLng();


            //Also only mainland
            if (!sourcelat.equals("") || !sourcelong.equals("") || !destlat.equals("") || !destlong.equals("")) {
                String url = Util.makeURL(sourcelat, sourcelong, destlat, destlong);
                try {
                    String json2 = new GetDirectionsAsync(url).execute().get();
                    mStationList.getStationFromPostion(i).setDirections(json2);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
