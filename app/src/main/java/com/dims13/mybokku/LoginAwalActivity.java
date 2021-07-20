package com.dims13.mybokku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dims13.mybokku.R;

public class LoginAwalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_awal);

        Button assuser = findViewById(R.id.login_as_user);
        Button asadmin = findViewById(R.id.login_as_admin);

        assuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAwalActivity.this, LoginActivity.class));
            }
        });

        asadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAwalActivity.this, LoginAdminActivity.class));
            }
        });
    }
}