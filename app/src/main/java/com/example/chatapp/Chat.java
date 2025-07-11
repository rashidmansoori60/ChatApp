package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

Toolbar toolbar;
TextView toolbartxt;
EditText messageedt;
CircleImageView toolbarimg;
ImageButton sendbtn;
RecyclerView recyclerView;
FirebaseAuth firebaseAuth;
ChatAdapter adapterchat;
String recieveruid;
String senderuid;
String senderroom;
String recieverroom;

ArrayList<Message> arr;
public static String revieverimage;
public static String senderimage;
ImageView backinfo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backinfo=findViewById(R.id.backinfo);
        backinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(Chat.this, Home.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        arr=new ArrayList<>();
        sendbtn=findViewById(R.id.sendbutton);
        messageedt=findViewById(R.id.messageedt);

        firebaseAuth= FirebaseAuth.getInstance();
        String name=getIntent().getStringExtra("name");
        revieverimage=getIntent().getStringExtra("image");

        recieveruid=getIntent().getStringExtra("uid");
       senderuid= firebaseAuth.getUid();

toolbar=findViewById(R.id.toolbar);
toolbartxt=findViewById(R.id.toolbartext);
toolbarimg=findViewById(R.id.toolbarimage);
recyclerView=findViewById(R.id.chatrecycler);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
linearLayoutManager.setStackFromEnd(true);
recyclerView.setLayoutManager(linearLayoutManager);
adapterchat=new ChatAdapter(this,arr,recieveruid);
recyclerView.setAdapter(adapterchat);
recyclerView.scrollToPosition(arr.size() - 1);



setSupportActionBar(toolbar);

      toolbartxt.setText(name);
        Picasso.get().load(revieverimage).into(toolbarimg);
        senderroom=senderuid+recieveruid;
        recieverroom=recieveruid+senderuid;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getUid());
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Chat").child(senderroom).child("Message");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arr.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message message=dataSnapshot.getValue(Message.class);
                    message.setMessageid(dataSnapshot.getKey());
                    arr.add(message);
                    recyclerView.scrollToPosition(arr.size() - 1);


                }
                adapterchat.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        senderimage=snapshot.child("imageurl").getValue().toString();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

    sendbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(messageedt.getText().toString().equals(null)){
                Toast.makeText(Chat.this, "fill massage", Toast.LENGTH_SHORT).show();
            }
            else {
                Date date=new Date();
                Message message=new Message(date.getTime(),messageedt.getText().toString(),senderuid);

                messageedt.setText("");
                FirebaseDatabase.getInstance().getReference().child("Chat").child(senderroom).child("Message")
                        .push()
                        .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseDatabase.getInstance().getReference().child("Chat").child(recieverroom)
                                        .child("Message")
                                        .push()
                                        .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });


            }
        }
    });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Chat.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}