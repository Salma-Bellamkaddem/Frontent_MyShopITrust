package com.example.app_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.R;
import com.example.app_mobile.ui.history.HistoriqueActivity;
import com.example.app_mobile.ui.user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startNewActivity(MainActivity.class);
                return true;
            } else if (itemId == R.id.bottom_search) {

                return true;
            } else if (itemId == R.id.bottom_settings) {
                startNewActivity(HistoriqueActivity.class);
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startNewActivity(ProfileActivity.class);
                return true;
            }
            return false;
        });


    }


    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    }