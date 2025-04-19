package com.example.chatapp;

import static com.example.chatapp.Chat.revieverimage;
import static com.example.chatapp.Chat.senderimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {

    int sender = 1;
    int reciever = 2;
    Context context;
    ArrayList<Message> arr;
    String recid;

    public ChatAdapter(Context context, ArrayList<Message> arr,  String recid) {
        this.arr = arr;
        this.context = context;
        this.recid=recid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == sender) {
            View v = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
            return new SenderViewHolder(v);

        }

        View v = LayoutInflater.from(context).inflate(R.layout.reciever, parent, false);
        return new RecieverViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Message message = arr.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;

            senderViewHolder.sendertxt.setText(message.getMsg());
            Picasso.get().load(senderimage).into(senderViewHolder.senderprofile);
        } else {
            RecieverViewHolder recieverViewHolder = (RecieverViewHolder) holder;
            recieverViewHolder.recievertxt.setText(message.getMsg());
            Picasso.get().load(revieverimage).into(recieverViewHolder.recieveprofile);
        }

 holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
     @Override
     public boolean onLongClick(View view) {
         AlertDialog.Builder builder=new AlertDialog.Builder(context);
         builder.setTitle("Delete");
         builder.setMessage("Are you sure want to delete message");
         builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
             @SuppressLint("NotifyDataSetChanged")
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().child("Chat").child(FirebaseAuth.getInstance().getUid()+recid).child("Message");
                 databaseReference1.child(message.getMessageid()).setValue(null);



             }
         });
         builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
         builder.show();
         return true;
     }
 });
    }


    @Override
    public int getItemCount() {
        return arr.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = arr.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderuid())) {
            return sender;
        }
        return reciever;


    }

    class RecieverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView recieveprofile;
        TextView recievertxt;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieveprofile = itemView.findViewById(R.id.reimg);
            recievertxt = itemView.findViewById(R.id.retxt);
        }
    }
    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView senderprofile;
        TextView sendertxt;

        public SenderViewHolder(@NonNull View itemView) {

            super(itemView);
            senderprofile = itemView.findViewById(R.id.senderimg);
            sendertxt = itemView.findViewById(R.id.sendertxt);
        }
    }

}
