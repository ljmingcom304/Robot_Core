package com.mmednet.baidu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtils {

    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String tmpDir;
        String sampleDir = "baiduTTS";
        String storageState = Environment.getExternalStorageState();
        if (TextUtils.equals(storageState, Environment.MEDIA_MOUNTED)) {
            tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
            if (!FileUtils.makeDir(tmpDir)) {
                File file = context.getExternalFilesDir(sampleDir);
                tmpDir = file.getAbsolutePath();
                if (!FileUtils.makeDir(tmpDir)) {
                    throw new RuntimeException("create model resources dir failed :" + tmpDir);
                }
            }
        } else {
            File file = context.getExternalFilesDir(sampleDir);
            tmpDir = file.getAbsolutePath();
            if (!FileUtils.makeDir(tmpDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }

    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (file.canWrite() && !file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest) {
        File file = new File(dest);
        if (!file.exists()) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
