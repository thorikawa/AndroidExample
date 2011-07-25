package com.polysfactory.speechtest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

public class Top extends Activity {
    private static final String TAG = "SpeechRecognitionTest";

    SpeechRecognizer sr;

    Handler handler = new Handler();

    Intent intent;

    TextView msg;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        msg = (TextView) findViewById(R.id.msg);
        intent = new Intent();
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub
                // Log.d(TAG, "onRmsChanged");
            }

            @Override
            public void onResults(Bundle results) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onResults");
                ArrayList<String> array = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (String s : array) {
                    Log.d(TAG, s);
                }

                if (array.size() > 0) {
                    msg.setText(msg.getText() + "\n" + array.get(0));
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sr.startListening(intent);
                    }
                }, 0);
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onReadyForSpeech");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onPartialResults");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onEvent");
            }

            @Override
            public void onError(int error) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onError:" + error);
                // handler.postDelayed(new Runnable() {
                // @Override
                // public void run() {
                // sr.startListening(intent);
                // }
                // }, 0);
            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
                Log.d(TAG, "onEndOfSpeech");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub
                // Log.d(TAG, "onBurfferReceived");
            }

            @Override
            public void onBeginningOfSpeech() {
                // TODO Auto-generated method stub
                Log.d(TAG, "onBeginningOfSpeech");
            }
        });
        sr.startListening(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sr.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sr.startListening(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sr.stopListening();
        sr.destroy();
    }

}