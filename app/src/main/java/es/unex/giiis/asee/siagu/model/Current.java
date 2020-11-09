
package es.unex.giiis.asee.siagu.model;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Current {

    @SerializedName("last_updated_epoch")
    @Expose
    private Integer lastUpdatedEpoch=0;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated=" ";
    @SerializedName("temp_c")
    @Expose
    private Double tempC=0.0;
    @SerializedName("temp_f")
    @Expose
    private Double tempF=0.0;
    @SerializedName("is_day")
    @Expose
    private Integer isDay=0;
    @SerializedName("condition")
    @Expose
    private Condition condition;
    @SerializedName("wind_mph")
    @Expose
    private Double windMph=0.0;
    @SerializedName("wind_kph")
    @Expose
    private Double windKph=0.0;
    @SerializedName("wind_degree")
    @Expose
    private Integer windDegree=0;
    @SerializedName("wind_dir")
    @Expose
    private String windDir=" ";
    @SerializedName("pressure_mb")
    @Expose
    private Double pressureMb=0.0;
    @SerializedName("pressure_in")
    @Expose
    private Double pressureIn=0.0;
    @SerializedName("precip_mm")
    @Expose
    private Double precipMm=0.0;
    @SerializedName("precip_in")
    @Expose
    private Double precipIn=0.0;
    @SerializedName("humidity")
    @Expose
    private Integer humidity=0;
    @SerializedName("cloud")
    @Expose
    private Integer cloud=0;
    @SerializedName("feelslike_c")
    @Expose
    private Double feelslikeC=0.0;
    @SerializedName("feelslike_f")
    @Expose
    private Double feelslikeF=0.0;
    @SerializedName("vis_km")
    @Expose
    private Double visKm=0.0;
    @SerializedName("vis_miles")
    @Expose
    private Double visMiles=0.0;
    @SerializedName("uv")
    @Expose
    private Double uv=0.0;
    @SerializedName("gust_mph")
    @Expose
    private Double gustMph=0.0;
    @SerializedName("gust_kph")
    @Expose
    private Double gustKph=0.0;

    public Integer getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    public void setLastUpdatedEpoch(Integer lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getTempC() {
        return tempC;
    }

    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    public Double getTempF() {
        return tempF;
    }

    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    public Integer getIsDay() {
        return isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Double getWindMph() {
        return windMph;
    }

    public void setWindMph(Double windMph) {
        this.windMph = windMph;
    }

    public Double getWindKph() {
        return windKph;
    }

    public void setWindKph(Double windKph) {
        this.windKph = windKph;
    }

    public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public Double getPressureMb() {
        return pressureMb;
    }

    public void setPressureMb(Double pressureMb) {
        this.pressureMb = pressureMb;
    }

    public Double getPressureIn() {
        return pressureIn;
    }

    public void setPressureIn(Double pressureIn) {
        this.pressureIn = pressureIn;
    }

    public Double getPrecipMm() {
        return precipMm;
    }

    public void setPrecipMm(Double precipMm) {
        this.precipMm = precipMm;
    }

    public Double getPrecipIn() {
        return precipIn;
    }

    public void setPrecipIn(Double precipIn) {
        this.precipIn = precipIn;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getCloud() {
        return cloud;
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public Double getFeelslikeC() {
        return feelslikeC;
    }

    public void setFeelslikeC(Double feelslikeC) {
        this.feelslikeC = feelslikeC;
    }

    public Double getFeelslikeF() {
        return feelslikeF;
    }

    public void setFeelslikeF(Double feelslikeF) {
        this.feelslikeF = feelslikeF;
    }

    public Double getVisKm() {
        return visKm;
    }

    public void setVisKm(Double visKm) {
        this.visKm = visKm;
    }

    public Double getVisMiles() {
        return visMiles;
    }

    public void setVisMiles(Double visMiles) {
        this.visMiles = visMiles;
    }

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    public Double getGustMph() {
        return gustMph;
    }

    public void setGustMph(Double gustMph) {
        this.gustMph = gustMph;
    }

    public Double getGustKph() {
        return gustKph;
    }

    public void setGustKph(Double gustKph) {
        this.gustKph = gustKph;
    }


}
