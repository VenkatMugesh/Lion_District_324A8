package com.example.leodistrict324a8.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leodistrict324a8.AddActivity;
import com.example.leodistrict324a8.GuestActivity;
import com.example.leodistrict324a8.MainActivity;
import com.example.leodistrict324a8.PresidentLandinActivity;
import com.example.leodistrict324a8.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MemberRegisterFragment extends Fragment {

    AppCompatSpinner clubSpinner, postSpinner;
    ArrayAdapter clubAdapter, postAdapter;
    EditText nameText, memIdText, emailText, dobText, addressText;
    Button registerBtn;
    RadioGroup genderRadio;
    String gender = "male";
    DatabaseReference reference;
    FirebaseUser user;
    boolean checked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_register, container, false);

        clubSpinner = view.findViewById(R.id.club_dropdown);
        postSpinner = view.findViewById(R.id.post_dorpdown);
        genderRadio = view.findViewById(R.id.gender_radio);
        nameText = view.findViewById(R.id.member_name_text);
        memIdText = view.findViewById(R.id.mem_id_text);
        emailText = view.findViewById(R.id.email_id);
        dobText = view.findViewById(R.id.dob_text);
        addressText = view.findViewById(R.id.address_text);
        registerBtn = view.findViewById(R.id.register_button);

        user = FirebaseAuth.getInstance().getCurrentUser();


        clubAdapter = ArrayAdapter.createFromResource(getContext(), R.array.club_list, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner.setAdapter(clubAdapter);

        postAdapter = ArrayAdapter.createFromResource(getContext(), R.array.post_list, android.R.layout.simple_spinner_item);
        postAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postSpinner.setAdapter(postAdapter);

        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View btnView = radioGroup.findViewById(i);
                checked = ((RadioButton) btnView).isChecked();

                switch (btnView.getId()) {
                    case R.id.male_button:
                        if (checked) {
                            gender = "male";
                        }
                        break;
                    case R.id.female_button:
                        if (checked) {
                            gender = "female";
                        }
                        break;

                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    final ProgressDialog pd = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
                    pd.setIndeterminate(true);
                    pd.setMessage("Registering Please Wait..");
                    pd.show();

                    int postVal = postSpinner.getSelectedItemPosition();
                    int clubVal = clubSpinner.getSelectedItemPosition();

                    String email = emailText.getText().toString();
                    String name = nameText.getText().toString();
                    String memId = memIdText.getText().toString();
                    String address = addressText.getText().toString();
                    String dob = dobText.getText().toString();
                    String post = (String) postSpinner.getItemAtPosition(postVal);
                    String clubName = (String) clubSpinner.getItemAtPosition(clubVal);

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("dob", dob);
                    map.put("memId", memId);
                    map.put("email", email);
                    map.put("address", address);
                    map.put("gender", gender);
                    map.put("post", post);
                    map.put("club", clubName);

                    if (post.equals("President")) {

                        reference = FirebaseDatabase.getInstance().getReference("User").child("President")
                                .child(user.getUid());

                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Intent i = new Intent(getContext(), PresidentLandinActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            }
                        });

                    } else {

                        reference = FirebaseDatabase.getInstance().getReference("User").child("Member")
                                .child(user.getUid());

                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Intent i = new Intent(getContext(), GuestActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            }
                        });
                    }

                }
            }
        });

        return view;
    }

    private boolean validate() {
        boolean isValid = true;

        String email = emailText.getText().toString();
        String name = nameText.getText().toString();
        String memId = memIdText.getText().toString();
        String address = addressText.getText().toString();
        String dob = dobText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            isValid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            isValid = false;
        } else {
            emailText.setError(null);
        }

        if (memId.isEmpty()) {
            memIdText.setError("Cannot be empty");
            isValid = false;
        } else {
            memIdText.setError(null);
        }
        if (address.isEmpty()) {
            addressText.setError("Can not be empty");
            isValid = false;
        } else {
            addressText.setError(null);
        }
        if (dob.length() != 8) {
            dobText.setError("Enter a valid Date of Birth");
            isValid = false;
        } else {
            dobText.setError(null);
        }

        if (!checked) {
            Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        int selectedItemOfMySpinner = clubSpinner.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) clubSpinner.getItemAtPosition(selectedItemOfMySpinner);

        int val = postSpinner.getSelectedItemPosition();
        String positionVal = (String) postSpinner.getItemAtPosition(val);

        if (positionVal.isEmpty()) {
            Toast.makeText(getContext(), "itsEmpty", Toast.LENGTH_SHORT).show();
            setSpinnerError(postSpinner, "Select a post");
            isValid = false;
        }

        if (actualPositionOfMySpinner.isEmpty()) {
            setSpinnerError(clubSpinner, "Select a club");
            isValid = false;

        }
        return isValid;

    }


    private void setSpinnerError(AppCompatSpinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error");
            selectedTextView.setTextColor(Color.RED);
            selectedTextView.setText(error);
            spinner.performClick();

        }
    }

}