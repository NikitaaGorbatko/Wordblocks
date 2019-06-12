package com.example.wordblocks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentTranslator extends Fragment {
    private EditText wordEditText, translationEditText;
    private YandexApi yandexApi;
    private Context context;
    private static final String ARG_LANGUAGES = "languages";

    public FragmentTranslator() { }

    public static FragmentTranslator newInstance(ArrayList<Language> languagesList) {
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
            Retrofit retrofit = new Retrofit.Builder()
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
        wordEditText = fragmentView.findViewById(R.id.edit_enter_text);
        translationEditText = fragmentView.findViewById(R.id.edit_translation_text);
        fragmentView.findViewById(R.id.button_translate).setOnClickListener(new OnTranslateClick());
        fragmentView.findViewById(R.id.button_save_translation).setOnClickListener(new OnSaveBtnClick());
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

    class OnSaveBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
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
