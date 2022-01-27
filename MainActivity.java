package com.example.mcalc;


import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ca.roumani.i2c.MPro;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import org.w3c.dom.Text;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {

    private TextToSpeech tts;
    public void onInit(int initStatus) {
    this.tts.setLanguage(Locale.US);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tts = new TextToSpeech(this,this);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void onAccuracyChanged (Sensor args, int arg1) {

    }

    public void onSensorChanged (SensorEvent event) {
    double ax = event.values[0];
    double ay = event.values[1];
    double az = event.values[2];

    double a = Math.sqrt(ax*ax + ay*ay + az*az);

    if (a > 20) {
        ((EditText)findViewById(R.id.input1)).setText("");
        ((EditText)findViewById(R.id.input2)).setText("");
        ((EditText)findViewById(R.id.input3)).setText("");

    }
    }


    public void onClick(View v) {
        EditText p = (EditText)findViewById(R.id.input1);
        String principle = p.getText().toString();

        EditText a = (EditText)findViewById(R.id.input2);
        String time = a.getText().toString();

        EditText i = (EditText)findViewById(R.id.input3);
        String interest = i.getText().toString();

        try {
            MPro mp = new MPro(principle, time, interest);
            mp.setPrinciple(principle);
            mp.setInterest(interest);
            mp.setAmortization(time);


            String answer = "Monthly Payment: " + mp.computePayment("%,.2f");
            answer += "\n\n";

            answer += "By making this payments monthly for " + time + " years the mortgage will be paid in full. But if you terminate the mortgage on its nth anniversary, the balance still owing depends on n as shown below: ";
            answer += "\n\n";
            answer += String.format("%8d", 0) + mp.outstandingAfter(0, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 1) + mp.outstandingAfter(1, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 2) + mp.outstandingAfter(2, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 3) + mp.outstandingAfter(3, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 4) + mp.outstandingAfter(4, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 5) + mp.outstandingAfter(5, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 10) + mp.outstandingAfter(10, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 15) + mp.outstandingAfter(15, "%,16.0f");
            answer += "\n\n";
            answer += String.format("%8d", 20) + mp.outstandingAfter(20, "%,16.0f");

            ((TextView) findViewById(R.id.output)).setText(answer);

            tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null);
        }catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
