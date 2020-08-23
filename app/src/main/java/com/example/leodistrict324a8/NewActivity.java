package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class NewActivity extends AppCompatActivity {

    EditText titleText, descTExt;
    AppCompatSpinner clubSpinner;
    Button postButton;
    ImageView addedImage;
    ArrayAdapter clubAdapter;
    Uri imageUri;
    StorageReference ref;
    FirebaseStorage storage;

    public final static int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        titleText = findViewById(R.id.title_text);
        descTExt = findViewById(R.id.desc_text);
        clubSpinner = findViewById(R.id.club_dropdown);
        postButton = findViewById(R.id.post_button);
        addedImage = findViewById(R.id.image_added);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference("activities");

        clubAdapter = ArrayAdapter.createFromResource(this, R.array.club_list, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner.setAdapter(clubAdapter);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {
                    uploadImage();
                }

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage() {
        if (imageUri != null) {

            final ProgressDialog pd = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            pd.setMessage("Uploading...");
            pd.show();

            final StorageReference storageReference = ref.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            String imageUrl = downloadUri.toString();
                            int val = clubSpinner.getSelectedItemPosition();
                            String club = (String) clubSpinner.getItemAtPosition(val);

                            String title = titleText.getText().toString();
                            String desc = descTExt.getText().toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activity");
                            String postId = reference.push().getKey();

                            HashMap<String, Object> map = new HashMap<>();

                            map.put("postId", postId);
                            map.put("title", title);
                            map.put("club", club);
                            map.put("desc", desc);
                            map.put("imageUrl", imageUrl);
                            map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reference.child(postId).setValue(map);
                            pd.dismiss();

                            startActivity(new Intent(NewActivity.this, AddActivity.class));
                            finishAffinity();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewActivity.this, "Upload Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    pd.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                }
            });
        }
    }

    private boolean isValid() {
        boolean valid = true;

        String title = titleText.getText().toString();
        String desc = descTExt.getText().toString();

        if (title.isEmpty()) {
            titleText.setError("Can not be empty");
            valid = false;
        } else {
            titleText.setError(null);
        }

        if (desc.isEmpty()) {
            descTExt.setError("Can not be empty");
            valid = false;
        } else {
            descTExt.setError(null);
        }

        int val = clubSpinner.getSelectedItemPosition();
        String positionVal = (String) clubSpinner.getItemAtPosition(val);

        if (positionVal.isEmpty()) {
            setSpinnerError(clubSpinner, "Select a post");
            valid = false;
        }
        return valid;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                addedImage.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something Gone Wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NewActivity.this, AddActivity.class));
            finish();
        }

    }
}