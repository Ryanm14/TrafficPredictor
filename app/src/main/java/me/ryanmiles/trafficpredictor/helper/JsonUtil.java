package me.ryanmiles.trafficpredictor.helper;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.ryanmiles.trafficpredictor.async.GetDirectionsAsync;
import me.ryanmiles.trafficpredictor.model.Hour;
import me.ryanmiles.trafficpredictor.model.LatLngData;
import me.ryanmiles.trafficpredictor.model.Station;
import me.ryanmiles.trafficpredictor.model.StationList;

import static me.ryanmiles.trafficpredictor.helper.Constants.mStationNames;

/**
 * Created by Ryan Miles on 11/21/2016.
 */

public class JsonUtil {
    private static final String TAG = "JsonUtil";
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

    public void LoadStationsFromJson(String jsonFile) {
        //Load json station data to Java Objects
        String json = loadJSONFromAsset(jsonFile);
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                jObj = items.getJSONObject(i);
                if (jObj.getString("Type").equals("Mainline")) {
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

    public void loadLatLongData(String jsonFile) {
        ArrayList<LatLngData> latLngDatas = new ArrayList<>();
        String json = loadJSONFromAsset(jsonFile);
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                jObj = items.getJSONObject(i);
                LatLngData latLngData = new LatLngData();
                latLngData.setStationId((jObj.getInt("link")));
                latLngData.setLat(jObj.getString("lat"));
                latLngData.setLng(jObj.getString("lng"));
                latLngDatas.add(latLngData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Station station : mStationList.getStations()) {
            LatLngData data = null;

            for (LatLngData latLngData : latLngDatas) {
                if (latLngData.getStationId() == station.getID()) {
                    data = latLngData;
                }
            }
            try {
                station.setLat(data.getLat());
                station.setLng(data.getLng());
            } catch (Exception e) {
                Log.e(TAG, "Error setting Lat and Lng", e);
            }
        }
    }


    public void loadAllDelayData() {
        //Set default delay values for each station for all months, doftw, and 24 hr cycle
        for (Station station : mStationList.getStations()) {
            station.setDefaultMonthValues();
        }

        //Loop through each month and Doftw and load delay json data
        for (String mStationName : mStationNames) {
            String name = mStationName + "_";

            for (int i = 0; i < 1; i++) { //12
                //months

                for (int j = 0; j < 7; j++) { //7
                    //DOFTW

                    String load = name + Util.getMonth(i) + "_" + Util.getDay(j) + ".json";
                    loadDelayDataToStations(load, i, j);
                }
            }
        }
    }

    public void loadPathData() {
        int apiCalls = SaveData.getCalls();
        int total = mStationList.getStations().size();
        for (int i = 0; i < mStationList.getStations().size() - 1; i++) {
            String sourcelat = mStationList.getStationFromPostion(i).getLat();
            String sourcelong = mStationList.getStationFromPostion(i).getLng();
            String destlat = mStationList.getStationFromPostion(i + 1).getLat();
            String destlong = mStationList.getStationFromPostion(i + 1).getLng();
            String intersect = mStationList.getStationFromPostion(i).getFwy();
            String nextInteresect = mStationList.getStationFromPostion(i + 1).getFwy();


            if (!sourcelat.equals("") && !sourcelong.equals("") && !destlat.equals("") && !destlong.equals("") && intersect.equals(nextInteresect)) {
                String url = Util.makeURL(sourcelat, sourcelong, destlat, destlong, Util.getApi(apiCalls));
                apiCalls++;
                Log.v(TAG, i + " / " + total + " URL: " + url);
                try {
                    String json2 = new GetDirectionsAsync(url).execute().get();
                    mStationList.getStationFromPostion(i).setDirections(json2);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        SaveData.saveCalls(apiCalls);
    }

    public void calcDiff() {
        ArrayList<LatLngData> latLngDatas = new ArrayList<>();
        double sumPercantError = 0;
        double totalCounts = 0;
        String json = loadJSONFromAsset("data_compare2.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                jObj = items.getJSONObject(i);
                Station station = mStationList.getStation(jObj.getInt("VDS"));
                for (Hour hour : station.getmMonths().get(0).getDays().get(1).getHours()) {
                    double real = jObj.getInt(hour.getHour() + "");
                    double delay = hour.getDelay();
                    if (delay == 0.0) {
                        delay += 1;
                        real += 1;
                    }
                    sumPercantError += (Math.abs(real - delay) / delay);
                    totalCounts++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double finalvalue = (sumPercantError / totalCounts) * 100;
        Log.wtf(TAG, "Final value: " + finalvalue + "%");
    }
}
