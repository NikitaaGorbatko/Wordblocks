package com.example.wordblocks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentTranslator extends Fragment {
    private EditText wordEditText, translationEditText;
    private Button translateButton, saveTranslationButton;
    private YandexApi yandexApi;
    private Retrofit retrofit;
    private Spinner spinner;
    private Context context;
    private ArrayList<String> internalLanguagesList;
    private static final String ARG_LANGUAGES = "languages";

    public FragmentTranslator() { }



    public static FragmentTranslator newInstance(ArrayList<String> languagesList) {
        FragmentTranslator fragment = new FragmentTranslator();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LANGUAGES, languagesList);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            internalLanguagesList = (ArrayList<String>) getArguments().getSerializable(ARG_LANGUAGES);
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            yandexApi = retrofit.create(YandexApi.class);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translator, container, false);
        context = inflater.getContext();
        spinner = fragmentView.findViewById(R.id.spinner_languages);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, internalLanguagesList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        wordEditText = fragmentView.findViewById(R.id.edit_enter_text);
        translationEditText = fragmentView.findViewById(R.id.edit_translation_text);
        translateButton = fragmentView.findViewById(R.id.button_translate);
        saveTranslationButton = fragmentView.findViewById(R.id.button_save_translation);
        translateButton.setOnClickListener(new OnTranslateClick());
        return fragmentView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class OnTranslateClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            yandexApi.listRepos(wordEditText.getText().toString()).enqueue(new Callback<PostModel>() {
                @Override
                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                    String outString = "";
                    if (response.body() != null) {
                        outString = response.body().getText().get(0);
                    }
                    translationEditText.setText(outString);
                }

                @Override
                public void onFailure(Call<PostModel> call, Throwable t) {
                    Toast.makeText(context, call.toString() + " " + t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
