package com.example.wordblocks;

import java.io.Serializable;

public class WordBlock  implements  Serializable{
    public final String name, description, language, id, topic;
    public final String data;
    public final int cost;

    public WordBlock(String id, String name, String description, String topic, int cost, String language, String data) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.topic = topic;
        this.cost = cost;
        this.language = language;
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }
}