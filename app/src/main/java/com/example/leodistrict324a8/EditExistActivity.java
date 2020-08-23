package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditExistActivity extends AppCompatActivity {

    ImageView officerImage;
    TextView changeText;
    EditText nameEdit , postEdit;
    Button editButton;
    DatabaseReference reference;
    int flag =0;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference ref;


    public final static int PICK_IMAGE_REQUEST = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exist);
        officerImage = findViewById(R.id.add_image);
        changeText = findViewById(R.id.change_photo);
        nameEdit = findViewById(R.id.name_member);
        postEdit = findViewById(R.id.post_member);
        editButton = findViewById(R.id.edit_button);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference("district officers");

        String dist = getIntent().getExtras().get("child").toString();
        String toggle = getIntent().getExtras().get("value").toString();

        if (dist.equals("DistrictOfficers")){
            reference = FirebaseDatabase.getInstance().getReference(dist).child(toggle);
        }else if (dist.equals("LionMembers")){
            reference = FirebaseDatabase.getInstance().getReference("MainMembers").child(dist).child(toggle);
        }else if (dist.equals("DCLC")){
            reference = FirebaseDatabase.getInstance().getReference("MainMembers").child(dist);
        }else{
            reference = FirebaseDatabase.getInstance().getReference("MainMembers").child(dist);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String post = dataSnapshot.child("post").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                nameEdit.setText(name);
                postEdit.setText(post);
                Picasso.get().load(imageUrl).into(officerImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
                flag = 1;
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0){
                    String name = nameEdit.getText().toString();
                    String post = postEdit.getText().toString();

                    HashMap<String, Object> map = new HashMap<>();

                    map.put("post", post);
                    map.put("name", name);
                    reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditExistActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditExistActivity.this , EditInfoActivity.class));
                            finishAffinity();
                        }
                    });

                }
                else {
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

    private void uploadImage(){
        if (imageUri != null) {

            final ProgressDialog pd = new ProgressDialog(EditExistActivity.this, R.style.AppTheme_Dark_Dialog);
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
                            String name = nameEdit.getText().toString();
                            String post = postEdit.getText().toString();

                            HashMap<String, Object> map = new HashMap<>();

                            map.put("post", post);
                            map.put("name", name);
                            map.put("imageUrl", imageUrl);
                            reference.updateChildren(map);
                            pd.dismiss();

                            Toast.makeText(EditExistActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditExistActivity.this , EditInfoActivity.class));
                            finishAffinity();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditExistActivity.this, "Upload Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                officerImage.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something Gone Wrong!", Toast.LENGTH_SHORT).show();
        }

    }

}