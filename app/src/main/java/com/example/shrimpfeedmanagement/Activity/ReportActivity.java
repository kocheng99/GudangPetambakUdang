package com.example.shrimpfeedmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.shrimpfeedmanagement.Fragment.ReportTab1;
import com.example.shrimpfeedmanagement.Fragment.ReportTab2;
import com.example.shrimpfeedmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private TabLayout tab;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // inisialisasi view pager
        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);

        tab.setupWithViewPager(viewPager);
        SetupViewPager();
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // inisialisasi bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set report selected
        bottomNavigationView.setSelectedItemId(R.id.report);

        //perform itemselected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId() ) {
                    case R.id.almanac:
                        startActivity(new Intent(getApplicationContext()
                                ,AlmanacActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.items:
                        startActivity(new Intent(getApplicationContext()
                                , HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//                    case R.id.user:
//                        startActivity(new Intent(getApplicationContext()
//                                , UserActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.report:
                        return true;
                }
                return false;
            }
        });

    }

    private void SetupViewPager() {

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new ReportTab1(), "Report 1");
        adapter.AddFragment(new ReportTab2(), "Report 2");
        viewPager.setAdapter(adapter);

    }
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fr = new ArrayList<>();
        private List<String> title = new ArrayList<>();
        public MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void AddFragment(Fragment fragment, String jd) {
            fr.add(fragment);
            this.title.add(jd);
        }

        @Override
        public Fragment getItem(int position){
            return fr.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return title.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



}
