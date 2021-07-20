package com.dims13.mybokku.Akses.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dims13.mybokku.model.Buku;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import dims13.mybokku.R;

public class UpdateBuku extends AppCompatActivity {

    //Deklarasi Variable
    private EditText namabuku, nama_loket, harga;
    private EditText gambar;

    //Get
    private String getNama_buku;
    private String getNama_loket;
    private String getHarga;
    private String getGambar;

    //gambar
    private Button btnUpload, btnSave;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_buku);

        //TEXT Biasa
        namabuku = findViewById(R.id.namabuku);
        nama_loket = findViewById(R.id.nama_loket);
        gambar = findViewById(R.id.imgView2);

        //tgl lahir
        harga = findViewById(R.id.harga);

        // Image
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnSave = (Button) findViewById(R.id.btnSave);
        imageView = (ImageView) findViewById(R.id.imgView);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
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


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Mengupload...");
            progressDialog.show();
            StorageReference ref = storageReference.child("gambar/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            getNama_buku = namabuku.getText().toString();
                            getNama_loket = nama_loket.getText().toString();
                            getHarga = harga.getText().toString();
                            String cekpeminjam = "null"; //DEFAULT NULL FOR SET WITHOUT PEMINJAM
                            String cektanggal = "null";

                            String getKey = getIntent().getExtras().getString("getPrimaryKey");
                            database.child("Buku")
                                    .child(getKey)
                                    .setValue(new Buku(getNama_buku, getNama_loket, getHarga, taskSnapshot.getUploadSessionUri().toString(), cekpeminjam, cektanggal));
                            //Toast.makeText(UpdateProfile.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateBuku.this, LihatBuku.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateBuku.this, "Gagal " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Proses " + (int) progress + "%");
                        }
                    });
        }
    }

    private void simpan() {
        getNama_buku = namabuku.getText().toString();
        getNama_loket = nama_loket.getText().toString();
        getHarga = harga.getText().toString();
        getGambar = gambar.getText().toString();
        String cekpeminjam = "null"; //DEFAULT NULL FOR SET WITHOUT PEMINJAM
        String cektanggal = "null";
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Buku")
                .child(getKey)
                .setValue(new Buku(getNama_buku, getNama_loket, getHarga, getGambar, cekpeminjam, cektanggal))
                //Toast.makeText(UpdateProfile.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(UpdateBuku.this, LihatBuku.class));
                    }

                    ;
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
