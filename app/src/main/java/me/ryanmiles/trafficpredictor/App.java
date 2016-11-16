package me.ryanmiles.trafficpredictor;

import android.app.Application;
import android.os.AsyncTask;
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
            loadLatLongData();
            loadPathData();
            SaveData.saveStations(stationList.getStations());
            Log.d(TAG, "Loaded Stations from json file");
        } else {
            stationList.setStationList(SaveData.loadStations());
            for (Station station : stationList.getStations()) {
                station.setDefaultMonthValues();
            }


            for (int i = 0; i < 1; i++) {
                //month
                for (int j = 0; j < 7; j++) {
                    //DOFTW
                    String load = "I5N_" + getMonth(i) + "_" + getDay(j) + ".json";
                    loadDelayDataToStations(load, i, j);
                }
            }

            Log.d(TAG, "Loaded Stations from memory");
        }

    }

    private String getDay(int j) {
        switch (j) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return "wow";
    }

    private String getMonth(int i) {
        switch (i) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "wow";
    }

    private void loadPathData() {
        for (int i = 0; i < stationList.getStations().size() - 1; i++) {
            String sourcelat = stationList.getStationFromPostion(i).getLat();
            String sourcelong = stationList.getStationFromPostion(i).getLng();
            String destlat = stationList.getStationFromPostion(i + 1).getLat();
            String destlong = stationList.getStationFromPostion(i + 1).getLng();

            if (!sourcelat.equals("") || !sourcelong.equals("") || !destlat.equals("") || !destlong.equals("")) {
                String url = makeURL(sourcelat, sourcelong, destlat, destlong);
                Log.v(TAG, "loadPathData: " + url + " ID: " + stationList.getStationFromPostion(i).getID());
                try {
                    String json2 = new connectAsyncTask(url).execute().get();
                    stationList.getStationFromPostion(i).setDirections(json2);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
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
        String json = loadJSONFromAsset("I5N_lat_lng.json");
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Station station = stationList.getStationFromCaPm(jObj.getString("ca_pm"));
                    station.setLat(jObj.getString("lat"));
                    station.setLng(jObj.getString("lng"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadDelayDataToStations(String jsonFileName, int month, int doftw) {
        String json = loadJSONFromAsset(jsonFileName);
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                    Station station = stationList.getStation(jObj.getInt("VDS"));
                        for (int time = 0; time < 24; time++) {
                            double value = jObj.getDouble(String.valueOf(time));
                            try {
                                station.setDataMax(month, doftw, time, value);
                            } catch (NullPointerException error) {
                                error.printStackTrace();
                            }
                            //  Log.d(TAG, "Station: " + station + "Month: " + month + " Sunday: 0 " + "Time: " + time + " Value: " + value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
