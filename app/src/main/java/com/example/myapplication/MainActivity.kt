package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.colorspace.connect
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class MainActivity : ComponentActivity() {

    private lateinit var bluetoothService: BluetoothService
    private lateinit var weightTextView: TextView
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weightTextView = findViewById(R.id.txtPeso)
        val connectButton: Button = findViewById(R.id.btnConnect)
        bluetoothService = BluetoothService(this)

        connectButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
            } else {
                connectToScale()
            }
        }

//        enableEdgeToEdge()
//        setContent {
//            MyApplicationTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android", modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToScale() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val device = pairedDevices?.find { it.name == "Báscula" }

        device?.let {
            if (bluetoothService.connectToDevice(it)) {
                val weight = bluetoothService.readWeight()
                weightTextView.text = "Peso: $weight kg"
            } else {
                weightTextView.text = "Conexión fallida"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService.disconnect()
    }

//    @Composable
//    fun Greeting(name: String, modifier: Modifier = Modifier) {
//        Text(
//            text = "Hello $name!", modifier = modifier
//        )
//    }
//
//    @Preview(showBackground = true)
//    @Composable
//    fun GreetingPreview() {
//        MyApplicationTheme {
//            Greeting("Android")
//        }
//    }
}



