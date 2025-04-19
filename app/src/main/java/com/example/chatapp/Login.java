package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,pass;
    Button signinbtn;
    TextView ifsignup;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();

        email=findViewById(R.id.loginemail);
        pass=findViewById(R.id.logipass);
        signinbtn=findViewById(R.id.sign_Inbtn);
        ifsignup=findViewById(R.id.ifalradysignup);

        signinbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email_s=email.getText().toString();
                String pass_s=pass.getText().toString();

                 auth.signInWithEmailAndPassword(email_s,pass_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             Intent intent=new Intent(Login.this,Home.class);

                             startActivity(intent);

                         }
                         else {
                             email.setError("invalid");
                             pass.setError("invalid");
                             Toast.makeText(Login.this, "Invalid data", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

            }
        });

        ifsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}