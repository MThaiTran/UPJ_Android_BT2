package com.example.android_bt2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniprojectbt2.data.entities.OrderEntity;
import com.example.miniprojectbt2.data.model.OrderSummary;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders WHERE user_id = :userId AND status = 'PENDING' ORDER BY order_id DESC LIMIT 1")
    OrderEntity getCurrentPendingOrderByUser(long userId);

    @Query("SELECT * FROM orders WHERE order_id = :orderId LIMIT 1")
    OrderEntity getById(long orderId);

    @Insert
    long insert(OrderEntity order);

    @Query("UPDATE orders SET status = :status, paid_at = :paidAt WHERE order_id = :orderId")
    int updateStatus(long orderId, String status, String paidAt);

    @Query("SELECT o.order_id, o.created_at, o.paid_at, o.status, IFNULL(SUM(od.quantity * od.unit_price), 0) AS total FROM orders o LEFT JOIN order_details od ON o.order_id = od.order_id WHERE o.user_id = :userId AND o.status = 'PAID' GROUP BY o.order_id ORDER BY o.order_id DESC")
    List<OrderSummary> getPaidOrdersByUser(long userId);
}

