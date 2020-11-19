package es.unex.giiis.asee.siagu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Setting_Siagu extends AppCompatActivity {

    private Setting_Siagu context;
    public static final String USERNAME = "USERNAME";
    public static final String USERDATA = "USERDATA";
    public static final String USERCITY = "USERCITY";
    public static final String PREFID = "USERID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_siagu);
        context = Setting_Siagu.this;

        EditText editName = findViewById(R.id.editNameUser);
        EditText editCity = findViewById(R.id.editCity);

        SharedPreferences sharedPref = getSharedPreferences(USERDATA,Context.MODE_PRIVATE);
        editName.setText(sharedPref.getString(USERNAME,"Nombre"));
        editCity.setText(sharedPref.getString(USERCITY,"Ciudad"));

        //Correo de contacto
        configEmail();

        //Boton de guardado de las preferencias
        configButtonSavePref();
        Button saveButon = findViewById(R.id.buttonSaveChange);
        saveButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editName.getText().toString();
                String city=editCity.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences(USERDATA,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(USERNAME, name);
                editor.putString(USERCITY, city);
                editor.commit();
                finish();
            }
        });

    }

    private void configButtonSavePref() {
    }

    private void configEmail() {
        TextView email = findViewById(R.id.emailContact);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mail to:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.getText().toString()});
                startActivity(emailIntent);
            }
        });
    }
}