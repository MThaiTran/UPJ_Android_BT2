package com.example.android_bt2.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "orders",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "user_id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {@Index(value = {"user_id"})}
)
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    public long orderId;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "paid_at")
    public String paidAt;

    @ColumnInfo(name = "status")
    public String status;
}

