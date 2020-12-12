package es.unex.giiis.asee.siagu.Vista.ui.cityList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CityListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CityListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is city list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}