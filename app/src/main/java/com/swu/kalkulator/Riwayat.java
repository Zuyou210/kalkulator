package com.swu.kalkulator;

public class Riwayat {
    private String perhitungan;
    private String hasil;

    public Riwayat(String perhitungan, String hasil) {
        this.perhitungan = perhitungan;
        this.hasil = hasil;
    }

    public String getPerhitungan() {
        return perhitungan;
    }

    public String getHasil() {
        return hasil;
    }

    public void setPerhitungan(String perhitungan) {
        this.perhitungan = perhitungan;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }
}
