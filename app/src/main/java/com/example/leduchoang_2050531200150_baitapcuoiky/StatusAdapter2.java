package com.example.leduchoang_2050531200150_baitapcuoiky;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter2 extends FirebaseRecyclerAdapter<Status, StatusAdapter2.myViewHolder> {
    private Context context;private ArrayList<Status> imageModelArrayList;

    public StatusAdapter2(@NonNull FirebaseRecyclerOptions<Status> options, Context context, ArrayList<Status> imageModelArrayList) {
        super(options);
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StatusAdapter2(@NonNull FirebaseRecyclerOptions<Status> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Status model) {
        holder.name.setText(model.getName());
        holder.singel.setText(model.getNameSingel());
        holder.time.setText(model.getTime());

        Glide.with(holder.img.getContext())
                .load(model.getImg())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.updatemusic))
                        .setExpanded(true,1200)
                        .create();

                dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText name = view1.findViewById(R.id.txtName);
                EditText nameSingel = view1.findViewById(R.id.txtNamesingel);
                EditText time = view1.findViewById(R.id.txtTime);
                EditText img = view1.findViewById(R.id.txtimg);

                Button btnupdate = view1.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                nameSingel.setText(model.getNameSingel());
                time.setText(model.getTime());
                img.setText(model.getImg());

                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("nameSingel", nameSingel.getText().toString());
                        map.put("time",time.getText().toString());
                        map.put("img", img.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Status")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "Data Update Successfully,", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Error Update", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Delete data can't be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Status")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.name.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item2, parent, false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, singel, time;
        ImageButton btnEdit, btnDelete;
        RelativeLayout layoutItem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem=(RelativeLayout)itemView.findViewById(R.id.layoutItem);
            img = (ImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.name);
            singel = (TextView) itemView.findViewById(R.id.nameSingel);
            time = (TextView) itemView.findViewById(R.id.time);

            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);



        }
    }
/*
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Glide
                .with(context)
                .load(imageModelArrayList.get(position).getImg())
                //.centerCrop()
                //.placeholder(R.mipmap.ic_launcher_round)
                .into(holder.img);
        holder.name.setText(imageModelArrayList.get(position).getName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MainActivity4.class);
                intent.putExtra("images@#",imageModelArrayList.get(position).getImg());
                intent.putExtra("name@#",imageModelArrayList.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    } public int getItemCount() {
        return imageModelArrayList.size();
    }*/
}
