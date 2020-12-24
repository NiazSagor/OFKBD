package com.ofk.bd.Utility;

import android.content.Context;

import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.R;

public class StringUtility {

    Context context;

    private static final String[] level_names = {"Apprentice 1", "Apprentice 2", "Apprentice 3",
            "Journeyman 1", "Journeyman 2", "Journeyman 3",
            "Master 1", "Master 2", "Master 3",
            "Grand Master 1", "Grand Master 2", "Grand Master 3",
            "Super Kids 1", "Super Kids 2", "Super Kids 3"};

    private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public StringUtility(Context context) {
        this.context = context;
    }

    public static String getString(long number) {
        return "" + number;
    }

    public String getSectionHeadline(String section) {

        switch (section) {
            case "Arts":
                return context.getResources().getString(R.string.artCourseHeadline);
            case "Robotics":
                return context.getResources().getString(R.string.roboticsCourseHeadline);
            case "Calligraphy":
                return context.getResources().getString(R.string.calligraphyCourseHeadline);
            case "Case Solving":
                return context.getResources().getString(R.string.caseCourseHeadline);
            case "Craft":
                return context.getResources().getString(R.string.craftCourseHeadline);
            case "Critical Thinking":
                return context.getResources().getString(R.string.criticalCourseHeadline);
            case "Digital Painting":
                return context.getResources().getString(R.string.paintingCourseHeadline);
            case "Guitar":
                return context.getResources().getString(R.string.guitarCourseHeadline);
            case "Programming":
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

    public static String getCourseCompletionPercentage(long videoWatched, long totalVideo) {
        double amount = (double) videoWatched / totalVideo;
        double res = amount * 100;
        return (int) res + "%";
    }

    public static String getFormattedDob(int year, int month, int day) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(day);

        if (day >= 10) {
            if (day % 10 == 1) {
                stringBuilder.append("st ");
            } else if (day % 10 == 2) {
                stringBuilder.append("nd ");
            } else if (day % 10 == 3) {
                stringBuilder.append("rd ");
            } else {
                stringBuilder.append("th ");
            }
        } else {
            if (day == 1) {
                stringBuilder.append("st ");
            } else if (day == 2) {
                stringBuilder.append("nd ");
            } else if (day == 3) {
                stringBuilder.append("rd ");
            } else {
                stringBuilder.append("th ");
            }
        }

        stringBuilder.append(months[month]);
        stringBuilder.append(" ");
        stringBuilder.append(year);

        return stringBuilder.toString();
    }

    public static boolean isUserInfoComplete(UserInfo userInfo) {
        if (userInfo != null) {
            return !userInfo.getUserEmail().equals("") && !userInfo.getUserClass().equals("") && !userInfo.getUserSchool().equals("") && !userInfo.getUserDOB().equals("") && !userInfo.getUserGender().equals("");
        }
        return false;
    }
}
