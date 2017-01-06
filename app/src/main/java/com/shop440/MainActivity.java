package com.shop440;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shop440.Utils.VolleySingleton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    VolleySingleton volleySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        if(findViewById(R.id.container) != null){
            MainActivityFragment mn = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, mn).commit();
        }
       /* if(sharedPreferences.getString(R.string.state, "no token")){

        }*/
        volleySingleton = VolleySingleton.getsInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
