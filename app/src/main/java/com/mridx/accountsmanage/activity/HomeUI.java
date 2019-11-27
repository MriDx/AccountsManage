package com.mridx.accountsmanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mridx.accountsmanage.MainActivity;
import com.mridx.accountsmanage.R;
import com.mridx.accountsmanage.fragment.Dashboard;
import com.mridx.accountsmanage.fragment.Profile;

public class HomeUI extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_ui);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new Dashboard());


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.home:
                fragment = new Dashboard();
                break;
            case R.id.profile:
                fragment = new Profile();
                break;
            default:
                fragment = new Dashboard();
        }

        return loadFragment(fragment);
    }

}
