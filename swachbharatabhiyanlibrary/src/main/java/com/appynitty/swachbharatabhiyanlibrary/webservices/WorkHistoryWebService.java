package com.appynitty.swachbharatabhiyanlibrary.webservices;

import com.appynitty.swachbharatabhiyanlibrary.pojos.EmpWorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface WorkHistoryWebService {

    @GET("api/Get/WorkHistory")
    Call<List<WorkHistoryPojo>> pullWorkHistoryList(@Header("appId") String appId,
                                                    @Header("userId") String userId,
                                                    @Header("year") String year,
                                                    @Header("month") String month);


    @GET("api//Get/WorkHistory/Details")
    Call<List<WorkHistoryDetailPojo>> pullWorkHistoryDetailList(@Header("appId") String appId,
                                                                @Header("userId") String userId,
                                                                @Header("fdate") String fDate,
                                                                @Header("LanguageId") String languageId);

    @GET("api/Get/QrWorkHistory ")
    Call<List<WorkHistoryPojo>> pullEmpWorkHistoryList(@Header("appId") String appId,
                                                    @Header("userId") String userId,
                                                    @Header("year") String year,
                                                    @Header("month") String month);


    @GET("api/Get/QrWorkHistoryDetails")
    Call<List<EmpWorkHistoryDetailPojo>> pullEmpWorkHistoryDetailList(@Header("appId") String appId,
                                                                   @Header("userId") String userId,
                                                                   @Header("Date") String fDate);
}
