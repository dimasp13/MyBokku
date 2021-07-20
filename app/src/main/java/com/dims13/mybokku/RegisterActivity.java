package com.dims13.mybokku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import dims13.mybokku.R;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_to_login;
    private Button btn_register;
    private EditText et_remail;
    private EditText et_rpassword;
    private EditText et_rpassword2;
    private ProgressBar pb_register;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        tv_to_login = findViewById(R.id.tv_to_login);
        et_remail = findViewById(R.id.et_remail);
        et_rpassword = findViewById(R.id.et_rpassword);
        et_rpassword2 = findViewById(R.id.et_rpassword2);
        pb_register = findViewById(R.id.ps_register);
        fAuth = FirebaseAuth.getInstance();


        tv_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_remail.getText().toString().trim();
                String password = et_rpassword.getText().toString().trim();
                String password2 = et_rpassword2.getText().toString().trim();


                if (et_remail.getText().toString().isEmpty()) {
                    et_remail.setError("This field is required!");
                    et_remail.requestFocus();
                } else if (!(isValidEmail(et_remail.getText().toString().trim()))) {
                    et_remail.setError("Email not Valid!");
                    et_remail.requestFocus();
                } else if (et_rpassword.getText().toString().isEmpty()) {
                    et_rpassword.setError("This field is required!");
                    et_rpassword.requestFocus();
                } else if (et_rpassword.getText().toString().trim().length() < 8) {
                    et_rpassword.setError("Password length minimum is 8");
                    et_rpassword.requestFocus();
                } else if (et_rpassword2.getText().toString().isEmpty()) {
                    et_rpassword2.setError("This field is required!");
                    et_rpassword2.requestFocus();
                } else if (!(et_rpassword2.getText().toString().equals(et_rpassword.getText().toString()))) {
                    et_rpassword2.setError("Password and Confirm Password must Equal!");
                    et_rpassword2.requestFocus();
                } else {
                    pb_register.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent!", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                pb_register.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                    Toast.makeText(RegisterActivity.this, "Your Password is Weak!", Toast.LENGTH_SHORT).show();
                                    et_rpassword.setError("Change your password for the Stronger one!");
                                    et_rpassword.requestFocus();
                                    pb_register.setVisibility(View.GONE);
                                } catch(FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Toast.makeText(RegisterActivity.this, "Email format is Invalid!", Toast.LENGTH_SHORT).show();
                                    et_remail.setError("Retype your email to valid Format!");
                                    et_remail.requestFocus();
                                    pb_register.setVisibility(View.GONE);
                                } catch(FirebaseAuthUserCollisionException existEmail) {
                                    Toast.makeText(RegisterActivity.this, "Email already taken!", Toast.LENGTH_SHORT).show();
                                    et_remail.setError("Change another email!");
                                    et_remail.requestFocus();
                                    pb_register.setVisibility(View.GONE);
                                }catch (Exception e) {

                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Register Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
