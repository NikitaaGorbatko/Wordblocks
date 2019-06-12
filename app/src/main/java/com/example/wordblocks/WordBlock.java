package com.example.wordblocks;

import java.io.Serializable;

public class WordBlock implements Serializable{
    private final String name, description, language, id, topic;
    private final String data;
    private final int cost;

    public WordBlock(String id, String name, String description, String topic, int cost, String language, String data) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.topic = topic;
        this.cost = cost;
        this.language = language;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name;
    }
}