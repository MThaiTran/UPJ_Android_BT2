package com.example.android_bt2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.OrderEntity;
import com.example.miniprojectbt2.data.model.CartItem;
import com.example.miniprojectbt2.session.SessionManager;
import com.example.miniprojectbt2.ui.adapter.CartAdapter;
import com.example.miniprojectbt2.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private SessionManager sessionManager;
    private CartAdapter adapter;
    private TextView textTotalView;
    private TextView textBalanceView;
    private double currentTotal = 0d;
    private long currentOrderId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        appDatabase = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_for_order, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ListView listCart = findViewById(R.id.listCartItems);
        textTotalView = findViewById(R.id.textCartTotal);
        textBalanceView = findViewById(R.id.textCartBalance);

        adapter = new CartAdapter(this);
        listCart.setAdapter(adapter);

        findViewById(R.id.buttonCheckout).setOnClickListener(v -> {
            if (currentOrderId <= 0 || adapter.getItems().isEmpty()) {
                Toast.makeText(this, R.string.empty_cart, Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.databaseWriteExecutor.execute(() -> {
                double balance = appDatabase.userDao().getBalance(sessionManager.getUserId());
                double total = currentTotal;
                if (balance < total) {
                    runOnUiThread(() -> Toast.makeText(this, R.string.insufficient_balance, Toast.LENGTH_SHORT).show());
                    return;
                }

                appDatabase.userDao().updateBalance(sessionManager.getUserId(), balance - total);
                appDatabase.orderDao().updateStatus(currentOrderId, "PAID", DateUtils.now());
                runOnUiThread(() -> {
                    Intent receiptIntent = new Intent(this, ReceiptActivity.class);
                    receiptIntent.putExtra(ReceiptActivity.EXTRA_ORDER_ID, currentOrderId);
                    startActivity(receiptIntent);
                    finish();
                });
            });
        });

        findViewById(R.id.buttonBackToProducts).setOnClickListener(v ->
                startActivity(new Intent(this, ProductListActivity.class)));

        loadCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null && sessionManager.isLoggedIn() && adapter != null) {
            loadCart();
        }
    }

    private void loadCart() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            double balance = appDatabase.userDao().getBalance(sessionManager.getUserId());
            OrderEntity pendingOrder = appDatabase.orderDao().getCurrentPendingOrderByUser(sessionManager.getUserId());
            if (pendingOrder == null) {
                runOnUiThread(() -> {
                    currentOrderId = -1L;
                    currentTotal = 0d;
                    adapter.submitList(new ArrayList<>());
                    textTotalView.setText(getString(R.string.total_format, formatMoney(0)));
                    textBalanceView.setText(getString(R.string.available_balance, formatMoney(balance)));
                });
                return;
            }

            List<CartItem> items = appDatabase.orderDetailDao().getItemsByOrder(pendingOrder.orderId);
            double total = 0;
            for (CartItem item : items) {
                total += item.subtotal;
            }

            double finalTotal = total;
            runOnUiThread(() -> {
                currentOrderId = pendingOrder.orderId;
                currentTotal = finalTotal;
                adapter.submitList(items);
                textTotalView.setText(getString(R.string.total_format, formatMoney(finalTotal)));
                textBalanceView.setText(getString(R.string.available_balance, formatMoney(balance)));
            });
        });
    }

    private String formatMoney(double value) {
        return String.format(Locale.US, "%,.0f", value);
    }
}

