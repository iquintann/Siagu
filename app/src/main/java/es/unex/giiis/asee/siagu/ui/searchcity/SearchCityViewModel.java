package es.unex.giiis.asee.siagu.ui.searchcity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchCityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchCityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is searchcity fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}