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
import com.example.wordblocks.dummy.DummyContent;
import com.example.wordblocks.dummy.DummyItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    private GetLanguagesTask retrieveFeedTask;
    private ArrayList<Character> characterArrayList = new ArrayList<>();
    //private TranslatorFragment.OnFragmentInteractionListener onFragmentInteractionListener;
    private Fragment selectedFragment;
    String string = "";
    TranslatorFragment f;

    OnDownloaded callback;

    public void setOnDownloadedListener(OnDownloaded callback) {
        this.callback = callback;
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    public interface OnDownloaded {
        void OnDownloaded(String languages);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment selectedFragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean done = false;
            switch (item.getItemId()) {
                case R.id.navigation_blocks_list:
                    ArrayList<DummyItem> arrayList = new ArrayList<>();
                    selectedFragment = ItemFragment.newInstance(2, arrayList);
                    done = true;
                    break;
                case R.id.navigation_manager:
                    //selectedFragment = ItemFragment.newInstance(3);
                    done = true;
                    break;
                case R.id.navigation_translator:
                    selectedFragment = TranslatorFragment.newInstance(string, "sadf");
                    retrieveFeedTask = new GetLanguagesTask();
                    retrieveFeedTask.execute("hello");
                    f = (TranslatorFragment)selectedFragment;
                    //retrieveFeedTask.

                    //Log.d("TAG", "\n\n\n\n\n" + string + "\n\n\n\n\n\n");
                    done = true;
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return done;
        }
    };


    /*@Override
    public void onFragmentInteraction(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }*/

    @Override
    public void onListFragmentInteraction(DummyItem item) {
        Toast.makeText(this, item.name, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }



    class GetLanguagesTask extends AsyncTask<String, Void, String> {
        private Writer clientWriter;
        private BufferedReader clientReader;

        @Override
        protected String doInBackground(String... strings) {
            String token = "", inputLine;
            try {
                Socket clientSocket = new Socket("192.168.0.103", 4004);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
            f.setSpinnerList(strings);
        }
    }


    class GetWordBlocksTask extends AsyncTask<String, Void, String> {
        private BufferedReader clientReader;
        private BufferedWriter clientWriter;

        @Override
        protected String doInBackground(String... strings) {
            String token = "", inputLine = "";
            try {
                Socket clientSocket = new Socket("192.168.0.103", 4004);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientWriter.write(2);
                clientWriter.close();
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
            f.setSpinnerList(strings);
        }
    }

}
