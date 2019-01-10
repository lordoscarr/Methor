package methor.se.methor.Minigames;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import methor.se.methor.R;

public class CompassFragment extends Fragment implements SensorEventListener, View.OnClickListener {
    private Activity activity;

    private enum Direction {
        North, East, South, West;
    }
    private Direction randomDirection, currentDirection;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor;
    private float[] accelerometerValues = null, magnetometerValues = null;

    private boolean done = false;
    private int timerCount = 3;
    private int currentTimerCount = timerCount;
    private long lastUpdate = 0, firstTimeUpdate = 0;

    private ArrayList<Float> lastDegrees = new ArrayList<>();
    private int finalDegree;

    private ImageView ivArrow, ivCompass;
    private TextView tvMain, tvDone;
    private Button btnMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        activity = getActivity();
        setRandomDirection();
        initializeComponents(view);
        initializeSensors();
        return view;
    }

    private void setRandomDirection() {
        Random random = new Random();
        int i = random.nextInt(4);
        switch (i) {
            case 0:
                randomDirection = Direction.North;
                break;
            case 1:
                randomDirection = Direction.East;
                break;
            case 2:
                randomDirection = Direction.South;
                break;
            case 3:
                randomDirection = Direction.West;
                break;
        }
    }

    private void initializeComponents(View view) {
        ivArrow = view.findViewById(R.id.ivArrow);

        ivCompass = view.findViewById(R.id.ivCompass);
        ivCompass.setVisibility(View.GONE);

        tvMain = view.findViewById(R.id.tvMain);
        tvMain.setText("Point towards " + randomDirection);
        tvDone = view.findViewById(R.id.tvDone);

        btnMain = view.findViewById(R.id.btnDone);
        btnMain.setOnClickListener(this);
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        registerSensorListener();
    }

    private void registerSensorListener() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterSensorListener() {
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
    }

    private void timerDone() {
        long res = 0;
        for (Float degree : lastDegrees)
            res += degree;

        finalDegree = (int) res / lastDegrees.size();

        ivArrow.setVisibility(View.GONE);
        tvMain.setVisibility(View.GONE);

        ivCompass.setRotation(-finalDegree);
        ivCompass.setVisibility(View.VISIBLE);

        currentDirection = getDirection(finalDegree);
        String text = "";
        if(currentDirection == randomDirection)
            text += "You won! \n";
        else
            text += "You lose! \n";
        text += "You pointed towards "+ currentDirection +" ("+ finalDegree +"°)";
        tvDone.setText(text);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Checks if the user has pressed the done-button
        if (done) {
            //Saves the current values from the Accelerometer and Magnetometer
            if (event.sensor == accelerometerSensor) {
                accelerometerValues = new float[event.values.length];
                System.arraycopy(event.values, 0, accelerometerValues, 0,
                        event.values.length);
            } else if (event.sensor == magnetometerSensor) {
                magnetometerValues = new float[event.values.length];
                System.arraycopy(event.values, 0, magnetometerValues, 0,
                        event.values.length);
            }

            //Limits the updates to 10 times a second
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastUpdate > 100) {
                float[] rotationMatrix = new float[16], orientationMatrix = new float[3];

                //Checks and saves when the first update was done
                if (firstTimeUpdate == 0)
                    firstTimeUpdate = currentTimeMillis;

                //Gets the OrientationMatrix from the SensorManager
                if (accelerometerValues != null && magnetometerValues != null) {
                    SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues,
                            magnetometerValues);
                    SensorManager.getOrientation(rotationMatrix, orientationMatrix);

                    //Calculates the current azimuth angle in degrees and saves it for later
                    float azimuthInDegree = (float) (Math.toDegrees(orientationMatrix[0]) + 360) % 360;
                    lastDegrees.add(azimuthInDegree);

                    //Update the UI to match the timer
                    updateTimer(currentTimeMillis);

                    //Checks if the timer is done.
                    if (currentTimeMillis - firstTimeUpdate > timerCount * 1000) {
                        timerDone();
                        done = false;
                    }
                }
            }
        }
    }

    private Direction getDirection(float degree){
        if(degree > 315 && degree <= 360
                || degree > 0 && degree <= 45) return Direction.North;
        else if(degree > 45  && degree <= 135) return Direction.East;
        else if(degree > 135 && degree <= 225) return Direction.South;
        else if(degree > 225 && degree <= 315) return Direction.West;
        else return null;
    }

    private void updateTimer(long currentTimeMillis) {
        //Updates the UI every second
        if (currentTimeMillis - firstTimeUpdate > (timerCount - currentTimerCount) * 1000)
            tvMain.setText(currentTimerCount-- + "");
    }

    @Override
    public void onClick(View v) {
        if (!done) {
            btnMain.setVisibility(View.GONE);
            done = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensorListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterSensorListener();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}