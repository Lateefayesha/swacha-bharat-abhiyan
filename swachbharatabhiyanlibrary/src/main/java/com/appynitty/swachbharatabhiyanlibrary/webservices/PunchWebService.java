package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PunchWebService {

    @POST("api/Save/UserAttendenceIn")
    Call<ResultPojo> saveInPunchDetails(@Header("appId") String appId,
                                        @Header("Content-Type") String content_type,
                                        @Header("batteryStatus") int batteryStatus,
                                        @Body InPunchPojo loginPojo);

    @POST("api/Save/UserAttendenceOut")
    Call<ResultPojo> saveOutPunchDetails(@Header("appId") String appId,
                                        @Header("Content-Type") String content_type,
                                         @Header("batteryStatus") int batteryStatus,
                                        @Body OutPunchPojo outPojo);
}
