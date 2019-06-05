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
    private final WordsActivity.OnWordInteractionListener fragmentInteractionListener;

    public WordRecyclerViewAdapter(ArrayList<Word> words, WordsActivity.OnWordInteractionListener fragmentInteractionListener) {
        this.words = words;
        this.fragmentInteractionListener = fragmentInteractionListener;
    }

    @NonNull
    @Override
    public WordsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_item, viewGroup, false);
        return new WordRecyclerViewAdapter.WordsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordRecyclerViewAdapter.WordsHolder viewHolder, int position) {
        Word worda = viewHolder.word = words.get(position);
        viewHolder.translated.setText(worda.word);
        viewHolder.translation.setText(worda.translation);
        viewHolder.viewHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != fragmentInteractionListener) {
                    fragmentInteractionListener.onListFragmentInteraction(viewHolder.word);
                    //Notify the active callbacks interface (the activity, if the
                    //fragment is attached to one) that an item has been selected.
                    //fragmentInteractionListener.onListFragmentInteraction(viewHolder.word);
                }
            }
        });
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