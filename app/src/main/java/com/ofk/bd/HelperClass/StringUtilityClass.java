package com.ofk.bd.HelperClass;

import android.content.Context;

import com.ofk.bd.R;

public class StringUtilityClass {

    Context context;

    public StringUtilityClass(Context context) {
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
}
