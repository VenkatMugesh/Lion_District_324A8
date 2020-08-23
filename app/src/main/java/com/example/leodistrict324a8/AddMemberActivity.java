package com.example.leodistrict324a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMemberActivity extends AppCompatActivity {

    EditText postMember , nameMember , emailMember , addressMember;
    Button addMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        postMember = findViewById(R.id.post_member);
        nameMember = findViewById(R.id.name_member);
        emailMember = findViewById(R.id.email_member);
        addressMember = findViewById(R.id.address_member);
        addMember = findViewById(R.id.add_button);

        final String val = getIntent().getExtras().get("key").toString();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Leoistic").child(val).child("members");

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = postMember.getText().toString();
                String name = nameMember.getText().toString();
                String email = emailMember.getText().toString();
                String address = addressMember.getText().toString();

                String id  = reference.push().getKey();

                HashMap<String , Object> map = new HashMap<>();
                map.put("name" , name);
                map.put("post" , post);
                map.put("id" , id);
                map.put("parent" , val);
                map.put("emailId" , email);
                map.put("address" , address);
                map.put("imageUrl" , "");

                reference.child(id).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddMemberActivity.this, "Member Added Successfully..!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(AddMemberActivity.this , ClubEditActivity.class);
                        i.putExtra("key"  , val);
                        startActivity(i);
                        finish();
                    }
                });

            }
        });
    }
}