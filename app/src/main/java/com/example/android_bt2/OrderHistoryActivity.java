package com.example.miniprojectbt2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.model.OrderSummary;
import com.example.miniprojectbt2.session.SessionManager;
import com.example.miniprojectbt2.ui.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private final List<OrderSummary> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_for_order, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView textSummary = findViewById(R.id.textOrderSummary);
        ListView listOrders = findViewById(R.id.listOrders);
        OrderAdapter adapter = new OrderAdapter(this);
        listOrders.setAdapter(adapter);

        listOrders.setOnItemClickListener((parent, view, position, id) -> {
            OrderSummary selected = adapter.getItemAt(position);
            Intent intent = new Intent(this, ReceiptActivity.class);
            intent.putExtra(ReceiptActivity.EXTRA_ORDER_ID, selected.orderId);
            startActivity(intent);
        });

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<OrderSummary> result = db.orderDao().getPaidOrdersByUser(sessionManager.getUserId());
            runOnUiThread(() -> {
                orders.clear();
                orders.addAll(result);
                adapter.submitList(result);
                textSummary.setText(getString(R.string.order_count, result.size()));
            });
        });
    }
}

