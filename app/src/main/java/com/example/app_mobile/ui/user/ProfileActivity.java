package com.example.app_mobile.ui.user;

import android.os.Bundle;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app_mobile.ui.home.MainActivity;
import com.example.app_mobile.R;
import com.example.app_mobile.ui.history.HistoriqueActivity;
import com.example.app_mobile.ui.home.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startNewActivity(MainActivity.class);
                return true;
            } else if (itemId == R.id.bottom_search) {
                startNewActivity(SearchActivity.class);
                return true;
            } else if (itemId == R.id.bottom_settings) {
                startNewActivity(HistoriqueActivity.class);
                return true;
            } else if (itemId == R.id.bottom_profile) {

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