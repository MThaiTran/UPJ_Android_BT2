package com.example.android_bt2.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.miniprojectbt2.data.dao.CategoryDao;
import com.example.miniprojectbt2.data.dao.OrderDao;
import com.example.miniprojectbt2.data.dao.OrderDetailDao;
import com.example.miniprojectbt2.data.dao.ProductDao;
import com.example.miniprojectbt2.data.dao.UserDao;
import com.example.miniprojectbt2.data.entities.CategoryEntity;
import com.example.miniprojectbt2.data.entities.OrderDetailEntity;
import com.example.miniprojectbt2.data.entities.OrderEntity;
import com.example.miniprojectbt2.data.entities.ProductEntity;
import com.example.miniprojectbt2.data.entities.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                UserEntity.class,
                CategoryEntity.class,
                ProductEntity.class,
                OrderEntity.class,
                OrderDetailEntity.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract UserDao userDao();

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();

    public abstract OrderDao orderDao();

    public abstract OrderDetailDao orderDetailDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "fruit_app.db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    bootstrapSeed();
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    bootstrapSeed();
                                }

                                private void bootstrapSeed() {
                                    databaseWriteExecutor.execute(() -> {
                                        if (INSTANCE != null) {
                                            SeedData.seed(INSTANCE);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

