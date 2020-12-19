package com.ofk.bd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    public static final int FETCH_INTERVAL = 172800;
    public static final String COURSE_TO_DISPLAY = "course_to_display";

    private final Handler handler = new Handler();
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // activating previously saved config
        remoteConfig.activate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!MyApp.IS_CONNECTED) {
            new AlertDialogUtility().showAlertDialog(this, "noConnection");
            return;
        }

        initRemoteConfig();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);

                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(SplashActivity.this, InfoActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                finish();
            }
        }, 1500);
    }

    private void initRemoteConfig() {
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
                .build();

        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.remote_default_values);

        remoteConfig.fetch();

        Common.courseToDisplay = remoteConfig.getString(COURSE_TO_DISPLAY);
        Common.courseHeadline = new StringUtility(getApplicationContext())
                .getSectionHeadline(remoteConfig.getString(COURSE_TO_DISPLAY));
    }
}