package es.unex.giiis.asee.siagu.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityRepository;
import es.unex.giiis.asee.siagu.vistaModelo.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.vistaModelo.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.vistaModelo.api_runable.CityNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.Vista.forecast.ForecastActivity;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.vistaModelo.roomDB.CityDataBase;

import static es.unex.giiis.asee.siagu.Vista.Util.imageTiempo;
import static java.lang.Long.parseLong;


public class City_Detail extends AppCompatActivity implements OnReposLoadedListener {

    private City cityToShow;
    private boolean mContenido = false;
    private CityDataBase mDataBase;
    private View mView;
    private City_Detail mContext;
    private CityRepository mRepository;
    private long idCityItent;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        mContext = this;


        String coorLat = null;
        String coorLong = null;

        //Obtenemos instancia del repositorio
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(this).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) this, this::onReposLoaded);
        mRepository.setBusqueda(true);


        idCityItent = parseLong(getIntent().getSerializableExtra("IDDAO").toString());
        cityName = getIntent().getSerializableExtra("CityName").toString();
        Log.d("Detail", "ID del la ciudad" + idCityItent);

        mRepository.actualizarCiudad(idCityItent);

        configuracionDeAppBar();

    }

    @Override
    public void onReposLoaded(List<City> cityList) {
        Log.d("CityDetail_OnRepost", "Recargado");

        Log.d("COMPCAD",cityName);
        for (City c : cityList) {
            long id=c.getId();
            String nombreCiuduad = c.getLocation().getName()+", "+c.getLocation().getRegion()+", "+c.getLocation().getCountry();
            Log.d("COMPCAD",nombreCiuduad);
            if (nombreCiuduad.equals(cityName)) {
                Log.d("CityDetail_OnRepost", c.getLocation().getName() + " contenida y mostrando");
                SetCityData(c);
                mContenido=c.isGuardado();
                cityToShow=c;
                botonFavAddDelete();
                forecastButtonConfig();

            }
        }

    }

    private City buscarFavorita(List<City> list) {
        for (City c : list) {
            if (c.isPrincipal())
                return c;
        }
        return null;
    }

    private void configuracionDeAppBar() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Detalles del clima");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private City obtenerElementoPorId(List<City> list, long idCity) {
        for (City elm : list) {
            if (elm.getId() == idCity) {
                return elm;
            }
        }
        return null;
    }

    private void procesadoPorBusqueda(String coorCity) {

        //Carga la ciudad que estamos buscando
        AppExecutors.getInstance().networkIO().execute(new CityNetworkLoaderRunnable((new OnReposLoadedListener() {
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
                        mContenido = cityToShow.isGuardado();
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
            if (mContenido) {
                butFav.setText(R.string.buttonEliminar);
                botonRojo(butFav);
            } else {
                butFav.setText("Añadir a la lista");
                botonVerde(butFav);

            }
        });

        butFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContenido) {
                    //boton
                    botonVerde(butFav);

                    Log.d("CityDetail", "Borrando de DAO");
                    mRepository.setCityGuardada(cityToShow.getId(),false);
                    /*AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDataBase = CityDataBase.getInstance(getBaseContext());
                            cityToShow.setGuardado(false);
                            if (mDataBase.getDao().update(cityToShow) != 1)
                                mDataBase.getDao().deleteById(cityToShow.getId());
                        }
                    });*/

                } else {
                    //boton verde
                    botonRojo(butFav);

                    Log.d("CityDetail", "Insertando");
                    mRepository.setCityGuardada(cityToShow.getId(),true);
                    /*AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDataBase = CityDataBase.getInstance(getBaseContext());
                            cityToShow.setGuardado(true);
                            if (mDataBase.getDao().update(cityToShow) != 1)
                                mDataBase.getDao().insert(cityToShow);
                        }
                    });*/
                }
                mContenido = !mContenido;
                if (mContenido) butFav.setText(R.string.buttonEliminar);
                else butFav.setText("Añadir a la lista");
            }
        });
    }

    private void botonVerde(Button butFav) {
        butFav.setBackgroundResource(R.drawable.custom_button_border_accept);
        butFav.setPadding(20, 10, 20, 10);
    }

    private void botonRojo(Button butFav) {
        butFav.setBackgroundResource(R.drawable.custom_button_border_cancel);
        butFav.setPadding(20, 10, 20, 10);
    }

    private void forecastButtonConfig() {
        Button foreButton = this.findViewById(R.id.buttonForecastCityDetail);
        foreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(City_Detail.this, ForecastActivity.class);
                String coord = cityToShow.getLocation().getLat().toString() + "," + cityToShow.getLocation().getLon().toString();
                intent.putExtra("Coord", coord);
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

    public static boolean dentroDeLista(List<City> cityList, City cityToView) {
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
        Log.d("CurrentCityDetail", city.getCurrent().getCondition().getText());

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

        ImageView imageView = findViewById(R.id.imageWheater);
        String tiempo = city.getCurrent().getCondition().getText();
        int source = imageTiempo(tiempo);
        imageView.setImageResource(source);

    }



}