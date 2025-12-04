#include "BluetoothSerial.h"
#include <typeinfo>
#include <Wire.h>

#define MPU_ADDR 0x68

BluetoothSerial SerialBT;
bool start_study = false;
int i = 0;

float getAccel() {
    Wire.beginTransmission(MPU_ADDR);
    Wire.write(0x3B);
    Wire.endTransmission(false);
    Wire.requestFrom(MPU_ADDR, 2, true);

    int16_t acX = Wire.read()<<8|Wire.read();
    return acX / 16384.0;
}

void setup() {
    Serial.begin(115200);
    SerialBT.begin("ESP32_Device2"); // Имя устройства
    //Serial.println("Bluetooth Device is Ready to Pair");
    Wire.begin(21, 22);
    Wire.setClock(100000);
}

void loop() {
    
    if (SerialBT.available()) {
        String inputString = SerialBT.readString();
        Serial.println(inputString);
        inputString.trim();
        if (inputString == "true") {
            start_study = true;
        } else if (inputString == "false") {
            start_study = false;
        }
    }
   // Serial.println(start_study);
    if (start_study == true) {
        //SerialBT.println("hi ");
        SerialBT.println(getAccel(), 2); 
        // Отправка данных по Bluetooth
    }
   
    delay(300);
    /*if (SerialBT.available()) {
        int message = SerialBT.read();
        Serial.print("Received: ");
        Serial.println(message);
    }
    if (Serial.available()) {
        SerialBT.write(Serial.read()); // Отправка данных по Bluetooth
    }*/
}
