package methor.se.methor.Minigames.RichGame;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import methor.se.methor.R;

public class RichFragment extends Fragment implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private RenderThread renderThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rich, container, false);
        initializeComponents(view);

        renderThread = new RenderThread(surfaceView.getHolder());
        surfaceView.getHolder().addCallback(this);

        return view;
    }

    private void initializeComponents(View view){
        surfaceView = view.findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        renderThread.run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
