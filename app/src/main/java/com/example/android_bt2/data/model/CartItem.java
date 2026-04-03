package com.example.android_bt2.data.model;

import androidx.room.ColumnInfo;

public class CartItem {
    @ColumnInfo(name = "product_id")
    public long productId;

    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "image_name")
    public String imageName;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "unit_price")
    public double unitPrice;

    @ColumnInfo(name = "subtotal")
    public double subtotal;
}

