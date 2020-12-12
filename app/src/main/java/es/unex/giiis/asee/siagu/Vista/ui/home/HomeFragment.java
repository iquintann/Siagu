package es.unex.giiis.asee.siagu.Vista.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityRepository;
import es.unex.giiis.asee.siagu.Vista.forecast.ForecastActivity;
import es.unex.giiis.asee.siagu.vistaModelo.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.vistaModelo.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.Vista.Setting_Siagu;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.vistaModelo.roomDB.CityDataBase;

import static es.unex.giiis.asee.siagu.Vista.Setting_Siagu.USERDATA;
import static es.unex.giiis.asee.siagu.Vista.Util.imageTiempo;

public class HomeFragment extends Fragment implements OnReposLoadedListener {

    private HomeViewModel homeViewModel;
    private View mRoot;
    private City prefCity;
    private CityDataBase mDataBase;
    private SharedPreferences sharedPreferences;

    private CityRepository mRepository;
    private int favId;


    /*
    on Create
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRoot = root;
        mDataBase = CityDataBase.getInstance(getContext());


        //-----

//        favId = sharedPreferences.getString(Setting_Siagu.PREFCITY, "NADA");
        //Obtenemos instancia del repositorio
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(getContext()).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) getContext(), this::onReposLoaded);
        mRepository.setBusqueda(false);



        return root;
    }

    /*
    Cuando este la ciudad cargada esto de encarga de observar el cambio y modificar los datos
     */
    @Override
    public void onReposLoaded(List<City> cityList) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                int numCity = cityList.size();
                if (numCity > 0) {
                    Log.d("onReposLoaded","Ciudades ?¿-"+cityList);
                    prefCity = getCityPrincipal(cityList);
                    AppExecutors.getInstance().mainThread().execute(() -> {
                        SetCityData(mRoot, prefCity);
                        //Configuración del boton de previsión
                        forecastButtonConfig();
                    });

                }
            }
        });

    }

    private City getCityPrincipal(List<City> cityList) {
        for (City c : cityList) {
            if (c.isPrincipal()) {
                return c;
            }
        }
        return cityList.get(0);
    }

    private void buscarFavorita(List<City> cityList, String defecto) {
        for (City c : cityList) {
            Log.d("buscarFavorita", c.getLocation().getName() + " - " + c.getId());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String cityName = sharedPreferences.getString(Setting_Siagu.USERCITY, "Ciudad");
        mRepository.setCityName(cityName, null);
    }

    private void forecastButtonConfig() {
        Button foreButton = mRoot.findViewById(R.id.buttonForecastHome);

        foreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefCity != null) {
                    Intent intent = new Intent(getActivity(), ForecastActivity.class);
                    String coord = prefCity.getLocation().getLat().toString() + "," + prefCity.getLocation().getLon().toString();
                    intent.putExtra("Coord", coord);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "No disponible sin internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void SetCityData(View root, City city) {

        TextView textViewNameUser = root.findViewById(R.id.username);
        textViewNameUser.setText("Hola " + sharedPreferences.getString(Setting_Siagu.USERNAME, "Nombre") + "!");

        String cityName = city.getLocation().getName();
        TextView textViewNameCity = root.findViewById(R.id.textViewNameCity);
        textViewNameCity.setText(cityName + ", " + city.getLocation().getCountry());


        TextView viewDay = root.findViewById(R.id.viewDay);
        viewDay.setText(city.getCurrent().getCondition().getText());

        TextView viewTemperature = root.findViewById(R.id.viewTemperature);
        viewTemperature.setText(city.getCurrent().getTempC().toString() + " º");

        TextView viewTermicSensation = root.findViewById(R.id.viewTermicSensation);
        viewTermicSensation.setText(city.getCurrent().getFeelslikeC().toString() + " º");

        TextView viewRadUv = root.findViewById(R.id.viewRadUv);
        viewRadUv.setText(city.getCurrent().getUv().toString());

        TextView viewHumidity = root.findViewById(R.id.viewHumidity);
        viewHumidity.setText(city.getCurrent().getHumidity().toString() + " %");

        TextView viewWind = root.findViewById(R.id.viewWind);
        viewWind.setText(city.getCurrent().getWindKph().toString() + " km/h");

        TextView posRain = root.findViewById(R.id.posRain);
        posRain.setText(city.getCurrent().getPrecipMm().toString() + " mm");

        ImageView imageView = root.findViewById(R.id.imageWheater);
        String tiempo = city.getCurrent().getCondition().getText();
        int source = imageTiempo(tiempo);
        imageView.setImageResource(source);

    }


}