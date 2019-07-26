package com.appynitty.swachbharatabhiyanlibrary.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.repository.EmpSyncServerRepository;

import java.util.List;

public class EmpSyncServerViewModel extends AndroidViewModel {

    private EmpSyncServerRepository mRepository;
    private LiveData<List<EmpSyncServerEntity>> mEntityList;

    public EmpSyncServerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EmpSyncServerRepository(application);
        mEntityList = mRepository.getSysncServerEntityList();
    }

    public LiveData<List<EmpSyncServerEntity>> getEmpSysncServerEntityList() { return mEntityList; }

    public void insert(EmpSyncServerEntity syncServerEntity) { mRepository.insert(syncServerEntity); }

    public void deleteAllRecord() { mRepository.deleteAllRecord();}

    public void deleteSelectedRecord(int id) { mRepository.deleteSelectedRecord(id);}
}
