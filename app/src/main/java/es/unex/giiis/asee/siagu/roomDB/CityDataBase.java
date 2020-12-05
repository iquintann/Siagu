package es.unex.giiis.asee.siagu.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giiis.asee.siagu.model.City;

@Database(entities = {City.class},version = 10)
public abstract class CityDataBase extends RoomDatabase {
    private static CityDataBase instance;
    public static CityDataBase getInstance(Context context){
        if(instance==null)
            instance= Room.databaseBuilder(context, CityDataBase.class,"city.db").build();
        return instance;
    }

    public abstract CityItemDao getDao();
}
