package es.unex.giiis.asee.siagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.api_runable.ReposNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;
import es.unex.giiis.asee.siagu.ui.home.HomeFragment;

public class City_Detail extends AppCompatActivity {

    private City cityToShow;
    private boolean mContenido = false;
    private CityDataBase mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        String coorLat=null;
        String coorLong=null;

        try {
             coorLat = getIntent().getSerializableExtra("Lat").toString();
             coorLong = getIntent().getSerializableExtra("Lon").toString();
        }
        catch (Exception e){

        }

        if(coorLat==null||coorLong==null) {
            long idCity = (long) getIntent().getSerializableExtra("Id");
            //Obtenemos del dao
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mContenido=true;
                    mDataBase = CityDataBase.getInstance(getBaseContext());
                    List <City> list=mDataBase.getDao().getAll();
                    cityToShow= obtenerElementoPorId(list,idCity);
                    Log.d("cityToShow","City with iD: "+cityToShow.toString());
                    runOnUiThread(() -> SetCityData(cityToShow));

                    botonFavAddDelete();

                }
            });

        }else {
            String coorCity = coorLat + "," + coorLong;
            Log.d("CityDetail", "Coordinates: " + coorLat + " " + coorLong);
            procesadoPorBusqueda(coorCity);
         }

        forecastButtonConfig();
    }

    private City obtenerElementoPorId(List<City> list, long idCity) {
        for(City elm:list){
            if(elm.getId()==idCity){
                return elm;
            }
        }
        return null;
    }

    private void procesadoPorBusqueda(String coorCity) {

        //Carga la ciudad que estamos buscando
        AppExecutors.getInstance().networkIO().execute(new ReposNetworkLoaderRunnable((new OnReposLoadedListener() {
            @Override
            public void onReposLoaded(List<City> cityList) {
                cityToShow = cityList.get(0);
                Log.d("CityDetail", "Ciudad cargada: " + cityToShow.toString());
                SetCityData(cityToShow);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDataBase = CityDataBase.getInstance(getBaseContext());
                        //
                        //mDataBase.getDao().deleteAll();

                        List<City> cityList = mDataBase.getDao().getAll();

                        //Esta contenido la ciudad que se ha metido en nuestro DAO?
                        mContenido = dentroDeLista(cityList, cityToShow);
                        Log.d("CityDetail", mContenido + " en DAO");

                        botonFavAddDelete();





                    }
                });
            }
        }), this, coorCity));
    }

    private void botonFavAddDelete() {
        Button butFav = findViewById(R.id.favIco);

        runOnUiThread(() -> {
            if (mContenido) butFav.setText(R.string.buttonEliminar);
            else butFav.setText("Añadir a la lista");
        });

        butFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContenido) {
                    //boton rojo

                    Log.d("CityDetail", "Borrando de DAO");
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDataBase = CityDataBase.getInstance(getBaseContext());
                            mDataBase.getDao().deleteById(cityToShow.getId());
                        }
                    });

                } else {
                    //boton gris

                    Log.d("CityDetail", "Insertando");
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDataBase = CityDataBase.getInstance(getBaseContext());
                            mDataBase.getDao().insert(cityToShow);
                        }
                    });
                }
                mContenido = !mContenido;
                if (mContenido) butFav.setText(R.string.buttonEliminar);
                else butFav.setText("Añadir a la lista");
            }
        });
    }

    private void forecastButtonConfig() {
        Button foreButton = this.findViewById(R.id.buttonForecastCityDetail);
        foreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(City_Detail.this, ForecastActivity.class);
                String coord=cityToShow.getLocation().getLat().toString()+","+cityToShow.getLocation().getLon().toString();
                intent.putExtra("Coord",coord);
                startActivity(intent);
            }
        });
    }

    private void parseoCityToShow(City cityToShow) {

        String name = cityToShow.getLocation().getName();
        String[] nameChar = name.split("=");
        Log.d("ParseoCityToShow", name);
        cityToShow.getLocation().setName(nameChar[1]);

        String country = cityToShow.getLocation().getCountry();
        String[] countryChar = country.split("=");
        cityToShow.getLocation().setCountry(countryChar[1]);

        String region = cityToShow.getLocation().getRegion();
        String[] regionChar = region.split("=");
        cityToShow.getLocation().setRegion(regionChar[1]);

    }

    private boolean dentroDeLista(List<City> cityList, City cityToView) {
        Log.d("CityDetail", "Param: " + cityToView.toString());
        boolean dentro = false;
        for (City c : cityList) {
            Log.d("CityDetail", "Lista elto: ID " + c.getId() + ", " + c.toString());
            if (c.equalCity(cityToView)) {
                cityToView.setId(c.getId());
                Log.d("CityDetail", "Param ID: " + c.getId());
                Log.d("CityDetail", "Lista elto ID: " + c.getId());
                return true;
            }

        }

        return dentro;
    }

    private void SetCityData(City city) {

        String cityName = city.getLocation().getName();
        TextView textViewNameCity = findViewById(R.id.textViewNameCity);
        textViewNameCity.setText(cityName + ", " + city.getLocation().getCountry());

        TextView viewDay = findViewById(R.id.viewDay);
        viewDay.setText(city.getCurrent().getCondition().getText());

        TextView viewTemperature = findViewById(R.id.viewTemperature);
        viewTemperature.setText(city.getCurrent().getTempC().toString() + " º");

        TextView viewTermicSensation = findViewById(R.id.viewTermicSensation);
        viewTermicSensation.setText(city.getCurrent().getFeelslikeC().toString() + " º");

        TextView viewRadUv = findViewById(R.id.viewRadUv);
        viewRadUv.setText(city.getCurrent().getUv().toString());

        TextView viewHumidity = findViewById(R.id.viewHumidity);
        viewHumidity.setText(city.getCurrent().getHumidity().toString() + " %");

        TextView viewWind = findViewById(R.id.viewWind);
        viewWind.setText(city.getCurrent().getWindKph().toString() + " km/h");

        TextView posRain = findViewById(R.id.posRain);
        posRain.setText(city.getCurrent().getPrecipMm().toString() + " mm");
    }
}