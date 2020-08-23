package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberLogin extends AppCompatActivity {

    EditText userName, passWord;
    Button loginButton;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        userName = findViewById(R.id.user_name);
        passWord = findViewById(R.id.pass_word);
        loginButton = findViewById(R.id.login_button);

        final String post = getIntent().getExtras().get("user").toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(post).child(user.getUid());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    final ProgressDialog pd = new ProgressDialog(MemberLogin.this, R.style.AppTheme_Dark_Dialog);
                    pd.setMessage("Logging In..");
                    pd.show();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String uName = snapshot.child("memId").getValue(String.class);
                            String pWord = snapshot.child("dob").getValue(String.class);

                            String userVal = userName.getText().toString();
                            String pass = passWord.getText().toString();

                            if (uName.equals(userVal) && pWord.equals(pass)) {
                                pd.dismiss();
                                if(post.equals("Member")){
                                Intent i = new Intent(MemberLogin.this, GuestActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                }else{
                                    Intent i = new Intent(MemberLogin.this, PresidentLandinActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(MemberLogin.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    public boolean isValid () {
        boolean valid = true;

        String name = userName.getText().toString();
        String pWord = passWord.getText().toString();

        if (name.isEmpty()) {
            userName.setError("User name cannot be empty!!");
            valid = false;
        }
        if (pWord.isEmpty()) {
            passWord.setError("Password cannot be empty!!");
            valid = false;
        }

        return valid;
    }
}