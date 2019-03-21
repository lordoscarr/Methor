package methor.se.methor.Fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import methor.se.methor.Activities.MainActivity;
import methor.se.methor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button btnStart;
    private Button btnHighscore;
    private TextInputEditText etUsername;
    private MainActivity activity;


    public ProfileFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        btnHighscore = view.findViewById(R.id.btnHighscore);
        btnStart = view.findViewById(R.id.btnStart);
        etUsername = view.findViewById(R.id.etUsername);

        btnStart.setOnClickListener((View v) -> {
            activity.startGame(etUsername.getText().toString());
        });
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
