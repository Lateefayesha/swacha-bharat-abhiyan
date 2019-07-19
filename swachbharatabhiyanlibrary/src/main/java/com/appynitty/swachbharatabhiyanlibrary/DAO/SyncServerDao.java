package com.appynitty.swachbharatabhiyanlibrary.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

@Dao
public interface SyncServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SyncServerEntity syncServerEntity);

    @Query("DELETE FROM "+ AUtils.COLLECTION_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM "+AUtils.COLLECTION_TABLE_NAME)
    LiveData<List<SyncServerEntity>> getAllRecord();

    @Query("DELETE FROM "+ AUtils.COLLECTION_TABLE_NAME +" WHERE _id =:index_id")
     void deleteSelectedRecord(int index_id);

    @Update
    void updateSelectedRecord(SyncServerEntity syncServerEntity);

    @Query("SELECT * FROM "+AUtils.COLLECTION_TABLE_NAME +" WHERE _id =:index_id")
    LiveData<SyncServerEntity> getSelectedRecord(int index_id);
}
