package methor.se.methor.Minigames;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import methor.se.methor.Models.RPS;
import methor.se.methor.R;

public class RPSFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Spinner myChoiceSpinner;
    private LinearLayout choiceLayout;
    private TextView tvInstructions;
    private TextView tvResult;
    private TextView tvResultAi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rps, container, false);
        initializeComponents(view);
        initializeSensors();
        return view;
    }

    private void initializeComponents(View view) {
        //Get UI elements
        myChoiceSpinner = view.findViewById(R.id.myChoiceSpinner);
        choiceLayout = view.findViewById(R.id.choiceLayout);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        tvResult = view.findViewById(R.id.tvResult);
        tvResultAi = view.findViewById(R.id.tvResultAi);

        myChoiceSpinner.setAdapter(new ArrayAdapter<RPS.Choice>(getActivity(), R.layout.support_simple_spinner_dropdown_item, RPS.Choice.values()));
        choiceLayout.setVisibility(View.GONE);
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void onShake(){
        if(shakeCount < 3){
            tvInstructions.setText("Shake " + (3 - shakeCount) + " more times");
        }else {
            RPS.Choice myChoice = (RPS.Choice) myChoiceSpinner.getSelectedItem();
            RPS.Result result = RPS.simulateGame(myChoice);
            Log.d(this.getTag(), result.name());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeSensors();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private long shakeTimestamp;
    private int shakeCount;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (shakeCount >= 3) {
            shakeCount = 0;

        }

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)
            if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            // reset the shake count after 3 seconds of no shakes
            if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                shakeCount = 0;
            }

            shakeTimestamp = now;
            shakeCount++;
            Log.d(this.getTag(), "onSensorChanged: Shake " + shakeCount);

            onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(this.getTag(), "ACCURACY CHANGED " + i);
    }
}