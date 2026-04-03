package com.example.android_bt2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
    public int categoryId;
    public String imageRes; // Tên ảnh trong drawable
}
