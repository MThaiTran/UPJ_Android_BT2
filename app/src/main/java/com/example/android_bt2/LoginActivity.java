package com.example.android_bt2;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectbt2.data.AppDatabase;
import com.example.miniprojectbt2.data.entities.UserEntity;
import com.example.miniprojectbt2.session.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appDatabase = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        EditText editUsername = findViewById(R.id.editUsername);
        EditText editPassword = findViewById(R.id.editPassword);
        Button buttonLogin = findViewById(R.id.buttonSubmitLogin);

        buttonLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.enter_credentials, Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.databaseWriteExecutor.execute(() -> {
                UserEntity user = appDatabase.userDao().findByCredentials(username, password);
                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sessionManager.login(user);
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                });
            });
        });
    }
}

