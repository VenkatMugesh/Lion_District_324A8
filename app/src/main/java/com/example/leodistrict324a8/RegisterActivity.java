package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.leodistrict324a8.Fragments.AdminRegisterFragment;
import com.example.leodistrict324a8.Fragments.MemberRegisterFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    Toolbar registerToolbar;
    TabLayout registerTab;
    ViewPager registerPager;

    MemberRegisterFragment memberRegisterFragment;
    AdminRegisterFragment adminRegisterFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerToolbar = findViewById(R.id.register_toolbar);
        registerTab = findViewById(R.id.register_tab);
        registerPager = findViewById(R.id.register_view_pager);

        setSupportActionBar(registerToolbar);

        memberRegisterFragment = new MemberRegisterFragment();
        adminRegisterFragment = new AdminRegisterFragment();

        registerTab.setupWithViewPager(registerPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(memberRegisterFragment, "Member");
        viewPagerAdapter.addFragment(adminRegisterFragment, "Admin");
        registerPager.setAdapter(viewPagerAdapter);

        registerTab.getTabAt(0).setIcon(R.drawable.member_icon);
        registerTab.getTabAt(1).setIcon(R.drawable.admin_icon);




    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}