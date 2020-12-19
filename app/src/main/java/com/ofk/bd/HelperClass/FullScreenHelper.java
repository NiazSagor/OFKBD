package com.ofk.bd.HelperClass;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ofk.bd.R;

public class FullScreenHelper {
    private android.app.Activity context;
    private View[] views;

    /**
     * @param context
     * @param views   to hide/show
     */
    public FullScreenHelper(android.app.Activity context, View... views) {
        this.context = context;
        this.views = views;
    }

    /**
     * call this method to enter full screen
     */
    public void enterFullScreen(float ratio) {
        View decorView = context.getWindow().getDecorView();

        hideSystemUi(decorView);

        AspectRatioFrameLayout frameLayout = context.findViewById(R.id.videoFrameLayout);
        frameLayout.setAspectRatio(ratio);

        BottomNavigationView bottomNavigationView = context.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        LinearLayout videoLayout = context.findViewById(R.id.videoLayout);
        videoLayout.setVisibility(View.GONE);

        ViewPager2 videoRecourseViewPager = context.findViewById(R.id.videoRecourseViewPager);
        videoRecourseViewPager.setVisibility(View.GONE);
/*
        MotionLayout parent = context.findViewById(R.id.parentLayout);
        ConstraintSet set = new ConstraintSet();
        set.clone(parent);
        set.constrainWidth(R.id.videoFrameLayout, 0);
        set.applyTo(parent);
*/

        for (View view : views) {
            view.setVisibility(View.GONE);
            view.invalidate();
        }
    }

    /**
     * call this method to exit full screen
     */
    public void exitFullScreen(float ratio) {
        View decorView = context.getWindow().getDecorView();

        showSystemUi(decorView);

        BottomNavigationView bottomNavigationView = context.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        LinearLayout videoLayout = context.findViewById(R.id.videoLayout);
        videoLayout.setVisibility(View.VISIBLE);

        ViewPager2 videoRecourseViewPager = context.findViewById(R.id.videoRecourseViewPager);
        videoRecourseViewPager.setVisibility(View.VISIBLE);

        ConstraintLayout parent = context.findViewById(R.id.parentLayout);
        ConstraintSet set = new ConstraintSet();
        set.clone(parent);
        set.constrainWidth(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
        set.applyTo(parent);

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
            view.invalidate();
        }
    }

    private void hideSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
