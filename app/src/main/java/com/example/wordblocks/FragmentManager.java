package com.example.wordblocks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentManager extends Fragment {
    private static final String ARG_BLOCKS = "blocks";
    private static final String ARG_LANGUAGES = "languages";
    private ArrayList<WordBlock> wordBlocks;
    private ArrayList<String> languages;
    private Spinner spinnerCounter, spinnerLanguages, spinnerWordBlocks;
    private Button buttonSave;
    private Context context;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedLang = sharedPreferences.getString(MainActivity.SHARED_LANGUAGE_KEY, MainActivity.DEFAULT_VALUE);
        String savedBlock = sharedPreferences.getString(MainActivity.CHOSED_WORD_BLOCK_KEY, MainActivity.DEFAULT_VALUE);
        int savedCount = sharedPreferences.getInt(MainActivity.DAILY_WORDS_COUNT_KEY, 0);

        context = inflater.getContext();
        buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new OnSaveClick());

        spinnerCounter = view.findViewById(R.id.spinner_counter);
        spinnerLanguages = view.findViewById(R.id.spinner_languages);
        spinnerWordBlocks = view.findViewById(R.id.spinner_word_blocks);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, languages);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(spinnerArrayAdapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.counters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCounter.setAdapter(adapter);
        ArrayAdapter<WordBlock> wordsArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, wordBlocks);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWordBlocks.setAdapter(wordsArrayAdapter);

        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).equals(savedLang)) {
                spinnerLanguages.setSelection(i);
            }
        }

        for (int i = 0; i < wordBlocks.size(); i++) {
            if (wordBlocks.get(i).name.equals(savedBlock)) {
                spinnerWordBlocks.setSelection(i);
            }
        }
        spinnerCounter.setSelection(savedCount);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    class OnSaveClick implements View.OnClickListener {
        SharedPreferences sharedPreferences;
        @Override
        public void onClick(View v) {
            sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MainActivity.SHARED_LANGUAGE_KEY, (String) spinnerLanguages.getSelectedItem());
            editor.putInt(MainActivity.DAILY_WORDS_COUNT_KEY, spinnerCounter.getSelectedItemPosition());
            WordBlock wordBlock = (WordBlock) spinnerWordBlocks.getSelectedItem();
            editor.putString(MainActivity.CHOSED_WORD_BLOCK_KEY, wordBlock.name);
            editor.commit();
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
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
