package methor.se.methor.Minigames;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

public class DiceFragment extends Fragment {
    private static String TAG = "DiceFragment";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView tvResult;
    private TextView tvResultAi;
    private ImageView ivd1;
    private ImageView ivd2;
    private ImageView ivd3;
    private ImageView ivd4;

    private TextView tvInstructions;
    private Random rnd = new Random();
    private int d1, d2;
    private int userScore;
    private int computerScore;
    private String result;
    private int[] dice;
    private Timer timer;
    private int loop;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MinigameActivity minigameActivity;

    private int score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dice, container, false);
        initializeDice();
        initializeComponents(view);
        initializeSensors();
        return view;

    }

    public int getScore() {
        return score;
    }

    private void initializeDice() {
        dice = new int[]{
                R.drawable.dice_1,
                R.drawable.dice_2,
                R.drawable.dice_3,
                R.drawable.dice_4,
                R.drawable.dice_5,
                R.drawable.dice_6,
        };
    }


    private void initializeComponents(View view) {
        //Get UI elements
        tvResult = view.findViewById(R.id.tvResult);
        tvResultAi = view.findViewById(R.id.tvResultAi);
        ivd1 = view.findViewById(R.id.ivd1);
        ivd2 = view.findViewById(R.id.ivd2);
        ivd3 = view.findViewById(R.id.ivd3);
        ivd4 = view.findViewById(R.id.ivd4);
        tvInstructions = view.findViewById(R.id.tvInstructions);

    }

    private void initializeSensors() {
        mSensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                if (count == 1) {
                    reset();
                }
                if (count == 3) {
                    unregisterSensor();
                    tvResult.setText("Throwing dice");

                    loop = 0;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Log.d(TAG, "run: LOOP");
                                    ivd1.setImageResource(dice[rollDie() - 1]);
                                    ivd2.setImageResource(dice[rollDie() - 1]);
                                    ivd3.setImageResource(dice[rollDie() - 1]);
                                    ivd4.setImageResource(dice[rollDie() - 1]);
                                    if (loop >= 10) {
                                        setScores();
                                      //  registerSensor();
                                        timer.cancel();
                                    }
                                    loop++;
                                }
                            });

                        }
                    }, 0, 250);

                } else {

                    tvResult.setText("Shake " + count);
                    tvResultAi.setText("");

                }

            }
        });
    }

    private void setScores() {

        d1 = rollDie();
        d2 = rollDie();
        ivd1.setImageResource(dice[d1 - 1]);
        ivd2.setImageResource(dice[d2 - 1]);
        userScore = d1 + d2;
        result = "You threw " + userScore;
        tvResult.setText(result);

        d1 = rollDie();
        d2 = rollDie();

        computerScore = d1 + d2;
        result = "BOT threw " + computerScore;
        ivd3.setImageResource(dice[d1 - 1]);
        ivd4.setImageResource(dice[d2 - 1]);
        tvResultAi.setText(result);

        if (computerScore > userScore) {
            tvInstructions.setTextColor(Color.RED);
            tvInstructions.setText("YOU LOST!");
            tvInstructions.setTextSize(30);
        } else if(computerScore < userScore) {
            tvInstructions.setText("YOU WON!");
            tvInstructions.setTextColor(Color.GREEN);
            tvInstructions.setTextSize(30);
            minigameActivity.setScore(20);
        }else{
            tvInstructions.setText("DRAW!");
            tvInstructions.setTextColor(Color.WHITE);
            tvInstructions.setTextSize(30);
            registerSensor();
        }
    }

    private int rollDie() {
        Log.d(TAG, "rollDie: Rolled");
        return rnd.nextInt(5) + 1;

    }


    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    public void onPause() {
        unregisterSensor();
        super.onPause();
    }

    private void registerSensor() {
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

    }

    private void unregisterSensor() {
        mSensorManager.unregisterListener(mShakeDetector);
    }

    public void reset() {
        tvInstructions.setText("Shake three times to throw your die");
        tvInstructions.setTextSize(18);
        tvInstructions.setTextColor(Color.WHITE);
        ivd1.setImageResource(R.drawable.dice_blank);
        ivd2.setImageResource(R.drawable.dice_blank);
        ivd3.setImageResource(R.drawable.dice_blank);
        ivd4.setImageResource(R.drawable.dice_blank);
    }

    public void setMinigameActivity(MinigameActivity minigameActivity) {
        this.minigameActivity = minigameActivity;
    }

    private static class ShakeDetector implements SensorEventListener {

        private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
        private static final int SHAKE_SLOP_TIME_MS = 500;
        private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

        private OnShakeListener mListener;
        private long mShakeTimestamp;
        private int mShakeCount;

        public void setOnShakeListener(OnShakeListener listener) {
            this.mListener = listener;
        }

        public interface OnShakeListener {
            public void onShake(int count);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // ignore
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (mListener != null) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float gX = x / SensorManager.GRAVITY_EARTH;
                float gY = y / SensorManager.GRAVITY_EARTH;
                float gZ = z / SensorManager.GRAVITY_EARTH;

                // gForce will be close to 1 when there is no movement.
                float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                if (mShakeCount >= 3) {
                    mShakeCount = 0;

                }

                if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                    final long now = System.currentTimeMillis();
                    // ignore shake events too close to each other (500ms)
                    if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                        return;
                    }

                    // reset the shake count after 3 seconds of no shakes
                    if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                        mShakeCount = 0;
                    }

                    mShakeTimestamp = now;
                    mShakeCount++;
                    Log.d(TAG, "onSensorChanged: Shake " + mShakeCount);

                    mListener.onShake(mShakeCount);
                }
            }
        }
    }

}
