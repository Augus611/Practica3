package com.grupo4.practica3

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi

lateinit var sensorManager : SensorManager
lateinit var accelerometer : Sensor
lateinit var vibrator : Vibrator
var vib = false

class Sensors : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_UI)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val cuadrado = findViewById<TextView>(R.id.cuadrado)
            val x = event.values[0]
            val y = event.values[1]

            cuadrado.apply {
                rotationX = y * 3f
                rotationY = x * 3f
                text = "x ${x.toInt()}\ny ${y.toInt()}"
            }

            if (x.toInt() == 0 && y.toInt() == 0) {
                if (!vib) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                    vib = true
                }
            } else {
                if (x.toInt() != 1 && x.toInt() != -1 && y.toInt() != 1 && y.toInt() != -1) {
                    vib = false
                }
            }

        }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this, accelerometer)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}