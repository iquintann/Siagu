
package es.unex.giiis.asee.siagu.model;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Location {

    @SerializedName("name")
    @Expose
    private String name=" ";
    @SerializedName("region")
    @Expose
    private String region=" ";
    @SerializedName("country")
    @Expose
    private String country=" ";
    @SerializedName("lat")
    @Expose
    private Double lat=0.0;
    @SerializedName("lon")
    @Expose
    private Double lon=0.0;
    @SerializedName("tz_id")
    @Expose
    private String tzId="tzId";
    @SerializedName("localtime_epoch")
    @Expose
    private Integer localtimeEpoch=0;
    @SerializedName("localtime")
    @Expose
    private String localtime="localtime";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTzId() {
        return tzId;
    }

    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    public Integer getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    public void setLocaltimeEpoch(Integer localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

}
