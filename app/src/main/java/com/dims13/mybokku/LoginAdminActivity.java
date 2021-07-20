package com.dims13.mybokku;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dims13.mybokku.R;

public class LoginAdminActivity extends AppCompatActivity  {
    private TextView status;
    private Button btnLogin;
    private EditText PasswordLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        btnLogin = findViewById(R.id.btnLoginAdmin);
        PasswordLogin = findViewById(R.id.edtPasswordAdmin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pass = Integer.parseInt(PasswordLogin.getText().toString());

                if(pass == 123456){
                    startActivity(new Intent(LoginAdminActivity.this, HomeAdminActivity.class));
                }else{
                    Toast.makeText(LoginAdminActivity.this, "Maaf, anda tidak di ijinkan untuk masuk!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

}
