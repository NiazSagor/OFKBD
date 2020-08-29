package com.ofk.bd.HelperClass;

import androidx.room.ColumnInfo;

public class SectionCourseNameTuple {
    @ColumnInfo(name = "sectionName")
    String sectionName;
    @ColumnInfo(name = "courseNameEnglish")
    String courseNameEnglish;

    public SectionCourseNameTuple(String sectionName, String courseNameEnglish) {
        this.sectionName = sectionName;
        this.courseNameEnglish = courseNameEnglish;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getCourseNameEnglish() {
        return courseNameEnglish;
    }

    public void setCourseNameEnglish(String courseNameEnglish) {
        this.courseNameEnglish = courseNameEnglish;
    }
}
