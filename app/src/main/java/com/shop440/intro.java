package com.shop440;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.shop440.Fragments.SampleSlide;

public class intro extends AppIntro {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();
        preferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        editor = preferences.edit();
        addSlide(SampleSlide.newInstance(R.layout.splash1));
        addSlide(SampleSlide.newInstance(R.layout.splash2));
        addSlide(SampleSlide.newInstance(R.layout.splash3));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.LTGRAY);
        setSeparatorColor(Color.parseColor("#ffffff"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        editor.putBoolean("isnotlogged", false);
        editor.commit();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        editor.putBoolean("isnotlogged", false);
        editor.commit();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
