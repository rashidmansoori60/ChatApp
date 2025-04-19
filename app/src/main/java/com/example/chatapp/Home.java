package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;



    BottomNavigationView bottomNavigationView;
    public static String homeimage;
    FirebaseDatabase firebaseDatabase;
    ImageView circleImageView;
    String homeimagestring,profilename,profilestatus;
    FrameLayout frameLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        circleImageView=findViewById(R.id.homeimggg);
        frameLayout=findViewById(R.id.framelayout);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        rep(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.chat){
                    rep(new HomeFragment());

                } else if (item.getItemId() == R.id.update) {
                    rep(new UpdateFragment());
                } else if (item.getItemId() == R.id.call) {
                    rep(new CallFragment());
                }

                return true;
            }
        });

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null){
            startActivity(new Intent(Home.this, Login.class));
        }

        firebaseDatabase=FirebaseDatabase.getInstance();


       FirebaseDatabase.getInstance().getReference().child("User").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             homeimagestring=snapshot.child("imageurl").getValue().toString();
             profilename=snapshot.child("name").getValue().toString();
             profilestatus=snapshot.child("status").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,ProfileActivity.class);
                i.putExtra("profileimage",homeimagestring);
                i.putExtra("profilename",profilename);
                i.putExtra("profilestatus",profilestatus);

                startActivity(i);

            }
        });



    }
    public void rep(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}