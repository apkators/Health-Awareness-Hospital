package com.health.threat.awareness.hospital;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.health.threat.awareness.hospital.adapter.CustomDrawerAdapter;
import com.health.threat.awareness.hospital.fragment.AddPatientDetailFragment;
import com.health.threat.awareness.hospital.fragment.AllAppointmentsFragment;
import com.health.threat.awareness.hospital.fragment.HomeFragment;
import com.health.threat.awareness.hospital.model.DrawerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        drawerLayout = findViewById(R.id.my_drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Add Drawer Item to dataList
        dataList.add(new DrawerItem("AddPatient", R.drawable.outline_person_add_24));
        dataList.add(new DrawerItem("Logout", R.drawable.baseline_directions_walk_24));
        //dataList.add(new DrawerItem("Disclaimer", R.drawable.outline_back_hand_24));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = findViewById(R.id.default_toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        setSupportActionBar(toolbar);

        try {
            // to make the Navigation drawer icon always appear on the action bar
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e("Error", e.getMessage() + "");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout , R.string.nav_open, R.string.nav_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        drawerLayout.addDrawerListener(mDrawerToggle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        //fragmentTransaction.addToBackStack("HomeFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void SelectItem(int position) {
        FragmentTransaction fragmentTransaction;

        switch (position) {
            case 0:
                if (drawerLayout.isOpen())
                    drawerLayout.close();

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new AddPatientDetailFragment());
                fragmentTransaction.addToBackStack("HomeFragment");
                fragmentTransaction.commit();

                return;

            case 1:
                if (drawerLayout.isOpen())
                    drawerLayout.close();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,LoginActivity.class));
                finish();

                return;
        }

        mDrawerList.setItemChecked(position, false);
        //mDrawerList.setSelected(false);
        //setTitle(dataList.get(position).getItemName());
        mDrawerList.setCacheColorHint(getColor(R.color.transparent));
        mDrawerList.getSelectedView().setBackgroundColor(getColor(R.color.transparent));
        mDrawerList.setSelector(android.R.color.transparent);
        drawerLayout.closeDrawer(mDrawerList);
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectItem(position);
            mDrawerList.setItemChecked(position, false);
        }
    }
}