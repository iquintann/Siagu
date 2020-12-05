package es.unex.giiis.asee.siagu.model;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import es.unex.giiis.asee.siagu.roomDB.CurrentConverter;
import es.unex.giiis.asee.siagu.roomDB.LocationConverter;

@Entity(tableName = "city")
public class City implements Serializable {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("location")
    @Expose
    //@ColumnInfo(name="location")
    @TypeConverters(LocationConverter.class)
    private Location location;
    @SerializedName("current")
    @Expose
    @TypeConverters(CurrentConverter.class)
    private Current current;

    @SerializedName("forecast")
    @Expose
    @Ignore
    private Forecast forecast;

    @SerializedName("principal")
    @Expose
    private boolean principal;




    //para el conversor util.searchCity2City
    @Ignore
    public City(String name, String region, String country, Double lat, Double lon) {
        location=new Location();
        location.setName(name);
        location.setRegion(region);
        location.setCountry(country);
        location.setLat(lat);
        location.setLon(lon);
        current=new Current();
        principal = false;
    }

    public City(long id){
        this.id=id;
        location = new Location();
        current = new Current();
        principal = false;
    }

    @Ignore
    public City() {

    }

    public long getId(){ return this.id; }

    public void setId(long id){ this.id = id; }

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

    public Forecast getForecast() {
        return forecast;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public City setForecast(Forecast forecast) {
        this.forecast = forecast;
        return this;
    }


    @Ignore
    @Override
    public String toString() {
        String toReturn="City{" +
                "name=" + location.getName() +
                ", region=" + location.getRegion() +
                ", country=" + location.getCountry() +
                ", coord={" + location.getLat()+" "+ location.getLon() + "}";
        toReturn=toReturn+"}";
        return toReturn;
                    }


    public boolean equalCity(City enter) {
        String thisCity = this.getLocation().getName();
        String paramCity = enter.getLocation().getName();
        Log.d("equalCity",thisCity+" -- "+paramCity);
        /*if(thisCity.equals(paramCity))
            return true;*/
        if(location.getName().equals(enter.getLocation().getName())&&
                location.getCountry().equals(enter.getLocation().getCountry())&&
                location.getRegion().equals(enter.getLocation().getRegion()))
            return true;
        return false;
    }



}
