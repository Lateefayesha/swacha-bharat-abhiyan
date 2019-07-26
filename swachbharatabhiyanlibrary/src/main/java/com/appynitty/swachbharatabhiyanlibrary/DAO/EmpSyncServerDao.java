package com.appynitty.swachbharatabhiyanlibrary.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

@Dao
public interface EmpSyncServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EmpSyncServerEntity syncServerEntity);

    @Query("DELETE FROM "+ AUtils.QR_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM "+AUtils.QR_TABLE_NAME)
    LiveData<List<EmpSyncServerEntity>> getAllRecord();

    @Query("DELETE FROM "+ AUtils.QR_TABLE_NAME +" WHERE _id =:index_id")
     void deleteSelectedRecord(int index_id);

    @Update
    void updateSelectedRecord(EmpSyncServerEntity syncServerEntity);

    @Query("SELECT * FROM "+AUtils.QR_TABLE_NAME +" WHERE _id =:index_id")
    LiveData<EmpSyncServerEntity> getSelectedRecord(int index_id);
}
