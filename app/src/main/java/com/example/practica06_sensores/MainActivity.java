package com.example.practica06_sensores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    int banC = 0;
    int banD = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView txt_mensaje = (TextView) findViewById(R.id.txt_mensaje);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);

        //Para agregar contenido mp3, se tiene que crear un nuevo Android Resource Directory de tipo raw, esto dentro de la carpeta res
        //Crear el objeto MediaPlayer, al cual le asignaremos la canción
        MediaPlayer creep = MediaPlayer.create(this, R.raw.creep);
        MediaPlayer dogDays = MediaPlayer.create(this, R.raw.dog_days_are_over);

        //Construimos un objeto que me permite acceder a todos los sensores del dispositivo
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Especificamos el sensor que se va a utilizar.
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(s == null){
            txt_mensaje.setText("El dispositivo no cuenta con el sensor 'Acelerometro'");
        }else{
            //Declaramos un evento que sicede cuando haya algún cambio del sensor
            SensorEventListener evento = new SensorEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onSensorChanged(SensorEvent event) {
                    //Código que se va a ejecutar cuando hay un cambio en los valores del sensor
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    //Muestra los valores de X, y y Z en el Text View
                    //txt_mensaje.setText("X= "+ x + ", Y= " + y + ", Z= " + z);

                    if(x<=6 && x >= -6){
                        layout.setBackgroundColor(Color.WHITE);
                        txt_mensaje.setText("Como te sientes hoy?");

                        try {
                            creep.stop();
                            creep.prepare();
                            banC = 0;
                            dogDays.stop();
                            dogDays.prepare();
                            banD = 0;
                        }catch (IOException e){
                            e.printStackTrace();
                        }


                    }
                    if(x>6){
                        layout.setBackgroundColor(Color.CYAN);
                        txt_mensaje.setText("Mood Sad :(");

                        if (banC == 0){
                            creep.seekTo(65000);
                            creep.start();
                        }
                        banC = 1;

                    }
                    if(x<-6){
                        layout.setBackgroundColor(Color.YELLOW);
                        txt_mensaje.setText("Mood Feli :D");

                        if (banD == 0){
                            dogDays.seekTo(65000);
                            dogDays.start();
                        }
                        banD = 1;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    //Código que se ejecuta cuando la precisión del sensor ha cambiado
                }
            };
            sm.registerListener(evento,s,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}