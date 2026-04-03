package com.example.android_bt2.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "order_details",
        foreignKeys = {
                @ForeignKey(
                        entity = OrderEntity.class,
                        parentColumns = "order_id",
                        childColumns = "order_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = ProductEntity.class,
                        parentColumns = "product_id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {@Index(value = {"order_id"}), @Index(value = {"product_id"})}
)
public class OrderDetailEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "detail_id")
    public long detailId;

    @ColumnInfo(name = "order_id")
    public long orderId;

    @ColumnInfo(name = "product_id")
    public long productId;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "unit_price")
    public double unitPrice;
}

