package com.example.wordblocks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {
    private OnWordInteractionListener onWordInteractionListener;
    private RecyclerView recyclerView;
    private ArrayList<Word> words;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        Intent intent = getIntent();
        String[] translated = intent.getStringArrayExtra(MainActivity.TRANSLATED);
        String[] traslations = intent.getStringArrayExtra(MainActivity.TRANSLATIONS);

        ArrayList<Word> wordArrayList = new ArrayList<>();
        for (int i = 0; i < translated.length; i++) {
            wordArrayList.add(new Word(translated[i].toLowerCase(), traslations[i].toLowerCase()));
        }

        recyclerView = findViewById(R.id.words_list);
        Context context = getApplicationContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setAdapter(new WordRecyclerViewAdapter(wordArrayList, onWordInteractionListener));

      /*  if (context instanceof OnWordInteractionListener) {
            onWordInteractionListener = (OnWordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }*/
    }



    public interface OnWordInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Word item);
    }

}
