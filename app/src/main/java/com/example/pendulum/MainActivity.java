package com.example.pendulum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    boolean startStudyCheck = false;
    long startTime = System.currentTimeMillis()/1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView blueView = findViewById(R.id.bluetoothEna);
        Button startStudy = findViewById(R.id.startStudy);
        startStudy.setVisibility(View.GONE);
        GraphView graphView = findViewById(R.id.graphView);
        ConnectThread ConnectThread = new ConnectThread();

        // есть переменная bool кооторая меняется по нажатию на кнопку если переменная bool = 1 то начианется
        //      исследование, если 0 то закакнчивается
        View.OnClickListener oclBtnStSt = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStudyCheck = !startStudyCheck;
                if (startStudyCheck == true) { //начало измерения
                    startTime = System.currentTimeMillis()/1000;
                    graphView.arrayRemove();
                    startStudy.setText("Закончить");
                    ConnectThread.sendData("true");
                    // на есп 32 отправляется true, андроид получает данные с есп32
                } else { // конец измерения
                    startStudy.setText("Начать");
                    ConnectThread.sendData("false");
                    // на есп 32 отправляется false, андроид ничего не получает
                }
                // Log.d("Click", String.valueOf(startStudyCheck));

            }
        };
        startStudy.setOnClickListener(oclBtnStSt); // кнопка ожидает действие

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        blueView.post(new Runnable() {
                            public void run() {
                                if (bluetoothAdapter.isEnabled()) {
                                    if (searchDeviceToConnect("ESP32_Device2")) {
                                        //код для передачи данных
                                        blueView.setVisibility(View.GONE);
                                        startStudy.setVisibility(View.VISIBLE);

                                        // вкючается отображение графика
                                        // запускается поток обмена данными с есп32 (сокет слушает данные )
                                        try {
                                            ConnectThread.start();
                                            //graphView.setPoint(ConnectThread.getPoint(startTime-System.currentTimeMillis()/60000));
                                            Log.d("ConnectTread", "ПОТОК СОЗДАН");
                                        } catch (Exception e) {
                                            //Log.d("ConnectTread", "ПОТОК НЕ СОЗДАН");
                                        }
                                        if (startStudyCheck == true) {
                                            try {
                                                graphView.setPoint(ConnectThread.getPoint(System.currentTimeMillis() / 1000 - startTime));

                                                //Log.d("point", "точка построена");
                                            } catch (Exception e) {
                                                //Log.e("point", "точка НЕ построена");
                                            }
                                        }
                                    } else {
                                        blueView.setVisibility(View.VISIBLE);
                                        blueView.setText("Подключитесь к ESP");
                                        startStudy.setVisibility(View.GONE);
                                    }
                                } else {
                                    blueView.setVisibility(View.VISIBLE);
                                    blueView.setText("Включите bluetooth");
                                    startStudy.setVisibility(View.GONE);
                                }

                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    protected boolean searchDeviceToConnect(String nameDevice) {
        if (ContextCompat.checkSelfPermission( this,android.Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED ) {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            //Log.d("dasg", "bondedDevices");
            if (bondedDevices.size() > 0) {
                //Log.d("dasg2", "bondedDevicesEmpty");
                for (BluetoothDevice device : bondedDevices) {
                    if (device.getName().equals(nameDevice)) {
                       // Log.d("MAC", device.getName());
                        return true;
                    }
                }
            }
        }
        return false;
    }


//    protected void sendMes(String Mes) {
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                Log.e("TODO", "НЕТ ЗАВИСИМОСТИ");
//                return;
//            }
//            //device.getUuids()[0].getUuid()
//            bluSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//            Log.e("TODO", "Сокет создан");
//        } catch (IOException e) {
//            Log.e("TODO", "Ошибка созадания сокета");
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        bluetoothAdapter.cancelDiscovery();
//        Log.e("TODO", "Завершил поиск даже если небыл включен");
//        try {
//            Log.e("TODO", "Включаю сокет");
//            bluSocket.connect();
//            if (bluSocket.isConnected()) {
//                Log.e("TODO", "ЕСТЬ КОНТАКТ");
//            }
//            outData = bluSocket.getOutputStream();
//            try {
//                outData.write(Mes.getBytes());
//            } catch (IOException e) {
//                Log.d("Error", "SSP OUT", e);
//            }
//        } catch (IOException connectException) {
//            Log.e("TODO", "Не могу подключить SPP", connectException);
//            try {
//                bluSocket.close();
//            } catch (IOException closeException) {
//                Log.e("TAG", "Не могу закрыть сокет", closeException);
//            }
//        }
//
//    }

}