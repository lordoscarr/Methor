package methor.se.methor.Minigames;


import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ShakeFragment extends Fragment implements SensorEventListener {
    private static final long START_TIME_IN_MILLIS = 10000;

    public boolean isButtonClicked = false;
    private TextView textViewProgressbar, textViewTimeLeft;
    private ProgressBar progressBar;
    private Button btnStart;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private double shakeCounter = 1;
    private int minRandomDouble = 2;
    private int maxRandomDouble = 10;
    private int randomShakePercantage = ThreadLocalRandom.current().nextInt(minRandomDouble, maxRandomDouble);
    private CountDownTimer timer;
    private boolean isTimerRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;
    private MinigameActivity minigameActivity;

    private int score = 10;


    public int getScore() {
        return score;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shake, container, false);
        initializeComponents(view);
        getSensors();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeComponents(View view) {
        textViewProgressbar = view.findViewById(R.id.textViewProgessbar);
        textViewTimeLeft = view.findViewById(R.id.textViewShowTime);
        progressBar = view.findViewById(R.id.progressBar);
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ButtonListener());
    }

    public void getSensors() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast.makeText(getContext(), "ACCELEROMETER SENSOR NOT AVAILABLE", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && isButtonClicked) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            if (accelerationSquareRoot >= 2.5 && progressBar.getProgress() < 100 &&  timeLeftInMillis < 10000) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                shakeCounter += randomShakePercantage;
                progressBar.setProgress((int) shakeCounter);
                textViewProgressbar.setText(shakeCounter + "%");
                if (progressBar.getProgress() == 100 && timeLeftInMillis > 00000) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                    textViewProgressbar.setText("100%! CHALLENGE COMPLETED!!!");
                    stopTimer();
                    minigameActivity.setScore(20);
                }
            }
        }
        if(textViewTimeLeft.getText().toString().equals("0s") && progressBar.getProgress() < 100) {
            onPause();
            stopTimer();
            textViewProgressbar.setText("MISSION FAILED! \nTRY AGAIN!");
        }
    }

    public void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%01d", seconds);
                textViewTimeLeft.setText(timeLeftFormatted+"s");
            }
            @Override
            public void onFinish() {
                isTimerRunning = false;
            }
        }.start();
    }

    public void stopTimer() {
        timer.cancel();
        isTimerRunning = false;
    }

    public void resetTimer() {
        textViewTimeLeft.setText("10s");
        timeLeftInMillis = START_TIME_IN_MILLIS;
        isTimerRunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensorAccelerometer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager = null;
        sensorAccelerometer = null;
    }

    public void setMinigameActivity(MinigameActivity minigameActivity) {
        this.minigameActivity=minigameActivity;
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (btnStart.getText().equals("START")) {
                onResume();
                isButtonClicked = true;
                btnStart.setText("QUIT");
                startTimer();
            } else if (btnStart.getText().equals("QUIT")) {
                getActivity().onBackPressed();
            }
        }
    }
}
