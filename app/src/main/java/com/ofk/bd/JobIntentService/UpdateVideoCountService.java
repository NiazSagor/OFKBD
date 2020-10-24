package com.ofk.bd.JobIntentService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class UpdateVideoCountService extends JobIntentService {

    private static final String TAG = "ExampleJobIntentService";
    public static final String RECEIVER = "receiver";

    private int total = 0;

    private ArrayList<Integer> integers;

    private ArrayList<String> courseList;
    private ArrayList<String> sectionList;
    private ResultReceiver mReceiver;

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");

    public static final int SHOW_RESULT = 123;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, UpdateVideoCountService.class, SHOW_RESULT, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sectionList = new ArrayList<>();
        courseList = new ArrayList<>();
        integers = new ArrayList<>();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        final Bundle bundle = new Bundle();

        mReceiver = intent.getParcelableExtra(RECEIVER);

        sectionList = intent.getStringArrayListExtra("sectionName");
        courseList = intent.getStringArrayListExtra("courseName");

        Log.d(TAG, "onHandleWork: " + courseList.toString());

        final CountDownLatch latch = new CountDownLatch(courseList.size());

        for (int i = 0; i < courseList.size(); i++) {
            Log.d(TAG, "onHandleWork: ");

            db.child(sectionList.get(i) + " Section").child(courseList.get(i)).child("Video")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                total = (int) (total + ds.getChildrenCount());
                            }
                            integers.add(total);
                            total = 0;
                            latch.countDown();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onHandleWork: " + integers.toString());

        bundle.putStringArrayList("courseName", courseList);
        bundle.putIntegerArrayList("count", integers);
        mReceiver.send(SHOW_RESULT, bundle);
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
