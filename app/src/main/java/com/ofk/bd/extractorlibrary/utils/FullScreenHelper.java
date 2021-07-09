package com.ofk.bd.extractorlibrary.utils;

import android.view.View;

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
    public void enterFullScreen() {
        View decorView = context.getWindow().getDecorView();

        hideSystemUi(decorView);

 /*       BottomNavigationView bottomNavigationView = context.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        FloatingActionButton backButton = context.findViewById(R.id.backButton);
        backButton.setVisibility(View.GONE);*/

        for (View view : views) {
            view.setVisibility(View.GONE);
            view.invalidate();
        }
    }

    /**
     * call this method to exit full screen
     */
    public void exitFullScreen() {
        View decorView = context.getWindow().getDecorView();

        showSystemUi(decorView);
/*
        BottomNavigationView bottomNavigationView = context.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        FloatingActionButton backButton = context.findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);*/

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
