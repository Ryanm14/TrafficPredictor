package me.ryanmiles.trafficpredictor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class TimeArg {
    private HashMap<String,Double> TimeAggMap;

    public TimeArg(String json) {
        TimeAggMap = new HashMap<>();
        try {
           JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TimeAggMap.put(jObj.getString("Time"), jObj.getDouble("Mean"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public double getTime(String time){
        return TimeAggMap.get(time);
    }


}
