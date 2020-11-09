package es.unex.giiis.asee.siagu.ui.cityList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;
import es.unex.giiis.asee.siagu.roomDB.CityItemDao;
import es.unex.giiis.asee.siagu.ui.searchcity.CityAdapter;

public class CityListFragment extends Fragment {

    private CityListViewModel cityListViewModel;
    private Context mContext;
    private List<City> mCityList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CityAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cityListViewModel =
                ViewModelProviders.of(this).get(CityListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);
        mContext=getContext();

        //Obtengo una instancia del DAO
        CityDataBase.getInstance(mContext);

        //Recycler
        mCityList= new ArrayList<>();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view_list_city);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CityAdapter(new CityAdapter.OnCityClickListener() {
            @Override
            public void onItemClick(City item) {
                Snackbar.make(getView(),"Item "+item.getLocation().getName()+" clicked!" ,Snackbar.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        //Ejecutar desde hilo secundario
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //TODO Echarle un vistazo
                CityDataBase dataBase= CityDataBase.getInstance(mContext);
                Double lat=45.0;
                Double lon=45.0;
                City prueba = new City("Prueba city","Region","COUNTRY",lat,lon);

                dataBase.getDao().deleteAll();
                dataBase.getDao().insert(prueba);
                mCityList = dataBase.getDao().getAll();

                Log.d("CityListFragment","Inserto: " +prueba);
                Log.d("CityListFragment",mCityList.toString());

                if(mCityList!=null){
                    //necesario hacerlo desde hilo rpincipal
                    getActivity().runOnUiThread(() -> {for (City c:mCityList){
                        Log.d("CityList","City: "+c);
                        mAdapter.add(c);
                    }} );


                }

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();



    }
}