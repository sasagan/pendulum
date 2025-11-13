#include "BluetoothSerial.h"
#include <typeinfo>

BluetoothSerial SerialBT;
bool start_study = false;
int i = 0;

void setup() {
    Serial.begin(115200);
    SerialBT.begin("ESP32_Device"); // Имя устройства
    //Serial.println("Bluetooth Device is Ready to Pair");
}

void loop() {
    
    if (SerialBT.available()) {
        String inputString = SerialBT.readString();
        Serial.println(inputString);
        inputString.trim();
        //if (inputString == "true") {
        //    start_study = true;
        //} else if (inputString == "false") {
        //    start_study = false;
        //}
        //Serial.println(start_study);
    }
   // Serial.println(start_study);
    //if (start_study == true) {
     //   Serial.println("hi ");
     //   SerialBT.println(i); 
        // Отправка данных по Bluetooth
    //}
   
    
    /*if (SerialBT.available()) {
        int message = SerialBT.read();
        Serial.print("Received: ");
        Serial.println(message);
    }
    if (Serial.available()) {
        SerialBT.write(Serial.read()); // Отправка данных по Bluetooth
    }*/
}
