package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GarbageCollectionGarbagePointWebService {

    @Multipart
    @POST("api/Save/GarbageCollection")
    Call<ResultPojo> saveGarbageCollectionGP(@Header("appId") String appId,
                                             @Part("userId") RequestBody userId,
                                             @Part("gpId") RequestBody gpId,
                                             @Part("Lat") RequestBody Lat,
                                             @Part("Long") RequestBody Long,
                                             @Part("beforeImage") RequestBody beforeImage,
                                             @Part("AfterImage") RequestBody afterImage,
                                             @Part("comment") RequestBody comment,
                                             @Part MultipartBody.Part image1,
                                             @Part MultipartBody.Part image2);
}
