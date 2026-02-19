package com.example.flavor.presentation.main.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.flavor.R;
import com.example.flavor.presentation.main.favorites.FavoritesFragment;
import com.example.flavor.presentation.main.home.HomeFragment;
import com.example.flavor.presentation.main.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // 1. Load the HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // 2. Handle Bottom Navigation Clicks
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_placeholder) return false;

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_explore) {
                // selectedFragment = new ExploreFragment();
            }else if(id == R.id.nav_favorites){
                selectedFragment = new FavoritesFragment();
            }else if(id == R.id.nav_profile){
            //     selectedFragment = new ProfileFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}