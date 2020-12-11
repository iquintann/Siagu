package es.unex.giiis.asee.siagu.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.giiis.asee.siagu.model.City;

@Dao
public interface CityItemDao {
    @Query("SELECT * FROM city")
    public List<City> getAll();

    @Insert
    public long insert(City city);

    @Delete
    public int delete(City city);

    @Query("DELETE FROM city")
    public void deleteAll();

    @Query("DELETE FROM city WHERE guardado = 0")
    public int deteleGuardadas();

    @Update
    public int update(City city);


    @Query("DELETE FROM city WHERE id = :userId")
    abstract void deleteById(long userId);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAllLiveData();



}
