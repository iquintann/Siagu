package es.unex.giiis.asee.siagu;

import android.util.Log;

import java.util.List;

import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.SearchCity;

public class Util {

    //Parseo de searchCity a City utilziar lo menos posible
    public static void searchCity2City(List<SearchCity> searchCities, List<City> cityList){
        for (SearchCity src: searchCities){
            String string = src.getName();
            String[] frac = string.split(",");
            String nameCity= frac[0];

            City dst=new City(nameCity,src.getRegion(),src.getCountry(),src.getLat(),src.getLon());
            cityList.add(dst);
            }
        Log.d("Converter",cityList.toString());
    }



}

