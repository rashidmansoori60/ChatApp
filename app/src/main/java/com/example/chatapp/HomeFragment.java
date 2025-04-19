package com.example.chatapp;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;





public class HomeFragment extends Fragment {
View view;

RecyclerView recyclerView;
Recycleradapter recycleradapter;



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_home, container, false);

         recyclerView=view.findViewById(R.id.framrecyler);

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), User.class)
                        .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleradapter=new Recycleradapter(options,getContext());
        recyclerView.setAdapter(recycleradapter);


         return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        recycleradapter.startListening();
    }


}