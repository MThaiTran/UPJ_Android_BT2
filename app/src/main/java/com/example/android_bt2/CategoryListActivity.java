package com.example.miniprojectbt2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.CategoryEntity;
import com.example.miniprojectbt2.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        TextView textSummary = findViewById(R.id.textCategorySummary);
        ListView listCategories = findViewById(R.id.listCategories);
        CategoryAdapter adapter = new CategoryAdapter(this);
        listCategories.setAdapter(adapter);

        listCategories.setOnItemClickListener((parent, view, position, id) -> {
            CategoryEntity selected = adapter.getItemAt(position);
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra(ProductListActivity.EXTRA_CATEGORY_ID, selected.categoryId);
            intent.putExtra(ProductListActivity.EXTRA_CATEGORY_NAME, selected.name);
            startActivity(intent);
        });

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CategoryEntity> result = db.categoryDao().getAll();
            runOnUiThread(() -> {
                adapter.submitList(result);
                textSummary.setText(getString(R.string.category_count, result.size()));
            });
        });
    }
}

