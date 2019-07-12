package com.appynitty.swachbharatabhiyanlibrary.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

@Dao
public interface UserLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserLocationEntity userLocationEntity);

    @Query("DELETE FROM "+ AUtils.LOCATION_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM "+AUtils.LOCATION_TABLE_NAME)
    LiveData<List<UserLocationEntity>> getAllLocations();

    @Delete
     void deleteSelectedRecord(UserLocationEntity userLocationEntity);

    @Update
    void updateSelectedRecord(UserLocationEntity userLocationEntity);

    @Query("SELECT * FROM "+AUtils.LOCATION_TABLE_NAME +" WHERE _id =:index_id")
    LiveData<UserLocationEntity> getTask(int index_id);
}
