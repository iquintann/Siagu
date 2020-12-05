package es.unex.giiis.asee.siagu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import es.unex.giiis.asee.siagu.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.Repository.CityRepository;
import es.unex.giiis.asee.siagu.forecast.ForecastActivity;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.Setting_Siagu;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;

import static es.unex.giiis.asee.siagu.City_Detail.dentroDeLista;
import static es.unex.giiis.asee.siagu.Util.imageTiempo;

public class HomeFragment extends Fragment implements OnReposLoadedListener {

    private HomeViewModel homeViewModel;
    private View mRoot;
    private City prefCity;
    private CityDataBase mDataBase;
    private SharedPreferences sharedPreferences;

    private CityRepository mRepository;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    /*
    on Create
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRoot = root;
        mDataBase = CityDataBase.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);

        //-----
        String cityName = sharedPreferences.getString(Setting_Siagu.USERCITY, "Ciudad");

        //Obtenemos instancia del repositorio
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(getContext()).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) getContext(), this::onReposLoaded);
        mRepository.setCityName(cityName,prefCity);


        //TODO Observe Shared pref
       /* sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });*/


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
                if(cityList.size() > 0) {
                    //Log.d("onRepostLoaded", "Paso por aqui");
                    //Log.d("onRepostLoaded", cityList.get(0).toString());
                    buscarFavorita(cityList,sharedPreferences.getString(Setting_Siagu.USERCITY,"DEFECTO"));

                    prefCity = cityList.get(cityList.size()-1);
                    SetCityData(mRoot, prefCity);
                    //Configuración del boton de previsión
                    forecastButtonConfig();
                }
            }
        });

    }

    private void buscarFavorita(List<City> cityList, String defecto) {
        for(City c:cityList){
            Log.d("buscarFavorita",c.getLocation().getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void forecastButtonConfig() {
        Button foreButton = mRoot.findViewById(R.id.buttonForecastHome);

        foreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefCity!=null) {
                    Intent intent = new Intent(getActivity(), ForecastActivity.class);
                    String coord = prefCity.getLocation().getLat().toString() + "," + prefCity.getLocation().getLon().toString();
                    intent.putExtra("Coord", coord);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"No disponible sin internet", Toast.LENGTH_SHORT).show();
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