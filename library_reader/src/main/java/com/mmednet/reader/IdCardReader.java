package com.mmednet.reader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.mmednet.reader.bean.PersonBean;
import com.mmednet.reader.bean.SimBean;
import com.mmednet.reader.listener.OnReadListener;
import com.mmednet.reader.listener.OnServerListener;
import com.sunrise.icardreader.helper.ConsantHelper;
import com.sunrise.icardreader.model.IdentityCardZ;
import com.sunrise.reader.ReaderManagerService;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sunrise.bluetooth.SRBluetoothCardReader;
import sunrise.nfc.SRnfcCardReader;
import sunrise.otg.SRotgCardReader;
import sunrise.pos.POSCardReader;


/**
 * Title:IdCardReader
 * <p>
 * Description:身份证读取器
 * 动态权限
 * Manifest.permission.READ_PHONE_STATE
 * Manifest.permission.READ_EXTERNAL_STORAGE
 * Manifest.permission.WRITE_EXTERNAL_STORAGE
 * Manifest.permission.WRITE_SETTINGS
 * </p>
 * Author Jming.L
 * Date 2019/3/13 13:55
 */
public class IdCardReader implements Reader {

    public static final int READ_SIM_SUCCESS = 1002;
    private OnReadListener mReadListener;
    private SRnfcCardReader mNFCReader;           //普通NFC方式接口类
    private SRotgCardReader mOTGReader;           //OTG方式接口类
    private SRBluetoothCardReader mBlueReader;    //蓝牙方式接口类
    private POSCardReader mPosCardReader;         //特殊POS接口类
    private ScheduledExecutorService mThreadPool;
    private String mTAG = getClass().getSimpleName();
    private Intent mIntent;
    private BluetoothReceiver mReceiver = new BluetoothReceiver();

    private NfcAdapter.ReaderCallback mCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            if (mPosCardReader.isEnable) {
                mPosCardReader.readCardWithIntent();
            } else if (mNFCReader.isNFC(tag)) {
                // NFC读取身份证
                mNFCReader.readIDCard();
            } else {
                if (mReadListener != null) {
                    //不是身份证
                    mReadListener.onFailure(ConsantHelper.READ_CARD_NO_CARD);
                }
            }
        }
    };

    Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            //读取成功
            if (msg.what == ConsantHelper.READ_CARD_SUCCESS) {
                //关闭NFC巡卡
                mNFCReader.DisableSystemNFCMessage();
                if (mReadListener != null) {
                    mReadListener.onIdCard(PersonBean.initPersonBean((IdentityCardZ) msg.obj));
                }
            }

            //读取SIM卡成功
            if (msg.what == READ_SIM_SUCCESS) {
                if (mReadListener != null) {
                    mReadListener.onSimCard((SimBean) msg.obj);
                }
            }

            //读取失败
            //NFC方式需要授权设备，授权方式：在拨号界面按*#06#，把显示的第一条串号（IMEI或者MEID）给我们授权
            if (msg.what == 6       //设备未授权
                    || msg.what == ConsantHelper.READ_CARD_NO_READ
                    || msg.what == ConsantHelper.READ_CARD_BUSY
                    || msg.what == ConsantHelper.READ_CARD_NET_ERR
                    || msg.what == ConsantHelper.READ_CARD_NO_CARD
                    || msg.what == ConsantHelper.READ_CARD_SAM_ERR
                    || msg.what == ConsantHelper.READ_CARD_OTHER_ERR
                    || msg.what == ConsantHelper.READ_CARD_NEED_TRY
                    || msg.what == ConsantHelper.READ_CARD_OPEN_FAILED
                    || msg.what == ConsantHelper.READ_CARD_NO_CONNECT
                    || msg.what == ConsantHelper.READ_CARD_NO_SERVER
                    || msg.what == ConsantHelper.READ_CARD_FAILED
                    || msg.what == ConsantHelper.READ_CARD_SN_ERR) {
                //关闭NFC巡卡
                mNFCReader.DisableSystemNFCMessage();
                if (mReadListener != null) {
                    mReadListener.onFailure(msg.what);
                }
            }

            //读取进度
            if (msg.what == ConsantHelper.READ_CARD_PROGRESS) {
                Log.e(mTAG, "进度：" + msg.obj.toString());
            }
        }
    };

    private IdCardReader(Context context) {
        this(context, "FE870B0163113409C134283661490AEF");
    }

    private Context mContext;

    private IdCardReader(Context context, String secret) {
        mContext = context;
        mThreadPool = Executors.newScheduledThreadPool(5);
        // 初始化阅读器,
        // 第一个参数handler,用于异步接收读身份证返回结果
        // 第二个参数context,调用接口的Activity上下文对象
        // 最后的参数为密钥,需要填入值才能使用
        // 使用时应向我们申请正式的密钥
        mNFCReader = new SRnfcCardReader(mHandler, context, secret);
        mOTGReader = new SRotgCardReader(mHandler, context, secret);
        mBlueReader = new SRBluetoothCardReader(mHandler, context, secret);
        mPosCardReader = new POSCardReader(mHandler, context, secret);
        OnServerListener serverListener = new OnServerListener();
        mNFCReader.setDecodeServerListener(serverListener);
        mOTGReader.setDecodeServerListener(serverListener);
        mBlueReader.setDecodeServerListener(serverListener);
        mPosCardReader.setDecodeServerListener(serverListener);
    }

    public static Reader initReader(Context context) {
        Reader reader = new IdCardReader(context);
        Class<? extends Reader> readerClass = reader.getClass();
        return (Reader) Proxy.newProxyInstance(
                readerClass.getClassLoader(),
                readerClass.getInterfaces(), reader);
    }


    /**
     * 获取蓝牙
     */
    public Bluetooth getBluetooth() {
        //注册广播接收器(监听蓝牙状态的改变)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙扫描状态(SCAN_MODE)发生改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //指明一个远程设备的连接状态的改变。比如，当一个设备已经被匹配。
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //指明一个与远程设备建立的低级别（ACL）连接。
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        //指明一个来自于远程设备的低级别（ACL）连接的断开
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        //指明一个为远程设备提出的低级别（ACL）的断开连接请求，并即将断开连接。
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        //发现远程设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //本地蓝牙适配器已经开始对远程设备的搜寻过程。
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        mIntent = mContext.registerReceiver(mReceiver, filter);
        return Bluetooth.initBluetooth();
    }

    /**
     * 通过蓝牙方式读SIM卡
     */
    public void readSimCardByBlueTooth(String address) {
        if (mBlueReader.registerBlueCard(address)) {
            SimBean simBean = new SimBean();
            byte[] cardNum = new byte[20];
            simBean.sn = mBlueReader.readMobileICCardSM();
            int result = mBlueReader.readSimICCID(cardNum);
            String iccid;
            if (result >= 0) {
                iccid = new String(cardNum);
            } else {
                iccid = String.valueOf(result);
            }
            mBlueReader.unRegisterBlueCard();
            simBean.iccid = iccid;
            mHandler.sendMessage(mHandler.obtainMessage(READ_SIM_SUCCESS, simBean));
        } else {
            mHandler.sendEmptyMessage(ConsantHelper.READ_CARD_NO_CARD);
        }
    }

    /**
     * 通过OTG方式读SIM卡
     */
    public void readSimCardByOTG() {
        if (mOTGReader.registerOTGCard()) {
            SimBean simBean = new SimBean();
            byte[] cardNum = new byte[20];
            simBean.sn = mOTGReader.readMobileICCardSM();
            int result = mOTGReader.readSimICCID(cardNum);
            String iccid;
            if (result >= 0) {
                iccid = new String(cardNum);
            } else {
                iccid = String.valueOf(result);
            }
            simBean.iccid = iccid;
            mHandler.sendMessage(mHandler.obtainMessage(READ_SIM_SUCCESS, simBean));
        } else {
            mHandler.sendEmptyMessage(ConsantHelper.READ_CARD_NO_CARD);
        }
    }

    /**
     * 通过蓝牙读取身份证
     *
     * @param address 蓝牙地址
     */
    public void readIdCardByBlueTooth(String address) {
        mBlueReader.unRegisterBlueCard();
        if (mBlueReader.registerBlueCard(address)) {
            mBlueReader.readCard(30);
        } else {
            mHandler.sendEmptyMessage(ConsantHelper.READ_CARD_NO_READ);
        }
    }

    /**
     * 通过OTG读取身份证
     */
    public void readIdCardByOTG() {
        if (mOTGReader.registerOTGCard()) {
            mOTGReader.readCard(30);
        } else {
            mHandler.sendEmptyMessage(ConsantHelper.READ_CARD_NO_READ);
        }
    }

    /**
     * 通过NFC读取身份证
     */
    public void readIdCardByNFC() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mPosCardReader.isEnable) {
                //特殊POS方式打开NFC监听
                mPosCardReader.openReader(mCallback);
            } else {
                //普通NFC类打开NFC监听
                mNFCReader.EnableSystemNFCMessage(mCallback);
            }
        } else {
            mHandler.sendEmptyMessage(ConsantHelper.READ_CARD_NO_READ);
        }
    }


    /**
     * 设置读取监听器
     */
    public void setOnReadListener(OnReadListener listener) {
        mReadListener = listener;
    }

    /**
     * 设置特定解密服务器
     *
     * @param address 服务器地址
     * @param port    服务器端口
     */
    public void setServer(String address, int port) {
        if (!TextUtils.isEmpty(address) && port != 0) {
            //若设置只连接特定的解密服务器, 第一步先把管控设为空；第二步设入特定的服务器
            //把管控设为关闭。否则，此服务器读取失败时切换到默认服务器
            mBlueReader.enableAutoServer(false);
            //把动态服务器设为关闭。否则，此服务器读取失败时切换到动态服务器
            mBlueReader.isAutoDServer(false);
            //设入特定的服务器
            mNFCReader.setTheServer(address, port);
            mOTGReader.setTheServer(address, port);
            mBlueReader.setTheServer(address, port);
        } else {
            //没有设置特定的解密服务器，先把管控设为可用
            mBlueReader.enableAutoServer(true);
            mBlueReader.isAutoDServer(true);
        }
    }

    /**
     * 设备是否可用
     *
     * @param address 设置地址
     * @return true设备可用false设备不可用
     */
    public boolean isEnableOfDevice(String address) {
        if (address != null) {
            String regex = "([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(address);
            return matcher.find();
        } else {
            return false;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        //关闭管控线程，防止报错
        ReaderManagerService.stopServer();
        if (mIntent != null) {
            mContext.unregisterReceiver(mReceiver);
            mIntent = null;
        }
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        final Object[] object = {null};
        if (method.isAnnotationPresent(Thread.class)) {
            mThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        object[0] = method.invoke(IdCardReader.this, args);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            return object;
        } else {
            return method.invoke(this, args);
        }
    }

}
