package com.example.android_bt2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniprojectbt2.data.entities.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products WHERE available_date = :date ORDER BY name")
    List<ProductEntity> getProductsByDate(String date);

    @Query("SELECT * FROM products WHERE available_date = :date AND category_id = :categoryId ORDER BY name")
    List<ProductEntity> getProductsByDateAndCategory(String date, long categoryId);

    @Query("SELECT * FROM products WHERE product_id = :productId LIMIT 1")
    ProductEntity getById(long productId);

    @Query("SELECT COUNT(*) FROM products")
    int count();

    @Insert
    void insertAll(List<ProductEntity> products);
}

