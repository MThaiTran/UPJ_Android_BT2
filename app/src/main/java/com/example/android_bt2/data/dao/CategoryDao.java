package com.example.android_bt2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniprojectbt2.data.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name")
    List<CategoryEntity> getAll();

    @Query("SELECT COUNT(*) FROM categories")
    int count();

    @Insert
    void insertAll(List<CategoryEntity> categories);
}

