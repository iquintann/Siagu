
package es.unex.giiis.asee.siagu.model;

import android.util.Log;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Condition {

    @SerializedName("text")
    @Expose
    private String text="";
    @SerializedName("icon")
    @Expose
    private String icon="";
    @SerializedName("code")
    @Expose
    private Integer code=0;

    public Condition(String toString) {
        String  [] cadena=toString.split("'");
        Log.d("Vector",toString);
        text= cadena[1];
    }

    public Condition() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "text='" + text + '\'' +
                "; icon='" + icon + '\'' +
                "; code=" + code +
                '}';
    }
}
