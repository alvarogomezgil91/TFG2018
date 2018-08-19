package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Alvaro Gomez on 15/08/2018.
 */

public class StockViewPagerActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int RED = Color.RED;
    private int BLUE = Color.BLUE;
    private int YELLOW = Color.YELLOW;
    private int GREEN = Color.GREEN;

    String mSimbolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        setContentView(R.layout.activity_stock_view_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSimbolo = getIntent().getStringExtra("simbolo");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {

        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(this, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item_search:
                return true;
            case R.id.item1:
                return true;
            case R.id.item2:
                return true;
            case R.id.item_log_out:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("logOut", "1");
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0:
                    return MainGraphicholderFragment.newInstance(position + 1, mSimbolo);
                case 1:
                    return StockPredictionholderFragment.newInstance(position + 1, mSimbolo);
                case 2:
                    return FeedholderFragment.newInstance(position + 1, mSimbolo);

            }

            return MainGraphicholderFragment.newInstance(position + 1, mSimbolo);

        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}
