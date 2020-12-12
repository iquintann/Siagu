package es.unex.giiis.asee.siagu.forecast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.Repository.CityRepository;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkForescast;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.Forecastday;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;

public class ForecastActivity extends AppCompatActivity implements OnReposLoadedListener{

    private ForecastListViewModel forecastListViewModel;
    private City mCity;
    private Context mContext;
    private List<Forecastday> mForecastList;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ForecastdayAdapter mAdapter;

    //Numero de dias, de momento fijo
    private int dias = 3;

    //Repository
    private CityRepository mRepository;

    private String coordInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast);

        //Recicler view in ativity forecast
        mRecyclerView = (RecyclerView) findViewById(R.id.forecastRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Crear adaptador
        mAdapter = new ForecastdayAdapter(new ForecastdayAdapter.OnCityClickListener() {
            @Override
            public void onItemClick(Forecastday item) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        //Nombre de la ciudad
        Intent intent = getIntent();
        coordInt = intent.getStringExtra("Coord");
        Log.d("Forecast","Coordenadas a buscar: "+coordInt);

        //-------------------
        //Obtenemos instancia del repositorio
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(this).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) this, this::onReposLoaded);
        mRepository.setBusqueda(true);
        mRepository.busquedaForecast(coordInt,this,3);
        //----------------------




        //LLamamiento a la API
        //Lo maximo que devuelve la API es 3 dias consecutivo contado desde hoy
        /*AppExecutors.getInstance().networkIO().execute(new CityNetworkForescast((new OnReposLoadedListener() {
            @Override
            public void onReposLoaded(List<City> cityList) {
                Log.d("Forecast", "Cargado");
                //Trocear
                mCity = cityList.get(0);
                List<Forecastday> forecastdayList = mCity.getForecast().getForecastday();
                mForecastList = forecastdayList;

                //Cargar en el recycler view
                runOnUiThread(() -> {
                    mAdapter.clear();
                    for (Forecastday c : mForecastList) {
                        Log.d("ForecastList", c.getDate());
                        mAdapter.add(c);
                    }
                });


            }
        }), this, cityName, 3));*/

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Previsión de los próximos " + dias + " días");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onReposLoaded(List<City> cityList) {
        for(City c:cityList){
            String cCoor=c.getLocation().getLat()+","+c.getLocation().getLon();
            Log.d("Forecast","Coordenadas ciudad "+c.getLocation().getName()+" "+cCoor);
            if(cCoor.equals(coordInt)){
                mCity=c;
            }
        }

        Log.d("onReposLoaded","Ciudad fore "+mCity.toString());
        try {
            List<Forecastday> forecastdayList = mCity.getForecast().getForecastday();
            mForecastList = forecastdayList;
            mAdapter.clear();
            for (Forecastday c : mForecastList) {
                Log.d("ForecastList", c.getDate());
                mAdapter.add(c);
            }
        }catch (Exception e){

        }

    }
}