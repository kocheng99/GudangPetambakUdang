package com.example.shrimpfeedmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shrimpfeedmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    TextView userName, userEmail;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhoto = findViewById(R.id.user_photo);

        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());

        // to load user photo kita pakai glide
        Glide.with(this).load(currentUser.getPhotoUrl()).into(userPhoto);


    }
}
