package es.unex.giiis.asee.siagu.model.api_runable;

import java.util.List;

import es.unex.giiis.asee.siagu.model.City;

public interface OnReposLoadedListener {
    //Interfaz de home
    public void onReposLoaded(List<City> cityList);
    }
