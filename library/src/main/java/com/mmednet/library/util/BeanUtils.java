package com.mmednet.library.util;

import com.mmednet.library.annotation.Ignore;
import com.mmednet.library.log.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:BeanUtils
 * <p>
 * Description:JavaBean工具类
 * </p>
 * Author Jming.L
 * Date 2017/12/4 17:02
 */
public class BeanUtils {

    /**
     * 成员变量是否忽略判断
     */
    public static boolean isIgnore(Field field) {
        if (field.isAnnotationPresent(Ignore.class)) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            return ignore.ignore();
        }
        return false;
    }

    /**
     * 对象转集合
     */
    public static Map<String, String> beanToMapString(Object object) {
        HashMap<String, String> map = new HashMap<>();
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String key = field.getName();
                Object value = field.get(object);
                if (value != null) {
                    map.put(key, value.toString());
                } else {
                    map.put(key, null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 对象转集合
     */
    public static Map<String, Object> beanToMap(Object object) {
        HashMap<String, Object> map = new HashMap<>();
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String key = field.getName();
                Object value = field.get(object);
                map.put(key, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 对象转集合
     */
    public static Object mapToBean(Map<String, Object> map, Class clazz) {
        try {
            Object object = clazz.newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getKey();
                for (Field field : clazz.getFields()) {
                    field.setAccessible(true);
                    if (field.getName().equals(key)) {
                        field.set(object, value);
                    }
                }
            }
            return object;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 集合的深度克隆，集合中对象必须实现序列化接口
     *
     * @param src 源数据
     * @return 克隆对象
     */
    public static Object clone(Object src) {
        Object dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            //noinspection unchecked
            dest = in.readObject();
        } catch (Exception e) {
            Logger.e("BeanUtils", e.getMessage());
        }
        return dest;
    }

    public static void formatValue(Object object) {
        formatNumber(object);
        formatString(object);
    }

    /**
     * 对象中类型为Number值为NULL的格式化为0
     */
    public static void formatNumber(Object object) {
        if (object != null) {
            Class<?> aClass = object.getClass();
            Field[] fields = aClass.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isIgnore(field)) continue;
                Class<?> type = field.getType();
                try {
                    Object value = field.get(object);
                    if (value == null) {
                        if (type == Byte.class) {
                            field.set(object, (byte) 0);
                        }
                        if (type == Short.class) {
                            field.set(object, (short) 0);
                        }
                        if (type == Integer.class) {
                            field.set(object, (int) 0);
                        }
                        if (type == Long.class) {
                            field.set(object, (long) 0);
                        }
                        if (type == Float.class) {
                            field.set(object, (float) 0);
                        }
                        if (type == Double.class) {
                            field.set(object, (double) 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对象中类型为String值为NULL的格式化为""
     */
    public static void formatString(Object object) {
        if (object != null) {
            Class<?> aClass = object.getClass();
            Field[] fields = aClass.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isIgnore(field)) continue;
                Class<?> type = field.getType();
                if (type == String.class) {
                    try {
                        Object value = field.get(object);
                        if (value == null) {
                            field.set(object, "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
