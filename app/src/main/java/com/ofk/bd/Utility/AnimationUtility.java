package com.ofk.bd.Utility;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.ofk.bd.R;

public class AnimationUtility {

    public static void startAnimation(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_down));
        view.setVisibility(View.GONE);
    }

    public static void endAnimation(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_above));
        view.setVisibility(View.VISIBLE);
    }
}
