package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginWebService {

    @POST("api/Account/Login")
    @FormUrlEncoded
    Call<ResultPojo> saveLoginDetails(@Header("appId") String key,
                                      @Header("Content-Type") String content_type,
                                      @Field("userLoginId") String userName,
                                      @Field("userPassword") String userPassword);
}
