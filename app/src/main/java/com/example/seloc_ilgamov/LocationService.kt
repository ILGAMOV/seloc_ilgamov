package com.example.seloc_ilgamov

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class LocationService : Service(){

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        toast("Сервис запущен.")

        mHandler = Handler()
        mRunnable = Runnable { fetchLocation() }
        mHandler.postDelayed(mRunnable, 5000) //милисек

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Сервис отключен.")
        mHandler.removeCallbacks(mRunnable)
    }
    private fun fetchLocation()
    {
        val task = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener {
            if(it !=null){
                toast("${it.latitude} ${it.longitude}")
            }
        }
        mHandler.postDelayed(mRunnable, 5000)
    }
}
