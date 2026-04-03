package com.example.android_bt2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.miniprojectbt2.data.entities.OrderDetailEntity;
import com.example.miniprojectbt2.data.model.CartItem;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Query("SELECT od.product_id, p.name AS product_name, p.image_name AS image_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS subtotal FROM order_details od INNER JOIN products p ON p.product_id = od.product_id WHERE od.order_id = :orderId ORDER BY p.name")
    List<CartItem> getItemsByOrder(long orderId);

    @Query("SELECT * FROM order_details WHERE order_id = :orderId AND product_id = :productId LIMIT 1")
    OrderDetailEntity findByOrderAndProduct(long orderId, long productId);

    @Insert
    long insert(OrderDetailEntity detail);

    @Update
    int update(OrderDetailEntity detail);
}

