package com.example.app_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.R;
import com.example.app_mobile.ui.history.HistoriqueActivity;
import com.example.app_mobile.ui.supermarche.SupermarcheActivity;
import com.example.app_mobile.ui.ticket.ScanActivity;

import com.example.app_mobile.ui.user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView elecImage = findViewById(R.id.elecImage);
        ImageView productlist = findViewById(R.id.beautyImage);
        ImageView scannImage = findViewById(R.id.scannImage);
        elecImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers une nouvelle activité affichant la liste des types de supermarchés
                Intent intent = new Intent(MainActivity.this, SupermarcheActivity.class);
                startActivity(intent);
            }
        });
        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers une nouvelle activité affichant la liste des types de supermarchés
                Intent intent = new Intent(MainActivity.this, SupermarcheActivity.class);
                startActivity(intent);
            }
        });
        scannImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers une nouvelle activité affichant la liste des types de supermarchés
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                // Home item selected, do nothing or navigate to home
                return true;
            } else if (itemId == R.id.bottom_search) {
                startNewActivity(SearchActivity.class);
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