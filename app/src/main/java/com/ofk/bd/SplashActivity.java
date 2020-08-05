package com.ofk.bd;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ofk.bd.databinding.ActivitySplashBinding;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = binding.progressBar;

        new ShowProgressBar(SplashActivity.this).execute(3);
    }

    private static class ShowProgressBar extends AsyncTask<Integer, Integer, Boolean> {

        private WeakReference<SplashActivity> activityWeakReference;

        public ShowProgressBar(SplashActivity activity) {
            this.activityWeakReference = new WeakReference<SplashActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) {
                publishProgress((i * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            SplashActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
            activity.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean isFinished) {
            super.onPostExecute(isFinished);

            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (isFinished) {
                activity.startActivity(new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                activity.finish();
            }
        }
    }
}