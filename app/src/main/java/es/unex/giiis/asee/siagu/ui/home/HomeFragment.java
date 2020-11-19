package es.unex.giiis.asee.siagu.ui.home;

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
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import es.unex.giiis.asee.siagu.forecast.ForecastActivity;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.api_runable.ReposNetworkLoaderRunnable;
import es.unex.giiis.asee.siagu.Setting_Siagu;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;

import static es.unex.giiis.asee.siagu.City_Detail.dentroDeLista;
import static es.unex.giiis.asee.siagu.Util.imageTiempo;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View mRoot;
    private City prefCity;
    private CityDataBase mDataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRoot = root;
        mDataBase = CityDataBase.getInstance(getContext());


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getContext().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);
        String cityName = sharedPref.getString(Setting_Siagu.USERCITY, "Ciudad");

        AppExecutors.getInstance().networkIO().execute(new ReposNetworkLoaderRunnable((new OnReposLoadedListener() {
            @Override
            public void onReposLoaded(List<City> cityList) {
                City city = cityList.get(0);
                SetCityData(mRoot, city);
                prefCity = city;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<City> list = mDataBase.getDao().getAll();
                        if (!dentroDeLista(list, city)) {
                            mDataBase.getDao().insert(city);
                        }
                    }
                });


            }
        }), getContext(), cityName));

        //No cargo la ciudad desde la API
        if (prefCity == null) {
            loadCityWithOutWeb(cityName);
        }

        //Configuración del boton de previsión
        forecastButtonConfig();

    }

    private void loadCityWithOutWeb(String cityName) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<City> list = mDataBase.getDao().getAll();
                for (City c : list) {
                    if (c.getLocation().getName().equals(cityName)) {
                        Log.d("HomeFragment", "No internet, cargando ciudad con id: " + c.getId());
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                SetCityData(mRoot, c);
                            }
                        });

                    }
                }
            }
        });
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

        SharedPreferences sharedPref = HomeFragment.this.getActivity().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);
        TextView textViewNameUser = root.findViewById(R.id.username);
        textViewNameUser.setText("Hola " + sharedPref.getString(Setting_Siagu.USERNAME, "Nombre") + "!");

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


        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                "Actualizados datos de " + cityName, Toast.LENGTH_SHORT);
        toast1.show();
    }
}