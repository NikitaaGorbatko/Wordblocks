package com.example.wordblocks;

import java.io.Serializable;

public class Word implements Serializable{
    private final String word, translation;

    public Word(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public String toString() {
        return word;
    }


}
