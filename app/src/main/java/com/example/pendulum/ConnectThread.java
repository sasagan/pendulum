package com.example.pendulum;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

// класс для отправки данных по блютуз
// тут должна быть основная функция run и функция для отправки данных
public class ConnectThread extends Thread {
    private BluetoothSocket bluSocket = null;
    private final BluetoothAdapter bluAdapter = BluetoothAdapter.getDefaultAdapter();
    private OutputStream outData = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String address = "78:1C:3C:CA:15:C6";
    BluetoothDevice device = bluAdapter.getRemoteDevice(address);

    @Override
    public void run() {
        // содание сокета
        try {
            bluSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.d("Socket", "Сокет не создан");
        }
        // соединение с устройством
        try {
            bluSocket.connect();
            Log.e("Except", "СОЕДИНЕНИЕ УСТАНОВЛЕНО");
        } catch (IOException e) {
            Log.e("Except", "СОЕДИНЕНИЯ НЕТ");
        }
    }
    public void sendData(String message) {
        try {
            outData = bluSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("Except", "данные не отправлены");
        }
        byte[] msgBuf = message.getBytes();
        try {
            outData.write(msgBuf);
        } catch (Exception e) {
            Log.e("senData", "Сообщение не отправлено");
        }
    }
}
