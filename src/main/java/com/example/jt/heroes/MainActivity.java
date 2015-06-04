package com.example.jt.heroes;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.jt.heroes.models.ScrapingFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private CharSequence mTitle;
    private int mCurrentSelectedPosition = 0;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigationView)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_primary));
        }

        setSupportActionBar(toolbar);
        mTitle = getTitle();

        setupNavDrawer();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HeroListFragment.newInstance(0))
                .commit();

        navigationView.setItemTextColor(getResources().getColorStateList(R.color.nav_drawer_text));
        navigationView.setItemIconTintList(getResources().getColorStateList(R.color.nav_drawer_text));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition =
                    savedInstanceState.getInt("selected_position");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        mCurrentSelectedPosition = 0;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, HeroListFragment.newInstance(0))
                                .commit();
                        return true;
                    case R.id.navigation_item_2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, NewsFragment.newInstance())
                                .commit();
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.navigation_item_3:
                        mCurrentSelectedPosition = 2;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, SavedBuildsFragment.newInstance())
                                .commit();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setupNavDrawer() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HeroListFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NewsFragment.newInstance())
                        .commit();
                break;
//            case 2:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, ScrapingFragment.newInstance())
//                        .commit();
//                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Heroes";
                break;
            case 2:
                mTitle = "News";
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("com.example.jt.heroes", Context.MODE_PRIVATE);
        prefs.edit().putInt("mCurrentSelectedPosition", mCurrentSelectedPosition).apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("selected_position", mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = getSharedPreferences("com.example.jt.heroes", Context.MODE_PRIVATE)
                .getInt("mCurrentSelectedPosition", 0);
        Menu menu = navigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);

        switch (mCurrentSelectedPosition) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, HeroListFragment.newInstance(0))
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, NewsFragment.newInstance())
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SavedBuildsFragment.newInstance())
                        .commit();
        }
    }
}
