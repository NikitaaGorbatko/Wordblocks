package com.example.wordblocks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexApi {
    @GET("translate?lang=en-ru&key=trnsl.1.1.20190416T105020Z.321cb82648e266a2.2587c410b6ad56c8d50bb5403116f5e5bdb1d424")
    Call<PostModel> listRepos(@Query("text") String text);
}
