package me.ryanmiles.trafficpredictor.model;

/**
 * Created by Ryan Miles on 10/31/2016.
 */

public class Station {
    private String fwy;
    private int district;
    private String county;
    private String city;
    private String CA_PM;
    private double Abs_PM;
    private double length;
    private int ID;
    private String name;
    private int lanes;
    private String type;
    private String sensorType;
    private boolean HOV;
    private String MS_ID;

    public Station() {
        fwy = "";
        district = 0;
        county = "";
        city = "";
        CA_PM = "";
        Abs_PM = 0;
        length = 0;
        ID = 0;
        name = "";
        lanes = 0;
        type = "";
        sensorType = "";
        HOV = false;
        MS_ID = "";
    }

    public String getFwy() {
        return fwy;
    }

    public void setFwy(String fwy) {
        this.fwy = fwy;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCA_PM() {
        return CA_PM;
    }

    public void setCA_PM(String CA_PM) {
        this.CA_PM = CA_PM;
    }

    public double getAbs_PM() {
        return Abs_PM;
    }

    public void setAbs_PM(double abs_PM) {
        Abs_PM = abs_PM;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLanes() {
        return lanes;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public boolean isHOV() {
        return HOV;
    }

    public void setHOV(boolean HOV) {
        this.HOV = HOV;
    }

    public void setHOV(String HOV) {
        if(HOV.equals("No")){
            this.HOV = false;
        }else{
            this.HOV = true;
        }
    }

    public String getMS_ID() {
        return MS_ID;
    }

    public void setMS_ID(String MS_ID) {
        this.MS_ID = MS_ID;
    }

    @Override
    public String toString() {
        return "Station{" +
                "fwy='" + fwy + '\'' +
                ", district='" + district + '\'' +
                ", county='" + county + '\'' +
                ", CA_PM='" + CA_PM + '\'' +
                ", Abs_PM='" + Abs_PM + '\'' +
                ", length=" + length +
                ", ID=" + ID +
                ", name='" + name + '\'' +
                ", lanes=" + lanes +
                ", type='" + type + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", HOV=" + HOV +
                ", MS_ID=" + MS_ID +
                '}';
    }
}