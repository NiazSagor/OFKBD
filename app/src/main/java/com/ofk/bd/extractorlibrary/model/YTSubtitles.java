package com.ofk.bd.extractorlibrary.model;

public class YTSubtitles {
    String baseUrl;
    SubtitleName name;
    String languageCode;
    boolean isTranslatable;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public SubtitleName getName() {
        return name;
    }

    public void setName(SubtitleName name) {
        this.name = name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setIsTranslatable(boolean isTranslatable) {
        this.isTranslatable = isTranslatable;
    }

    public boolean isTranslatable() {
        return isTranslatable;
    }

    public class SubtitleName {
        String simpleText;

        public String getSimpleText() {
            return simpleText;
        }

        public void setSimpleText(String simpleText) {
            this.simpleText = simpleText;
        }
    }
}
