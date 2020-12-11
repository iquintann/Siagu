package es.unex.giiis.asee.siagu.ui.searchcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.City_Detail;
import es.unex.giiis.asee.siagu.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.Repository.CityRepository;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.api_runable.CityNetworkSearchCityRunnable;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;

public class SearchCityFragment extends Fragment implements OnReposLoadedListener {

    private SearchCityViewModel searchCityViewModel;
    private List<City> mCityList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CityAdapter mAdapter;

    //Repository
    private CityRepository mRepository;
    private SharedPreferences sharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchCityViewModel =
                ViewModelProviders.of(this).get(SearchCityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_city, container, false);
        mCityList = new ArrayList<>();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        //Recicler view
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CityAdapter(new CityAdapter.OnCityClickListener() {
            @Override
            public void onItemClick(City item) {
                //lanzar un intent a una nueva actividad con los datos de la seleccionada + petición api
                Intent intent = new Intent(getActivity(), City_Detail.class);
                intent.putExtra("Lat", item.getLocation().getLat());
                intent.putExtra("Lon", item.getLocation().getLon());
                intent.putExtra("IDDAO",item.getId());
                String nombreCiuduad = item.getLocation().getName()+", "+item.getLocation().getRegion()+", "+item.getLocation().getCountry();
                intent.putExtra("CityName",nombreCiuduad);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        //Busqueda de api
        botonBusqueda(root);

        //mRepository
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(getContext()).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) getContext(), this::onReposLoaded);
        mRepository.setBusqueda(true);

        return root;
    }

    private void botonBusqueda(View root) {

        Button buttonSearch = root.findViewById(R.id.buttonSearchCity);
        EditText cityText = root.findViewById(R.id.editTextTextSearchCity);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();


                String textoBusqueda = cityText.getText().toString();
                Log.d("OnClick", "texto de búsqueda: " + textoBusqueda);
                boolean esBusqueda = true;
                mRepository.setCitySearch(textoBusqueda);

            }
        });

    }

    @Override
    public void onReposLoaded(List<City> cityList) {
        mAdapter.clear();
        Log.d("SearchCityFrag", cityList.toString());

        Log.d("SearchCity", "Lista de ciudades: " + cityList);
        for (City city : cityList)
            if (!city.isGuardado())
                mAdapter.add(city);
    }
}