package com.meet.speechtotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView txvResult;
    private TextView txtTimer,txtName,txtLName,txtDOB,txtready,txtempID;
    private LinearLayout routeempID,routeempName,routeempLName,routeempDOB;
    private Intent intent;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txtResult);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtempID = (TextView) findViewById(R.id.txtEmpID);
        txtName = (TextView) findViewById(R.id.txtname);
        txtLName = (TextView) findViewById(R.id.txtlname);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtready = (TextView) findViewById(R.id.txtReady);
        routeempID = (LinearLayout) findViewById(R.id.routeEmpID);
        routeempName = (LinearLayout) findViewById(R.id.routeEmpName);
        routeempLName = (LinearLayout) findViewById(R.id.routeEmpLName);
        routeempDOB = (LinearLayout) findViewById(R.id.routeEmpDOB);

        speechTimer();

    }

    private void speechTimer(){
        CountDownTimer timer = null;
            timer = new CountDownTimer(10000,1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    txtTimer.setText("Speak after: "+ millisUntilFinished/1000);
                    Log.e("test","Timer"+millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    txtTimer.setText("Speak Now!!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            if (intent.resolveActivity(getPackageManager()) != null){
                                startActivityForResult(intent, 1);
                            }else{
                                Toast.makeText(MainActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                            }

                        }
                    },1000);
                }
            }.start();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null) {
                    txvResult.setText(result.get(0));
                }
                if (txvResult.getText().toString().trim().equals("123456")){
                    txtempID.setText(result.get(0));
                    txtName.setText("John");
                    txtLName.setText("Doe");
                    txtDOB.setText("2/1/91");
                    txtready.setText("Is this information is correct? "+"YES OR NO");
                    speechTimer();
                }
                if (txvResult.getText().toString().trim().equals("yes")){
                    txtready.setText("Your Temprature test will start now, Please be ready");
                    routeempID.setVisibility(View.GONE);
                    routeempName.setVisibility(View.GONE);
                    routeempLName.setVisibility(View.GONE);
                    routeempDOB.setVisibility(View.GONE);
                    txtTimer.setVisibility(View.GONE);

                }else if (txvResult.getText().toString().trim().equals("no")){
                    txtready.setText("Once Ready, Speak your 6 Digit \nEmployee ID");
                    txtempID.setText("");
                    txtName.setText("");
                    txtLName.setText("");
                    txtDOB.setText("");
                    routeempID.setVisibility(View.VISIBLE);
                    routeempName.setVisibility(View.VISIBLE);
                    routeempLName.setVisibility(View.VISIBLE);
                    routeempDOB.setVisibility(View.VISIBLE);
                    txtTimer.setVisibility(View.VISIBLE);
                    speechTimer();
                }
            }
        }
    }
}
