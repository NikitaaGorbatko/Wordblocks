package com.example.wordblocks;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordblocks.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements TranslatorFragment.OnFragmentInteractionListener, ItemFragment.OnListFragmentInteractionListener {
    private RetrieveFeedTask retrieveFeedTask;
    private ArrayList<Character> characterArrayList = new ArrayList<>();
    private TranslatorFragment.OnFragmentInteractionListener onFragmentInteractionListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment selectedFragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean done = false;
            switch (item.getItemId()) {
                case R.id.navigation_blocks_list:
                    selectedFragment = ItemFragment.newInstance(2);
                    done = true;
                    break;
                case R.id.navigation_manager:
                    selectedFragment = ItemFragment.newInstance(3);
                    done = true;
                    break;
                case R.id.navigation_translator:
                    selectedFragment = TranslatorFragment.newInstance("asxdgbn","sdfasdfasdfad");
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
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this, item.content, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //translatorFragment = (TranslatorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_translator);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        onFragmentInteractionListener = this;

        Thread thread = new Thread(new Runnable() {
            private Writer clientWriter;
            private BufferedReader clientReader;
            @Override
            public void run() {
                try {
                    Socket clientSocket = new Socket("192.168.0.103", 4004);
                    clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    clientWriter.write("hello");
                    clientWriter.flush();
                    String inputLine;
                    while ((inputLine = clientReader.readLine()) != null) {

                    }

                } catch (IOException ex) {

                }
            }
        });
        thread.start();
        //retrieveFeedTask = new RetrieveFeedTask();
        //retrieveFeedTask.execute("hello");
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {
        private Writer clientWriter;
        private BufferedReader clientReader;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Socket clientSocket = new Socket("192.168.0.103", 4004);
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                if (strings.length > 0) {
                    clientWriter.write(strings[strings.length - 1]);
                    clientWriter.flush();
                }
            } catch (IOException ex) {
                Log.d("LOG", "gfcjgchghg\n\n\n" + ex.getMessage());
            }
            return null;
        }
    }

}
