package com.example.wordblocks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wordblocks.ItemFragment.OnListFragmentInteractionListener;
import com.example.wordblocks.dummy.DummyItem;
import java.util.ArrayList;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(ArrayList<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameView.setText(mValues.get(position).name);
        holder.descriptionView.setText(mValues.get(position).description);
        holder.topicView.setText(mValues.get(position).topic);
        holder.languageView.setText(mValues.get(position).language);
        holder.costView.setText(mValues.get(position).cost + "");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView, descriptionView, costView, languageView, topicView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = view.findViewById(R.id.name_view);
            descriptionView = view.findViewById(R.id.description_view);
            topicView = view.findViewById(R.id.topic_view);
            languageView = view.findViewById(R.id.language_view);
            costView = view.findViewById(R.id.cost_view);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + descriptionView.getText() + "'";
        }
    }
}
