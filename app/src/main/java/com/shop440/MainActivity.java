package com.shop440;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shop440.Fragments.MainActivityFragment;
import com.shop440.Utils.AppEventsLogger;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    VolleySingleton volleySingleton;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //printKeyHash(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isnotlogged", true)){
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }
        Log.d("token", sharedPreferences.getString(Urls.INSTANCE.getTOKEN(), "null"));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewStoreActivity.class);
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        AppEventsLogger.logMainActivityOpenedEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem searchItem = menu.findItem(R.id.search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.profile) {
            if(!sharedPreferences.getString("tokens", "Null").equals("Null")){
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
            }else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }

            return true;
        }

        if(id == R.id.search){
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

   /* public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }*/


}
