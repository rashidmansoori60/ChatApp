package com.example.chatapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycleradapter extends FirebaseRecyclerAdapter<User, Recycleradapter.Viewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public Recycleradapter(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull User model) {
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getUniqid())){
            holder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        }
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.imagedialog);

        ImageView info= dialog.findViewById(R.id.infodialog);
        ImageView imagedialog=dialog.findViewById(R.id.imagedialog);
        TextView textViewdialog=dialog.findViewById(R.id.textdialog);
        textViewdialog.setText(model.getName());
        Picasso.get().load(model.getImageurl()).into(imagedialog);

imagedialog.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialog.dismiss();
        Intent intent=new Intent(context,ShowUserimage.class);
        intent.putExtra("name",model.getName());
        intent.putExtra("image",model.getImageurl());
        context.startActivity(intent);
    }
});
                dialog.show();
               info.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialog.dismiss();
                       Intent intent=new Intent(context,ShowUserimage.class);
                       intent.putExtra("name",model.getName());
                       intent.putExtra("image",model.getImageurl());
                       context.startActivity(intent);
                   }
               });
            }
        });
        holder.username.setText(model.getName());
        FirebaseDatabase.getInstance().getReference().child("Chat").child(FirebaseAuth.getInstance().getUid()+model.getUniqid())
                        .child("Message").orderByChild("date").limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()){
                                    for(DataSnapshot snapshot1:snapshot.getChildren()){

                                        holder.userstatus.setText(snapshot1.child("msg").getValue().toString());

                                    }

                                }else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        Picasso.get().load(model.getImageurl()).into(holder.circleImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Chat.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("image",model.getImageurl());
                intent.putExtra("uid",model.getUniqid());
                context.startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new Viewholder(view);
    }

    class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView username,userstatus;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            username=itemView.findViewById(R.id.username);
            userstatus=itemView.findViewById(R.id.userstatus);

        }
    }
}
