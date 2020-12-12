package es.unex.giiis.asee.siagu.vistaModelo.roomDB;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.model.Condition;
import es.unex.giiis.asee.siagu.model.Day;
import es.unex.giiis.asee.siagu.model.Forecast;
import es.unex.giiis.asee.siagu.model.Forecastday;

public class ForecastConverter {

    @TypeConverter
    public static String toString(Forecast forecast) {
        String outChar = "";
        if (forecast == null) {
            return "vacio";
        }
        List<Forecastday> list = forecast.getForecastday();
        for (Forecastday f : list) {
            outChar = outChar +
                    f.getDate() + "," +
                    f.getDay().getCondition().getText() + "," +
                    f.getDay().getMaxtempC() + "," +
                    f.getDay().getMintempC() + ",";
            outChar = outChar + ";";
        }

        return outChar;
    }

    @TypeConverter
    public static Forecast toForecastday(String forecastStatus) {
        Forecast fore = new Forecast();
        List<Forecastday> list = new ArrayList<>();
        Log.d("toForecastday", "Cadena " + forecastStatus);
        if (forecastStatus.equals("vacio") || forecastStatus.equals("")) {
            return fore;
        }
        String[] listaVector = forecastStatus.split(";");

        for (int j = 0; j < listaVector.length; j++) {
            Log.d("toForecastday", listaVector[j]);
        }

        for (int i = 0; i < listaVector.length; i++) {
            String[] fvector = listaVector[i].split(",");

            if (fvector.length > 3) {
                Log.d("toForecastday", fvector[0]);
                Log.d("toForecastday", fvector[1]);
                Log.d("toForecastday", fvector[2]);
                Log.d("toForecastday", fvector[3]);

                Forecastday forecastday = new Forecastday();
                forecastday.setDate(fvector[0]);
                Day day = new Day();
                day.setMaxtempC(Double.valueOf(fvector[2]));
                day.setMintempC(Double.valueOf(fvector[3]));

                Condition condition = new Condition();
                condition.setText(fvector[1]);
                day.setCondition(condition);
                forecastday.setDay(day);
                list.add(forecastday);
            }
        }

        fore.setForecastday(list);
        return fore;
    }
}
