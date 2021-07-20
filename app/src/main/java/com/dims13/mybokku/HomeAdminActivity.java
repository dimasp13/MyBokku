package com.dims13.mybokku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dims13.mybokku.Akses.Admin.LihatBuku;
import com.dims13.mybokku.Akses.Admin.LihatPeminjam;
import com.dims13.mybokku.Akses.Admin.TambahBuku;
import com.google.firebase.auth.FirebaseAuth;

import dims13.mybokku.R;

public class HomeAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Button Add = findViewById(R.id.add_book);
        Button See = findViewById(R.id.see_book);
        Button Peminjam = findViewById(R.id.peminjam);
        Button logoutadmin = findViewById(R.id.logoutadmin);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdminActivity.this, TambahBuku.class));
            }
        });

        See.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdminActivity.this, LihatBuku.class));
            }
        });

        Peminjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdminActivity.this, LihatPeminjam.class));
            }
        });

        logoutadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginAdminActivity.class);
                Toast.makeText(HomeAdminActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}