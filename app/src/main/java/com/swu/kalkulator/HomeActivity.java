
package com.swu.kalkulator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnKalkulator = findViewById(R.id.btnKalkulator);
        Button btnTentang = findViewById(R.id.btnTentang);
        Button btnRiwayat = findViewById(R.id.buttonHistory);


        btnKalkulator.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnTentang.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        btnRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}