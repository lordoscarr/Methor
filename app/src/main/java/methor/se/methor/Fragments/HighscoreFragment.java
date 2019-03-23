package methor.se.methor.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import methor.se.methor.Database.Database;
import methor.se.methor.Models.User;
import methor.se.methor.R;

public class HighscoreFragment extends Fragment {

    private ListView highscoreListView;

    public HighscoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);
        highscoreListView = view.findViewById(R.id.highscoreListView);
        getHighscores();

        return view;
    }

    private void getHighscores(){
        new Thread(() -> {
            List<User> users = Database.getDatabase(getActivity()).userDao().getAllUsers();
            Collections.sort(users, (User o1, User o2) -> Integer.compare(o2.getHighscore(), o1.getHighscore()));


            if (users.size() < 1){
                Toast.makeText(getActivity(), "No highscores", Toast.LENGTH_SHORT).show();
            }

            getActivity().runOnUiThread(() -> {
                highscoreListView.setAdapter(new HighscoreAdapter(getActivity(), users));
            });
        }).start();
    }


    private class HighscoreAdapter extends ArrayAdapter<User>{

        public HighscoreAdapter(@NonNull Context context, @NonNull List objects) {
            super(context, R.layout.highscore_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            User user = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_item, parent, false);
            }

            TextView rankText = convertView.findViewById(R.id.rankText);
            TextView usernameText = convertView.findViewById(R.id.usernameText);
            TextView scoreText = convertView.findViewById(R.id.scoreText);

            rankText.setText("" + (position+1));
            if (user.getUsername().length() > 0 && user.getUsername() != null){
                usernameText.setText(user.getUsername());
            }else{
                usernameText.setText("anon");
            }
            scoreText.setText(user.getHighscore() + " p");

            return convertView;
        }
    }
}
