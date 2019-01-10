package methor.se.methor.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import methor.se.methor.Activities.MainActivity;
import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameMenuFragment extends Fragment {
    private Button rpsButton;
    private Button diceButton;
    private Button richButton;
    private Button shakeButton;
    private Button ttsButton;
    private Button tedButton;


    public GameMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        initComponents(view);
        return view;

    }

    public void initComponents(View view) {
        rpsButton = view.findViewById(R.id.rpsButton);
        diceButton = view.findViewById(R.id.diceButton);
        richButton = view.findViewById(R.id.richButton);
        shakeButton = view.findViewById(R.id.shakeButton);
        ttsButton = view.findViewById(R.id.ttsButton);
        tedButton = view.findViewById(R.id.tedButton);

        ButtonListener bl = new ButtonListener();
        rpsButton.setOnClickListener(bl);
        diceButton.setOnClickListener(bl);
        richButton.setOnClickListener(bl);
        shakeButton.setOnClickListener(bl);
        ttsButton.setOnClickListener(bl);
        tedButton.setOnClickListener(bl);
    }


    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String id = "";
            if (view.equals(rpsButton)) {
                id = "RPS";
            } else if (view.equals(diceButton)) {
                id = "DICE";
            } else if (view.equals(richButton)) {
                id = "RICH";
            } else if (view.equals(shakeButton)) {
                id = "SHAKE";
            } else if (view.equals(ttsButton)) {
                id = "TTS";
            } else if (view.equals(tedButton)) {
                id = "TED";
            }

            Intent intent = new Intent(getActivity().getApplicationContext(), MinigameActivity.class);
            intent.putExtra("FragmentID", id);

            startActivity(intent);
        }
    }
}
