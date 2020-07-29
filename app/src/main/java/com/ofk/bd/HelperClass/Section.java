package com.ofk.bd.HelperClass;

public class Section {
    String sectionName;
    boolean isExpanded;

    public Section() {
    }

    public Section(String sectionName) {
        this.sectionName = sectionName;
        isExpanded = false;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
