package methor.se.methor.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import methor.se.methor.Fragments.GameMenuFragment;
import methor.se.methor.Fragments.MapFragment;
import methor.se.methor.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        setNavigationListener();


    }


    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("MainActivity", "onNavigationItem: " + menuItem);
                fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_minigames:
                        menuItem.setChecked(true);
                        fragment = new GameMenuFragment();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_highscore:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_map:
                        menuItem.setChecked(true);
                        fragment = new MapFragment();

                        drawerLayout.closeDrawers();
                }

                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_container, fragment);
                    ft.commit();
                }
                return false;

            }
        });
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


}
