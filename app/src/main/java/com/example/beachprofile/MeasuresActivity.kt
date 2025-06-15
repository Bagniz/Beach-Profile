package com.example.beachprofile

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.beachprofile.ui.theme.BeachProfileTheme

class MeasuresActivity : ComponentActivity(), SensorEventListener, LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private val accelValues = FloatArray(3)
    private val magnetValues = FloatArray(3)
    private var hasAccel = false
    private var hasMagnet = false

    private var inclination = mutableFloatStateOf(0f)
    private var longitude = mutableDoubleStateOf(0.0)
    private var latitude = mutableDoubleStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        enableEdgeToEdge()
        setContent {
            BeachProfileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MeasuresList(it, inclination, latitude, longitude, ::startRegistering, ::stopRegistering)
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, accelValues, 0, event.values.size)
                hasAccel = true
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetValues, 0, event.values.size)
                hasMagnet = true
            }
        }

        if (hasAccel && hasMagnet) {
            val r = FloatArray(9)
            val i = FloatArray(9)

            if (SensorManager.getRotationMatrix(r, i, accelValues, magnetValues)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                inclination.floatValue = pitch
            }
        }
    }

    fun startRegistering() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f, this)
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopRegistering() {
        locationManager.removeUpdates(this)
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onLocationChanged(location: Location) {
        latitude.doubleValue = location.latitude
        longitude.doubleValue = location.longitude
    }
}
