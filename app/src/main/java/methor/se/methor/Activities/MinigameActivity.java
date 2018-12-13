package methor.se.methor.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import methor.se.methor.Minigames.DiceFragment;
import methor.se.methor.Minigames.RPSFragment;
import methor.se.methor.Minigames.RichGame.RichFragment;
import methor.se.methor.Minigames.ShakeFragment;
import methor.se.methor.Minigames.TTSFragment;
import methor.se.methor.Minigames.TedFragment;
import methor.se.methor.R;

public class MinigameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

        Intent intent = getIntent();
        String fragmentID = intent.getStringExtra("FragmentID");
        Fragment fragment = null;
        switch (fragmentID){
            case "RPS":
                fragment = new RPSFragment();
                break;
            case "RICH":
                fragment = new RichFragment();
                break;
            case "DICE":
                fragment = new DiceFragment();
                break;
            case "TTS":
                fragment = new TTSFragment();
                break;
            case "SHAKE":
                fragment = new ShakeFragment();
                break;
            case "TED":
                fragment = new TedFragment();
                break;

        }

        if(fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragmentContainer, fragment, "Minigame Fragment");
            transaction.commit();
        }
    }
}
