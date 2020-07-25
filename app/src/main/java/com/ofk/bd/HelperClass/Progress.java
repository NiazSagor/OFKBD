package com.ofk.bd.HelperClass;

public class Progress {
    String subjectTitle;
    int subjectProgress;

    public Progress(String subjectTitle, int subjectProgress) {
        this.subjectTitle = subjectTitle;
        this.subjectProgress = subjectProgress;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getSubjectProgress() {
        return subjectProgress;
    }

    public void setSubjectProgress(int subjectProgress) {
        this.subjectProgress = subjectProgress;
    }
}
