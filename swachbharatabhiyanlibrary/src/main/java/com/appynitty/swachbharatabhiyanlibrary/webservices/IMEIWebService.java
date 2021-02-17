package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IMEIWebService {
    @POST("api/save/")
    Call<ResponseBody> compareIMEINumber (
            @Header("appId") String appId,
            @Header("userId") String userId,
            @Header("isSync") boolean isSync,
            @Header("batteryStatus") int batteryStatus,
            @Header("Content-Type") String contentType );

}