package com.lynhill.ghpc.util;

import com.lynhill.ghpc.R;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

//    @Headers({
//            "Content-Type: application/json",
//            "Authorization: Bearer"+ R.string.jobnimbus_token
//    })
    @POST(APIConstans.JOB)
    Call<Object> getUser(@Body Map<String, Object> body);
}
