package com.swu.kalkulator;

import java.util.ArrayList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // deklarasi variabel
    EditText editNum1, editNum2;
    Button buttonPlus, buttonMin, buttonMulti, buttonDiv, buttonCalc, buttonClear;
    TextView textResult;

    String operatorTerpilih = "";
    public static ArrayList<Riwayat> dataRiwayat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // inisialisasi variabel
        editNum1 = findViewById(R.id.editNum1);
        editNum2 = findViewById(R.id.editNum2);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMin = findViewById(R.id.buttonMin);
        buttonMulti = findViewById(R.id.buttonMulti);
        buttonDiv = findViewById(R.id.buttonDiv);
        buttonCalc = findViewById(R.id.buttonCalc);
        buttonClear = findViewById(R.id.buttonClear);
        textResult = findViewById(R.id.textResult);

        // tambah event
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "+";
                Toast.makeText(MainActivity.this, "Operator + dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "-";
                Toast.makeText(MainActivity.this, "Operator - dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "x";
                Toast.makeText(MainActivity.this, "Operator x dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = ":";
                Toast.makeText(MainActivity.this, "Operator : dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungHasil();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNum1.setText("");
                editNum2.setText("");
                textResult.setText("");
                operatorTerpilih = "";
                editNum1.requestFocus();
            }
        });
    }

    private void hitungHasil() {
        String input1 = editNum1.getText().toString();
        String input2 = editNum2.getText().toString();

        if (input1.isEmpty() || input2.isEmpty()) {
            Toast.makeText(this, "Mohon isi kedua angka terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (operatorTerpilih.isEmpty()) {
            Toast.makeText(this, "Mohon pilih operator terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        double angka1 = Double.parseDouble(input1);
        double angka2 = Double.parseDouble(input2);
        double hasil = 0;

        switch (operatorTerpilih) {
            case "+":
                hasil = angka1 + angka2;
                break;
            case "-":
                hasil = angka1 - angka2;
                break;
            case "x":
                hasil = angka1 * angka2;
                break;
            case ":":
                if (angka2 == 0) {
                    textResult.setText("Tidak bisa dibagi 0");
                    return;
                }
                hasil = angka1 / angka2;
                break;
        }

        // teks perhitungan, contoh: "10.0 + 5.0"
        String teksPerhitungan = angka1 + " " + operatorTerpilih + " " + angka2;

        textResult.setText(String.valueOf(hasil));
        dataRiwayat.add(new Riwayat(teksPerhitungan, String.valueOf(hasil)));

        cekSecret(hasil);
    }

    // ===================== SECRET CALCULATOR =====================
    // NIM STI202403834 -> 5 digit terakhir: 0 3 8 3 4
    // 0 = kamera | 3 = audio + GPS | 8 = gambar | 4 = accelerometer & gyroscope
    private void cekSecret(double hasil) {
        if (hasil != Math.floor(hasil)) return;   // abaikan hasil desimal
        int h = (int) hasil;

        switch (h) {
            case 0:  // digit 1 -> kamera
                try {
                    startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                } catch (Exception e) {
                    Toast.makeText(this, "Kamera tidak tersedia", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:  // digit 2 (audio) dan digit 4 (GPS) sama-sama 3 -> tampilkan pilihan
                tampilPilihanFitur();
                break;
            case 8:  // digit 3 -> gambar
                tampilGambar();
                break;
            case 4:  // digit 5 -> accelerometer & gyroscope
                startActivity(new Intent(this, SensorActivity.class));
                break;
        }
    }

    private void tampilPilihanFitur() {
        String[] pilihan = {"Play Audio", "Buka GPS"};
        new AlertDialog.Builder(this)
                .setTitle("Pilih Fitur Rahasia")
                .setItems(pilihan, (dialog, which) -> {
                    if (which == 0) {
                        putarAudio();
                    } else {
                        bukaGps();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void putarAudio() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.audio);   // file: res/raw/audio.mp3
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.start();
        Toast.makeText(this, "Memutar audio...", Toast.LENGTH_SHORT).show();
    }

    private void tampilGambar() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.bmw);                      // file: res/drawable/bmw.jpg
        iv.setAdjustViewBounds(true);
        new AlertDialog.Builder(this)
                .setTitle("Secret Image")
                .setView(iv)
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void bukaGps() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        String pesan = (loc != null)
                ? "Latitude: " + loc.getLatitude() + "\nLongitude: " + loc.getLongitude()
                : "Lokasi belum tersedia. Pastikan GPS aktif lalu coba lagi.";
        new AlertDialog.Builder(this)
                .setTitle("Secret GPS")
                .setMessage(pesan)
                .setPositiveButton("Tutup", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bukaGps();
        }
    }
}