package es.unex.giiis.asee.siagu.forecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForecastListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ForecastListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is city list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
