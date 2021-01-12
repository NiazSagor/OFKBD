package com.ofk.bd.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.Interface.ActivityPicLoadCallback;
import com.ofk.bd.Interface.CategoryCallback;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.Interface.VideoLoadCallback;
import com.ofk.bd.Model.Activity;
import com.ofk.bd.Model.Course;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.Video;
import com.ofk.bd.Repository.CommonRepository;
import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivityViewModel";

    // sql lite repo
    UserProgressRepository userProgressRepository;

    //
    CommonRepository commonRepository;

    // already enrolled courses in offline db
    LiveData<List<SectionCourseNameTuple>> enrolledCoursesFromOfflineDb;

    LiveData<List<SectionCourseTuple>> combinedList;

    // course list
    MutableLiveData<List<Course>> allCategoriesLiveData = new MutableLiveData<>();

    //blogList
    MutableLiveData<List<Course>> blogCategoriesLiveData = new MutableLiveData<>();

    /*
     *
     * User info
     *
     *
     * */

    UserInfoRepository userInfoRepository;

    LiveData<UserInfo> userInfoLiveData;
    LiveData<UserInfo> userInfoLiveData2;

    LiveData<Integer> userCourseCompleted;

    MutableLiveData<Integer> currentIndexOnBadge = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        userProgressRepository = new UserProgressRepository(application);
        commonRepository = new CommonRepository(MyApp.executorService);
        enrolledCoursesFromOfflineDb = userProgressRepository.getCourseEnrolled();
        combinedList = userProgressRepository.getCombinedSectionCourseList();

        userInfoRepository = new UserInfoRepository(application);
        userInfoLiveData = userInfoRepository.getUserInfoLiveData();
        userInfoLiveData2 = userInfoRepository.getUserInfoLiveData();
        userCourseCompleted = userInfoRepository.getUserCurrentCourseCompleted();

        doSomeWorks();
    }

    private void doSomeWorks() {
        List<Course> list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.add(new Course("Arts", "আর্টস"));
                list.add(new Course("Calligraphy", "ক্যালিগ্রাফি"));
                list.add(new Course("Case Solving", "কেইস সল্ভিং"));
                list.add(new Course("Craft", "ক্রাফট"));
                list.add(new Course("Critical Thinking", "ক্রিটিকাল থিংকিং"));
                list.add(new Course("Digital Painting", "ডিজিটাল পেইন্টিং"));
                list.add(new Course("Guitar", "গিটার"));
                list.add(new Course("Programming", "প্রোগ্রামিং"));
                list.add(new Course("Robotics", "রোবোটিক্স"));
            }
        }).start();
        allCategoriesLiveData.setValue(list);
    }

    /*
     * get all categories generating at run time
     * */

    public MutableLiveData<List<Course>> getAllCategoriesLiveData() {
        return allCategoriesLiveData;
    }

    /*
     * get all blog categories generating at run time
     * */

    public MutableLiveData<List<Course>> getBlogCategoriesLiveData() {
        commonRepository.getBlogCategories(new CategoryCallback() {
            @Override
            public void onCategoryCallback(List<Course> categories) {
                blogCategoriesLiveData.postValue(categories);
            }
        });
        return blogCategoriesLiveData;
    }

    // not from server
    public LiveData<List<SectionCourseTuple>> getCombinedList() {
        return combinedList;
    }

    public LiveData<List<SectionCourseNameTuple>> getEnrolledCoursesFromOfflineDb() {
        return enrolledCoursesFromOfflineDb;
    }

    /*******************UPDATE VIDEO COUNT IN USER PROGRESS TABLE**********************/

    public void updateTotalVideoCourse(String courseName, int count) {
        userProgressRepository.updateVideoCount(count, courseName);
    }

    public LiveData<Boolean> isFirstCourseCompleted(){
        return userProgressRepository.getIsFirstCourseCompleted();
    }

    /*
     *
     *
     * *************************User Info Table Query**************************
     *
     *
     * */


    public void updateUserInfo(String text, String entry) {
        userInfoRepository.updateUserInfo(text, entry);
    }

    public void insert(UserInfo userInfo) {
        userInfoRepository.insert(userInfo);
    }

    public LiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public LiveData<UserInfo> getUserInfoLiveData2() {
        return userInfoLiveData2;
    }

    public LiveData<Integer> getUserCourseCompleted() {
        return userCourseCompleted;
    }

    public MutableLiveData<Integer> getCurrentIndexOnBadge() {
        return currentIndexOnBadge;
    }

    public void postData(UserInfo userInfo) {
        userInfoRepository.postData(userInfo);
    }

    /*
     * Cloud operations
     *
     * */

    /*
     * get activity photos from cloud
     * */


    private final MutableLiveData<List<Activity>> activityPicLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Activity>> getActivityPicLiveData() {
        commonRepository.getActivityPhotos(new ActivityPicLoadCallback() {
            @Override
            public void onPicLoadCallback(List<Activity> activityPics) {
                activityPicLiveData.postValue(activityPics);
            }
        }, "Activity Pics");

        return activityPicLiveData;
    }

    /*
     * get field works photos from cloud
     * */

    private final MutableLiveData<List<Activity>> fieldWorkPicLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Activity>> getFieldWorkLiveData() {

        commonRepository.getActivityPhotos(new ActivityPicLoadCallback() {
            @Override
            public void onPicLoadCallback(List<Activity> activityPics) {
                fieldWorkPicLiveData.postValue(activityPics);
            }
        }, "Field Work Pics");

        return fieldWorkPicLiveData;
    }

    /*
     * get activity videos from cloud
     * */

    private final MutableLiveData<List<Video>> activityVideoLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Video>> getActivityVideoLiveData() {

        commonRepository.getActivityVideo(new VideoLoadCallback() {
            @Override
            public void onLoadCallback(List<Video> list) {
                activityVideoLiveData.postValue(list);
            }
        });

        return activityVideoLiveData;
    }

    /*
     * get all the courses available in a category from cloud
     * */

    private final MutableLiveData<List<DisplayCourse>> randomCourseLiveData = new MutableLiveData<>();

    public MutableLiveData<List<DisplayCourse>> getRandomCourseLiveData(String sectionName) {
        commonRepository.getAllCoursesFromSection(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {
                randomCourseLiveData.postValue(courses);
            }
        }, sectionName);

        return randomCourseLiveData;
    }
}
