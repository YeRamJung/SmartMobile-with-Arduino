package com.example.swudoit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GitHubService {
    @FormUrlEncoded
    @POST("user/signin")
    Call<Contributor> signin(@Field("id") String id,
                             @Field("password") String password);
}
