package me.ryanmiles.trafficpredictor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class DayArg {
    private HashMap<String,Double> DayAggMap;

    public DayArg(String json) {
        DayAggMap = new HashMap<>();
        try {
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jObj = null;
                try {
                    jObj = items.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DayAggMap.put(jObj.getString("Day"), jObj.getDouble("Mean"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public double getDay(String day){
        return DayAggMap.get(day);
    }
}
