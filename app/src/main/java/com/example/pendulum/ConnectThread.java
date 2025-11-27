package com.example.pendulum;

import static java.lang.Integer.parseInt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Point;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.UUID;

// класс для отправки данных по блютуз
// тут должна быть основная функция run и функция для отправки данных
public class ConnectThread extends Thread {
    private BluetoothSocket bluSocket = null;
    private final BluetoothAdapter bluAdapter = BluetoothAdapter.getDefaultAdapter();
    private OutputStream outData = null;
    private InputStream inData = null;
    private String strInData = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String address = "44:1D:64:F6:11:CA";
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
        // получение данных
        try {
            inData = bluSocket.getInputStream();
            outData = bluSocket.getOutputStream();
        } catch (Exception e) {
            Log.e("inDataExcept", "данные не получены");
        }
        byte[] buf = new byte[256];
        int bytes;
        try {
            bytes = inData.read(buf);
            strInData = new String(buf,0, bytes);
            Log.d("inDataRead", strInData);
            // тут обновляется массив точек

            // обновляется view нашего графика
        } catch (IOException e) {
            Log.e("inDataExcept", "данные не получилось обработать");
        }

    }
    public FloatPoint getPoint(float time) {

        FloatPoint point = new FloatPoint(time, parseStrInDataFloat(strInData)); // В strInData большое количство чисел, ф-ия не может сделать из них одно число
        Log.d("getPoint", String.valueOf(strInData.length()));     // strInData выглядит: "0.03\r\n-0.03\r\n-0.03\r\n    .....    "
        return point;                                                  // Нужно взять только одно первое число без \r\n
    }

    private float parseStrInDataFloat(String strData) {
        return Float.parseFloat(strData.split("\r\n")[0].trim());
    }
    public void sendData(String message) {
//        try {
//            outData = bluSocket.getOutputStream();
//        } catch (IOException e) {
//            Log.e("Except", "данные не отправлены");
//        }
        byte[] msgBuf = message.getBytes();
        try {
            outData.write(msgBuf);
            Log.e("senData", "Сообщение отправлено");
        } catch (Exception e) {
            Log.e("senData", "Сообщение НЕ отправлено");
        }
    }
}
