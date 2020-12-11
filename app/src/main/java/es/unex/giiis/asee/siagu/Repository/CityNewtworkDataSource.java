package es.unex.giiis.asee.siagu.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkSearchCityRunnable;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.model.City;

public class CityNewtworkDataSource {
    private static final String LOG_TAG = CityNewtworkDataSource.class.getSimpleName();
    private static CityNewtworkDataSource sInstance;

    private final MutableLiveData<List<City>> mDownloadedCities;

    private CityNewtworkDataSource() {
        mDownloadedCities = new MutableLiveData<>();
    }

    public synchronized static CityNewtworkDataSource getInstance() {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            sInstance = new CityNewtworkDataSource();
            Log.d(LOG_TAG, "Made new network data source");
        }
        return sInstance;
    }

    public LiveData <List<City>> getCurrentRepos() {
        return mDownloadedCities;
    }

    /**
     * Obtiene de la API la ciudad mas nueva
     */
    public void fetchRepos(String cityName) {
        Log.d("Cityrepository", "Obteniendo elemento de la API con nombre "+cityName);
        // Get gata from network and pass it to LiveData
        AppExecutors.getInstance().networkIO().execute(new CityNetworkLoaderRunnable(
                cityList -> {
                    Log.d("Cityrepository",""+cityList.get(0));
                    mDownloadedCities.postValue(cityList);
                }, null, cityName));
    }

    /**
     * Obtiene de la API las ciudades
     *
     */
    public void fetchCitySearch(String cityName) {
        Log.d("Cityrepository", "Obteniendo elemento de la API con nombre "+cityName);
        // Get gata from network and pass it to LiveData
        AppExecutors.getInstance().networkIO().execute(new CityNetworkSearchCityRunnable(
                cityList -> {
                    Log.d("CityrepositorySearch",""+cityList.toString());
                    mDownloadedCities.postValue(cityList);
                }, cityName));
    }

}