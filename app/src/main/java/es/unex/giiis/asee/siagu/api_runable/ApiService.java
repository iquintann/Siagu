package es.unex.giiis.asee.siagu.api_runable;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.SearchCity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //La estructura de ejemplo para current city Caceres lenguaje español
    //http://api.weatherapi.com/v1/current.json?key=f9b6c8f8d9d6478894a122855200110&q=Caceres&lang=es
    //se le añade a partir del & incluido
    @GET("current.json")
    Call<City> listCity(@Query("key") String key, @Query("q") String city, @Query("lang") String lang);

    @GET("search.json")
    Call <List <SearchCity>> searchCity(@Query("key") String key, @Query("q") String city);

    @GET("forecast.json")
    Call <City> forecastCity(@Query("key") String key, @Query("q") String city, @Query("lang") String lang, @Query("days") String days);
}
