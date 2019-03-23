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

import methor.se.methor.Database.Database;
import methor.se.methor.Database.DatabaseDaos;
import methor.se.methor.Fragments.GameMenuFragment;
import methor.se.methor.Fragments.HighscoreFragment;
import methor.se.methor.Fragments.MapFragment;
import methor.se.methor.Fragments.ProfileFragment;
import methor.se.methor.Models.User;
import methor.se.methor.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment fragment;
    private ProfileFragment profileFragment;
    private DatabaseDaos databaseDaos;
    private User user;

    private int score;
    public static final int REQUEST_SCORE = 1;

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

        setDatabase();
        setStartFragment();
        setNavigationListener();


    }

    private void setDatabase() {
        if (databaseDaos == null) {
            databaseDaos = new DatabaseDaos();
        }
    }

    private void setStartFragment() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            profileFragment.setActivity(this);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, profileFragment);
        ft.commit();
    }


    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("MainActivity", "onNavigationItem: " + menuItem);
                fragment = profileFragment;

                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        fragment = profileFragment;
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
                        showHighscore();
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


    public void startGame(String username) {
        Log.d("MainActivity", "Username : " + username);
        fragment = new MapFragment();
        setUser(username);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    private void setUser(String username) {
        new Thread(() -> {

            user = Database.getDatabase(this).userDao().checkUsername(username);

            if (user == null) {
                user = new User(username);
                Database.getDatabase(this).userDao().insertUser(user);
            }
        }).start();
    }

    private void updateScore(int score) {
        user.setHighscore(score);

        new Thread(() -> {
            Database.getDatabase(this).userDao().updateUser(user.getHighscore(), user.getUsername());
            Log.d("Database size", Database.getDatabase(this).userDao().getAllUsers().size() + " users");
        }).start();


    }

    public void showHighscore(){
        fragment = new HighscoreFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    private void printScore() {

        new Thread(() -> {
            Log.d("MainActivity", "printScore: " + Database.getDatabase(this).userDao().getScore(user.getUsername()));
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == RESULT_OK) {
            score = data.getIntExtra("Score", 0);
            updateScore(score);
            printScore();

        }
    }
}
