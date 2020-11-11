package es.unex.giiis.asee.siagu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("forecastday")
    @Expose
    private List<Forecastday> forecastday = null;

    public List<Forecastday> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<Forecastday> forecastday) {
        this.forecastday = forecastday;
   }

    @Override
    public String toString() {
        String string="";
        for(Forecastday c:forecastday){
            string=","+string;
        }
        return string;
    }
}
