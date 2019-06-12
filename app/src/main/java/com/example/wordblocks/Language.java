package com.example.wordblocks;

import java.io.Serializable;

public class Language implements Serializable {
    private final String language, key;

    public Language(String language, String key) {
        this.language = language;
        this.key = key;
    }

    public String getLanguage() {
        return language;
    }

    public String getKey() {
        return key;
    }
}
