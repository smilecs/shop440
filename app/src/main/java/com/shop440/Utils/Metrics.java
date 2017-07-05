package com.shop440.Utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by SMILECS on 2/13/17.
 */

public class Metrics {
    public static int GetMetrics(RecyclerView list, Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth =(int) metrics.xdpi;
        int spans = (int) Math.floor(list.getContext().getResources().getDisplayMetrics().widthPixels / (float) cardWidth);
        return spans;
    }
}
