package com.sm.testbleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView infoTv;
    EditText macTx;
    BluetoothAdapter btAdapter;
    String devMac;
    BluetoothDevice btDevice;
    BluetoothGatt btGatt;
    MediaPlayer player;

    UUID UUID_KEY_SERV = UUID
            .fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID UUID_KEY_DATA = UUID
            .fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTv = findViewById(R.id.infoTv);
        macTx = findViewById(R.id.macTx);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        player = new MediaPlayer();
        if(btAdapter == null) {
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1. 블루투스 기능 활성화여부 체크 --> 활성화시키기
        if(!btAdapter.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, 0);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        btAdapter.stopLeScan(leScanCallback);

        if(btGatt != null) { // 리소스 반납하기
            btGatt.disconnect();
            btGatt.close();
        }
    }

    public void register(View view) {
        devMac = macTx.getText().toString().trim();
        showStatus("Device Registration");
    }

    public void scanStart(View view) {
        btAdapter.startLeScan(leScanCallback);
        showStatus("Scan Started");
    }

    public void scanStop(View view) {
        btAdapter.stopLeScan(leScanCallback);
        showStatus("Scan Stopped");
    }

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("INFO", "~~~~~~~" + device.getAddress());
            if(devMac.equals(device.getAddress())) {
                btDevice = device;
                btAdapter.stopLeScan(leScanCallback);
                showStatus("found the sensor [ " + device.getAddress()+" ]");
            }
        }
    };

    public void connectScanner(View view) {
        // 연결하기
        btGatt = btDevice.connectGatt(this, false, gattCallback);
    }

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if(newState == BluetoothProfile.STATE_CONNECTED) {

                showStatus("Connected to Sensor" +
                        "\nI am "+Thread.currentThread().getName());
                Log.i("INFO", "connected to sensor");

                btGatt.discoverServices();

            } else if(newState == BluetoothProfile.STATE_DISCONNECTED) {
                showStatus("Disconnected from Sensor" +
                        "\nI am "+Thread.currentThread().getName()+"\n");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            enableSensor();
/*
            List<BluetoothGattService> servList = gatt.getServices();
            showStatus("[ Services");
            for(BluetoothGattService serv:servList) {
                showStatus("------- "+serv.getUuid());
                List<BluetoothGattCharacteristic> gCharList = serv.getCharacteristics();
                for(BluetoothGattCharacteristic gchar:gCharList) { // 하나의 서비스에 포함된 Characteristics 출력하기
                    showStatus("    >> "+gchar.getUuid());
                }
            }
            showStatus("Services End ]");
*/
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            List<BluetoothGattCharacteristic> servCharList = gatt.getService(characteristic.getUuid()).getCharacteristics();
            showStatus("[ Characteristics");
            for(BluetoothGattCharacteristic gchar:servCharList) {
                showStatus("------- "+gchar.getUuid());
            }
            showStatus("Characteristics End ]");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic ch) {
            super.onCharacteristicChanged(gatt, ch);

            Log.e("INFO", "onCharacteristicChanged ()");
            changeData(ch);
        }
    };

    private void changeData(BluetoothGattCharacteristic ch) {
        // 1. 넘어온 ch를 이용하여 value값을 (추출 -> 변환)한 후
        BluetoothGattDescriptor descriptor = ch.getDescriptor(ch.getUuid());
        byte[] rawValue = ch.getValue();
        String value = Conversion.BytetohexString(rawValue, rawValue.length);

        // 2. 값이 01이면 MediaPlayer서비스 Start
        if (value.equals("01")) {
            startService(new Intent(this, MyService.class));
            showStatus("@ music play");
        }
        // 3. 값이 02이면 서비스를 Stop한다
        else if(value.equals("02")) {
            stopService(new Intent(this, MyService.class));
            showStatus("@ music stop");
        }
        // TestService 참고하기
    }



    private void enableSensor() {

        // Key Event 관련 기능을 활성화시키는 코드 구현
        BluetoothGattCharacteristic c =
                btGatt.getService(UUID_KEY_SERV).getCharacteristic(UUID_KEY_DATA);
        btGatt.setCharacteristicNotification(c, true);

        BluetoothGattDescriptor descriptor = c.getDescriptor(UUID
                .fromString("00002902-0000-1000-8000-00805f9b34fb"));

        if (descriptor != null) {
            byte[] val = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            descriptor.setValue(val);
            btGatt.writeDescriptor(descriptor);
        }

        showStatus(">>>>>> Key Sensor Enabled <<<<<<");
    }

    private void showStatus(String data) {

        final String tmp = data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTv.append("\n"+tmp);
            }
        });
    }
}
