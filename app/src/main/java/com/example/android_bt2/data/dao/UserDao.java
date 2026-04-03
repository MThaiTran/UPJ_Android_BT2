package com.example.android_bt2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.miniprojectbt2.data.entities.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity findByCredentials(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    UserEntity getById(long userId);

    @Query("SELECT balance FROM users WHERE user_id = :userId LIMIT 1")
    double getBalance(long userId);

    @Query("UPDATE users SET balance = :balance WHERE user_id = :userId")
    int updateBalance(long userId, double balance);

    @Update
    int update(UserEntity user);

    @Query("SELECT COUNT(*) FROM users")
    int count();

    @Insert
    void insertAll(List<UserEntity> users);
}

