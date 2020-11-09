package es.unex.giiis.asee.siagu.api_runable;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.MainActivity;
import es.unex.giiis.asee.siagu.model.City;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposNetworkLoaderRunnable implements Runnable {

    private final OnReposLoadedListener mOnReposLoadedListener;
    private Context mContext;
    private String mCity;

    public ReposNetworkLoaderRunnable(OnReposLoadedListener onReposLoadedListener,Context context,String city){
        mOnReposLoadedListener = onReposLoadedListener;
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
            //String sentence="current.json\\?key=f9b6c8f8d9d6478894a122855200110&q=Caceres&lang=es";
            //String sentence="key=f9b6c8f8d9d6478894a122855200110&q=Caceres&lang=es";
            String key= MainActivity.KEY_API;
            //Carga el valor de las preferences
            //String city="Caceres";
            //sharedPref.getString(Setting_Siagu.USERCITY,"Ciudad");
            String city=mCity;
            //SharedPreferences sharedPref = mContext.getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);

            //Log.d("Load city home","City: "+city);
            String lang="es";
            //String sentence="current.json";
            Log.d("Retrofit","Incio servicio");
            //Log.d("get", retrofit.baseUrl().url()+" / "+service.listCity(key,city,lang).execute().raw().request().url().toString());
            City cityRet = service.listCity(key,city,lang).execute().body();
            Log.d("Retrofit",cityRet.toString());
            List<City> repos = new ArrayList<>();
            repos.add(cityRet);

            //Para hace rla llamada desde el hilo principal, uso de funcion lambda
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    //Cuidado con reposFinal??!
                    mOnReposLoadedListener.onReposLoaded(repos);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Llamada al Listener con los datos obtenidos
    }
}
