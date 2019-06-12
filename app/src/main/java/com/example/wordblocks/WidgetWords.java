package com.example.wordblocks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WidgetWords extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //LocalBroadcastManager.getInstance(context).registerReceiver(YourWidgetReceiver, new IntentFilter("your_intent_action"));

        String words = context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(MainActivity.DAILY_WORDS_SET_KEY, MainActivity.DEFAULT_VALUE);
        final int count = context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(MainActivity.DAILY_WORDS_COUNT_KEY, 0);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_words);
        final String[] wordsArray = words.substring(1, words.length() - 1).split(",");
        words = "";

        for (int i = 0; i < count + 1; i++) {
            words += (wordsArray[i] + "\n");
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final YandexApi yandexApi = retrofit.create(YandexApi.class);
        final String copyWords = words;

        yandexApi.listRepos(copyWords).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                String outString = "";
                if (response.body() != null) {
                    outString = response.body().getText().get(0);
                }
                final String[] traslations  = outString.split("\n");
                final String[] translated  = copyWords.split("\n");

                String wo = "";
                for (int i = 0; i < translated.length; i++) {
                    wo += translated[i].toLowerCase() + " - " + traslations[i].toLowerCase() + "\n";
                }
                views.setTextViewText(R.id.appwidget_text, wo);
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {

            }
        });

        //views.setTextViewText(R.id.appwidget_text, words);
        //Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

