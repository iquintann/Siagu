package es.unex.giiis.asee.siagu.roomDB;

import androidx.room.TypeConverter;

import es.unex.giiis.asee.siagu.model.Condition;
import es.unex.giiis.asee.siagu.model.Current;

public class CurrentConverter  {

    @TypeConverter
    public static String toString(Current current){
        String outChar=""+
        current.getLastUpdatedEpoch()+","+
        current.getLastUpdated()+","+
        current.getTempC()+","+
        current.getTempF()+","+
        current.getIsDay()+",";
        if (current.getCondition() == null) {
            outChar = outChar + "Condition{text='Despejado'; icon=''; code=0},";
        }else {
            outChar = outChar + current.getCondition().toString() + ",";
        }
        outChar=outChar+
        current.getWindMph()+","+
        current.getWindKph()+","+
        current.getWindDegree()+","+
        current.getWindDir()+","+
        current.getPressureMb()+","+
        current.getPressureIn()+","+
        current.getPrecipMm()+","+
        current.getPrecipIn()+","+
        current.getHumidity()+","+
        current.getCloud()+","+
        current.getFeelslikeC()+","+
        current.getFeelslikeF()+","+
        current.getVisKm()+","+
        current.getVisMiles()+","+
        current.getUv()+","+
        current.getGustMph()+","+
        current.getGustKph();



        return outChar;
    }
    @TypeConverter
   public static Current toCurrent(String status){
        String [] frac=status.split(",");
        Current current=new Current();
        current.setLastUpdatedEpoch(Integer.parseInt(frac[0]));
                current.setLastUpdated(frac[1]);
                current.setTempC(Double.parseDouble(frac[2]));
                current.setTempF(Double.parseDouble(frac[3]));
                current.setIsDay(Integer.parseInt(frac[4]));
                current.setCondition(new Condition(frac[5].toString()));
                current.setWindMph(Double.parseDouble(frac[6]));
                current.setWindKph(Double.parseDouble(frac[7]));
                current.setWindDegree(Integer.parseInt(frac[8]));
                current.setWindDir(frac[9]);
                current.setPressureMb(Double.parseDouble(frac[10]));
                current.setPressureIn(Double.parseDouble(frac[11]));
                current.setPrecipMm(Double.parseDouble(frac[12]));
                current.setPrecipIn(Double.parseDouble(frac[13]));
                current.setHumidity(Integer.parseInt(frac[14]));
                current.setCloud(Integer.parseInt(frac[15]));
                current.setFeelslikeC(Double.parseDouble(frac[16]));
                current.setFeelslikeF(Double.parseDouble(frac[17]));
                current.setVisKm(Double.parseDouble(frac[18]));
                current.setVisMiles(Double.parseDouble(frac[19]));
                current.setUv(Double.parseDouble(frac[20]));
                current.setGustMph(Double.parseDouble(frac[21]));
                current.setGustKph(Double.parseDouble(frac[22]));
        return current;
    }
}
