package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Interface.QuizOptionLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class QuizOptionTask implements Runnable {

    private final String sectionName;
    private final String courseName;
    private final String question;
    private final QuizOptionLoadCallback quizOptionLoadCallback;

    private List<String> quizOptionList;

    public QuizOptionTask(QuizOptionLoadCallback quizOptionLoadCallback, String sectionName, String courseName, String question) {
        this.sectionName = sectionName;
        this.courseName = courseName;
        this.question = question;
        this.quizOptionLoadCallback = quizOptionLoadCallback;
    }

    @Override
    public void run() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Sub Section")
                .child(sectionName + " Section")
                .child(courseName)
                .child("Quiz")
                .child(question);

        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    quizOptionList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        quizOptionList.add(ds.getValue(String.class));
                    }

                    quizOptionLoadCallback.onQuizOptionLoadCallback(quizOptionList);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
