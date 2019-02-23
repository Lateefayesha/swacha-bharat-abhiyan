package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GarbageCollectionWebService {

    @Multipart
    @POST("api/Save/GarbageCollection")
    Call<GcResultPojo> saveGarbageCollectionGP(@Header("appId") String appId,
                                               @Part("userId") RequestBody userId,
                                               @Part("gpId") RequestBody gpId,
                                               @Part("Lat") RequestBody Lat,
                                               @Part("Long") RequestBody Long,
                                               @Part("beforeImage") RequestBody beforeImage,
                                               @Part("AfterImage") RequestBody afterImage,
                                               @Part("note") RequestBody comment,
                                               @Part("vehicleNumber") RequestBody vehicleNumber,
                                               @Part MultipartBody.Part image1,
                                               @Part MultipartBody.Part image2);


    @Multipart
    @POST("api/Save/GarbageCollection")
    Call<GcResultPojo> saveGarbageCollectionH(@Header("appId") String appId,
                                              @Part("userId") RequestBody userId,
                                              @Part("houseId") RequestBody houseId,
                                              @Part("Lat") RequestBody Lat,
                                              @Part("Long") RequestBody Long,
                                              @Part("beforeImage") RequestBody beforeImage,
                                              @Part("AfterImage") RequestBody afterImage,
                                              @Part("note") RequestBody comment,
                                              @Part("vehicleNumber") RequestBody vehicleNumber,
                                              @Part MultipartBody.Part image1,
                                              @Part MultipartBody.Part image2/*,
                                              @Part("garbageType") RequestBody garbageType*/);
}
