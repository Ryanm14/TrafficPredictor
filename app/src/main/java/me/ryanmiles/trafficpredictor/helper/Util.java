package me.ryanmiles.trafficpredictor.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Miles on 11/21/2016.
 */

public class Util {

    public static String getMonth(int i) {
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
        return null;
    }

    public static String getDay(int j) {
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


    public static String makeURL(String sourcelat, String sourcelog, String destlat, String destlog) {
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

    public static List getMonths() {
        List<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return months;
    }

    public static List getDotw() {
        List<String> doftw = new ArrayList<>();
        doftw.add("Sunday");
        doftw.add("Monday");
        doftw.add("Tuesday");
        doftw.add("Wednesday");
        doftw.add("Thursday");
        doftw.add("Friday");
        doftw.add("Saturday");
        return doftw;
    }

    public static List getTimes() {
        List<String> times = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            times.add(i + ":00 am");
        }
        for (int i = 1; i <= 12; i++) {
            times.add(i + ":00 pm");
        }
        return times;
    }

    public static String getColor(double dataDelayMax) {
        if (dataDelayMax <= 3) {
            return "#98fb98";
        } else if (dataDelayMax <= 20) {
            return "#ffff00";
        } else {
            return "#ff0000";
        }
    }
}
