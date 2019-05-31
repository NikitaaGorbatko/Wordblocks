package com.example.wordblocks.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    private static final int COUNT = 25;

    DummyContent(ArrayList<DummyItem> dummyItemArrayList)  {
        for (int i = 1; i <= COUNT; i++) {
            String[] as = {"f","f"};
            //addItem(createDummyItem(i + "", "asdf", " fff", 2, "fff", as));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(String id, String name, String description, String topic, int cost, String language, String data) {
        return new DummyItem(id, name, description, topic, cost, language, data);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }


}
