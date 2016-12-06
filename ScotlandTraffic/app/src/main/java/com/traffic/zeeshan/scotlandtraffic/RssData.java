package com.traffic.zeeshan.scotlandtraffic;


public class RssData {
    public enum RssDataType{
        INCIDENTS,
        ROADWORKS,
        FUTURE_ROADWORKS
    }

    public int id;
    public RssDataType type;
    public String title;
    public String description;
    public String date;
    public String link;
    public float[] geo;

    public RssData(String title, RssDataType type)
    {
        this.title = title;
        this.type = type;
    }
}
