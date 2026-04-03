package com.example.miniprojectbt2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.OrderEntity;
import com.example.miniprojectbt2.data.entities.UserEntity;
import com.example.miniprojectbt2.data.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {
    public static final String EXTRA_ORDER_ID = "extra_order_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        long orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, -1L);
        if (orderId <= 0) {
            finish();
            return;
        }

        TextView textReceiptHeader = findViewById(R.id.textReceiptHeader);
        TextView textReceiptTotal = findViewById(R.id.textReceiptTotal);
        TextView textReceiptBalance = findViewById(R.id.textReceiptBalance);
        ListView listReceiptItems = findViewById(R.id.listReceiptItems);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listReceiptItems.setAdapter(adapter);

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            OrderEntity order = db.orderDao().getById(orderId);
            if (order == null) {
                runOnUiThread(this::finish);
                return;
            }

            UserEntity user = db.userDao().getById(order.userId);
            List<CartItem> items = db.orderDetailDao().getItemsByOrder(orderId);
            double balance = user == null ? 0d : user.balance;

            List<String> rows = new ArrayList<>();
            double total = 0;
            for (CartItem item : items) {
                rows.add(item.productName + " x" + item.quantity + " - " + formatMoney(item.subtotal));
                total += item.subtotal;
            }

            double finalTotal = total;
            String header = getString(R.string.receipt_header, order.orderId, user == null ? "Unknown" : user.username, order.paidAt);
            runOnUiThread(() -> {
                textReceiptHeader.setText(header);
                textReceiptTotal.setText(getString(R.string.total_format, formatMoney(finalTotal)));
                textReceiptBalance.setText(getString(R.string.balance_after_checkout, formatMoney(balance)));
                adapter.clear();
                adapter.addAll(rows);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private String formatMoney(double value) {
        return String.format(Locale.US, "%,.0f", value);
    }
}

