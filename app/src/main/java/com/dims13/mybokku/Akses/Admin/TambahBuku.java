package com.dims13.mybokku.Akses.Admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dims13.mybokku.model.Buku;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import dims13.mybokku.R;

public class TambahBuku extends AppCompatActivity {

    //Deklarasi Variabel
    //private ProgressBar progressBar;

    private EditText namabuku, nama_loket, harga;
    private Button ShowData;

    //Get
    private String ceknamabuku;
    private String ceknama_loket;
    //private String cekFoto;
    private String cekharga;

    //gambar
    private ImageButton btnChoose;
    private Button btnUpload;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;


    //tanggal
    Calendar calendar;
    DatePickerDialog datePickerDialog;


    DatabaseReference getReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ShowData=findViewById(R.id.showdata);

        //TEXT Biasa
        namabuku= findViewById(R.id.namabuku);
        nama_loket= findViewById(R.id.nama_loket);
        harga=findViewById(R.id.harga);

        //Date Picker

        // Image
        btnChoose = (ImageButton) findViewById(R.id.ibGambar);
        btnUpload = (Button) findViewById(R.id.save);
        imageView = (ImageView) findViewById(R.id.ivGambar);


        //mendapatkan Intstance dari database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference= database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageContext;
                Intent intent = new Intent(TambahBuku.this, LihatBuku.class);
                startActivity(intent);
            }
        });


        btnChoose.setOnClickListener(new View.OnClickListener() {
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
                            //Menyimpan Data yang diinputkan user kedalam variabel
                            ceknamabuku = namabuku.getText().toString();
                            ceknama_loket = nama_loket.getText().toString();
                            cekharga = harga.getText().toString();
                            String cekpeminjam = "null"; //DEFAULT NULL FOR SET WITHOUT PEMINJAM
                            String cektanggal = "null";
                            getReference.child("Buku").push()
                                    .setValue(new Buku(ceknamabuku, ceknama_loket, cekharga, taskSnapshot.getUploadSessionUri().toString(), cekpeminjam, cektanggal));
                            Toast.makeText(TambahBuku.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(TambahBuku.this, "Gagal " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }



        ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageContext;
                Intent intent = new Intent(TambahBuku.this, LihatBuku.class);
                startActivity(intent);
            }
        });
    }
}