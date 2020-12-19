package com.ofk.bd.HelperClass;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static String courseToDisplay = "";
    public static String courseHeadline = "";
    public static List<SectionVideo> sectionVideoList = new ArrayList<>();
    public static float ratio = 0;
    public static UserForFirebase user;
    public static String videoId = "";
    public static MutableLiveData<DataSnapshot> liveData = new MutableLiveData<>();
    public static int CORRECT_ANSWER_COUNT = 1;
}
