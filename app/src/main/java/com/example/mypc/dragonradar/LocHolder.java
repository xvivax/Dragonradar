package com.example.mypc.dragonradar;

public class LocHolder
{
    private double lat;
    private double lng;

    public LocHolder(double lat, double lng)
    {
        this.lat = lat;
        this.lng = lng;
    }

    public double GetLat()
    {
        return lat;
    }

    public double GetLng()
    {
        return lng;
    }
}
