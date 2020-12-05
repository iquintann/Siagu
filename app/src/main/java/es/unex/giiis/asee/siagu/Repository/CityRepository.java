package es.unex.giiis.asee.siagu.Repository;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.giiis.asee.siagu.api_runable.ApiService;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkForescast;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityItemDao;

import static es.unex.giiis.asee.siagu.Util.searchCity2City;


/**
 * Handles data operations in Sunshine. Acts as a mediator between {@link CityNetworkForescast}
 * and {@link ApiService}
 */
public class CityRepository {
    private static final String LOG_TAG = CityRepository.class.getSimpleName();

    // For Singleton instantiation
    private static CityRepository sInstance;
    private final CityItemDao cityDAO;
    private final CityNewtworkDataSource cityNewtworkDataSource;
    private final AppExecutors appExecutors = AppExecutors.getInstance();


    //Searccity
    private boolean busqueda = false;

    //Es el nombre que al cambiarlo se desencadena
    //private final MutableLiveData<String> userFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> cityFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> actualizateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> citySearchFilterLiveData = new MutableLiveData<>();

    private final Map<String, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 60000;

    /**
     * Constructor para HomeFragment y CityListFragment
     * @param repoDao
     * @param repoNetworkDataSource
     */
    private CityRepository(CityItemDao repoDao, CityNewtworkDataSource repoNetworkDataSource) {
        cityDAO = repoDao;
        cityNewtworkDataSource = repoNetworkDataSource;
        // LiveData that fetches repos from network
        LiveData <List <City>> networkData = cityNewtworkDataSource.getCurrentRepos();

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        networkData.observeForever(new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OnChange","executing");
                        if(cityList.size()>0) {
                            List<City> daoList = cityDAO.getAll();

                            Log.d("OnChange", "actualizando ciudad");
                            City cityContain = ciudadContenidaEnLista(daoList, cityList.get(0));
                            if (cityContain == null) {
                                Log.d("OnChange", "ciudad no encontrada insertando");
                                //Significa que este elemento no existe en el DAO y lo implmentamos
                                City newCi = cityList.get(0);
                                newCi.setPrincipal(true);
                                cityDAO.insert(cityList.get(0));

                                Log.d("OnChange", "ciudad no encontrada insertada");
                                City actuPrincipal = obtenerPrincipal(daoList);

                                if(actuPrincipal!=null){
                                    actuPrincipal.setPrincipal(false);
                                    cityDAO.update(actuPrincipal);
                                }
                            } else {
                                City actuPrincipal = obtenerPrincipal(daoList);

                                if(actuPrincipal!=null){
                                    actuPrincipal.setPrincipal(false);
                                    cityDAO.update(actuPrincipal);
                                }

                                Log.d("OnChange", "Ciudad ant p:" + actuPrincipal);
                                cityContain.setPrincipal(true);
                                Log.d("OnChange", "Ciudad ahora p:" + cityContain);

                                cityDAO.update(cityContain);

                            }
                        }
                    }
                });
            }

        });
    }

    private City obtenerPrincipal(List<City> daoList) {
        for(City c:daoList){
            if(c.isPrincipal()){
                Log.d("ciudadContenidaEnLista","Ciudad contenida");
                return c;
            }
        }
        Log.d("ciudadContenidaEnLista","Ciudad NO contenida");
        return null;
    }


    private City ciudadContenidaEnLista (List<City> cityList, City city){
        boolean contenida=false;
        for(City c:cityList){
            if(c.equalCity(city)){
                Log.d("ciudadContenidaEnLista","Ciudad contenida");
                return c;
            }

        }
        Log.d("ciudadContenidaEnLista","Ciudad NO contenida");
        return null;
    };

    public synchronized static CityRepository getInstance(CityItemDao dao, CityNewtworkDataSource nds) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new CityRepository(dao, nds);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    //Cambio el nombre de la ciudad
    public void setCityName(String cityName,City prefCity) {
        Log.d("Cityrepository","Cambio el nombre");
        cityFilterLiveData.setValue(cityName);
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (isFetchNeeded(cityName,prefCity)) {
                Log.d("Cityrepository","Hago el cambio");
                doFetchRepos(cityName);
            }
        });
    }

    //Cambio el nombre de la busqueda
    public void setCitySearch(String citySearchName) {
        busqueda = true;
        Log.d("Cityrepository","Cambio el nombre");
        citySearchFilterLiveData.setValue(citySearchName);
        AppExecutors.getInstance().diskIO().execute(() -> {
            doFetchSearchCity(citySearchName);
            /*if (isFetchSearchNeeded(citySearchName)) {
                Log.d("Cityrepository","Hago el cambio");
                doFetchSearchCity(citySearchName);
            }*/
        });
        busqueda = false;
    }

    public void setActualizateLiveData(String actualizate){
        Log.d("Cityrepository","Cambio la actualizacion");
        actualizateLiveData.setValue(actualizate);

    }

    public void doFetchRepos(String cityName){
        Log.d("Cityrepository", "--");
        AppExecutors.getInstance().diskIO().execute(() -> {
            //apiService.deleteReposByUser(username);
            cityNewtworkDataSource.fetchRepos(cityName);
            lastUpdateTimeMillisMap.put(cityName, System.currentTimeMillis());
        });
    }

    public void doFetchSearchCity(String citySearch){
        Log.d("Cityrepository", "Buscando ciudades en aAPI");
        AppExecutors.getInstance().diskIO().execute(() -> {
            //apiService.deleteReposByUser(username);
            cityNewtworkDataSource.fetchCitySearch(citySearch);
            });
    }

    /**
     * Database related operations
     **/

    public LiveData<List<City>> getCurrentRepos() {
        // - Return LiveData from Room. Use Transformation to get owner
        //para cambiar el nombre lo coge del mutable y cuando este cambie se activará
        return Transformations.switchMap(cityFilterLiveData, new Function<String, LiveData<List<City>>>() {
            @Override
            public LiveData<List<City>> apply(String input) {
                //el input hace referencia al userFilterLiveData
                return cityDAO.getAllLiveData();
            }
        });

    }

    /**
     * Se comprueba si es necesario su actualización / busqueda en la API
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded(String cityName, City prefCity) {
        //Busco la ciudad en la BD
        //Si no hay ninguna actualizo
        if(prefCity == null)
            return true;
        boolean estaEnBD = contenidaEnLista(cityDAO.getAll(),cityName);


        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get(cityName);
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        //Calculo del tiempo desde cuando no se actualiza
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        // - Implement cache policy: When time has passed or no repos in cache
        return !estaEnBD || timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;

    }

    /**
     * Comprobamos si es necesario actualizar
     * @param cityName
     * @return
     */
    private boolean isFetchSearchNeeded(String cityName){
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get(cityName);
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        //Calculo del tiempo desde cuando no se actualiza
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;
    }

    //TODO PROVISIONAL llevarmelo a uti o a otro lado
    private boolean contenidaEnLista(List<City> all, String cityName) {
        for(City c: all){
            if(c.getLocation().getName().equals(cityName))
                return true;
        }
        return false;
    }


}