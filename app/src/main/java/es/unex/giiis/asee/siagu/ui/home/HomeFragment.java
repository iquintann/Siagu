package es.unex.giiis.asee.siagu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import static es.unex.giiis.asee.siagu.Util.imageTiempo;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View mRoot;
    private City prefCity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRoot=root;


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getContext().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);
        String cityName = sharedPref.getString(Setting_Siagu.USERCITY,"Ciudad");;

        AppExecutors.getInstance().networkIO().execute(new ReposNetworkLoaderRunnable((new OnReposLoadedListener() {
            @Override
            public void onReposLoaded(List<City> cityList) {
                City city=cityList.get(0);
                SetCityData(mRoot,city);
                prefCity=city;
            }
        }),getContext(),cityName));

        //Configuración del boton de previsión
        forecastButtonConfig();

    }

    private void forecastButtonConfig() {
        Button foreButton = mRoot.findViewById(R.id.buttonForecastHome);
        foreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForecastActivity.class);
                String coord=prefCity.getLocation().getLat().toString()+","+prefCity.getLocation().getLon().toString();
                intent.putExtra("Coord",coord);
                startActivity(intent);
            }
        });
    }

    public void SetCityData(View root, City city) {

        SharedPreferences sharedPref = HomeFragment.this.getActivity().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);
        TextView textViewNameUser = root.findViewById(R.id.username);
        textViewNameUser.setText("Hola "+sharedPref.getString(Setting_Siagu.USERNAME,"Nombre")+"!");

        String cityName = city.getLocation().getName();
        TextView textViewNameCity = root.findViewById(R.id.textViewNameCity);
        textViewNameCity.setText(cityName+", "+city.getLocation().getCountry());


        TextView viewDay = root.findViewById(R.id.viewDay);
        viewDay.setText(city.getCurrent().getCondition().getText());

        TextView viewTemperature = root.findViewById(R.id.viewTemperature);
        viewTemperature.setText(city.getCurrent().getTempC().toString()+" º");

        TextView viewTermicSensation = root.findViewById(R.id.viewTermicSensation);
        viewTermicSensation.setText(city.getCurrent().getFeelslikeC().toString()+" º");

        TextView viewRadUv = root.findViewById(R.id.viewRadUv);
        viewRadUv.setText(city.getCurrent().getUv().toString());

        TextView viewHumidity = root.findViewById(R.id.viewHumidity);
        viewHumidity.setText(city.getCurrent().getHumidity().toString()+" %");

        TextView viewWind = root.findViewById(R.id.viewWind);
        viewWind.setText(city.getCurrent().getWindKph().toString()+" km/h");

        TextView posRain = root.findViewById(R.id.posRain);
        posRain.setText(city.getCurrent().getPrecipMm().toString()+" mm");

        ImageView imageView = root.findViewById(R.id.imageWheater);
        String tiempo = city.getCurrent().getCondition().getText();
        int source = imageTiempo(tiempo);
        imageView.setImageResource(source);


        Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),
                "Actualizados datos de "+cityName, Toast.LENGTH_SHORT);
        toast1.show();
    }
}