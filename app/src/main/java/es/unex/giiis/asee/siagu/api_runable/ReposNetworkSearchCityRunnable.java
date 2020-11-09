package es.unex.giiis.asee.siagu.api_runable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.MainActivity;
import es.unex.giiis.asee.siagu.Util;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.SearchCity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposNetworkSearchCityRunnable implements Runnable {

    private final OnReposLoadedListener mOnReposLoadedListener;
    private final String mTextSearch;

    public ReposNetworkSearchCityRunnable(OnReposLoadedListener onReposLoadedListener, String textSearch){
        mOnReposLoadedListener=onReposLoadedListener;
        mTextSearch=textSearch;
    }

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        String key= MainActivity.KEY_API;
        String textSearch=mTextSearch;
        //TODO necesario?
        String lang="es";

        try {
            List<SearchCity> listSearch = service.searchCity(key,textSearch).execute().body();
            List<City> cityList=new ArrayList<>();
            //Converter, para que funcione la interfaz, ya que la API nos devuelve información de otra manera al realizar el llamamiento
            Util.searchCity2City(listSearch,cityList);

            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    mOnReposLoadedListener.onReposLoaded(cityList);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
