package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivityViewModel";

    // sql lite repo
    UserProgressRepository repository;

    // already enrolled courses in offline db
    LiveData<List<SectionCourseNameTuple>> enrolledCoursesFromOfflineDb;

    LiveData<List<SectionCourseTuple>> combinedList;

    // course list
    MutableLiveData<List<Course>> listMutableLiveData = new MutableLiveData<>();

    //blogList
    MutableLiveData<List<Course>> blogListMutableLiveData = new MutableLiveData<>();

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

        repository = new UserProgressRepository(application);
        enrolledCoursesFromOfflineDb = repository.getCourseEnrolled();
        combinedList = repository.getCombinedSectionCourseList();

        userInfoRepository = new UserInfoRepository(application);
        userInfoLiveData = userInfoRepository.getUserInfoLiveData();
        userInfoLiveData2 = userInfoRepository.getUserInfoLiveData();
        userCourseCompleted = userInfoRepository.getUserCurrentCourseCompleted();

        createCourseList();
        createBlogList();
    }

    /*******************OFFLINE QUERY**********************/

    // not from server
    private void createCourseList() {
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

        listMutableLiveData.setValue(list);
    }

    // get all the sections
    public MutableLiveData<List<Course>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    private void createBlogList() {
        List<Course> blogList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                blogList.add(new Course("অনুপ্রেরণামূলক"));
                blogList.add(new Course("গল্প"));
                blogList.add(new Course("টিপস এন্ড ট্রিকস"));
                blogList.add(new Course("দক্ষতা উন্নয়নমূলক"));
                blogList.add(new Course("সচেতনতামূলক"));
                blogList.add(new Course("ইংরেজি"));
            }
        }).start();

        blogListMutableLiveData.setValue(blogList);
    }

    public MutableLiveData<List<Course>> getBlogListMutableLiveData() {
        return blogListMutableLiveData;
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
        repository.updateVideoCount(count, courseName);
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

    public LiveData<DataSnapshot> getActivityPicLiveData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Activity Pics");
        return new FirebaseQueryLiveData(db);
    }

    public LiveData<DataSnapshot> getFieldWorkLiveData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Field Work Pics");
        return new FirebaseQueryLiveData(db);
    }

    public LiveData<DataSnapshot> getActivityVideoLiveData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Activity Videos");
        return new FirebaseQueryLiveData(db);
    }

    public LiveData<DataSnapshot> getRandomCourseLiveData(String sectionName) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Section").child(sectionName);
        return new FirebaseQueryLiveData(db);
    }
}
