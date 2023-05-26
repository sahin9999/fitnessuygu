package com.example.fffproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText txtadi, txtemail, txtsifre;
    private Button btnkayit, btngiris;

    private String adi,email,sifre;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;

    private HashMap<String,Object> mData;
    Intent intent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtadi=findViewById(R.id.editTextTextPersonName);
        txtemail=findViewById(R.id.editTextTextEmailAddress);
        txtsifre=findViewById(R.id.editTextTextPassword);
        btnkayit=findViewById(R.id.kayitOl);
        btngiris=findViewById(R.id.giris);
        mRef= FirebaseDatabase.getInstance().getReference();


        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adi=txtadi.getText().toString();
                email=txtemail.getText().toString();
                sifre=txtsifre.getText().toString();

                if (!TextUtils.isEmpty(adi)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(sifre)){
                    mAuth.createUserWithEmailAndPassword(email,sifre)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(MainActivity.this, "Firebase kayıt yapıldı.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adi=txtadi.getText().toString();
                email=txtemail.getText().toString();
                sifre=txtsifre.getText().toString();

                if (!TextUtils.isEmpty(adi)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(sifre)){
                    mAuth.signInWithEmailAndPassword(email,sifre)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mUser=mAuth.getCurrentUser();
                                    mData= new HashMap<>();
                                    mData.put("Kullanıcı Adı:",adi);
                                    mData.put("Kullanıcı E-mail:",email);
                                    mData.put("Kullanıcı Şifre:",sifre);
                                    mData.put("Kullanıcı ID:",mUser.getUid());
                                    mRef.child("Kullanıcılar").child(mUser.getUid())
                                            .setValue(mData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MainActivity.this, "Veri Tabanına Kayıt Edildi.", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    Toast.makeText(MainActivity.this, "Giriş yapıldı", Toast.LENGTH_SHORT).show();
                                    intent=new Intent(MainActivity.this, MainActivity2.class);
                                    startActivity(intent);
                                }
                            });
                }

            }
        });


        mAuth=FirebaseAuth.getInstance();
    }
}