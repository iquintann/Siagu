package es.unex.giiis.asee.siagu.ui.cityList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import es.unex.giiis.asee.siagu.R;

public class CityListFragment extends Fragment {

    private CityListViewModel cityListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cityListViewModel =
                ViewModelProviders.of(this).get(CityListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);
        final TextView textView = root.findViewById(R.id.text_city_list);
        cityListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}