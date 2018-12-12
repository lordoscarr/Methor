package methor.se.methor.Minigames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import methor.se.methor.R;

public class TedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ted, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view){
        //Get UI elements
    }
}
