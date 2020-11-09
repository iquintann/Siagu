package es.unex.giiis.asee.siagu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class City {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private Current current;

    //para el conversor util.searchCity2City
    public City(String name, String region, String country, Double lat, Double lon) {
        location=new Location();
        location.setName(name);
        location.setRegion(region);
        location.setCountry(country);
        location.setLat(lat);
        location.setLon(lon);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "City{" +
                "name=" + location.getName() +
                ", region=" + location.getRegion() +
                ", country=" + location.getCountry() +
                ", coord={" + location.getLat()+" "+ location.getLon() + "}" +
                '}';
    }
}
