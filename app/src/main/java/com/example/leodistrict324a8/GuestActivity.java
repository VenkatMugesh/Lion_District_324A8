package com.example.leodistrict324a8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.leodistrict324a8.Fragments.AboutUsFragment;
import com.example.leodistrict324a8.Fragments.ActivitiesFragment;
import com.example.leodistrict324a8.Fragments.PeopleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class GuestActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser().getUid().equals("xzociiJbrIUt4b0BKpOqU2V2ajp1"))
            auth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PeopleFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i) {
                    case R.id.bottom_activity:
                        fragment = new ActivitiesFragment();
                        break;
                    case R.id.bottom_people:
                        fragment = new PeopleFragment();
                        break;
                    case R.id.bottom_about_us:
                        fragment = new AboutUsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}