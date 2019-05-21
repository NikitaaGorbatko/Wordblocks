package com.example.wordblocks;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TranslatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TranslatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslatorFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText wordEditText, translationEditText;
    private Button translateButton, saveTranslationButton;
    private YandexApi yandexApi;
    private Retrofit retrofit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public TranslatorFragment() {
        // Required empty public constructor
    }

    public static TranslatorFragment newInstance(String word, String param2) {
        TranslatorFragment fragment = new TranslatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, word);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexApi = retrofit.create(YandexApi.class);
        /*
        SharedViewModel model;
        model = ViewModelProvider.of(getActivity()).get(SharedViewModel.class);
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translator, container, false);
        Context context = inflater.getContext();
        Spinner spinner = fragmentView.findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        wordEditText = fragmentView.findViewById(R.id.edit_enter_text);
        translationEditText = fragmentView.findViewById(R.id.edit_translation_text);
        translateButton = fragmentView.findViewById(R.id.button_translate);
        saveTranslationButton = fragmentView.findViewById(R.id.button_save_translation);
        translateButton.setOnClickListener(new OnTranslateClick(context));
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    class OnTranslateClick implements View.OnClickListener {
        private Context context;
        OnTranslateClick(Context context) {
            this.context = context;
        }
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
