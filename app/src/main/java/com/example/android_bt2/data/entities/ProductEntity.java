package com.example.android_bt2.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "products",
        foreignKeys = {
                @ForeignKey(
                        entity = CategoryEntity.class,
                        parentColumns = "category_id",
                        childColumns = "category_id",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {@Index(value = {"category_id"})}
)
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    public long productId;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "unit")
    public String unit;

    @ColumnInfo(name = "stock")
    public int stock;

    @ColumnInfo(name = "available_date")
    public String availableDate;

    @ColumnInfo(name = "image_name")
    public String imageName;
}

