package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.fragments.AboutFragment;
import com.program.supgnhelper.fragments.AddFlatFragment;
import com.program.supgnhelper.fragments.AddHomeFragment;
import com.program.supgnhelper.fragments.AddLocalFragment;
import com.program.supgnhelper.fragments.AddStreetFragment;
import com.program.supgnhelper.fragments.CatalogFragment;
import com.program.supgnhelper.fragments.ListFragment;
import com.program.supgnhelper.fragments.ProfileFragment;
import com.program.supgnhelper.fragments.TaskFragment;
import com.program.supgnhelper.fragments.WorkFragment;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private FragmentRefreshListener fragmentRefreshListener;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.left_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        setupDrawerContent(navigationView);

        openItems();
    }

    // Алгоритм настройки нижней панели, запуск либо списка работников, либо задач
    @SuppressLint("NonConstantResourceId")
    private void openItems(){
        ListFragment list = new ListFragment();
        TaskFragment task = new TaskFragment();
        WorkFragment work = new WorkFragment();

        if(getIntent().getBooleanExtra("list", false)){
            setFragment(list);
        }else{
            setFragment(task);
        }

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.page_1);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.page_1:
                    setFragment(task);
                    break;
                case R.id.page_2:
                    setFragment(work);
                    break;
            }
            return true;
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();  //add this two lines
                drawerView.requestLayout();
            }
        };
    }

    // Старт фрагментов
    public void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.commit();
    }

    // Обработка нажатий на шторку
    @SuppressLint("NonConstantResourceId")
    private void selectListItem(MenuItem item){
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.tasks:
                if(getIntent().getBooleanExtra("list", false)) {
                    fragment = new ListFragment();
                }else {
                    fragment = new TaskFragment();
                }
                break;
            case R.id.catalog:
                fragment = new CatalogFragment();
                break;
            case R.id.profile:
                fragment = new ProfileFragment();
                break;
            case R.id.about:
                fragment = new AboutFragment();
                break;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("login", false);
                editor.apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.add_local:
                fragment = new AddLocalFragment();
                break;
            case R.id.add_street:
                fragment = new AddStreetFragment();
                break;
            case R.id.add_home:
                fragment = new AddHomeFragment();
                break;
            case R.id.add_flat:
                fragment = new AddFlatFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit();

            item.setChecked(true);
            setTitle(item.getTitle());
            drawerLayout.closeDrawers();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectListItem(menuItem);
                        return true;
                    }
                });
    }

    // Установка заголовка из фрагментов
    @Override
    public void setTitle(CharSequence title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    // Установка даты после завершения всплывающего окна
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        editor.putLong("date", calendar.getTimeInMillis());
        editor.apply();

        getFragmentRefreshListener().onRefresh();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("login", false);
        editor.apply();
    }
}
