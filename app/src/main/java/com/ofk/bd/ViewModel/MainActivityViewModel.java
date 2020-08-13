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
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivityViewModel";
    /*
     *
     * User progress table
     *
     * */

    // sql lite repo
    UserProgressRepository repository;

    // already enrolled courses in offline db
    LiveData<List<String>> enrolledCoursesFromOfflineDb;

    LiveData<List<SectionCourseTuple>> combinedList;

    // course list
    MutableLiveData<List<Course>> listMutableLiveData = new MutableLiveData<>();

    //blogList
    MutableLiveData<List<Course>> blogListMutableLiveData = new MutableLiveData<>();

    // activity pic
    LiveData<DataSnapshot> activityPicLiveData;

    // field work pic
    LiveData<DataSnapshot> fieldWorkPicLiveData;

    // activity videos
    LiveData<DataSnapshot> activityVideoLiveData;

    // random course to display 1
    LiveData<DataSnapshot> randomCourseLiveData_1;

    // random course to display 2
    LiveData<DataSnapshot> randomCourseLiveData_2;

    // get enrolled courses
    LiveData<DataSnapshot> enrolledCourses;

    /*
     *
     * User info
     *
     *
     * */

    UserInfoRepository userInfoRepository;

    LiveData<UserInfo> userInfoLiveData;

    MutableLiveData<Integer> currentIndexOnBadge = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new UserProgressRepository(application);
        enrolledCoursesFromOfflineDb = repository.getCourseEnrolled();
        combinedList = repository.getCombinedSectionCourseList();

        userInfoRepository = new UserInfoRepository(application);
        userInfoLiveData = userInfoRepository.getUserInfoLiveData();

        Log.d(TAG, "MainActivityViewModel: ");
        

        createCourseList();
        createBlogList();
        getActivityPicFromDatabase();
        getFieldWorkPicFromDatabase();
        getActivityVideoFromDatabase();
        getRandomCourseToDisplay_1();
        getRandomCourseToDisplay_2();
    }

    /*******************SERVER QUERY**********************/

    // from server
    public void getEnrolledCourses(String userId) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Courses Enrolled");
        db.keepSynced(true);
        enrolledCourses = new FirebaseQueryLiveData(db);
    }

    // from server
    private void getRandomCourseToDisplay_1() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child("Robotics Section");
        db.keepSynced(true);
        randomCourseLiveData_1 = new FirebaseQueryLiveData(db);
    }

    // from server
    private void getRandomCourseToDisplay_2() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child("Arts Section");
        db.keepSynced(true);
        randomCourseLiveData_2 = new FirebaseQueryLiveData(db);
    }

    // from server
    private void getActivityVideoFromDatabase() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Activity Videos");
        db.keepSynced(true);
        activityVideoLiveData = new FirebaseQueryLiveData(db);
    }

    // from server
    private void getActivityPicFromDatabase() {
        DatabaseReference db_activity_pic = FirebaseDatabase.getInstance().getReference().child("Activity Pics");
        db_activity_pic.keepSynced(true);
        activityPicLiveData = new FirebaseQueryLiveData(db_activity_pic);
    }

    // from server
    private void getFieldWorkPicFromDatabase() {
        Log.d(TAG, "getFieldWorkPicFromDatabase: ");
        DatabaseReference db_activity_pic = FirebaseDatabase.getInstance().getReference().child("Field Work Pics");
        db_activity_pic.keepSynced(true);
        fieldWorkPicLiveData = new FirebaseQueryLiveData(db_activity_pic);
    }


    // not from server
    private void createCourseList() {
        List<Course> list = new ArrayList<>();

        list.add(new Course("Arts", "আর্টস"));
        list.add(new Course("Calligraphy", "ক্যালিগ্রাফি"));
        list.add(new Course("Case Solving", "কেইস সল্ভিং"));
        list.add(new Course("Craft", "ক্রাফট"));
        list.add(new Course("Critical Thinking", "ক্রিটিকাল থিংকিং"));
        list.add(new Course("Digital Painting", "ডিজিটাল পেইন্টিং"));
        list.add(new Course("Guitar", "গিটার"));
        list.add(new Course("Programming", "প্রোগ্রামিং"));
        list.add(new Course("Robotics", "রোবোটিক্স"));

        listMutableLiveData.setValue(list);
    }

    private void createBlogList() {
        List<Course> blogList = new ArrayList<>();

        blogList.add(new Course("অনুপ্রেরণামূলক"));
        blogList.add(new Course("গল্প"));
        blogList.add(new Course("টিপস এন্ড ট্রিকস"));
        blogList.add(new Course("দক্ষতা উন্নয়নমূলক"));
        blogList.add(new Course("সচেতনতামূলক"));
        blogList.add(new Course("ইংরেজি"));

        blogListMutableLiveData.setValue(blogList);
    }


    // from server
    public LiveData<DataSnapshot> getActivityPicLiveData() {
        return activityPicLiveData;
    }

    // from server
    public LiveData<DataSnapshot> getActivityVideoLiveData() {
        return activityVideoLiveData;
    }

    // from server
    public LiveData<DataSnapshot> getRandomCourseLiveData_1() {
        return randomCourseLiveData_1;
    }

    // from server
    public LiveData<DataSnapshot> getRandomCourseLiveData_2() {
        return randomCourseLiveData_2;
    }

    // from server
    public LiveData<DataSnapshot> getEnrolledCourses() {
        return enrolledCourses;
    }

    public LiveData<DataSnapshot> getFieldWorkPicLiveData() {
        return fieldWorkPicLiveData;
    }

    public MutableLiveData<List<Course>> getBlogListMutableLiveData() {
        return blogListMutableLiveData;
    }

    /*******************OFFLINE QUERY**********************/

    // get all the sections
    public MutableLiveData<List<Course>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    // not from server
    public LiveData<List<String>> getEnrolledCoursesFromOfflineDb() {
        return enrolledCoursesFromOfflineDb;
    }

    // not from server
    public LiveData<List<SectionCourseTuple>> getCombinedList() {
        return combinedList;
    }

    /*
     *
     *
     * *************************User Info Table Query**************************
     *
     *
     * */


    public void insert(UserInfo userInfo){
        userInfoRepository.insert(userInfo);
    }

    public LiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public MutableLiveData<Integer> getCurrentIndexOnBadge() {
        return currentIndexOnBadge;
    }
}
