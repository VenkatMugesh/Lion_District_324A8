package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import org.w3c.dom.Text;

public class StartActivity extends AppCompatActivity {

    Button verifyButton;
    TextView guestText;
    CountryCodePicker countryCodePicker;
    EditText phoneNumberEdit;
    FirebaseUser user;
    FirebaseAuth auth;
    String email = "venkatmugesh7@gmail.com";
    String password = "Venki123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        verifyButton = findViewById(R.id.verify_button);
        guestText = findViewById(R.id.guest_enter);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumberEdit = findViewById(R.id.number_text);


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateNumber()) {
                    String pNo = "+" + countryCodePicker.getSelectedCountryCode() + phoneNumberEdit.getText().toString();
                    Intent i = new Intent(StartActivity.this, OtpActivity.class);
                    i.putExtra("phoneNo", pNo);
                    startActivity(i);
                }
            }
        });
        guestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent i = new Intent(StartActivity.this, GuestActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(StartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }


    private boolean validateNumber() {
        boolean validate = true;
        String userPhone = phoneNumberEdit.getText().toString();

        if (userPhone.isEmpty() || userPhone.length() < 10) {
            phoneNumberEdit.setError("Enter a valid phone Number");
            validate = false;
        }

        return validate;
    }

}