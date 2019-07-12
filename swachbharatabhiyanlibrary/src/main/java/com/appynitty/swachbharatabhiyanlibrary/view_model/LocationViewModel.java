package com.appynitty.swachbharatabhiyanlibrary.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.repository.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mRepository;
    private LiveData<List<UserLocationEntity>> mUserLocationEntityList;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LocationRepository(application);
        mUserLocationEntityList = mRepository.getUserLocationEntityList();
    }

    public LiveData<List<UserLocationEntity>> getUserLocationEntityList() { return mUserLocationEntityList; }

    public void insert(UserLocationEntity userLocationEntity) { mRepository.insert(userLocationEntity); }
}
