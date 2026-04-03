package com.example.android_bt2.data.model;

import androidx.room.ColumnInfo;

public class OrderSummary {
    @ColumnInfo(name = "order_id")
    public long orderId;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "paid_at")
    public String paidAt;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "total")
    public double total;
}

