package com.example.wordblocks;

import android.arch.lifecycle.ViewModel;
import android.content.ClipData;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final ArrayList<String> arrayList = new ArrayList<>();

    public void select(String item) {
        arrayList.add(item);
    }

    public ArrayList<String> getSelected() {
        return arrayList;
    }
}

