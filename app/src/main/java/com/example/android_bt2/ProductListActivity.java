package com.example.miniprojectbt2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.ProductEntity;
import com.example.miniprojectbt2.ui.adapter.ProductAdapter;
import com.example.miniprojectbt2.util.DateUtils;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        TextView textTitle = findViewById(R.id.textProductsTitle);
        TextView textSubtitle = findViewById(R.id.textProductsSubtitle);
        ListView listProducts = findViewById(R.id.listProducts);

        long categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1L);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        if (categoryId > 0 && categoryName != null) {
            textTitle.setText(getString(R.string.products_today_by_category, categoryName));
        }

        ProductAdapter adapter = new ProductAdapter(this);
        listProducts.setAdapter(adapter);

        listProducts.setOnItemClickListener((parent, view, position, id) -> {
            ProductEntity selected = adapter.getItemAt(position);
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, selected.productId);
            startActivity(intent);
        });

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductEntity> result;
            String today = DateUtils.today();
            if (categoryId > 0) {
                result = db.productDao().getProductsByDateAndCategory(today, categoryId);
            } else {
                result = db.productDao().getProductsByDate(today);
            }

            runOnUiThread(() -> {
                adapter.submitList(result);
                textSubtitle.setText(getString(R.string.today_product_count, result.size()));
            });
        });

        findViewById(R.id.buttonOpenCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
    }
}

