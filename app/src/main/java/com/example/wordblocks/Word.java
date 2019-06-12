package com.example.wordblocks;

import java.io.Serializable;

public class Word implements Serializable{
    public final String word, translation;

    public Word(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    @Override
    public String toString() {
        return word;
    }


}
