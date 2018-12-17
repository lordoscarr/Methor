package methor.se.methor.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import methor.se.methor.Minigames.DiceFragment;
import methor.se.methor.Minigames.RPSFragment;
import methor.se.methor.Minigames.RichFragment;
import methor.se.methor.Minigames.ShakeFragment;
import methor.se.methor.Minigames.TTSFragment;
import methor.se.methor.Minigames.TedFragment;
import methor.se.methor.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Button rpsButton;
    private Button diceButton;
    private Button richButton;
    private Button shakeButton;
    private Button ttsButton;
    private Button tedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setNavigationListener();

        Toast.makeText(this, "METHOR BIIITCHES", Toast.LENGTH_SHORT).show();

        rpsButton = findViewById(R.id.rpsButton);
        diceButton = findViewById(R.id.diceButton);
        richButton = findViewById(R.id.richButton);
        shakeButton = findViewById(R.id.shakeButton);
        ttsButton = findViewById(R.id.ttsButton);
        tedButton = findViewById(R.id.tedButton);

        ButtonListener bl = new ButtonListener();
        rpsButton.setOnClickListener(bl);
        diceButton.setOnClickListener(bl);
        richButton.setOnClickListener(bl);
        shakeButton.setOnClickListener(bl);
        ttsButton.setOnClickListener(bl);
        tedButton.setOnClickListener(bl);
    }

    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String id = "";
            if(view.equals(rpsButton)){
                id = "RPS";
            }else if(view.equals(diceButton)){
                id = "DICE";
            }else if(view.equals(richButton)){
                id = "RICH";
            }else if(view.equals(shakeButton)){
                id = "SHAKE";
            }else if(view.equals(ttsButton)){
                id = "TTS";
            }else if(view.equals(tedButton)){
                id = "TED";
            }

            Intent intent = new Intent(MainActivity.this, MinigameActivity.class);
            intent.putExtra("FragmentID", id);

            startActivity(intent);
        }
    }

    private void setNavigationListener(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_profile:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_minigames:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_highscore:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
