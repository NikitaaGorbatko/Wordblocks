package com.example.wordblocks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wordblocks.FragmentWordBlocks.OnListFragmentInteractionListener;
import java.util.ArrayList;

public class WordBlockRecyclerViewAdapter extends RecyclerView.Adapter<WordBlockRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<WordBlock> wordBlocksArrayList;
    private final OnListFragmentInteractionListener fragmentInteractionListener;

    public WordBlockRecyclerViewAdapter(ArrayList<WordBlock> blocks, OnListFragmentInteractionListener listener) {
        wordBlocksArrayList = blocks;
        fragmentInteractionListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_word_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        WordBlock wBlock = holder.wordBlockHolderItem = wordBlocksArrayList.get(position);
        holder.nameView.setText(wBlock.getName());
        holder.descriptionView.setText(wBlock.getDescription());
        //holder.topicView.setText(wBlock.topic);
        holder.languageView.setText(wBlock.getLanguage());
        holder.costView.setText(wBlock.getCost() + "");
        holder.viewHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != fragmentInteractionListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    fragmentInteractionListener.onListFragmentInteraction(holder.wordBlockHolderItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordBlocksArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View viewHolderView;
        public final TextView nameView, descriptionView, costView, languageView;
        public WordBlock wordBlockHolderItem;

        public ViewHolder(View view) {
            super(view);
            viewHolderView = view;
            nameView = view.findViewById(R.id.name_view);
            descriptionView = view.findViewById(R.id.description_view);
            //topicView = view.findViewById(R.id.topic_view);
            languageView = view.findViewById(R.id.language_view);
            costView = view.findViewById(R.id.cost_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + descriptionView.getText() + "'";
        }
    }
}
