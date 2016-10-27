package me.ryanmiles.trafficpredictor;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.ryanmiles.trafficpredictor.model.LearnExtra;
import me.ryanmiles.trafficpredictor.model.TimeArg;


/**
 * Created by Ryan Miles on 10/19/2016.
 */

public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        //Speed
        //TimeArg timeArgSat = new TimeArg(loadJSONFromAsset("1-1-2015--12-31-2015TimeAggSat.json"));
        //DayArg dayArg = new DayArg(loadJSONFromAsset("10-1-2015--10-31-2015DayAgg.json"));
        //TimeArg actualTimeArg = new TimeArg(loadJSONFromAsset("10-1-2016Actual.json"));

        //Flow
        TimeArg timeArgSat = new TimeArg(loadJSONFromAsset("Flow_1-1-2015--12-31-2015TimeAggSat.json"));
        TimeArg timeArgMonth = new TimeArg(loadJSONFromAsset("Flow_10-1-2015--10-31-2015TimeAgg.json"));
        TimeArg actualTimeArg = new TimeArg(loadJSONFromAsset("Flow_10-1-2016Actual.json"));

        ArrayList<LearnExtra> allTestsEachHour = new ArrayList<>();
        ArrayList<LearnExtra> bestOfEachHour = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String time = String.valueOf(i) + ":00";
            double calcspeed = 0;
            allTestsEachHour.clear();
            for (double a = 0; a <= 1; a += .001) {
                //A = dayofweekdayarg mult
                calcspeed = timeArgSat.getTime(time) * a;

                //B = AggTimeofMonth mult
                double b = (1 - a);
                calcspeed += timeArgMonth.getTime(time) * b;

                double diff = Math.abs(calcspeed - actualTimeArg.getTime(time));

                allTestsEachHour.add(new LearnExtra(a,b,diff));
              //  Log.d(TAG,"Added new hour");
            }
            double lowestdiff = 9999999;
            for (LearnExtra test : allTestsEachHour) {
                if(test.getDiff() < lowestdiff){
                    lowestdiff = test.getDiff();
                }
            }

            for (LearnExtra test : allTestsEachHour) {
                if(test.getDiff() == lowestdiff){
                    bestOfEachHour.add(test);
                    break;
                }
            }
         //   Log.d(TAG,"Added new best for the hour");
        }
       // Log.d(TAG, bestOfEachHour.toString());
        double combA = 0;
        double combB = 0;
        for (LearnExtra extra : bestOfEachHour) {
            combA += extra.getA();
            combB += extra.getB();
        }
        LearnExtra Final = new LearnExtra(combA/24,combB/24,-1);
        Log.wtf("BEST FINAL", Final.toString());
        Toast.makeText(this,Final.toString(),Toast.LENGTH_LONG).show();
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
