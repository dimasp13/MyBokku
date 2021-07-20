package com.dims13.mybokku.model;

public class Buku {

    //buku
    private String nama_buku;
    private String nama_loket;
    private String harga;
    private String gambar;

    //PEMINJAM
    private String nama_peminjam;
    private String tanggal; //tanggal meminjam

    private String key;

    public String getNama_buku() {
        return nama_buku;
    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getNama_loket() {
        return nama_loket;
    }

    public void setNama_loket(String nama_loket) {
        this.nama_loket = nama_loket;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama_Peminjam() {
        return nama_peminjam;
    }

    public void setNama_Peminjam(String nama_peminjam) {
        this.nama_peminjam = nama_peminjam;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Buku() {
    }

    public Buku(String nama_buku, String nama_loket, String harga, String gambar, String nama_peminjam, String tanggal) {
        this.nama_buku = nama_buku;
        this.nama_loket = nama_loket;
        this.harga = harga;
        this.gambar = gambar;
        this.nama_peminjam = nama_peminjam;
        this.tanggal = tanggal;
        this.key = key;
    }
}
