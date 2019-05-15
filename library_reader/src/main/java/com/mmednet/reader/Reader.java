package com.mmednet.reader;

import com.mmednet.reader.listener.OnReadListener;

import java.lang.reflect.InvocationHandler;

public interface Reader extends InvocationHandler {

    Bluetooth getBluetooth();

    @Thread
    void readSimCardByBlueTooth(String address);

    @Thread
    void readSimCardByOTG();

    @Thread
    void readIdCardByBlueTooth(String address);

    @Thread
    void readIdCardByOTG();

    @Thread
    void readIdCardByNFC();

    void setOnReadListener(OnReadListener listener);

    void setServer(String address, int port);

    boolean isEnableOfDevice(String address);

    void release();

}
