package com.shop440.Utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by SMILECS on 2/13/17.
 */

public class Metrics {
    public static int GetMetrics(RecyclerView list){
        //DisplayMetrics metrics = new DisplayMetrics();
        int spans = (int) Math.floor(list.getContext().getResources().getDisplayMetrics().widthPixels / (float) 240);
        return spans;
    }
}
