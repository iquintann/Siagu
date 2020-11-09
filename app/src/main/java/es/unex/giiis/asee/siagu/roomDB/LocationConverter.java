package es.unex.giiis.asee.siagu.roomDB;

import android.util.Log;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import es.unex.giiis.asee.siagu.model.Location;

public class LocationConverter {

    @TypeConverter
    public String toString(Location location) {
        String cadena = "" +
                location.getName() + "," +
                location.getRegion() + "," +
                location.getCountry() + "," +
                location.getLat() + "," +
                location.getLon() + "," +
                location.getTzId() + "," +
                location.getLocaltimeEpoch() + "," +
                location.getLocaltime();
        Log.d("CadenaCoverter","toString-> "+cadena);
        return cadena;
    }

    @TypeConverter
    public Location toLocation(String cadena) {
        Location location = new Location();
        Log.d("CadenaCoverter","toLocation-> "+cadena);
        String[] frac = cadena.split(",");
        location.setName(frac[0]);
        location.setRegion(frac[1]);
        location.setCountry(frac[2]);
        location.setLat(Double.parseDouble(frac[3]));
        location.setLon(Double.parseDouble(frac[4]));
        location.setTzId(frac[5]);
        location.setLocaltimeEpoch(Integer.parseInt((frac[6])));
        location.setLocaltime(frac[7]);

        return location;
    }
}
