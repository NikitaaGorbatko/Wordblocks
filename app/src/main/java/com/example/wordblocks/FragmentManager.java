package com.example.wordblocks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentManager extends Fragment{
    private static final String ARG_BLOCKS = "blocks";
    private static final String ARG_LANGUAGES = "languages";
    private static final int words3 = 3;
    private static final int words5 = 5;
    private static final int words7 = 7;
    private static final int words10 = 10;
    private static final int words15 = 15;
    private ArrayList<WordBlock> wordBlocks;
    private ArrayList<String> languages = new ArrayList<>();
    private Spinner spinnerWordBlocks;
    private ListView listViewNative, listViewGoal;
    private RadioGroup radioGroup;
    private boolean saved = false;

    public FragmentManager() { }

    public static FragmentManager newInstance(ArrayList<WordBlock> blocks, ArrayList<Language> langs) {
        final FragmentManager fragment = new FragmentManager();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_BLOCKS, blocks);
        args.putSerializable(ARG_LANGUAGES, langs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordBlocks = (ArrayList<WordBlock>) getArguments().getSerializable(ARG_BLOCKS);
            ArrayList<Language> languagesArray = (ArrayList<Language>) getArguments().getSerializable(ARG_LANGUAGES);
            for (Language local : languagesArray) {
                languages.add(local.getLanguage());
            }

        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        final RadioButton button3 = view.findViewById(R.id.radioButton3);
        final RadioButton button5 = view.findViewById(R.id.radioButton5);
        final RadioButton button7 = view.findViewById(R.id.radioButton7);
        final RadioButton button10 = view.findViewById(R.id.radioButton10);
        final RadioButton button15 = view.findViewById(R.id.radioButton15);
        radioGroup = view.findViewById(R.id.words_amount_radio_group);
        listViewNative = view.findViewById(R.id.list_native_languages);
        listViewGoal = view.findViewById(R.id.list_goal_languages);
        listViewNative.setAdapter(new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_single_choice, languages));
        listViewGoal.setAdapter(new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_single_choice, languages));

        final SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        final String savedGoalLang = sharedPreferences.getString(MainActivity.SHARED_GOAL_LANGUAGE_KEY, MainActivity.DEFAULT_VALUE);
        final String savedNativeLang = sharedPreferences.getString(MainActivity.SHARED_NATIVE_LANGUAGE_KEY, MainActivity.DEFAULT_VALUE);
        final String savedBlock = sharedPreferences.getString(MainActivity.CHOSED_WORD_BLOCK_KEY, MainActivity.DEFAULT_VALUE);
        final int savedCount = sharedPreferences.getInt(MainActivity.DAILY_WORDS_COUNT_KEY, 0);
        final Context context = inflater.getContext();
        switch (savedCount) {
            case words3:
                button3.setChecked(true);
                break;
            case words5:
                button5.setChecked(true);
                break;
            case words7:
                button7.setChecked(true);
                break;
            case words10:
                button10.setChecked(true);
                break;
            case words15:
                button15.setChecked(true);
                break;
        }
        spinnerWordBlocks = view.findViewById(R.id.spinner_word_blocks);

        ArrayAdapter<WordBlock> wordsArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, wordBlocks);
        wordsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWordBlocks.setAdapter(wordsArrayAdapter);

        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).equals(savedGoalLang)) {
                listViewGoal.setItemChecked(i, true);
            }
        }

        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).equals(savedNativeLang)) {
                listViewNative.setItemChecked(i, true);
            }
        }

        for (int i = 0; i < wordBlocks.size(); i++) {
            if (wordBlocks.get(i).getName().equals(savedBlock)) {
                spinnerWordBlocks.setSelection(i);
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!saved)
            saved = saveState();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!saved)
            saved = saveState();
    }

    private boolean saveState() {
        final WordBlock wordBlock = (WordBlock) spinnerWordBlocks.getSelectedItem();
        final SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        int amount = 0;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButton3:
                amount = words3;
                break;
            case R.id.radioButton5:
                amount = words5;
                break;
            case R.id.radioButton7:
                amount = words7;
                break;
            case R.id.radioButton10:
                amount = words10;
                break;
            case R.id.radioButton15:
                amount = words15;
                break;
        }

        editor.putString(MainActivity.SHARED_GOAL_LANGUAGE_KEY, (String) listViewGoal.getItemAtPosition(listViewGoal.getCheckedItemPosition()));
        editor.putString(MainActivity.SHARED_NATIVE_LANGUAGE_KEY, (String) listViewNative.getItemAtPosition(listViewNative.getCheckedItemPosition()));
        editor.putInt(MainActivity.DAILY_WORDS_COUNT_KEY, amount);
        editor.putString(MainActivity.CHOSED_WORD_BLOCK_KEY, wordBlock.getName());
        editor.putString(MainActivity.DAILY_WORDS_SET_KEY, wordBlock.getData());
        editor.apply();

        final Intent intent = new Intent(getActivity().getApplicationContext(), WidgetWords.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(getActivity().getApplicationContext());
        final int[] ids = widgetManager.getAppWidgetIds(new ComponentName(getActivity().getApplicationContext(), WidgetWords.class));
        widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().getApplicationContext().sendBroadcast(intent);
        return true;
    }
}
