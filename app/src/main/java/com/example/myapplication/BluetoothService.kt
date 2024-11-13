package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class BluetoothService(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private val sppUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun connectToDevice(device: BluetoothDevice): Boolean {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
//        bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothDevice.EXTRA_UUID))
        bluetoothSocket = device.createRfcommSocketToServiceRecord(sppUUID)
        bluetoothAdapter?.cancelDiscovery()
        return try {
            bluetoothSocket?.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun readWeight(): String {
        val inputStream: InputStream? = bluetoothSocket?.inputStream
        val buffer = ByteArray(1024)
        val bytes = inputStream?.read(buffer) ?: 0
        return String(buffer, 0, bytes).trim()
    }

    fun disconnect() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}