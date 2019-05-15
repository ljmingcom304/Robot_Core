package com.mmednet.library.hotfix;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * Title:Hotfix
 * <p>
 * Description:热修复
 * 生成DEX：dx –dex –output=dex文件完整路径 （空格） 要打包的完整class文件所在目录
 * </p>
 * Author Jming.L
 * Date 2018/12/4 15:28
 */
public class Hotfix {

    //补丁目录
    private static final String DIR_PATCH = "patch";
    //解压目录
    private static final String DIR_HOTFIX = "hotfix";


    public void loadFixDex(Context context) {
        HashSet<File> dexFiles = new HashSet<>();
        String[] suffixNames = {".dex", ".apk", ".jar", ".zip"};

        //遍历所有需要热修复的补丁
        File patchDir = new File(context.getFilesDir(), DIR_PATCH);
        if (!patchDir.exists() && !patchDir.mkdirs()) {
            return;
        }
        File[] listFiles = patchDir.listFiles();
        for (File file : listFiles) {
            String fileName = file.getName();
            //必须以classes开头
            if (fileName.startsWith("classes")) {
                for (String suffixName : suffixNames) {
                    if (fileName.endsWith(suffixName)) {
                        dexFiles.add(file);
                    }
                }
            }
        }

        //创建解压目录
        File hotfixDir = new File(context.getFilesDir(), DIR_HOTFIX);
        if (!hotfixDir.exists() && !hotfixDir.mkdirs()) {
            return;
        }

        //加载应用程序的dex
        ClassLoader parentLoader = context.getClassLoader();

        for (File dex : dexFiles) {
            DexClassLoader dexLoader = new DexClassLoader(dex.getAbsolutePath(),
                    hotfixDir.getAbsolutePath(), null, parentLoader);

            Object dexElements = getDexElements(dexLoader);
            Object parentElements = getDexElements(parentLoader);

            //合并元素
            if (dexElements != null && parentElements != null) {
                Class<?> aClass = dexElements.getClass();
                Class<?> componentType = aClass.getComponentType();
                int dexLength = Array.getLength(dexElements);
                int parentLength = Array.getLength(parentElements);
                int length = dexLength + parentLength;
                Object result = Array.newInstance(componentType, length);
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(dexElements, 0, result, 0, dexLength);
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(parentElements, 0, result, dexLength, parentLength);

                setDexElements(parentLoader, result);
            }
        }
    }

    private void setDexElements(ClassLoader classLoader, Object result) {
        try {
            Field pathField = BaseDexClassLoader.class.getField("pathList");
            pathField.setAccessible(true);
            Object pathList = pathField.get(classLoader);

            Field elements = pathList.getClass().getDeclaredField("dexElements");
            elements.setAccessible(true);
            elements.set(pathList, result);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getDexElements(ClassLoader classLoader) {
        try {
            //获取PathList
            Field pathField = BaseDexClassLoader.class.getField("pathList");
            pathField.setAccessible(true);
            Object pathList = pathField.get(classLoader);

            //获取dexElements
            Field dexField = pathList.getClass().getField("dexElements");
            dexField.setAccessible(true);
            return dexField.get(pathList);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
