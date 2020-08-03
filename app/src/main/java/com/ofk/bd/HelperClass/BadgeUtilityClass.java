package com.ofk.bd.HelperClass;

public class BadgeUtilityClass {

    int courseCompleted;

    int currentBadgeIconIndex;
    String currentLevel_currentBadgeName;

    public BadgeUtilityClass(int courseCompleted) {
        this.courseCompleted = courseCompleted;
        calculate();
    }

    private void calculate() {
        if (courseCompleted == 1 || courseCompleted == 0) {
            currentLevel_currentBadgeName = "Apprentice " + 1;
            currentBadgeIconIndex = 0;
        } else if (courseCompleted == 2) {
            currentLevel_currentBadgeName = "Apprentice " + 2;
            currentBadgeIconIndex = 1;
        } else if (courseCompleted == 3) {
            currentLevel_currentBadgeName = "Apprentice " + 3;
            currentBadgeIconIndex = 2;
        } else if (courseCompleted >= 4 && courseCompleted <= 6) {
            currentLevel_currentBadgeName = "Journeyman " + 1;
            currentBadgeIconIndex = 3;
        } else if (courseCompleted >= 7 && courseCompleted <= 8) {
            currentLevel_currentBadgeName = "Journeyman " + 2;
            currentBadgeIconIndex = 4;
        } else if (courseCompleted >= 9 && courseCompleted <= 10) {
            currentLevel_currentBadgeName = "Journeyman " + 3;
            currentBadgeIconIndex = 5;
        } else if (courseCompleted >= 11 && courseCompleted <= 13) {
            currentLevel_currentBadgeName = "Master " + 1;
            currentBadgeIconIndex = 6;
        } else if (courseCompleted >= 14 && courseCompleted <= 16) {
            currentLevel_currentBadgeName = "Master " + 2;
            currentBadgeIconIndex = 7;
        } else if (courseCompleted >= 17 && courseCompleted <= 19) {
            currentLevel_currentBadgeName = "Master " + 3;
            currentBadgeIconIndex = 8;
        } else if (courseCompleted >= 20 && courseCompleted <= 22) {
            currentLevel_currentBadgeName = "Grand Master " + 1;
            currentBadgeIconIndex = 9;
        } else if (courseCompleted >= 23 && courseCompleted <= 25) {
            currentLevel_currentBadgeName = "Grand Master " + 2;
            currentBadgeIconIndex = 10;
        } else if (courseCompleted >= 26 && courseCompleted <= 28) {
            currentLevel_currentBadgeName = "Grand Master " + 3;
            currentBadgeIconIndex = 11;
        } else if (courseCompleted >= 29 && courseCompleted <= 32) {
            currentLevel_currentBadgeName = "Super Kids " + 1;
            currentBadgeIconIndex = 12;
        } else if (courseCompleted >= 33 && courseCompleted <= 37) {
            currentLevel_currentBadgeName = "Super Kids " + 2;
            currentBadgeIconIndex = 13;
        } else if (courseCompleted >= 38) {
            currentLevel_currentBadgeName = "Super Kids " + 3;
            currentBadgeIconIndex = 14;
        }
    }

    public int getCurrentBadgeIconIndex() {
        return currentBadgeIconIndex;
    }

    public String getCurrentLevel_currentBadgeName() {
        return currentLevel_currentBadgeName;
    }
}
