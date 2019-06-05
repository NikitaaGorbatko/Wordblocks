package com.example.wordblocks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private GetLanguagesTask retrieveFeedTask;
    private static final int SEND_LANGUAGES = 3;
    private static final int SEND_TOPICS = 2;
    private static final int SEND_BLOCKS = 1;
    private Fragment selectedFragment;
    private TranslatorFragment translatorFragment;
    private BufferedWriter clientWriter;
    private BufferedReader clientReader;
    private Socket clientSocket;
    private final String HOST = "192.168.0.103";
    private final int PORT = 4004;
    private YandexApi yandexApi;
    private Retrofit retrofit;
    public static final String TRANSLATIONS = "translations";
    public static final String TRANSLATED = "translated";
    StringBuilder stringBuilder = new StringBuilder();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase sqlDb = db.getWritableDatabase();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexApi = retrofit.create(YandexApi.class);
        intent = new Intent(this, WordsActivity.class);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean done = false;
        //Fragment selectedFragment;
        switch (item.getItemId()) {
            case R.id.navigation_blocks_list:
                GetWordBlocksTask getWordBlocksTask = new GetWordBlocksTask();
                getWordBlocksTask.execute();
                done = true;
                break;
            case R.id.navigation_manager:
                done = true;
                break;
            case R.id.navigation_translator:
                selectedFragment = TranslatorFragment.newInstance();
                translatorFragment = (TranslatorFragment) selectedFragment;
                retrieveFeedTask = new GetLanguagesTask();
                retrieveFeedTask.execute("hello");
                done = true;
                break;
            default:

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return done;
    }

    @Override
    public void onListFragmentInteraction(WordBlock item) {
        String line = item.data;
        ArrayList<String> list = new ArrayList<>();
        line = line.substring(1, line.length() - 1);
        for (String token : line.split(","))
            stringBuilder.append(token.concat("\n"));

        yandexApi.listRepos(stringBuilder.toString()).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                String outString = "";
                if (response.body() != null) {
                    outString = response.body().getText().get(0);
                }
                String[] traslations  = outString.split("\n");
                String[] translated  = stringBuilder.toString().split("\n");
                intent.putExtra(TRANSLATIONS, traslations);
                intent.putExtra(TRANSLATED, translated);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), traslations[1], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString() + " " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //Toast.makeText(this, stringBuilder, Toast.LENGTH_LONG).show();
    }

    class GetLanguagesTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String inputLine;
            ArrayList<String> languagesList = new ArrayList<String>();
            try {
                clientSocket = new Socket(HOST, PORT);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(SEND_LANGUAGES);
                clientWriter.flush();
                while ((inputLine = clientReader.readLine()) != null) {
                    languagesList.add(inputLine);
                }
                clientWriter.close();
                clientReader.close();
                clientSocket.close();
            } catch (IOException ex) {
                Log.d("LOG", "gfcjgchghg\n\n\n" + ex.getMessage());
            }
            return languagesList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> array) {
            super.onPostExecute(array);
            translatorFragment.setSpinnerList(array);
        }
    }

    class GetWordBlocksTask extends AsyncTask<Void, Void, ArrayList<WordBlock>> {
        private static final String ID_KEY = "id";
        private static final String NAME_KEY = "name";
        private static final String DESCRIPTION_KEY = "description";
        private static final String TOPIC_KEY = "topic";
        private static final String LANGUAGE_KEY = "language";
        private static final String COST_KEY = "cost";
        private static final String DATA_KEY = "data";

        @Override
        protected ArrayList<WordBlock> doInBackground(Void... voids) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();;
            ArrayList<WordBlock> wordBlocks = new ArrayList<>();
            try {
                clientSocket = new Socket(HOST, PORT);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(SEND_BLOCKS);
                clientWriter.flush();
                while ((inputLine = clientReader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                clientWriter.close();
                clientReader.close();
                clientSocket.close();
                try {
                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject localDummyJsonObject = jsonArray.getJSONObject(i);
                        wordBlocks.add(new WordBlock(localDummyJsonObject.getString(ID_KEY),
                                localDummyJsonObject.getString(NAME_KEY),
                                localDummyJsonObject.getString(DESCRIPTION_KEY),
                                localDummyJsonObject.getString(TOPIC_KEY),
                                localDummyJsonObject.getInt(COST_KEY),
                                localDummyJsonObject.getString(LANGUAGE_KEY),
                                localDummyJsonObject.getString(DATA_KEY)));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return wordBlocks;
        }

        @Override
        protected void onPostExecute(ArrayList<WordBlock> wordBlocks) {
            super.onPostExecute(wordBlocks);
            selectedFragment = ItemFragment.newInstance(1, wordBlocks);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }

    }
}
