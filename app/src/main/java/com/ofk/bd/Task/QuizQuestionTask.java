package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Interface.QuizQuestionLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionTask implements Runnable {

    private final String sectionName;
    private final String courseName;
    private final QuizQuestionLoadCallback quizQuestionLoadCallback;

    private List<String> quizQuestionList;

    public QuizQuestionTask(QuizQuestionLoadCallback callback, String sectionName, String courseName) {
        this.quizQuestionLoadCallback = callback;
        this.sectionName = sectionName;
        this.courseName = courseName;
    }

    @Override
    public void run() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Sub Section")
                .child(sectionName + " Section")
                .child(courseName)
                .child("Quiz");

        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    quizQuestionList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String question = ds.getKey();
                        quizQuestionList.add(question);
                    }

                    quizQuestionLoadCallback.onQuizQuestionLoadCallback(quizQuestionList);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
