package com.example.wordblocks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.WordsHolder> {
    private final ArrayList<Word> words;

    public WordRecyclerViewAdapter(ArrayList<Word> words) {
        this.words = words;
    }

    @NonNull
    @Override
    public WordsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_item, viewGroup, false);
        return new WordRecyclerViewAdapter.WordsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordRecyclerViewAdapter.WordsHolder viewHolder, int position) {
        Word word = viewHolder.word = words.get(position);
        viewHolder.translated.setText(word.getWord());
        viewHolder.translation.setText(word.getTranslation());
    }


    @Override
    public int getItemCount() {
        return words.size();
    }

    public class WordsHolder extends RecyclerView.ViewHolder {
        public final View viewHolderView;
        public final TextView translation, translated;
        public Word word;

        public WordsHolder(View view) {
            super(view);
            viewHolderView = view;
            translation = view.findViewById(R.id.word_text_view);
            translated = view.findViewById(R.id.translation_text_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + translation.getText() + "'";
        }
    }
}