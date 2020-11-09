package es.unex.giiis.asee.siagu.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.unex.giiis.asee.siagu.roomDB.CurrentConverter;
import es.unex.giiis.asee.siagu.roomDB.LocationConverter;

@Entity(tableName = "city")
public class City {

    @PrimaryKey(autoGenerate = true)
    private long id;
    //TODO QUITAR IGNORE
    @SerializedName("location")
    @Expose
    //@ColumnInfo(name="location")
    @TypeConverters(LocationConverter.class)
    private Location location;
    @SerializedName("current")
    @Expose
    @TypeConverters(CurrentConverter.class)
    private Current current;

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
    }

    public City(long id){
        location = new Location();
        current = new Current();
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

    @Ignore
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
