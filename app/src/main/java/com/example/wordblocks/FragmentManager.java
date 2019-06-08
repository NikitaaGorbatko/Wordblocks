package com.example.wordblocks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentManager extends Fragment {
    private static final String ARG_BLOCKS = "blocks";
    private static final String ARG_LANGUAGES = "languages";
    private ArrayList<WordBlock> wordBlocks;
    private ArrayList<String> languages;
    private Spinner spinnerCounter, spinnerLanguages, spinnerWordBlocks;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public FragmentManager() { }

    public static FragmentManager newInstance(ArrayList<WordBlock> blocks, ArrayList<String> langs) {
        FragmentManager fragment = new FragmentManager();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BLOCKS, blocks);
        args.putStringArrayList(ARG_LANGUAGES, langs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordBlocks = (ArrayList<WordBlock>) getArguments().getSerializable(ARG_BLOCKS);
            languages = getArguments().getStringArrayList(ARG_LANGUAGES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        context = inflater.getContext();
        spinnerCounter = view.findViewById(R.id.spinner_counter);
        spinnerLanguages = view.findViewById(R.id.spinner_languages);
        spinnerWordBlocks = view.findViewById(R.id.spinner_word_blocks);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.counters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCounter.setAdapter(adapter);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
