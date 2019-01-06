package methor.se.methor.Minigames;


import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
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
import methor.se.methor.R;

public class ShakeFragment extends Fragment implements SensorEventListener {

    public boolean isButtonClicked = false;
    private TextView textViewProgressbar;
    private ProgressBar progressBar;
    private Button btnStart;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private int shakeCounter = 0;
    private int shakePercantage = 5;

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
            if (accelerationSquareRoot >= 2.5 & progressBar.getProgress() < 100) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                shakeCounter += shakePercantage;
                Toast.makeText(getContext(), "SHAKE DETECTED", Toast.LENGTH_SHORT).show();
                progressBar.setProgress(shakeCounter);
                textViewProgressbar.setText(shakeCounter + "%");
                if (progressBar.getProgress() == 100) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                    textViewProgressbar.setText("100%! CHALLENGE COMPLETED!!!");
                }
            }
        }
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

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (btnStart.getText().equals("START")) {
                isButtonClicked = true;
                btnStart.setText("STOP");
            } else if (btnStart.getText().equals("STOP")) {
                isButtonClicked = false;
                btnStart.setText("START");
                progressBar.setProgress(0);
                textViewProgressbar.setText("PRESS START TO PLAY!");
            }
        }
    }
}
