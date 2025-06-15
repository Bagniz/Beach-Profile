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
    private lateinit var magnetometer: Sensor

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val inclinationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var hasGravity = false
    private var hasMagnet = false

    private var inclination = mutableFloatStateOf(0f)
    private var longitude = mutableDoubleStateOf(0.0)
    private var latitude = mutableDoubleStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

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
                event.values.copyInto(gravity)
                hasGravity = true
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                event.values.copyInto(geomagnetic)
                hasMagnet = true
            }
        }

        if (hasGravity && hasMagnet) {
            if (SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravity, geomagnetic)) {
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                val pitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                inclination.floatValue = pitch
            }
        }
    }

    fun startRegistering() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 1f, this)
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
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
