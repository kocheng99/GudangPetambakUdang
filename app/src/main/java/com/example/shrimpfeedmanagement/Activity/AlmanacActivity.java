package com.example.shrimpfeedmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.shrimpfeedmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AlmanacActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView card_udang, card_budidaya, card_virus, card_perawatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almanac);

        // inisialisasi bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // inisialisasi card button
        card_udang = findViewById(R.id.card_udang);
        card_budidaya = findViewById(R.id.card_budidaya);
        card_virus = findViewById(R.id.card_virus);
        card_perawatan = findViewById(R.id.card_perawatan);

        // click listener to the card
        card_udang.setOnClickListener(this);
        card_budidaya.setOnClickListener(this);
        card_virus.setOnClickListener(this);
        card_perawatan.setOnClickListener(this);

        // set home selected
        bottomNavigationView.setSelectedItemId(R.id.almanac);

        // perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId() ) {
                    case R.id.almanac:
                        return true;
                    case R.id.items:
                        startActivity(new Intent(getApplicationContext()
                                , HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.report:
                        startActivity(new Intent(getApplicationContext()
                                ,ReportActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//                    case R.id.user:
//                        startActivity(new Intent(getApplicationContext()
//                                , UserActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId() ) {
            case R.id.card_udang :
                i = new Intent(this, UdangActivity.class);
                startActivity(i);
                break;
            case R.id.card_budidaya:
                i = new Intent(this, BudidayaActivity.class);
                startActivity(i);
                break;
            case R.id.card_virus:
                i = new Intent(this, VirusActivity.class);
                startActivity(i);
                break;
            case R.id.card_perawatan:
                i = new Intent(this, PerawatanActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }


}
