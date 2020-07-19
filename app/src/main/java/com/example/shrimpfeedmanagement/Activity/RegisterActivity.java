package com.example.shrimpfeedmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shrimpfeedmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView user_image;
    static int PReCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;

    private EditText userName, userEmail, userPassword, userPassword2;
    private Button regBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_image = findViewById(R.id.user_image);
        userName = findViewById(R.id.name);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        userPassword2 = findViewById(R.id.confirm_password);
        regBtn = findViewById(R.id.btn_daftar);

        mAuth = FirebaseAuth.getInstance();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regBtn.setVisibility(View.INVISIBLE);
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {

                    // show error message : all field must be filled
                    // display error message
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                }

                else {
                    // everything is ok and all field are filled, now start creating user akun
                    // createUserAccount method will try to create th euser account if the email is valid

                    createUserAccount(name, email, password);

                }

            }
        });

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }

            }
        });

    }

    private void createUserAccount(final String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // user accoutn created successfully
                            showMessage("Akun berhasil di buat");
                            // after we created user account we need to update the profile pict and name
                            updateUserInfo(name, pickedImageUri, mAuth.getCurrentUser());
                        }

                        else {

                            // account creation failed
                            showMessage("Akun gagal di buat" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                        }

                    }
                });

    }

    // update user photo and name
    private void updateUserInfo(final String name, Uri pickedImageUri, final FirebaseUser currentUser) {

        // first : upload photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user-photos");
        final StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded successfully
                // now we can get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain image url
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            showMessage("Registrasi Selesai");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });

            }
        });

    }

    private void updateUI() {

        Intent homeActivity = new Intent(this, com.example.shrimpfeedmanagement.Activity.HomeActivity.class);
        startActivity(homeActivity);
        finish();

    }

    // simple method to show toast method
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void openGallery(){
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);

    }

    public void checkAndRequestForPermission(){

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    PReCode);
            }
        }

        else {
            openGallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {

            // teh user has successfull picked an image
            // we need to save its reference to a Uri variable
            pickedImageUri = data.getData();
            user_image.setImageURI(pickedImageUri);

        }

    }
}
