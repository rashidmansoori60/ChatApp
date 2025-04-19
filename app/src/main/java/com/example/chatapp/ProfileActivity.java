package com.example.chatapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

CircleImageView profileactimage ,right;
Toolbar toolbarrr;
TextView name,status;
String proname,proimage,prostatus;
EditText editname,editstatus;
Button logout;
String editnamestring,editstatusstring;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logout=findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure want to logout");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(ProfileActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        name=findViewById(R.id.nameValue);
        status=findViewById(R.id.aboutValue);
        profileactimage=findViewById(R.id.profileimageid);
        toolbarrr=findViewById(R.id.profiletoolbar);
        proname=getIntent().getStringExtra("profilename");
        proimage=getIntent().getStringExtra("profileimage");
        prostatus=getIntent().getStringExtra("profilestatus");


        Picasso.get().load(proimage).into(profileactimage);
        name.setText(proname);
        status.setText(prostatus);
        setSupportActionBar(toolbarrr);
        getSupportActionBar().setTitle("Setting");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.editname);
                dialog.show();
                right=dialog.findViewById(R.id.right);
                editstatus=dialog.findViewById(R.id.editnamee);
                editstatus.setHint("Enter status");
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String,Object> map=new HashMap<>();
                        dialog.dismiss();

                        editstatusstring=editstatus.getText().toString();

                        status.setText(editstatusstring);
                        map.put("status",editstatusstring);
                        FirebaseDatabase.getInstance().getReference().child("User")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ProfileActivity.this, "status update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                });
            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.editname);
                dialog.show();
                right=dialog.findViewById(R.id.right);
                editname=dialog.findViewById(R.id.editnamee);
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String,Object> map=new HashMap<>();
                        dialog.dismiss();
                        editnamestring=editname.getText().toString();
                        name.setText(editnamestring);
                        map.put("name",editnamestring);
                        FirebaseDatabase.getInstance().getReference().child("User")
                                .child(FirebaseAuth.getInstance().getUid())
                                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ProfileActivity.this, "name update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                });
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}