package com.example.yupromise;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverApi {
    @GET("v1/driving")
    public Call<ResultPath> getPath(@Header("") String apiKeyID,
                                    @Header("") String apiKey,
                                    @Query("") String start,
                                    @Query("") String goal);
}
