package com.mocircle.flowsample.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mocircle.flowsample.R;

public class MainActivity extends AppCompatActivity {

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Sample1Fragment();
            } else if (position == 1) {
                return new Sample2Fragment();
            } else if (position == 2) {
                return new Sample3Fragment();
            } else if (position == 3) {
                return new Sample4Fragment();
            } else {
                throw new RuntimeException();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.sample1_title);
            } else if (position == 1) {
                return getString(R.string.sample2_title);
            } else if (position == 2) {
                return getString(R.string.sample3_title);
            } else if (position == 3) {
                return getString(R.string.sample4_title);
            } else {
                throw new RuntimeException();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

}