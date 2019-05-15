package com.mmednet.reader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Title:Bluetooth
 * <p>
 * Description:蓝牙工具类
 * </p>
 * Author Jming.L
 * Date 2019/3/13 14:39
 */
public class Bluetooth {

    private static final Bluetooth instance = new Bluetooth();
    private BluetoothAdapter mAdapter;
    private OnScanListener mScanListener;
    private OnBondListener mBondListener;
    private OnOpenListener mOpenListener;

    public interface OnBondListener {
        /**
         * 已经配对
         */
        void onBond(BluetoothDevice device);
    }

    public interface OnScanListener {
        /**
         * 扫描蓝牙
         */
        void onFind(BluetoothDevice device);
    }

    public interface OnOpenListener {
        /**
         * 蓝牙是否开启
         */
        void onOpen(boolean isOpen);
    }

    private Bluetooth() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    static Bluetooth initBluetooth() {
        return instance;
    }

    /**
     * 扫描监听
     */
    public void setOnScanListener(OnScanListener listener) {
        mScanListener = listener;
    }

    /**
     * 配对监听
     */
    public void setOnBondListener(OnBondListener listener) {
        mBondListener = listener;
    }

    /**
     * 开关监听
     */
    public void setOnOpenListener(OnOpenListener listener) {
        mOpenListener = listener;
    }

    /**
     * 判断蓝牙是否开启
     */
    public boolean isOpen() {
        if (mAdapter == null) {
            Log.e(getClass().getSimpleName(), "此设备不支持蓝牙传输功能！");
            return false;
        }
        return mAdapter.isEnabled();
    }

    /**
     * 开启蓝牙
     */
    public void open(boolean open) {
        if (open) {
            mAdapter.enable();
        } else {
            mAdapter.disable();
        }
    }

    /**
     * 获取已经配对的蓝牙设备
     */
    public ArrayList<BluetoothDevice> getBondedDevices() {
        Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> list = new ArrayList<>();
        list.addAll(devices);
        return list;
    }

    /**
     * 扫描蓝牙设备
     */
    public boolean startScan() {
        return mAdapter.startDiscovery();
    }

    /**
     * 取消蓝牙扫描
     */
    public boolean stopScan() {
        return mAdapter.cancelDiscovery();
    }

    /**
     * 蓝牙配对
     */
    public boolean createBond(BluetoothDevice device) {
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                Method createBondMethod = BluetoothDevice.class
                        .getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(device);
                return returnValue;
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解除配对
     */
    public boolean removeBond(BluetoothDevice btDevice) {
        try {
            Method removeBondMethod = BluetoothDevice.class
                    .getMethod("removeBond");
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
            return returnValue;
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打印相关信息
     */
    public void printAllInform() {
        try {
            // 取得所有方法
            Method[] hideMethod = BluetoothDevice.class.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                Log.e("method name", hideMethod[i].getName());
            }
            // 取得所有常量
            Field[] allFields = BluetoothDevice.class.getFields();
            for (i = 0; i < allFields.length; i++) {
                Log.e("Field name", allFields[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 扫描判断
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {// 每扫描到一个设备，系统都会发送此广播。
            if (mScanListener == null) {
                return;
            }
            // 获取蓝牙设备
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
                mScanListener.onFind(device);
            }
        }

        // 配对判断
        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            if (mBondListener == null) {
                return;
            }
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            mBondListener.onBond(device);
        }

        // 状态改变
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (mOpenListener == null) {
                return;
            }
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            if (state == BluetoothAdapter.STATE_ON) {// 蓝牙开启
                mOpenListener.onOpen(true);
            } else if (state == BluetoothAdapter.STATE_OFF) {// 蓝牙关闭
                mOpenListener.onOpen(false);
            }
        }
    }

}
