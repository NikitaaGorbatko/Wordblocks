package com.example.wordblocks;

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
import com.example.wordblocks.dummy.DummyItem;
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

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    private GetLanguagesTask retrieveFeedTask;
    //private ArrayList<Character> characterArrayList = new ArrayList<>();
    //private TranslatorFragment.OnFragmentInteractionListener onFragmentInteractionListener;
    private Fragment selectedFragment;
    TranslatorFragment translatorFragment;
    //ItemFragment itemFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment selectedFragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean done = false;
            switch (item.getItemId()) {
                case R.id.navigation_blocks_list:
                    //ArrayList<DummyItem> arrayList = new ArrayList<>();
                    //selectedFragment = new ItemFragment();
                    //ItemFragment.newInstance(2, arrayList);
                    //selectedFragment = ItemFragment.newInstance(2, arrayList);
                    GetWordBlocksTask getWordBlocksTask = new GetWordBlocksTask();
                    getWordBlocksTask.execute();
                    done = true;
                    break;
                case R.id.navigation_manager:
                    //selectedFragment = ItemFragment.newInstance(3);
                    done = true;
                    break;
                case R.id.navigation_translator:
                    selectedFragment = TranslatorFragment.newInstance();
                    translatorFragment = (TranslatorFragment) selectedFragment;
                    retrieveFeedTask = new GetLanguagesTask();
                    retrieveFeedTask.execute("hello");
                    done = true;
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return done;
        }
    };

    @Override
    public void onListFragmentInteraction(DummyItem item) {
        Toast.makeText(this, item.data, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    class GetLanguagesTask extends AsyncTask<String, Void, String> {
        private static final int SEND_LANGUAGES = 3;
        private static final int SEND_TOPICS = 2;
        private static final int SEND_BLOCKS = 1;
        private BufferedWriter clientWriter;
        private BufferedReader clientReader;

        @Override
        protected String doInBackground(String... strings) {
            String token = "", inputLine;
            try {
                Socket clientSocket = new Socket("192.168.0.103", 4004);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(SEND_LANGUAGES);
                clientWriter.flush();
                //clientWriter.close();
                while ((inputLine = clientReader.readLine()) != null) {
                    token += "\n" + inputLine;
                }
                clientReader.close();
            } catch (IOException ex) {
                Log.d("LOG", "gfcjgchghg\n\n\n" + ex.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            translatorFragment.setSpinnerList(strings);
        }
    }


    class GetWordBlocksTask extends AsyncTask<String, Void, ArrayList<DummyItem>> {
        private static final int SEND_LANGUAGES = 3;
        private static final int SEND_TOPICS = 2;
        private static final int SEND_BLOCKS = 1;
        private static final String ID_KEY = "id";
        private static final String NAME_KEY = "name";
        private static final String DESCRIPTION_KEY = "description";
        private static final String TOPIC_KEY = "topic";
        private static final String LANGUAGE_KEY = "language";
        private static final String COST_KEY = "cost";
        private static final String DATA_KEY = "data";
        private BufferedWriter clientWriter;
        private BufferedReader clientReader;

        @Override
        protected ArrayList<DummyItem> doInBackground(String... strings) {
            String token = "", inputLine;
            ArrayList<DummyItem> dummyItems = new ArrayList<>();
            try {
                Socket clientSocket = new Socket("192.168.0.103", 4004);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(SEND_BLOCKS);
                clientWriter.flush();
                //clientWriter.close();
                while ((inputLine = clientReader.readLine()) != null) {
                    token += "\n" + inputLine;
                }
                //clientReader.close();
                try {
                    JSONArray j = new JSONArray(token);
                    for(int i = 0; i < j.length(); i++) {
                        JSONObject localDummyJsonObject = j.getJSONObject(i);
                        dummyItems.add(new DummyItem(localDummyJsonObject.getString(ID_KEY),
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
                Log.d("LOG", "gfcjgchghg\n\n\n" + ex.getMessage());
            }
            return dummyItems;
        }

        @Override
        protected void onPostExecute(ArrayList<DummyItem> dummyItems) {
            super.onPostExecute(dummyItems);
            selectedFragment = ItemFragment.newInstance(1, dummyItems);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }
    }
}
