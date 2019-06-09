package com.example.wordblocks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements FragmentWordBlocks.OnListFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private GetLanguagesTask retrieveFeedTask;
    private static final int GET_LANGUAGES = 3;
    private static final int GET_TOPICS = 2;
    private static final int GET_BLOCKS = 1;
    private Fragment selectedFragment;
    private FragmentTranslator translatorFragment;
    private BufferedWriter clientWriter;
    private BufferedReader clientReader;
    private Socket clientSocket;
    private final String HOST = "192.168.0.103";
    private final int PORT = 4004;
    private YandexApi yandexApi;
    private Retrofit retrofit;
    public static final String TRANSLATIONS = "translations";
    public static final String TRANSLATED = "translated";
    public static final String SHARED_LANGUAGE_KEY = "language";
    public static final String DAILY_WORDS_COUNT_KEY = "count_key";
    public static final String CHOSED_WORD_BLOCK_KEY = "word_block_key";
    public static final String DEFAULT_VALUE = "";
    private MyReceiver receiver;
    private ArrayList<String> languages = new ArrayList<>();
    private ArrayList<WordBlock> blocks = new ArrayList<>();
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        receiver = new MyReceiver();

        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase sqlDb = db.getWritableDatabase();
        //loclalDB is required
        //Broadcast notificator is reguired???
        //Feature of choosing a language key instead of language name.......

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String msg = sharedPreferences.getString(SHARED_LANGUAGE_KEY, "");
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexApi = retrofit.create(YandexApi.class);
        new GetLanguagesTask().execute();
        new GetWordBlocksTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragment(FragmentWordBlocks.newInstance(blocks));
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            //String description = getString(R.string.channel_description);
            //int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", "name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setContentTitle("Words for today:")
                //.setContentText("Hello World!\nsdgdgf\nsfdgsdfgsd\nsdfgsdfg\ndfgsd")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Much \nlonger \ntext that \ncannot \nfit one \nline..."))
                .setWhen(3000)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(15567, builder.build());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean done = false;
        switch (item.getItemId()) {
            case R.id.navigation_blocks_list:
                setFragment(FragmentWordBlocks.newInstance(blocks));
                done = true;
                break;
            case R.id.navigation_manager:
                setFragment(FragmentManager.newInstance(blocks, languages));
                done = true;
                break;
            case R.id.navigation_translator:
                System.out.println(languages.get(1));
                setFragment(FragmentTranslator.newInstance(languages));
                done = true;
                break;
            default:

        }
        return done;
    }

    @Override
    public void onListFragmentInteraction(WordBlock item) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Intent intent = new Intent(this, WordsActivity.class);
        String line = item.data;
        line = line.substring(1, line.length() - 1);
        for (String token : line.split(",")) {
            stringBuilder.append(token.concat("\n"));
            if (stringBuilder.length() > 9960)
                break;//ыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыы
        }



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
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString() + " " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    class GetLanguagesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            String inputLine;
            ArrayList<String> languagesList = new ArrayList<>();
            try {
                clientSocket = new Socket(HOST, PORT);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(GET_LANGUAGES);
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
            languages = array;
            setFragment(FragmentTranslator.newInstance(array));
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
                clientWriter.write(GET_BLOCKS);
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
            blocks = wordBlocks;
            setFragment(FragmentWordBlocks.newInstance(wordBlocks));

        }
    }

}
