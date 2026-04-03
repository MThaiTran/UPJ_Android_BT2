package com.example.android_bt2.data;

import com.example.miniprojectbt2.data.entities.CategoryEntity;
import com.example.miniprojectbt2.data.entities.ProductEntity;
import com.example.miniprojectbt2.data.entities.UserEntity;
import com.example.miniprojectbt2.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public final class SeedData {
    private static final double ADMIN_BALANCE = 10_000_000;
    private static final double SELLER_BALANCE = 5_000_000;

    private SeedData() {
    }

    public static void seed(AppDatabase db) {
        ensureDemoUser(db, "admin", "123456", "Admin User", ADMIN_BALANCE);
        ensureDemoUser(db, "seller", "123456", "Seller User", SELLER_BALANCE);

        if (db.categoryDao().count() == 0) {
            List<CategoryEntity> categories = new ArrayList<>();
            categories.add(createCategory("Tropical", "Fresh tropical fruits"));
            categories.add(createCategory("Citrus", "Vitamin C rich fruits"));
            categories.add(createCategory("Berries", "Premium berries"));
            db.categoryDao().insertAll(categories);
        }

        if (db.productDao().count() == 0) {
            String today = DateUtils.today();
            List<ProductEntity> products = new ArrayList<>();
            products.add(createProduct(1, "Banana", "Sweet banana", 22000, "kg", 50, today, "img"));
            products.add(createProduct(1, "Mango", "Ripe cat mango", 45000, "kg", 30, today, "img_1"));
            products.add(createProduct(2, "Orange", "Navel orange", 38000, "kg", 40, today, "img_2"));
            products.add(createProduct(2, "Lemon", "Fresh lemon", 28000, "kg", 60, today, "img_3"));
            products.add(createProduct(3, "Strawberry", "Da Lat strawberry", 95000, "box", 20, today, "img_1"));
            products.add(createProduct(3, "Blueberry", "Imported blueberry", 120000, "box", 15, today, "img_2"));
            db.productDao().insertAll(products);
        }
    }

    private static void ensureDemoUser(AppDatabase db, String username, String password, String fullName, double balance) {
        UserEntity existing = db.userDao().findByUsername(username);
        if (existing == null) {
            db.userDao().insertAll(java.util.Collections.singletonList(createUser(username, password, fullName, balance)));
            return;
        }

        existing.password = password;
        existing.fullName = fullName;
        existing.balance = balance;
        db.userDao().update(existing);
    }

    private static UserEntity createUser(String username, String password, String fullName, double balance) {
        UserEntity user = new UserEntity();
        user.username = username;
        user.password = password;
        user.fullName = fullName;
        user.balance = balance;
        return user;
    }

    private static CategoryEntity createCategory(String name, String description) {
        CategoryEntity category = new CategoryEntity();
        category.name = name;
        category.description = description;
        return category;
    }

    private static ProductEntity createProduct(
            long categoryId,
            String name,
            String description,
            double price,
            String unit,
            int stock,
            String availableDate,
            String imageName
    ) {
        ProductEntity product = new ProductEntity();
        product.categoryId = categoryId;
        product.name = name;
        product.description = description;
        product.price = price;
        product.unit = unit;
        product.stock = stock;
        product.availableDate = availableDate;
        product.imageName = imageName;
        return product;
    }
}

