package es.unex.giiis.asee.siagu.model.api_runable;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.Vista.MainActivity;
import es.unex.giiis.asee.siagu.model.City;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityNetworkLoaderRunnable implements Runnable {

    private final OnReposLoadedListener mOnCitiesLoadedListener;
    private Context mContext;
    private String mCity;

    public CityNetworkLoaderRunnable(OnReposLoadedListener onReposLoadedListener, Context context, String city){
        mOnCitiesLoadedListener = onReposLoadedListener;
        mContext=context;
        mCity=city;
    }

    @Override
    public void run() {
        // Instanciación de Retrofit y llamada síncrona
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("Retrofit",retrofit.toString());

        ApiService service = retrofit.create(ApiService.class);
        //GitHubService service = retrofit.create(GitHubService.class);
        try {
            String key= MainActivity.KEY_API;
            String city=mCity;
            String lang="es";
            Log.d("Retrofit","Incio servicio");
            City cityRet = service.listCity(key,city,lang).execute().body();
            Log.d("Retrofit",cityRet.toString());
            List<City> repos = new ArrayList<>();
            repos.add(cityRet);

            //Para hace rla llamada desde el hilo principal, uso de funcion lambda
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    //Cuidado con reposFinal??!
                    mOnCitiesLoadedListener.onReposLoaded(repos);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Llamada al Listener con los datos obtenidos
    }
}
