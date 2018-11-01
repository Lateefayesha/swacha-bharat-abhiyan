package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserLocationWebService {

    @POST("api/Save/UserLocation")
    Call<ResultPojo> saveLoginDetails(@Header("appId") String appId,
                                      @Header("Content-Type") String content_type,
                                      @Header("userId") String userId,
                                      @Body UserLocationPojo userLocationPojo);
}
