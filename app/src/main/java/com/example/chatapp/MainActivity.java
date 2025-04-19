package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
EditText name,email,pass,conpass;
Button singupBtn;
TextView ifsignin;
FirebaseAuth auth;
String status="hey there i am using this app";
String imageuri="https://media-hosting.imagekit.io/c3b98c5848464364/rashid.jpg?Expires=1838488608&Key-Pair-Id=K2ZIVPTIP2VGHC&Signature=uzwfEF6DY3nam1JiBFWNwG~CSqIeDGQDO7FAYbxZC0dC2PWzex18wdrS0JVu9GnviL7GfuGYwiaucsfxhUUf-sX85BZpwQ9rKOtchxFyr7iw4x2BKtffMZDrzBqMnvWLXIpFPSCPxjk0hIea0pPhfAW2MMTXI3AUy5~-liFne1O3Vh3YODv1vAoTXC8ym05fE6a8Ac5ZkpW6V-DweHWhQnBvgFIq2jXFcpniC4yLNx0pCUdAeAWAI~2gDR5JdxqeMJ8f~nIzFMxObIFRSfEpygw16g-jzrU1BER16DwIIwtoECrKVg5VzSesr5lfcJhTU5R2aPFCcdeEoQp2QxEPzA__";
FirebaseDatabase firebaseDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        name=findViewById(R.id.ragistername);
        email=findViewById(R.id.ragisteremail);
        pass=findViewById(R.id.ragisterpass);
        conpass=findViewById(R.id.ragishterconpassword);
        ifsignin=findViewById(R.id.ifalradysignin);
        singupBtn=findViewById(R.id.sign_upbtn);
        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,Home.class);

            startActivity(intent);
            finish();
        }

        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           
            public void onClick(View view) {
                String email_s=email.getText().toString();
                String pass_s=pass.getText().toString();
                String name_s=name.getText().toString();
                String conpass_s=conpass.getText().toString();

                 if(!conpass_s.equals(pass_s)){
                    conpass.setError("Invalid password");
                    Toast.makeText(MainActivity.this, "Invalid password match", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name_s)||TextUtils.isEmpty(email_s)||TextUtils.isEmpty(pass_s)||TextUtils.isEmpty(conpass_s)) {
                    Toast.makeText(MainActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                } else if (pass_s.length()<6) {
                     pass.setError("Invalid password");
                    Toast.makeText(MainActivity.this, "Fill Minimun six cherecter in password", Toast.LENGTH_SHORT).show();
                    
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_s).matches()) {
                     email.setError("Invalid email");
                    Toast.makeText(MainActivity.this, "Fill Valid email", Toast.LENGTH_SHORT).show();

                }

                  else {
                     auth.createUserWithEmailAndPassword(email_s,pass_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 User user=new User(name_s,email_s,imageuri,auth.getUid(),pass_s,status);

                                 firebaseDatabase.getReference().child("User").child(auth.getUid())
                                         .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful()){
                                                     Intent intent=new Intent(MainActivity.this,Login.class);
                                                     startActivity(intent);
                                                     Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                                 }
                                                 else {

                                                 }
                                             }
                                         });


                             }
                             else {
                                 Toast.makeText(MainActivity.this, "Error to creation", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }

            }
        });

        ifsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

}