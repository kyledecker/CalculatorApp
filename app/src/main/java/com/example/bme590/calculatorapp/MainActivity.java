package com.example.bme590.calculatorapp;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.widget.Toast;
import android.widget.ToggleButton;

import net.mitchtech.adb.simpledigitaloutput.R;
import org.microbridge.server.Server;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    boolean audio = false;
    ToggleButton t;
    RelativeLayout r;
    TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t= (ToggleButton) findViewById(R.id.toggleButton2);
        r = (RelativeLayout) findViewById(R.id.layout);
        t.setOnCheckedChangeListener(this);
        r.setBackgroundColor(Color.rgb(0, 0, 156));

        // Initialize TextToSeech Method
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void changestate (View v)
    {
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    audio = true; //audio is on
                } else {
                    // The toggle is disabled
                    audio = false;
                }
            }
        });

    }


    ArrayList<String> arrayList = new ArrayList<String>();
    String string = "";
    String string1 = "";
    String string_sound = "";
    String toSpeak1 = "";

    public void button_sound (View v){

        Button button = (Button) v;
        string_sound = (String) button.getText().toString();

        final MediaPlayer mp = new MediaPlayer();
        // Noise effect every time button is pressed
        if(mp.isPlaying())
        {
            mp.stop();
        }


        try {
            mp.reset();
            AssetFileDescriptor afd;
            if(!string_sound.contains("clear")){
                afd = getAssets().openFd("button_click.mp3");
            }
            else{
                afd = getAssets().openFd("trash_sound.mp3");
            }
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
    }

    public void onClick1 (View v){

        // Make button noise if clicked and true
        if(audio==true) {
            button_sound(v);
        }

        TextView textView_commands = (TextView) findViewById(R.id.textView_commands);

        Button button = (Button) v;

        string = (String) button.getText().toString();

        if(!string.contains("+") && !string.contains("-") && !string.contains("/") && !string.contains("*")) {
            string1 = string1+string;

            if (arrayList.size()>0) {

                arrayList.remove((arrayList.size()-1));
            }
            arrayList.add(string1);

        }
        else {
// why is this here twice?
            arrayList.add(string);
            arrayList.add(string);
            string1="";
        }


        textView_commands.setText(textView_commands.getText().toString()+string);
//        textView_commands.setText(arrayList.toString());


        toSpeak1 = textView_commands.getText().toString();
    }

    public void onClick (View v) {

        TextView textView_results = (TextView)findViewById(R.id.textView_results);
        String toSpeak = "";


        float calc_result = 0;
        int calc_size = arrayList.size();


        while (calc_size!=1){
            if (calc_size>3){

                // If second operation is * or /, perform this first
                if (arrayList.get(3).contains("*") || arrayList.get(3).contains("/")){

                    if (arrayList.get(3).contains("*")){
                        calc_result = Float.parseFloat(arrayList.get(2))* Float.parseFloat(arrayList.get(4));
                    }

                    if (arrayList.get(3).contains("/")){
                        calc_result = Float.parseFloat(arrayList.get(2))/ Float.parseFloat(arrayList.get(4));
                    }

                    arrayList.remove(2);
                    arrayList.remove(2);
                    arrayList.remove(2);
                    arrayList.add(2, Float.toString(calc_result));
                    calc_size = arrayList.size();
                }

                else{

                    // Else just perform first operation
                    if (arrayList.get(1).contains("+")){
                        calc_result = Float.parseFloat(arrayList.get(0))+ Float.parseFloat(arrayList.get(2));
                    }

                    if (arrayList.get(1).contains("-")){
                        calc_result = Float.parseFloat(arrayList.get(0))- Float.parseFloat(arrayList.get(2));
                    }

                    if (arrayList.get(1).contains("*")){
                        calc_result = Float.parseFloat(arrayList.get(0))* Float.parseFloat(arrayList.get(2));
                    }

                    if (arrayList.get(1).contains("/")){
                        calc_result = Float.parseFloat(arrayList.get(0))/ Float.parseFloat(arrayList.get(2));
                    }

                    arrayList.remove(0);
                    arrayList.remove(0);
                    arrayList.remove(0);
                    arrayList.add(0, Float.toString(calc_result));
                    calc_size = arrayList.size();

                }

            }

            // if array size is less then 3 there is only 1 operation to perform
            else{
                if (arrayList.get(1).contains("+")){
                    calc_result = Float.parseFloat(arrayList.get(0))+ Float.parseFloat(arrayList.get(2));
                }

                if (arrayList.get(1).contains("-")){
                    calc_result = Float.parseFloat(arrayList.get(0))- Float.parseFloat(arrayList.get(2));
                }

                if (arrayList.get(1).contains("*")){
                    calc_result = Float.parseFloat(arrayList.get(0))* Float.parseFloat(arrayList.get(2));
                }

                if (arrayList.get(1).contains("/")){
                    calc_result = Float.parseFloat(arrayList.get(0))/ Float.parseFloat(arrayList.get(2));
                }

                arrayList.remove(0);
                arrayList.remove(0);
                arrayList.remove(0);
                arrayList.add(0, Float.toString(calc_result));
                calc_size = arrayList.size();
            }

        }

        textView_results.setText(Float.toString(calc_result));

        toSpeak = textView_results.getText().toString();

        // Speak the result if sound is on
        if(audio== true) {


            toSpeak = toSpeak1 + " equals " + toSpeak;
            toSpeak = toSpeak.replace("+"," plus ");
            toSpeak = toSpeak.replace("*"," times ");
            toSpeak = toSpeak.replace("/"," divided by ");
            toSpeak = toSpeak.replace("-"," minus ");

            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }



    }


    public void clear (View v){
        TextView textView_results = (TextView)findViewById(R.id.textView_results);
        TextView textView_commands = (TextView)findViewById(R.id.textView_commands);

        // Make button noise if clicked and true
        if(audio==true) {
            button_sound(v);
        }

        string1="";
        string="";
        textView_results.setText("0");
        textView_commands.setText("");
        arrayList.clear();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked)
        {
            r.setBackgroundColor(Color.rgb(86, 160, 211));

        }
        else
        {
            r.setBackgroundColor(Color.rgb(0,0,156));

        }
    }
}
