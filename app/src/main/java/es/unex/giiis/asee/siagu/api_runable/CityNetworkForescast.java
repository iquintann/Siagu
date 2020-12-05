package es.unex.giiis.asee.siagu.api_runable;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.MainActivity;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.Forecastday;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityNetworkForescast implements Runnable {

    private final OnReposLoadedListener mOnReposLoadedListener;
    private Context mContext;
    private String mCity;
    private int daysForecast;

    public CityNetworkForescast(OnReposLoadedListener mOnReposLoadedListener, Context context, String city, int days) {
        this.mOnReposLoadedListener = mOnReposLoadedListener;
        mContext = context;
        mCity = city;
        daysForecast = days;
    }

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        String key = MainActivity.KEY_API;
        String lang = "es";

        String days = Integer.toString(daysForecast);
        try {
            Log.d("get", retrofit.baseUrl().url() + " / " + service.forecastCity(key, mCity, lang, days).execute().raw().request().url().toString());
            City ciudadObtenida = service.forecastCity(key, mCity, lang, days).execute().body();

            Log.d("ForecastCity", "Obtencion:------------\n" + ciudadObtenida.toString() + ciudadObtenida.getForecast().toString());

            List<Forecastday> listForecast = ciudadObtenida.getForecast().getForecastday();
            for (Forecastday f : listForecast) {
                Log.d("Forecast", "Fecha: " + f.getDate() + "\nPrevision: " + f.getDay().getCondition().getText() + "\n");
            }
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    //Poco churro
                    List<City> retorno=new ArrayList<>();
                    retorno.add(ciudadObtenida);
                    //Cuidado con reposFinal??!
                    mOnReposLoadedListener.onReposLoaded(retorno);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}