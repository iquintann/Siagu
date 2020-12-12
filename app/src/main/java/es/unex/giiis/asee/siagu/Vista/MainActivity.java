package es.unex.giiis.asee.siagu.Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityNewtworkDataSource;
import es.unex.giiis.asee.siagu.vistaModelo.Repository.CityRepository;
import es.unex.giiis.asee.siagu.model.roomDB.CityDataBase;

public class MainActivity extends AppCompatActivity {

    public static final int MENU_SETTING = Menu.FIRST;
    private AppBarConfiguration mAppBarConfiguration;
    public static final String KEY_API="f9b6c8f8d9d6478894a122855200110";
    private CityRepository mRepository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_cities, R.id.nav_search_city)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mRepository = CityRepository.getInstance(CityDataBase.getInstance(this).getDao(), CityNewtworkDataSource.getInstance());


        //Carga de los datos ciudad por defecto
        sharedPreferences = this.getSharedPreferences(Setting_Siagu.USERDATA, Context.MODE_PRIVATE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        menu.add(Menu.NONE, MENU_SETTING, Menu.NONE, "Ajustes");
        menu.add(Menu.NONE, MENU_SETTING+1, Menu.NONE, "Borrar ciudades guardadas");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case MENU_SETTING:

                Intent intent=new Intent(MainActivity.this,Setting_Siagu.class);
                startActivity(intent);
                return true;

            case MENU_SETTING+1:
                Log.d("onOptionsItemSelected","Purgar");
                mRepository.deleteAll();
                /*AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        CityDataBase.getInstance(getBaseContext()).getDao().deleteAll();
                        String cityName = sharedPreferences.getString(Setting_Siagu.USERCITY, "Ciudad");

                        AppExecutors.getInstance().mainThread().execute(() -> mRepository.setCityName(cityName,null));

                    }
                });*/
                return true;



            default:
                Log.d("onOptionsItemSelected","Seleccionado"+item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}