package methor.se.methor.Minigames;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

public class RichFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView tvTarget, tvScore, tvResult, tvInstructions;
    private Button buttonStart;

    private final float TILT_THRESHOLD = 3;
    private final float GAME_TIME = 20;
    private final float TILT_TIME = 0.1f * 1000000000;

    private float tiltTimer = 0;
    private long lastTime = 0;
    private boolean wasOnTarget = false;

    private boolean gameRunning = false;
    private CountDownTimer timer;
    private int score = 0;
    private int target;

    private final int NONE = 0;
    private final int LEFT = 1;
    private final int RIGHT = 2;
    private final int DOWN = 4;
    private final int UP = 8;

    private final int UPLEFT = 9;
    private final int UPRIGHT = 10;
    private final int DOWNLEFT = 5;
    private final int DOWNRIGHT = 6;

    private final int[] targets = {LEFT, RIGHT, DOWN, UP, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};
    private final String[] targetStrings = {"Left", "Right", "Down", "Up", "Up Left", "Up Right", "Down Left", "Down Right"};

    private MinigameActivity minigameActivity;
    Random random;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rich, container, false);
        initializeComponents(view);

        return view;
    }

    public int getScore() {
        return score;
    }

    private void initializeComponents(View view) {
        random = new Random();
        tvTarget = view.findViewById(R.id.tvTarget);
        tvScore = view.findViewById(R.id.tvScore);
        tvResult = view.findViewById(R.id.tvResult);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        buttonStart = view.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new CountDownTimer((long) (GAME_TIME * 1000), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        gameRunning = false;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResult.setText("You scored " + score + " points!");
                                tvScore.setText("");
                                tvTarget.setText("");
                                minigameActivity.setScore(score);
                            }
                        });
                    }
                }.start();
                randomizeTarget();
                gameRunning = true;
                buttonStart.setVisibility(View.INVISIBLE);
                showInstructions(false);
            }
        });
        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        tvInstructions.setText("Tilt the device towards the shown destination to score a point.\nScore as much as you can in " + GAME_TIME + " seconds.");
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (gameRunning) {
            float x = event.values[0];
            float y = event.values[1];
            float tilt = 0;

            if (x > TILT_THRESHOLD) {
                tilt += LEFT;
            } else if (x < -TILT_THRESHOLD) {
                tilt += RIGHT;
            }

            if (y > TILT_THRESHOLD) {
                tilt += DOWN;
            } else if (y < -TILT_THRESHOLD) {
                tilt += UP;
            }

            if (tilt == target) {
                if (wasOnTarget) {
                    long delta = event.timestamp - lastTime;
                    tiltTimer += delta;
                }
                if (tiltTimer > TILT_TIME) {
                    score++;
                    tvScore.setText(score + "");
                    randomizeTarget();
                    tiltTimer = 0;
                }
                wasOnTarget = true;
            } else {
                wasOnTarget = false;
            }
            lastTime = event.timestamp;
        }


    }

    private void randomizeTarget() {
        int index = random.nextInt(targets.length);
        while (target == targets[index]) {
            index = random.nextInt(targets.length);
        }
        target = targets[index];
        tvTarget.setText(targetStrings[index]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showInstructions(boolean show) {
        if (show) {
            tvInstructions.setVisibility(View.VISIBLE);
        } else {
            tvInstructions.setVisibility(View.INVISIBLE);
        }
    }

    public void setMinigameActivity(MinigameActivity minigameActivity) {
        this.minigameActivity = minigameActivity;
    }
}
