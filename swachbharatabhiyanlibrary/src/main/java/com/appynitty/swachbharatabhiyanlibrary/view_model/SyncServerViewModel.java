package com.appynitty.swachbharatabhiyanlibrary.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncServerRepository;

import java.util.List;

public class SyncServerViewModel extends AndroidViewModel {

    private SyncServerRepository mRepository;
    private LiveData<List<SyncServerEntity>> mSyncServerEntityList;

    public SyncServerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SyncServerRepository(application);
        mSyncServerEntityList = mRepository.getSysncServerEntityList();
    }

    public LiveData<List<SyncServerEntity>> getSysncServerEntityList() { return mSyncServerEntityList; }

    public void insert(SyncServerEntity syncServerEntity) { mRepository.insert(syncServerEntity); }

    public void deleteAllRecord() { mRepository.deleteAllRecord();}

    public void deleteSelectedRecord(int id) { mRepository.deleteSelectedRecord(id);}
}
