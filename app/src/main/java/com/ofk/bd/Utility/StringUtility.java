package com.ofk.bd.Utility;

import android.content.Context;

import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.R;

public class StringUtility {

    Context context;

    private static final String[] level_names = {"Apprentice 1", "Apprentice 2", "Apprentice 3",
            "Journeyman 1", "Journeyman 2", "Journeyman 3",
            "Master 1", "Master 2", "Master 3",
            "Grand Master 1", "Grand Master 2", "Grand Master 3",
            "Super Kids 1", "Super Kids 2", "Super Kids 3"};

    public StringUtility(Context context) {
        this.context = context;
    }

    public static String getSting(int number) {
        return "" + number;
    }

    public String getSectionHeadline(String section) {

        switch (section) {
            case "Arts Section":
                return context.getResources().getString(R.string.artCourseHeadline);
            case "Robotics Section":
                return context.getResources().getString(R.string.roboticsCourseHeadline);
            case "Calligraphy Section":
                return context.getResources().getString(R.string.calligraphyCourseHeadline);
            case "Case Solving Section":
                return context.getResources().getString(R.string.caseCourseHeadline);
            case "Craft Section":
                return context.getResources().getString(R.string.craftCourseHeadline);
            case "Critical Thinking Section":
                return context.getResources().getString(R.string.criticalCourseHeadline);
            case "Digital Painting Section":
                return context.getResources().getString(R.string.paintingCourseHeadline);
            case "Guitar Section":
                return context.getResources().getString(R.string.guitarCourseHeadline);
            case "Programming Section":
                return context.getResources().getString(R.string.programmingCourseHeadline);

            default:
                return context.getResources().getString(R.string.artCourseHeadline);
        }
    }


    public static String getCurrentLevelName(int index) {
        return level_names[index];
    }

    public static String getGreetingMessage(int timeOfDay) {
        if (timeOfDay >= 0 && timeOfDay < 12) {
            return ("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return ("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            return ("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return ("Good Night");
        }
        return "How are you";
    }

    public static String getCourseCompletionPercentage(int videoWatched, int totalVideo) {
        double amount = (double) videoWatched / totalVideo;
        double res = amount * 100;
        return (int) res + "%";
    }
}
