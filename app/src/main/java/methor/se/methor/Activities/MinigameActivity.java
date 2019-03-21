package methor.se.methor.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import methor.se.methor.Minigames.DiceFragment;
import methor.se.methor.Minigames.RPSFragment;
import methor.se.methor.Minigames.RichFragment;
import methor.se.methor.Minigames.ShakeFragment;
import methor.se.methor.Minigames.TTSFragment;
import methor.se.methor.Minigames.CompassFragment;
import methor.se.methor.R;

public class MinigameActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

        Intent intent = getIntent();
        String fragmentID = intent.getStringExtra("FragmentID");
        switch (fragmentID) {
            case "RPS":
                fragment = new RPSFragment();
                ((RPSFragment) fragment).setMinigameActivity(this);
                break;
            case "RICH":
                fragment = new RichFragment();
                ((RichFragment) fragment).setMinigameActivity(this);
                break;
            case "DICE":
                fragment = new DiceFragment();
                ((DiceFragment) fragment).setMinigameActivity(this);
                break;
            case "TTS":
                fragment = new TTSFragment();
                ((TTSFragment) fragment).setMinigameActivity(this);
                break;
            case "SHAKE":
                fragment = new ShakeFragment();
                ((ShakeFragment) fragment).setMinigameActivity(this);
                break;
            case "COMPASS":
                fragment = new CompassFragment();
                ((CompassFragment) fragment).setMinigameActivity(this);
                break;

        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragmentContainer, fragment, "Minigame Fragment");
            transaction.commit();
        }
    }

    public void setScore(int score) {
        this.score = score;

    }

    @Override
    public void onBackPressed() {
        if (score != 0) {
            Intent intent = new Intent();
            intent.putExtra("Score", score);
            setResult(MainActivity.RESULT_OK, intent);
        }
        finish();
    }


}
