package com.mmednet.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.mmednet.library.Library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Title:FileUtils
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2018/11/2 11:21
 */
public class FileUtils {

    private static final String tag = FileUtils.class.getSimpleName();

    public static File getCacheDir() {
        Library library = Library.getInstance();
        return getCacheDir(library.getContext());
    }

    /**
     * 获取数据库存储路径/SDCard/Android/data/包名/cache/
     */
    public static File getCacheDir(Context context) {
        Library library = Library.getInstance();
        File root;
        if (library.isDebug()) {
            root = context.getExternalCacheDir();
        } else {
            root = context.getCacheDir();
        }
        return root;
    }

    public static String getCachePath(String dirName) {
        Library library = Library.getInstance();
        return getCachePath(library.getContext(), dirName);
    }

    /**
     * 获取数据库存储路径/SDCard/Android/data/包名/cache/
     * 设置：对应清除缓存
     */
    public static String getCachePath(Context context, String dirName) {
        File root = getCacheDir(context);
        if (root != null) {
            if (!TextUtils.isEmpty(dirName)) {
                String path = root.getAbsolutePath() + "/" + dirName + "/";
                File file = new File(path);
                if (!file.exists() && !file.mkdirs()) {
                    throw new RuntimeException("can't make dirs in "
                            + file.getAbsolutePath());
                }
                return path;
            } else {
                return root.getAbsolutePath();
            }
        }
        return null;
    }

    public static File getFileDir() {
        Library library = Library.getInstance();
        return getFileDir(library.getContext());
    }

    public static File getFileDir(Context context) {
        Library library = Library.getInstance();
        File root;
        if (library.isDebug()) {
            root = context.getExternalFilesDir(null);
        } else {
            root = context.getFilesDir();
        }
        return root;
    }

    public static String getFilesPath(String dirName) {
        Library library = Library.getInstance();
        return getFilesPath(library.getContext(), dirName);
    }

    /**
     * 获取数据库存储路径/SDCard/Android/data/包名/files/
     * 设置：对应清除数据
     */
    public static String getFilesPath(Context context, String dirName) {
        File root = getFileDir(context);
        if (root != null) {
            if (!TextUtils.isEmpty(dirName)) {
                String path = root.getAbsolutePath() + "/" + dirName + "/";
                File file = new File(path);
                if (!file.exists() && !file.mkdirs()) {
                    throw new RuntimeException("can't make dirs in "
                            + file.getAbsolutePath());
                }
                return path;
            } else {
                return root.getAbsolutePath();
            }
        }
        return null;
    }


    /**
     * 获取数据库存储路径/data/data/包名/databases/
     */
    public static String getDatabasePath(Context context, String dirName) {
        File root = context.getDatabasePath(null);
        if (root != null) {
            String path = root.getAbsolutePath() + "/" + dirName + "/";
            File file = new File(path);
            if (!file.exists() && !file.mkdirs()) {
                throw new RuntimeException("can't make dirs in "
                        + file.getAbsolutePath());
            }
            return path;
        }
        return null;
    }

    /**
     * 获取内存卡下存储路径/mnt/sdcard/
     * 6.0以后需要动态申请权限
     */
    public static String getDirectoryPath(String dirName) {
        String root = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String path = root + "/" + Library.getInstance().getRootDir();
        if (!TextUtils.isEmpty(dirName)) {
            path = path + "/" + dirName + "/";
        }

        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + dir.getAbsolutePath());
        }
        return path;
    }

    /**
     * Bitmap保存为本地文件
     */
    public static void write2File(Bitmap bitmap, String fileName) {
        File file = new File(fileName + ".jpg");
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void write2File(InputStream input, String filePath) {
        FileOutputStream output = null;
        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = input.read(b)) != -1) {
                output.write(b, 0, length);
            }
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取文件或目录的大小，单位M
     */
    public static float getFileSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                float size = 0;
                for (File f : children) {
                    size += getFileSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return (float) file.length() / 1024 / 1024;
            }
        }
        return 0.0f;
    }

    /**
     * 删除文件或目录
     */
    public static int deleteFile(File file) {
        int count = 0;
        if (!file.exists()) {
            return count;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i = 0; children != null && i < children.length; i++) {
                count += deleteFile(children[i]);
            }
        }
        if (file.delete()) {
            count++;
        }
        return count;
    }

}


