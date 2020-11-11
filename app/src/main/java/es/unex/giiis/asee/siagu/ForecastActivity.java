package es.unex.giiis.asee.siagu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.api_runable.ReposNetworkForescast;
import es.unex.giiis.asee.siagu.api_runable.ReposNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.Forecast;
import es.unex.giiis.asee.siagu.model.Forecastday;
import es.unex.giiis.asee.siagu.ui.cityList.CityListViewModel;
import es.unex.giiis.asee.siagu.ui.searchcity.CityAdapter;

public class ForecastActivity extends AppCompatActivity {

    private ForecastListViewModel forecastListViewModel;
    private City mCity;
    private Context mContext;
    private List<Forecastday> mForecastList;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ForecastdayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast);

        //Recicler view in ativity forecast
        mRecyclerView = (RecyclerView) findViewById(R.id.forecastRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Crear adaptador
        mAdapter= new ForecastdayAdapter(new ForecastdayAdapter.OnCityClickListener() {
            @Override
            public void onItemClick(Forecastday item) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        //

        //Nombre de la ciudad
        Intent intent = getIntent();
        String cityName=intent.getStringExtra("Coord");
        //Numero de dias, de momento fijo
        int dias=7;

        //LLamamiento a la API
        //Lo maximo que devuelve la API es 3 dias consecutivo contado desde hoy
        AppExecutors.getInstance().networkIO().execute(new ReposNetworkForescast((new OnReposLoadedListener() {
            @Override
            public void onReposLoaded(List<City> cityList) {
                Log.d("Forecast","Cargado");
                //Trocear
                mCity=cityList.get(0);
                List<Forecastday> forecastdayList = mCity.getForecast().getForecastday();
                mForecastList=forecastdayList;

                //Cargar en el recycler view
               runOnUiThread(() -> {
                   mAdapter.clear();
                   for (Forecastday c : mForecastList) {
                       Log.d("ForecastList",c.getDate());
                       mAdapter.add(c);
                   }
               });


            }
        }),this,cityName,3));

        }


}