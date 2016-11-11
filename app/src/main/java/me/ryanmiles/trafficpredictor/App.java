package me.ryanmiles.trafficpredictor;

import android.app.Application;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

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
            Log.d(TAG, "Loaded Stations from json file");
        } else {
            stationList.setStationList(SaveData.loadStations());
            Log.d(TAG, "Loaded Stations from memory");
        }
        //Fix to go at saved - idk why not working

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loadLatLongData();

        if (stationList.getStationFromPostion(0).getJsonPath().equals("")) {
            loadPathData();
        }
        //    loadMinDelayDataToStations();
        loadMaxDelayDataToStations();

    }

    private void loadPathData() {
        JSONParser jParser = new JSONParser();
        for (int i = 0; i < stationList.getStations().size() - 1; i++) {
            String sourcelat = stationList.getStationFromPostion(i).getLat();
            String sourcelong = stationList.getStationFromPostion(i).getLng();
            String destlat = stationList.getStationFromPostion(i + 1).getLat();
            String destlong = stationList.getStationFromPostion(i + 1).getLng();

            if (!sourcelat.equals("") || !sourcelong.equals("") || !destlat.equals("") || !destlong.equals("")) {
                String url = makeURL(sourcelat, sourcelong, destlat, destlong);
                Log.d(TAG, "loadPathData: " + url + " ID: " + stationList.getStationFromPostion(i).getID());
                try {
                    String json2 = new connectAsyncTask(url).execute().get();
                    stationList.getStationFromPostion(i).setJsonPath(json2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
//        SaveData.saveStations(stationList.getStations());
    }

    private String makeURL(String sourcelat, String sourcelog, String destlat, String destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(sourcelat);
        urlString.append(",");
        urlString.append(sourcelog);
        urlString.append("&destination=");// to
        urlString.append(destlat);
        urlString.append(",");
        urlString.append(destlog);
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyB99QV5Y97Pinr6I0W0Nem1HGGw37x38DE");
        return urlString.toString();
    }

    public void loadLatLongData() {
        String json = loadJSONFromAsset("Station 1-200 Lats.json");
        Log.wtf(TAG, "loadLatLongData: " + json);
        try {
            JSONArray items = new JSONArray(json);
            Log.wtf(TAG, "loadLatLongData: " + items.toString());
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Log.d(TAG, "loadLatLongData: " + jObj.toString());
                    //Double check the id later
                    Station station = stationList.getStationFromPostion(i);
                    station.setLat(jObj.getString("lat"));
                    station.setLng(jObj.getString("lng"));
                    Log.d(TAG, "loadLatLongData: " + station);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMinDelayDataToStations() {
        String json = loadJSONFromAsset("SundayMaxDelayDataI5-101-331.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Station station = stationList.getStation(jObj.getInt("VDS"));
                    for (int month = 0; month < 3; month++) {
                        for (int time = 0; time < 24; time++) {
                            double value = jObj.getDouble(String.valueOf(time));
                            try {
                                station.setDataMax(month, 0, time, value);
                            } catch (NullPointerException error) {
                                error.printStackTrace();
                            }
                            //                           Log.d(TAG,"Station: " + station + "Month: " + month + " Sunday: 0 " + "Time: " + time + " Value: " + value);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMaxDelayDataToStations() {
        String json = loadJSONFromAsset("SundayMaxDelayDataI5-101-331.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Station station = stationList.getStation(jObj.getInt("VDS"));
                    for (int month = 0; month < 3; month++) {
                        for (int time = 0; time < 24; time++) {
                            double value = jObj.getDouble(String.valueOf(time));
                            try {
                                station.setDataMax(month, 0, time, value);
                            } catch (NullPointerException error) {
                                error.printStackTrace();
                            }
                            Log.d(TAG, "Station: " + station + "Month: " + month + " Sunday: 0 " + "Time: " + time + " Value: " + value);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Test");
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
                    stationList.addStation(station);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
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

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

    }
}


//USE HIGH AND LOW FOR DELAY FOR WHOLE ROUTE FOR HOUR / DAY OR DAYY OF WEEK AND RUN TESTS WITH THOSES
