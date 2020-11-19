package es.unex.giiis.asee.siagu;

import android.util.Log;

import java.util.List;

import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.SearchCity;

public class Util {

    //Parseo de searchCity a City utilziar lo menos posible
    public static void searchCity2City(List<SearchCity> searchCities, List<City> cityList) {
        if (searchCities != null)
            for (SearchCity src : searchCities) {
                String string = src.getName();
                String[] frac = string.split(",");
                String nameCity = frac[0];

                City dst = new City(nameCity, src.getRegion(), src.getCountry(), src.getLat(), src.getLon());
                cityList.add(dst);
            }
        Log.d("Converter", cityList.toString());
    }


    public static int imageTiempo(String tiempo) {
        int source = 0;
        switch (tiempo) {
            case "Niebla helada":
            case "Neblina":
                source = R.drawable.icw_fog;
                break;
            case "Soleado":
                source = R.drawable.icw_sunny;
                break;
            case "Despejado":
                source = R.drawable.icw_sunny_clear;
                break;
            case "Ligeras precipitaciones":
                source = R.drawable.icw_rainny_low;
                break;
            case "Lluvia moderada":
            case "Lluvia  moderada a intervalos":
                source = R.drawable.icw_rainny;
                break;
            case "Fuertes lluvias":
            case "Lluvias fuertes o moderadas":
                source = R.drawable.icw_rainny_hard;
                break;
            case "Nublado":
            case "Parcialmente nublado":
                source = R.drawable.icw_cloud;
                break;
            case "Cielo cubierto":
                source = R.drawable.icw_cloud_covered;
                break;
            case "Fuertes nevadas":
                source = R.drawable.icw_snow;
                break;

            case "Ventisca":
                source = R.drawable.icw_wind;
                break;

            case "Ligeras lluvias heladas":
            case "Ligeras precipitaciones de nieve":
                source = R.drawable.icw_rain_swnow;
                break;
            case "Nevadas ligeras":
            case "Nevadas ligeras a intervalos":
            case " Nevadas ligeras":
                source = R.drawable.icw_snow_low;
                break;
            case "Nieve moderada":
            case "Nieve moderada a intervalos":
                source = R.drawable.icw_snow_medium;
                break;

            case "Chubascos de nieve fuertes o moderados":
            case "Chubascos de nieve":
                source = R.drawable.icw_rain_swnow;
                break;

            default:
                source = R.drawable.ic_lens;
                Log.d("TIEMPO", "Incluir: " + tiempo);
        }
        return source;
    }


}