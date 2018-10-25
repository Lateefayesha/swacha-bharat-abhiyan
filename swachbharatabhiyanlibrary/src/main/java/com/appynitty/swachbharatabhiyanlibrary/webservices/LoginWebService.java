package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginWebService {

    @POST("api/Account/Login")
    Call<ResultPojo> saveLoginDetails(@Header("appId") String key,
                                           @Header("Content-Type") String content_type,
                                           @Part("userLoginId") RequestBody userName,
                                           @Part("userPassword") RequestBody userPassword);
}
