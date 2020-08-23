package com.example.leodistrict324a8.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leodistrict324a8.AddActivity;
import com.example.leodistrict324a8.Holders.MemberHolder;
import com.example.leodistrict324a8.Model.Member;
import com.example.leodistrict324a8.PostViewActivity;
import com.example.leodistrict324a8.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemberAdapter extends RecyclerView.Adapter<MemberHolder> {

    Context c;
    List<Member> memberList;

    public  MemberAdapter(Context c , List<Member> memberList){
        this.c = c;
        this.memberList = memberList;
    }
    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.member_item , parent , false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberHolder holder, int position) {

        final Member member = memberList.get(position);

        holder.postText.setText(member.getPost());
        holder.emailText.setText(member.getEmailId());
        holder.addressText.setText(member.getAddress());
        holder.nameText.setText(member.getName());

        final DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference("Leoistic").child(member.getParent()).child("members").child(member.getId());

        holder.memberCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child("Admin");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUid()).exists()) {
                            holder.deleteIcon.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUid()).exists()){
                            final Dialog dialog = new Dialog(c);
                            dialog.setContentView(R.layout.add_new_dialog);
                            final EditText nameText , postTest , addressText , emailText ;
                            Button updateButton;

                            nameText = dialog.findViewById(R.id.name_member);
                            postTest = dialog.findViewById(R.id.post_member);
                            addressText = dialog.findViewById(R.id.address_member);
                            emailText = dialog.findViewById(R.id.email_member);
                            updateButton = dialog.findViewById(R.id.update_button);


                            postTest.setText(member.getPost());
                            emailText.setText(member.getEmailId());
                            addressText.setText(member.getAddress());
                            nameText.setText(member.getName());

                            updateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String post = postTest.getText().toString();
                                    String name = nameText.getText().toString();
                                    String email = emailText.getText().toString();
                                    String address = addressText.getText().toString();


                                    HashMap<String , Object> map = new HashMap<>();
                                    map.put("name" , name);
                                    map.put("post" , post);
                                    map.put("emailId" , email);
                                    map.put("address" , address);
                                    map.put("imageUrl" , "");

                                    reference1.updateChildren(map);
                                    Toast.makeText(c, "Updated..!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.splash_dialog);
                Button yesBtn = dialog.findViewById(R.id.yes_button);
                Button noBtn = dialog.findViewById(R.id.no_button);

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
