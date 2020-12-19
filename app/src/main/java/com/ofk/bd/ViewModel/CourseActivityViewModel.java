package com.ofk.bd.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;
import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

public class CourseActivityViewModel extends AndroidViewModel {
    private static final String TAG = "CourseActivityViewModel";

    private final MutableLiveData<String> currentVideoFromList = new MutableLiveData<>();

    private final UserProgressRepository userProgressRepository;

    private final UserInfoRepository userInfoRepository;

    private final MutableLiveData<Integer> currentOptionPosition = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLastQuestionLiveData = new MutableLiveData<>();

    private final MutableLiveData<Integer> rightAnswerCountLiveData = new MutableLiveData<>();

    public CourseActivityViewModel(@NonNull Application application) {
        super(application);
        userProgressRepository = new UserProgressRepository(application);
        userInfoRepository = new UserInfoRepository(application);
    }

    // update the value in a particular course
    public void updateVideoWatched(String courseName) {
        userProgressRepository.updateVideoWatched(courseName);
    }

    // updates overall value
    public void updateTotalVideoWatched() {
        userInfoRepository.updateUserVideoTotal();
    }

    /*
     * Quiz
     * */
    public LiveData<DataSnapshot> getQuizQuestions(String courseName, String sectionName) {
        DatabaseReference QUIZ_REF = FirebaseDatabase.getInstance().getReference("Sub Section")
                .child(sectionName + " Section")
                .child(courseName)
                .child("Quiz");

        return new FirebaseQueryLiveData(QUIZ_REF);
    }

    public LiveData<DataSnapshot> getQuizOptions(String courseName, String sectionName, String question) {
        DatabaseReference QUIZ_OPTIONS_REF = FirebaseDatabase.getInstance().getReference("Sub Section")
                .child(sectionName + " Section")
                .child(courseName)
                .child("Quiz")
                .child(question);

        return new FirebaseQueryLiveData(QUIZ_OPTIONS_REF);
    }

    // to observe the current quiz position in QuizFragment
    public MutableLiveData<Integer> currentOptionPosition() {
        return currentOptionPosition;
    }

    public MutableLiveData<Integer> getRightAnswerCount() {
        return rightAnswerCountLiveData;
    }

    public MutableLiveData<Boolean> getIsLastQuestion() {
        return isLastQuestionLiveData;
    }

    /*
     * Video from list in VideoFragment
     * */

    public MutableLiveData<String> getCurrentVideoFromList() {
        return currentVideoFromList;
    }


}
