package methor.se.methor.Minigames;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

import static android.app.Activity.RESULT_OK;

public class TTSFragment extends Fragment {
    private MinigameActivity minigameActivity;
    private EditText etSayThis;
    private TextView tvSayThis;
    private TextView tvResult;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private ImageView iv;
    private String[] tongueTwisters = new String[]{"Sex laxar i en laxask", "Flyg fula fluga flyg och den fula flugan flög",
            "Packa pappas kappsäck", "Kvistfritt kvastskaft", "Typiskt västkustskt",
            "Sju sjösjuka sjömän på sjunkande skeppet Shanghai sköttes av sju sköna sjuksköterskor"};
    private String tongueTwister;

    private int score;


    public int getScore() {
        return score;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tts, container, false);
        initializeComponents(view);
        initializeToungeTwister();
        checkPermission();
        initSpeechRecognizer();
        registerListeners();

        return view;
    }


    private void initializeComponents(View view) {
        etSayThis = (EditText) view.findViewById(R.id.etSayThis);
        tvSayThis = (TextView) view.findViewById(R.id.tvSayThis);
        tvResult = (TextView) view.findViewById(R.id.tvResult);
        iv = (ImageView) view.findViewById(R.id.ivMic);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.i("INFO", "checkPermission: ");
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package: " + getActivity().getPackageName()));
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void initializeToungeTwister() {
        Random rand = new Random();
        tongueTwister = tongueTwisters[rand.nextInt(6)];
        tvSayThis.setText(tongueTwister);
    }

    public void checkSpeech(String speech) {
        if (speech.equals(tongueTwister.toLowerCase())) {
            tvResult.setText("Good job");
            minigameActivity.setScore(20);
        } else {
            tvResult.setText("You suck");
        }
    }

    private void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.i("INFO", "onReadyForSpeech: ");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i("INFO", "onBeginningOfSpeech: ");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d("INFO", result.get(0));
                Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
                checkSpeech(result.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    private void registerListeners() {
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        Log.d("INFO", "UP");
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        Log.d("INFO", "DOWN");
                        return true;
                }
                return false;
            }
        });
    }

    public void setMinigameActivity(MinigameActivity minigameActivity) {
        this.minigameActivity = minigameActivity;
    }
}
