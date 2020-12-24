package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.Model.SectionVideo;
import com.ofk.bd.Interface.QuizOptionLoadCallback;
import com.ofk.bd.Interface.QuizQuestionLoadCallback;
import com.ofk.bd.Interface.SectionVideoLoadCallback;
import com.ofk.bd.Repository.CommonRepository;
import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.List;

public class CourseActivityViewModel extends AndroidViewModel {

    private static final String TAG = "CourseActivityViewModel";

    private final MutableLiveData<String> currentVideoFromList = new MutableLiveData<>();

    private final UserProgressRepository userProgressRepository;

    private final UserInfoRepository userInfoRepository;

    private final CommonRepository commonRepository;

    private final MutableLiveData<Integer> currentOptionPosition = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLastQuestionLiveData = new MutableLiveData<>();

    private final MutableLiveData<Integer> rightAnswerCountLiveData = new MutableLiveData<>();

    public CourseActivityViewModel(@NonNull Application application) {
        super(application);
        userProgressRepository = new UserProgressRepository(application);
        commonRepository = new CommonRepository(MyApp.executorService);
        userInfoRepository = new UserInfoRepository(application);
    }

    /*
     * Update the value of watched video by the user in a particular course
     * */

    public void updateVideoWatched(String courseName) {
        userProgressRepository.updateVideoWatched(courseName);
    }

    /*
     * Updates value of total videos watched by the user
     * */

    public void updateTotalVideoWatched() {
        userInfoRepository.updateUserVideoTotal();
    }

    /*
     * Getter method to get the quiz Questions
     * */

    private final MutableLiveData<List<String>> quizQuestionsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<String>> getQuizQuestions(String courseName, String sectionName) {

        commonRepository.getQuizQuestions(new QuizQuestionLoadCallback() {
            @Override
            public void onQuizQuestionLoadCallback(List<String> quizQuestions) {
                quizQuestionsLiveData.postValue(quizQuestions);
            }
        }, sectionName, courseName);

        return quizQuestionsLiveData;
    }


    /*
     * Getter method to get teh quiz Options
     * */

    private final MutableLiveData<List<String>> quizOptionsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<String>> getQuizOptions(String courseName, String sectionName, String question) {

        commonRepository.getQuizOptions(new QuizOptionLoadCallback() {
            @Override
            public void onQuizOptionLoadCallback(List<String> quizOptions) {
                quizOptionsLiveData.postValue(quizOptions);
            }
        }, sectionName, courseName, question);

        return quizOptionsLiveData;
    }

    /*
     * To observe the current quiz position in QuizFragment
     * */

    public MutableLiveData<Integer> currentOptionPosition() {
        return currentOptionPosition;
    }

    /*
     * To observe the correct answer position
     * */

    public MutableLiveData<Integer> getRightAnswerCount() {
        return rightAnswerCountLiveData;
    }

    /*
     * To observe the if the user has come to the last question of the quiz
     * */

    public MutableLiveData<Boolean> getIsLastQuestion() {
        return isLastQuestionLiveData;
    }

    /*
     * Video from list in VideoFragment
     * */

    public MutableLiveData<String> getCurrentVideoFromList() {
        return currentVideoFromList;
    }

    /*
     * Sections with their videos list
     * */

    private final MutableLiveData<List<SectionVideo>> sectionVideoLiveData = new MutableLiveData<>();


    /*
     * Total videos on a particular course
     * */

    private final MutableLiveData<Integer> totalVideosOnTakenCourse = new MutableLiveData<>();

    public MutableLiveData<List<SectionVideo>> getSectionWithVideos(String sectionName, String courseName) {
        commonRepository.getSectionWithVideos(new SectionVideoLoadCallback() {
            @Override
            public void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos) {
                sectionVideoLiveData.postValue(sectionVideoList);
                totalVideosOnTakenCourse.postValue(totalVideos);
            }
        }, sectionName, courseName);

        return sectionVideoLiveData;
    }

    /*
     * Getter method for the total videos in a particular course
     * */
    public MutableLiveData<Integer> getTotalVideosOnTakenCourse() {
        return totalVideosOnTakenCourse;
    }

    /*
     * Updates total videos of a particular course
     * */
    public void updateTotalVideoCourse(String courseName, int count) {
        userProgressRepository.updateVideoCount(count, courseName);
    }
}
