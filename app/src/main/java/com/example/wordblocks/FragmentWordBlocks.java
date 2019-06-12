package com.example.wordblocks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class FragmentWordBlocks extends Fragment {
    private static final String ARG_BLOCK_ITEMS = "blocks";
    private ArrayList<WordBlock> localWordBlocks;
    private OnListFragmentInteractionListener mListener;


    public FragmentWordBlocks() { }

    public static FragmentWordBlocks newInstance(ArrayList<WordBlock> wordBlocks) {
        FragmentWordBlocks fragment = new FragmentWordBlocks();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BLOCK_ITEMS, wordBlocks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            localWordBlocks = (ArrayList<WordBlock>) getArguments().getSerializable(ARG_BLOCK_ITEMS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_blocks_list, container, false);
        RecyclerView recyclerView;
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new WordBlockRecyclerViewAdapter(localWordBlocks, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(WordBlock item);
    }
}
