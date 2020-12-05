package es.unex.giiis.asee.siagu.ui.cityList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.City_Detail;
import es.unex.giiis.asee.siagu.MainActivity;
import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.Repository.CityRepository;
import es.unex.giiis.asee.siagu.Setting_Siagu;
import es.unex.giiis.asee.siagu.api_runable.AppExecutors;
import es.unex.giiis.asee.siagu.api_runable.OnReposLoadedListener;
import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.roomDB.CityDataBase;
import es.unex.giiis.asee.siagu.ui.searchcity.CityAdapter;

import static es.unex.giiis.asee.siagu.MainActivity.MENU_SETTING;

public class CityListFragment extends Fragment implements OnReposLoadedListener {

    private CityListViewModel cityListViewModel;
    private Context mContext;
    private List<City> mCityList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CityAdapter mAdapter;

    //Repository
    private CityRepository mRepository;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cityListViewModel =
                ViewModelProviders.of(this).get(CityListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);
        mContext = getContext();

        //Obtengo una instancia del DAO
        CityDataBase.getInstance(mContext);

        //Recycler
        mCityList = new ArrayList<>();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view_list_city);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CityAdapter(new CityAdapter.OnCityClickListener() {
            @Override
            public void onItemClick(City item) {
                Snackbar.make(getView(), "Item " + item.getLocation().getName() + " clicked!", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), City_Detail.class);
                intent.putExtra("Id", item.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        //Ejecutar desde hilo secundario
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                CityDataBase dataBase = CityDataBase.getInstance(mContext);
                mCityList = dataBase.getDao().getAll();
                // Log.d("CityListFragment","Inserto: " +prueba);
                Log.d("CityListFragment", mCityList.toString());
            }
        });

        //mRepository
        mRepository = CityRepository.getInstance(CityDataBase.getInstance(getContext()).getDao(), CityNewtworkDataSource.getInstance());
        mRepository.getCurrentRepos().observe((LifecycleOwner) getContext(), this::onReposLoaded);
        sharedPreferences = getContext().getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);
        String cityName = sharedPreferences.getString(Setting_Siagu.USERCITY, "Ciudad");

        return root;
    }


    @Override
    public void onReposLoaded(List<City> cityList) {
        Log.d("OnReposCityList","Se actualiza");

        mAdapter.clear();
        for (City c : cityList) {
            Log.d("CityList", "City: " + c);
            mAdapter.add(c);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        /*mAdapter.clear();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                CityDataBase dataBase = CityDataBase.getInstance(mContext);
                mCityList = dataBase.getDao().getAll();

                if (mCityList != null) {
                    //necesario hacerlo desde hilo rpincipal
                    getActivity().runOnUiThread(() -> {
                        for (City c : mCityList) {
                            Log.d("CityList", "City: " + c);
                            mAdapter.add(c);
                        }
                    });
                }
            }
        });*/

    }


}