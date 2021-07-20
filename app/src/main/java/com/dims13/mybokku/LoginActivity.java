package com.dims13.mybokku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dims13.mybokku.R;

public class LoginActivity extends AppCompatActivity {
    private EditText EmailLogin, PasswordLogin;
    private TextView btnRegister;
    private Button btnLogin;
    private TextView btnVerif;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener Listener;


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(Listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Listener != null) {
            auth.removeAuthStateListener(Listener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && auth.getCurrentUser().isEmailVerified()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        setContentView(R.layout.activity_login);
        initView();
        login();

        btnVerif = findViewById(R.id.verifikasi);
    }

    private void login() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = EmailLogin.getText().toString().trim();
                final String Pass = PasswordLogin.getText().toString().trim();

                auth.signInWithEmailAndPassword(EmailLogin.getText().toString(),
                        PasswordLogin.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        btnVerif.setVisibility(View.VISIBLE);
                                        //Toast.makeText(LoginActivity.this, "Harap verifikasi email terlebih dahulu", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                btnVerif.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Checking if the user is present or not and sending a verification link on his/her respected email
                                        if (auth != null) {
                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(LoginActivity.this, "Gagal Mengirim", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Cek Email untuk Verifikasi", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
            }
        });
    }

    private void initView() {
        EmailLogin = findViewById(R.id.edtEmailLogin);
        PasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.tv_to_register);
        auth = FirebaseAuth.getInstance();
    }
}
