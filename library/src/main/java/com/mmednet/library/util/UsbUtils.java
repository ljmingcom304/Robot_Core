package com.ljming.excel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.github.mjdev.libaums.partition.Partition;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Title: UsbUtils
 * <p>
 * Description:USB读写工具类
 * <uses-permission android:name="android.permission.USB_PERMISSION" />
 * <uses-permission
 *      android:name="android.permission.MANAGE_USB"
 *      tools:ignore="ProtectedPermissions" />
 * <uses-feature
 *      android:name="android.hardware.usb.host"
 *      android:required="true" />
 * <uses-permission android:name="android.hardware.usb.accessory" />
 * </p>
 * Author Jming.L
 * Date 2022/8/4 10:01
 */
public class UsbUtils {

    public static String writeToUsb(Context context, File file) throws IOException {
        return writeToUsb(context, file.getName(), new FileInputStream(file));
    }

    public static String writeToUsb(Context context, String fileName, InputStream inputStream) throws IOException {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
        if (devices.length > 0) {
            //获取第一个设备
            UsbMassStorageDevice device = devices[0];
            if (usbManager.hasPermission(device.getUsbDevice())) {
                //设备初始化
                device.init();
                List<Partition> partitions = device.getPartitions();
                if (partitions.size() == 0) {
                    Toast.makeText(context, "挂载分区不存在", Toast.LENGTH_SHORT).show();
                    return null;
                }
                //获取第一个分区
                Partition partition = partitions.get(0);
                //获取文件系统
                FileSystem fileSystem = partition.getFileSystem();
                //获取根目录
                UsbFile rootDirectory = fileSystem.getRootDirectory();

                UsbFile excelUsbFile = rootDirectory.createFile("usb_" + fileName);
                excelUsbFile.close();
                OutputStream outputStream = new UsbFileOutputStream(excelUsbFile);
                IOUtils.copy(inputStream, outputStream);
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
                return excelUsbFile.getAbsolutePath();
            } else {
                //没有权限申请权限
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, new Intent("com.android.usb.USB_PERMISSION"), 0);
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        } else {
            Toast.makeText(context, "未发现USB设备", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
