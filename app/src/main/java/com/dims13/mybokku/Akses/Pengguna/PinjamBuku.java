package com.dims13.mybokku.Akses.Pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dims13.mybokku.model.Buku;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dims13.mybokku.R;

public class PinjamBuku extends AppCompatActivity {

    //Deklarasi Variable
    private TextView namabuku, nama_loket, harga;
    private EditText nama_peminjam, tanggal;
    private EditText gambar;

    //Get
    private String getNama_buku;
    private String getNama_loket;
    private String getHarga;
    private String getGambar;

    //gambar
    private Button btnSave;
    private ImageView imageView;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);

        //TEXT Biasa
        namabuku = findViewById(R.id.namabuku);
        nama_loket = findViewById(R.id.nama_loket);
        gambar = findViewById(R.id.imgView2);

        //tgl lahir
        harga = findViewById(R.id.harga);

        nama_peminjam = findViewById(R.id.nama_peminjam);
        tanggal = findViewById(R.id.tanggal);

        // Image
        btnSave = (Button) findViewById(R.id.btnSave);
        imageView = (ImageView) findViewById(R.id.imgView);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getData();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
    }


    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData() {
        final String getNama_buku = getIntent().getExtras().getString("datanama_buku");
        final String getNama_loket = getIntent().getExtras().getString("datanama_loket");
        final String getHarga = getIntent().getExtras().getString("dataharga");
        final String getGambar = getIntent().getExtras().getString("datagambar");


        //FUNGSI REPLACE PADA URL GAMBAR
        String sumber = getGambar;
        String a1 = sumber.replace("o?name=", "o/");
        String a2 = a1.replace("&uploadType=resumable", "?alt=media&uploadType=resumable");
        String a3 = a2.replace("&upload_id", "&upload_ids");
        String a4 = a3.replace("&upload_protocol=resumable", "");
        String imgsumber = a4;

        //img
        Glide.with(this)
                .load(imgsumber)
                .placeholder(R.drawable.ic_add)
                .centerCrop()
                .into(imageView);

        //SET TEXT
        namabuku.setText(getNama_buku);
        nama_loket.setText(getNama_loket);
        harga.setText(getHarga);
        gambar.setText(getGambar);
    }

    private void simpan() {
        getNama_buku = namabuku.getText().toString();
        getNama_loket = nama_loket.getText().toString();
        getHarga = harga.getText().toString();
        getGambar = gambar.getText().toString();
        String cekpeminjam = nama_peminjam.getText().toString(); //DEFAULT NULL FOR SET WITHOUT PEMINJAM
        String cektanggal = tanggal.getText().toString();
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Pinjam")
                .child(getKey + cekpeminjam)
                .setValue(new Buku(getNama_buku, getNama_loket, getHarga, getGambar, cekpeminjam, cektanggal))
                //Toast.makeText(UpdateProfile.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(PinjamBuku.this, LihatBuku.class));
                    }
                });
    }
}