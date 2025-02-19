package com.sensorberg.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

import com.radiusnetworks.bluetooth.BluetoothCrashResolver;

/**
 * convenience wrapper to abstract the {@link com.radiusnetworks.bluetooth.BluetoothCrashResolver} code
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CrashCallBackWrapper implements BluetoothAdapter.LeScanCallback{

    private final BluetoothAdapter.LeScanCallback NONE = new BluetoothAdapter.LeScanCallback(){

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

        }
    };

    private final BluetoothCrashResolver bluetoothCrashResolver;

    private BluetoothAdapter.LeScanCallback callback;

    /**
     * default constructor, internally setting up the {@link com.radiusnetworks.bluetooth.BluetoothCrashResolver}
     * @param application parameter, required for the initialization of the {@link com.radiusnetworks.bluetooth.BluetoothCrashResolver}
     */
    public CrashCallBackWrapper(Context application){
        bluetoothCrashResolver = new BluetoothCrashResolver(application);
        bluetoothCrashResolver.start();
    }

    public CrashCallBackWrapper() {
        bluetoothCrashResolver = null;
    }

    /**
     * set the callback and automatically stop/start the {@link com.radiusnetworks.bluetooth.BluetoothCrashResolver}
     */
    public void setCallback(BluetoothAdapter.LeScanCallback incoming){
        if (incoming == null){
            callback = NONE;
        }
        else {
            callback = incoming;
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (bluetoothCrashResolver != null) {
            bluetoothCrashResolver.notifyScannedDevice(device, this);
        }
        callback.onLeScan(device, rssi, scanRecord);
    }
}