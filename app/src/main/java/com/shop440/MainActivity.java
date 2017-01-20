package com.shop440;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shop440.Fragments.MainActivityFragment;
import com.shop440.Utils.VolleySingleton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    VolleySingleton volleySingleton;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        Log.d("token", sharedPreferences.getString("token", "null"));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewStore.class);
                startActivity(i);
            }
        });
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
            if(!sharedPreferences.getString("token", "Null").equals("Null")){
                Intent i = new Intent(this, Profile.class);
                startActivity(i);
            }else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
