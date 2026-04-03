package com.example.miniprojectbt2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.OrderDetailEntity;
import com.example.miniprojectbt2.data.entities.OrderEntity;
import com.example.miniprojectbt2.data.entities.ProductEntity;
import com.example.miniprojectbt2.session.SessionManager;
import com.example.miniprojectbt2.ui.DrawableUtils;
import com.example.miniprojectbt2.util.DateUtils;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private AppDatabase appDatabase;
    private SessionManager sessionManager;
    private ProductEntity product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        appDatabase = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1L);
        if (productId <= 0) {
            finish();
            return;
        }

        TextView textName = findViewById(R.id.textProductName);
        TextView textDescription = findViewById(R.id.textProductDescription);
        TextView textMeta = findViewById(R.id.textProductMeta);
        TextView textBalance = findViewById(R.id.textAvailableBalance);
        ImageView imageProduct = findViewById(R.id.imageProductDetail);
        EditText editQuantity = findViewById(R.id.editQuantity);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            product = appDatabase.productDao().getById(productId);
            runOnUiThread(() -> {
                if (product == null) {
                    finish();
                    return;
                }
                textName.setText(product.name);
                textDescription.setText(product.description);
                textMeta.setText(getString(R.string.product_meta, formatMoney(product.price), product.unit, product.stock));
                imageProduct.setImageResource(DrawableUtils.resolve(this, product.imageName, R.drawable.img));
            });
        });

        if (sessionManager.isLoggedIn()) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                double balance = appDatabase.userDao().getBalance(sessionManager.getUserId());
                runOnUiThread(() -> textBalance.setText(getString(R.string.available_balance, formatMoney(balance))));
            });
        } else {
            textBalance.setText(R.string.login_required_for_order);
        }

        findViewById(R.id.buttonAddToCart).setOnClickListener(v -> {
            if (product == null) {
                return;
            }

            int qty;
            try {
                qty = Integer.parseInt(editQuantity.getText().toString().trim());
            } catch (NumberFormatException ex) {
                qty = 0;
            }

            if (qty <= 0) {
                Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!sessionManager.isLoggedIn()) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.login_required_for_order)
                        .setPositiveButton(R.string.login, (dialog, which) -> startActivity(new Intent(this, LoginActivity.class)))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return;
            }

            int finalQty = qty;
            AppDatabase.databaseWriteExecutor.execute(() -> {
                long userId = sessionManager.getUserId();
                OrderEntity order = appDatabase.orderDao().getCurrentPendingOrderByUser(userId);
                if (order == null) {
                    order = new OrderEntity();
                    order.userId = userId;
                    order.createdAt = DateUtils.now();
                    order.status = "PENDING";
                    long orderId = appDatabase.orderDao().insert(order);
                    order.orderId = orderId;
                }

                OrderDetailEntity existing = appDatabase.orderDetailDao()
                        .findByOrderAndProduct(order.orderId, product.productId);

                if (existing == null) {
                    OrderDetailEntity detail = new OrderDetailEntity();
                    detail.orderId = order.orderId;
                    detail.productId = product.productId;
                    detail.quantity = finalQty;
                    detail.unitPrice = product.price;
                    appDatabase.orderDetailDao().insert(detail);
                } else {
                    existing.quantity += finalQty;
                    appDatabase.orderDetailDao().update(existing);
                }

                runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setMessage(R.string.added_to_cart)
                        .setPositiveButton(R.string.go_to_cart, (dialog, which) ->
                                startActivity(new Intent(this, CartActivity.class)))
                        .setNegativeButton(R.string.continue_shopping, null)
                        .show());
            });
        });
    }

    private String formatMoney(double value) {
        return String.format(Locale.US, "%,.0f", value);
    }
}

