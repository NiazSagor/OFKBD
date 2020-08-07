package com.ofk.bd.HelperClass;

public class Section {
    String sectionCode;
    String sectionName;
    boolean isExpanded;

    public Section() {
    }

    public Section(String sectionCode, String sectionName) {
        this.sectionCode = sectionCode;
        this.sectionName = sectionName;
    }

    public Section(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
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
