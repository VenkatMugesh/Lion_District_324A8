package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.leodistrict324a8.Adapters.MemberAdapter;
import com.example.leodistrict324a8.Model.Member;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubEditActivity extends AppCompatActivity {


    EditText clubText , addressText;
    ImageView logoImage;
    TextView changeLogo , addMember;
    Button saveButton;
    RecyclerView clubRecycler;
    List<Member> mMembers;
    MemberAdapter memberAdapter;
    String val = "1";

    DatabaseReference reference;

    int flag =0;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference ref;


    public final static int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_edit);

        clubText = findViewById(R.id.club_name);
        logoImage = findViewById(R.id.club_logo);
        clubRecycler = findViewById(R.id.member_recycler);
        addressText = findViewById(R.id.address_edit_text);
        changeLogo = findViewById(R.id.change_logo);
        addMember = findViewById(R.id.add_new_member);
        saveButton = findViewById(R.id.save_button);


        storage = FirebaseStorage.getInstance();
        ref = storage.getReference("leoistic_logo");

        val = getIntent().getExtras().get("key").toString();
        reference = FirebaseDatabase.getInstance().getReference("Leoistic").child(val);

        clubRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new GridLayoutManager(this , 1);
        clubRecycler.setLayoutManager(linearLayoutManager);
        mMembers = new ArrayList<>();
        memberAdapter = new MemberAdapter(this , mMembers);
        clubRecycler.setAdapter(memberAdapter);

        reference.child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMembers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Member member = dataSnapshot.getValue(Member.class);
                    mMembers.add(member);
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClubEditActivity.this , AddMemberActivity.class);
                i.putExtra("key"  , val);
                startActivity(i);
            }
        });

        changeLogo.setOnClickListener(new View.OnClickListener() {
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag ==0){
                    String club = clubText.getText().toString();
                    String address = addressText.getText().toString();

                    HashMap<String , Object> map = new HashMap<>();

                    map.put("name" , club);
                    map.put("address" , address);

                    reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ClubEditActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ClubEditActivity.this , EditInfoActivity.class));
                            finishAffinity();
                        }
                    });
                }else{
                    uploadImage();
                }
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);

                clubText.setText(name);
                addressText.setText(address);
                Picasso.get().load(imageUrl).into(logoImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

            final ProgressDialog pd = new ProgressDialog(ClubEditActivity.this, R.style.AppTheme_Dark_Dialog);
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
                            String name = clubText.getText().toString();
                            String address = addressText.getText().toString();

                            HashMap<String, Object> map = new HashMap<>();

                            map.put("name", name);
                            map.put("address", address);
                            map.put("imageUrl", imageUrl);
                            reference.updateChildren(map);
                            pd.dismiss();

                            Toast.makeText(ClubEditActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ClubEditActivity.this , EditInfoActivity.class));
                            finishAffinity();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ClubEditActivity.this, "Upload Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                logoImage.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something Gone Wrong!", Toast.LENGTH_SHORT).show();
        }

    }

}