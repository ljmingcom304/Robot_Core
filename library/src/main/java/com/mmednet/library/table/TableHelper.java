package com.mmednet.library.table;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mmednet.library.log.Logger;
import com.mmednet.library.table.assign.Table;
import com.mmednet.library.table.assign.VoiceTable;
import com.mmednet.library.table.bean.BeanBinder;
import com.mmednet.library.table.bean.Binder;
import com.mmednet.library.util.DateUtils;
import com.mmednet.library.util.StringUtils;
import com.mmednet.library.view.EditLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * Title:TableHelper
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2018/8/18 13:53
 */
public class TableHelper {

    private String tag = "TableHelper";

    //Key:控件上key;Value:控件
    private Map<String, Binder> mKeyView;
    //Object:绑定对象
    private List<BeanBinder> mBeanBinders;

    private Object object;
    private Context mContext;
    private Dictionary dictionary;

    private static final String CODE_COMPART = ",";// 逗号分隔符
    private static final String CODE_VERTICAL = "\\|";
    private static final String CODE_EQUALITY = "=";

    private Comparator<String> comparator = new Comparator<String>() {
        //字符串由长到短排序
        @Override
        public int compare(String str1, String str2) {
            int len1 = str1.length();
            int len2 = str2.length();
            if (len1 > len2) {
                return -1;
            }
            if (len1 < len2) {
                return 1;
            }
            return 0;
        }
    };

    public TableHelper() {

    }

    public TableHelper(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public TableHelper(Activity activity) {
        inject(activity);
    }

    public TableHelper(Activity activity, Dictionary dictionary) {
        this.dictionary = dictionary;
        inject(activity);
    }

    public TableHelper(Fragment fragment) {
        inject(fragment);
    }

    public TableHelper(Fragment fragment, Dictionary dictionary) {
        this.dictionary = dictionary;
        inject(fragment);
    }

    /**
     * 若构造方法中未传入上下文则需要注入Activity
     *
     * @param activity Activity
     */
    public void inject(Activity activity) {
        this.mContext = activity;
        initView(activity);
    }

    /**
     * 若构造方法中未传入上下文则需要注入Fragment
     *
     * @param fragment Fragment
     */
    public void inject(Fragment fragment) {
        this.mContext = fragment.getContext();
        initView(fragment);
    }

    private <E> void initView(E e) {
        if (e != null) {
            this.object = e;
            mKeyView = new LinkedHashMap<>();
            mBeanBinders = new ArrayList<>();
            Class<?> clazz = e.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Class<?> type = field.getType();
                field.setAccessible(true);
                try {
                    if (Table.class.isAssignableFrom(type)) {
                        Table table = (Table) field.get(e);
                        if (table == null) {
                            // 若控件没有赋值（findViewById或new）则提示初始化
                            Logger.e(tag, field.getType().getSimpleName() + " of " + clazz.getSimpleName()
                                    + " is null and must be initialized.");
                        } else {
                            // 如果存在注解则优先将注解设置为关键词
                            String key = getKey(field);
                            if (TextUtils.isEmpty(key)) {
                                if (table instanceof EditLayout) {
                                    EditLayout layout = (EditLayout) table;
                                    key = layout.getTitle();
                                }
                            }
                            if (TextUtils.isEmpty(key)) continue;
                            if (mKeyView.containsKey(key)) {
                                throw new IllegalStateException("The " + key + " of key has already belonged to a View.");
                            } else {
                                // 若关键词不为空则创建绑定对象
                                Binder binder = new Binder();
                                binder.setKey(key);
                                binder.setTable(table);
                                binder.setViewField(field);
                                if (field.isAnnotationPresent(BindView.class)) {
                                    BindView bindView = field.getAnnotation(BindView.class);
                                    binder.setBindView(bindView);
                                }
                                mKeyView.put(key, binder);
                            }
                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取属性注解的Key
     *
     * @param field 属性
     * @return Key
     */
    private String getKey(Field field) {
        String key = null;
        if (field.isAnnotationPresent(BindTable.class)) {
            int resId = field.getAnnotation(BindTable.class).resId();
            if (resId != 0) {
                key = mContext.getResources().getString(resId);
            } else {
                key = field.getAnnotation(BindTable.class).key();
            }
        }
        return key;
    }

    /**
     * 获取所有的对象绑定关系
     *
     * @return 绑定关系集合
     */
    public List<BeanBinder> getBeanBinders() {
        return mBeanBinders;
    }

    /**
     * 获取所有的EditLayout
     *
     * @return EditLayout集合
     */
    public List<EditLayout> getEditLayouts() {
        Collection<Binder> binders = mKeyView.values();
        ArrayList<EditLayout> list = new ArrayList<>();
        for (Binder binder : binders) {
            Table text = binder.getTable();
            if (text instanceof EditLayout) {
                EditLayout layout = (EditLayout) text;
                list.add(layout);
            }
        }
        return list;
    }

    /**
     * 语音给控件赋值
     *
     * @param content 语音输入内容
     */
    public int textToView(String content) {
        int flag = 0;
        if (object == null) {
            Log.e(tag, "Activity or Fragment is null!");
            return flag;
        }
        //根据关键词切分字符串，存在多个重复的key时默认取最后一个key对应的值
        TreeMap<String, String> map = split(content, mKeyView.keySet());
        // 根据关键词进行赋值
        for (Entry<String, Binder> entry : mKeyView.entrySet()) {
            Binder binder = entry.getValue();
            Table table = binder.getTable();// 获取关键词的绑定对象
            if (!(table instanceof VoiceTable)) {
                continue;
            }
            VoiceTable view = (VoiceTable) table;
            BindView bindView = binder.getBindView();
            boolean toView = true;
            if (bindView != null) {
                toView = bindView.toView();
            }
            //若控件不显示、不可编辑、注解为false，则不支持语音
            if (view.getVisibility() != View.VISIBLE
                    || !view.isEditable() || !toView) {
                continue;
            }
            String key = entry.getKey(); // 控件的关检词
            String value = map.get(key); // 根据控件关键词获取对应的内容
            if (TextUtils.isEmpty(value)) {
                continue;//若关键词没有对应内容则执行下一循环
            }
            List<String> hints = view.getHints();
            if (hints != null && view.isLimit()) {// 单选框、复选框、下拉框

                TreeMap<Integer, String> treeMap = new TreeMap<>();
                ArrayList<String> temp = new ArrayList<>();

                //0.数据从长到短依次排序（当待选项存在包含关系时，长度优先）
                Collections.sort(hints, comparator);

                //1.优先完全匹配关系
                if (hints.contains(value)) {
                    treeMap.put(0, value);
                    temp.add(value);
                }

                //移除已经匹配的Hint
                if (temp.size() > 0) {
                    hints.removeAll(temp);
                    temp.clear();
                }

                //2.再次包含关系
                for (int i = 0; i < hints.size(); i++) {
                    String hint = hints.get(i).trim();
                    int indexOf = value.lastIndexOf(hint);
                    if (indexOf != -1) {
                        treeMap.put(indexOf, hint);
                        temp.add(hint);// 从待选项中移除
                        // 将使用过的字符串替换掉
                        String replace = "";
                        for (int j = 0; j < hint.length(); j++) {
                            replace += "*";
                        }
                        value = value.replace(hint, replace);
                    }
                }
                //移除已经匹配的Hint
                if (temp.size() > 0) {
                    hints.removeAll(temp);
                    temp.clear();
                }

                //3.最后取公共关系
                for (int i = 0; i < hints.size(); i++) {
                    String hint = hints.get(i).trim();
                    if (hint.length() > 3) {
                        String subValue = StringUtils.maxSubstring(hint, value);// 求最长公共子串
                        if (subValue != null && subValue.length() > 3) {
                            //公共子串所占的索引值
                            int indexOf = value.lastIndexOf(subValue);
                            if (indexOf != -1) {
                                treeMap.put(indexOf, hint);
                                // 将使用过的字符串替换掉
                                String replace = "";
                                for (int j = 0; j < subValue.length(); j++) {
                                    replace += "*";
                                }
                                value = value.replace(subValue, replace);
                            }
                        } else {
                            Logger.i(tag, "Hint=" + hint + " Value=" + subValue + " Length<=1");
                        }
                    }
                }

                // 对于单选控件默认取第一个值，语音取最后一个值
                ArrayList<String> list = new ArrayList<>(treeMap.values());
                ArrayList<String> textsNew = new ArrayList<>();// 存储新结果
                List<String> textsOld = view.getTexts();// 已经选择的结果
                for (String result : list) {
                    // 如果已经选中则取消选择
                    if (!textsOld.contains(result)) {
                        textsNew.add(result);
                        flag += 1;
                    }
                }
                //如果存在结果项则可以设置值
                if (list.size() > 0) {
                    view.setText(textsNew);
                }
            } else {// 编辑框、文本框
                if (bindView != null) {
                    String[] formats = bindView.formats();
                    String date = null;
                    for (String format : formats) {
                        date = DateUtils.time2Date(value, format, bindView.result());
                        //若日期格式化正确则跳出循环
                        if (!TextUtils.isEmpty(date)) {
                            break;
                        }
                    }
                    value = TextUtils.isEmpty(date) ? "" : date;
                }
                List<String> list = new ArrayList<>();
                list.add(value);
                view.setText(list);
                flag += 1;
            }
        }
        return flag;
    }

    /**
     * 根据关键词对内容进行切分，切分后的键值以最后出现的关键词为准
     *
     * @param content 需要切分的内容
     * @param keys    关键词的集合
     * @return 切分后的键值对
     */
    private TreeMap<String, String> split(String content, Set<String> keys) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        if (content == null || keys == null) {
            return treeMap;
        }
        String s = "|";
        for (String key : keys) {
            String temp = s + key + s;
            content = content.replaceAll(key, temp);// 关键词两边添加竖线用于分词
        }
        String[] results = content.split(CODE_VERTICAL);
        String key = "";
        String value = "";
        for (int i = 0; i < results.length; i++) {
            String result = results[i];
            if (!TextUtils.isEmpty(result)) {
                if (keys.contains(result)) {
                    key = result;
                    value = "";
                } else {
                    value += result;
                }
                if (key.length() > 0 && value.length() > 0) {
                    Logger.i(tag, "key=" + key + " value=" + value);
                    treeMap.put(key, value);
                }
            }
        }
        return treeMap;
    }

    /**
     * 注入JaveBean属性，关键词取Bean的指定索引
     *
     * @param object 对象
     */
    public void bind(Object object) {
        if (object != null) {
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<Binder> binders = new ArrayList<>();
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                //获取Bean成员变量的key
                String beanKey = getKey(field);
                if (TextUtils.isEmpty(beanKey)) continue;
                if (keys.contains(beanKey)) {
                    throw new IllegalStateException("The " + beanKey + " of key has already belonged to a Field.");
                } else {
                    keys.add(beanKey);
                    if (mKeyView.containsKey(beanKey)) {
                        // 将相同key的Bean属性与View相互绑定
                        Binder binder = mKeyView.get(beanKey);
                        binder.setBeanField(field);
                        binders.add(binder);
                        Logger.i("BIND[Key=" + beanKey + "][View=" + binder.getViewField().getName() + "][Field=" + field.getName() + "]");
                    }
                }
            }

            BeanBinder beanBinder = new BeanBinder();
            beanBinder.setBean(object);
            beanBinder.setBinders(binders);
            mBeanBinders.add(beanBinder);
        }
    }

    /**
     * 接触对象的属性绑定
     */
    public boolean unBind(Object obj) {
        for (BeanBinder binder : mBeanBinders) {
            if (binder.getBean() == obj) {
                mBeanBinders.remove(binder);
                return true;
            }
        }
        return false;
    }

    /**
     * JavaBean向View赋值
     */
    public void beanToView() {
        for (BeanBinder beanBinder : mBeanBinders) {
            Object obj = beanBinder.getBean();   //绑定对象
            List<Binder> binders = beanBinder.getBinders();
            for (Binder binder : binders) {
                Table text = binder.getTable();
                int visibility = text.getVisibility();
                if (visibility == View.VISIBLE) {
                    List<String> result = getFieldText(obj, binder);
                    text.setText(result);
                }
            }
        }
    }

    // 获取Bean属性的字典文本赋值给View
    private List<String> getFieldText(Object obj, Binder binder) {
        Field field = binder.getBeanField();
        //属性的值
        String content = null;
        try {
            Object o = field.get(obj);
            if (o != null) {
                content = o.toString();
            }
        } catch (IllegalAccessException | IllegalArgumentException e1) {
            e1.printStackTrace();
            Log.e(tag, e1.getMessage());
        }

        Field viewField = binder.getViewField();
        String keyName = binder.getKey();
        String viewName = viewField.getName();
        String beanName = field.getName();
        Logger.i("[Key:" + keyName + "][View:" + viewName + "]"
                + "[Field:" + beanName + "][Value:" + content + "]");
        String[] result = {};
        if (content != null) {
            result = new String[]{content};
            if (field.isAnnotationPresent(BindBean.class)) {
                BindBean bindBean = field.getAnnotation(BindBean.class);

                //通过注解的形式完成key-value映射
                Bind[] binds = bindBean.value();
                if (binds.length > 0) {
                    HashMap<String, String> map = new HashMap<>();
                    for (Bind bind : binds) {
                        map.put(bind.value(), bind.key());
                    }
                    //每个属性的值通过英文逗号切割为数组
                    String[] splitText = content.split(CODE_COMPART);// 索引
                    result = new String[splitText.length];//结果
                    //遍历属性的数组的值
                    for (int i = 0; i < result.length; i++) {
                        String position = splitText[i];
                        if (!TextUtils.isEmpty(position)) {
                            //获取属性值对应的选项，如果该选项不存在则返回原值
                            String text = map.get(position);
                            if (TextUtils.isEmpty(text)) {
                                result[i] = position;
                            } else {
                                result[i] = text;
                            }
                        }
                    }
                    return Arrays.asList(result);
                }

                //查询字典（需要查询字典的选项默认用英文逗号分隔）
                String type = bindBean.dict();
                if (!TextUtils.isEmpty(type)) {// 需要查询字典
                    String[] splitText = content.split(CODE_COMPART);
                    result = new String[splitText.length];
                    for (int i = 0; i < result.length; i++) {
                        String text = dictionary.codeToText(type, splitText[i]);
                        result[i] = text;
                    }
                    return Arrays.asList(result);
                }

                //----------------------------------------以下不推荐使用-------------------------------
                // 编码：男=1|女=2|未知性别=3
                String code = bindBean.code();
                if (!TextUtils.isEmpty(code)) {
                    //通过竖线切割出key-value字符串数组
                    String[] splitIndex = code.split(CODE_VERTICAL);// 选项
                    HashMap<String, String> map = new HashMap<>();
                    //将数组中每个字符串按等号切割
                    for (int i = 0; i < splitIndex.length; i++) {
                        String position = splitIndex[i];
                        String[] keyValue = position.split(CODE_EQUALITY, 2);
                        map.put(keyValue[1], keyValue[0]);
                    }

                    //每个属性的值通过英文逗号切割为数组
                    String[] splitText = content.split(CODE_COMPART);// 索引
                    result = new String[splitText.length];//结果
                    //遍历属性的数组的值
                    for (int i = 0; i < result.length; i++) {
                        String position = splitText[i];
                        if (!TextUtils.isEmpty(position)) {
                            //获取属性值对应的选项，如果该选项不存在则返回原值
                            String text = map.get(position);
                            if (TextUtils.isEmpty(text)) {
                                result[i] = position;
                            } else {
                                result[i] = text;
                            }
                        }
                    }
                    return Arrays.asList(result);
                }

                // 索引：男|女|未知性别 编码：0|1|2
                String index = bindBean.index();
                if (!TextUtils.isEmpty(index)) {// 需要索引转换
                    String[] splitIndex = index.split(CODE_VERTICAL);// 选项
                    String[] splitText = content.split(CODE_COMPART);// 索引
                    result = new String[splitText.length];//结果
                    for (int i = 0; i < result.length; i++) {
                        String position = splitText[i];
                        if (!TextUtils.isEmpty(position) && TextUtils.isDigitsOnly(position)) {
                            int p = Integer.valueOf(position);
                            if (p < splitIndex.length) {
                                result[i] = splitIndex[p];
                            } else {
                                Log.e(tag, "数组角标越界：" + "角标=" + p + " 数组=" + splitIndex.length + " 选项=" + index + " 索引=" + content);
                            }
                        }
                    }
                    return Arrays.asList(result);
                }
                //----------------------------------------以上不推荐使用-------------------------------
            }
        }
        return Arrays.asList(result);

    }

    // 获取View的内容赋值给Bean属性
    private String getFieldCode(Object obj, Field field, Table view) {
        String result = "";
        List<String> texts = view.getTexts();
        if (field.isAnnotationPresent(BindBean.class)) {
            BindBean bindBean = field.getAnnotation(BindBean.class);

            //通过注解的形式完成key-value映射
            Bind[] binds = bindBean.value();
            if (binds.length > 0) {
                HashMap<String, String> map = new HashMap<>();
                for (Bind bind : binds) {
                    map.put(bind.key(), bind.value());
                }
                for (int i = 0; i < texts.size(); i++) {
                    String text = texts.get(i);
                    String code = map.get(text);
                    if (!TextUtils.isEmpty(result)) {
                        result += CODE_COMPART;
                    }
                    //有编码传编码，没编码传原文
                    if (TextUtils.isEmpty(code)) {
                        result += text;
                    } else {
                        result += code;
                    }
                }
                return result;
            }

            //查询字典
            String type = bindBean.dict();
            if (!TextUtils.isEmpty(type)) {// 需要查询字典
                for (int i = 0; i < texts.size(); i++) {
                    String text = texts.get(i);
                    String code = dictionary.textToCode(type, text);
                    if (!TextUtils.isEmpty(result)) {
                        result += CODE_COMPART;
                    }
                    result += code;
                }
                return result;
            }

            //过时废弃
            String value = bindBean.code();
            if (!TextUtils.isEmpty(value)) {
                String[] splitIndex = value.split(CODE_VERTICAL);// 选项
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < splitIndex.length; i++) {
                    String position = splitIndex[i];
                    String[] keyValue = position.split(CODE_EQUALITY);
                    map.put(keyValue[0], keyValue[1]);
                }
                for (int i = 0; i < texts.size(); i++) {
                    String text = texts.get(i);
                    String code = map.get(text);
                    if (!TextUtils.isEmpty(result)) {
                        result += CODE_COMPART;
                    }
                    //有编码传编码，没编码传原文
                    if (TextUtils.isEmpty(code)) {
                        result += text;
                    } else {
                        result += code;
                    }
                }
                return result;
            }

            //过时废弃
            String index = bindBean.index();
            if (!TextUtils.isEmpty(index)) {// 需要索引转换
                String[] splitIndex = index.split(CODE_VERTICAL);// 选项
                for (int i = 0; i < splitIndex.length; i++) {
                    String position = splitIndex[i];
                    int indexOf = texts.indexOf(position);
                    if (indexOf != -1) {
                        if (!TextUtils.isEmpty(result)) {
                            result += CODE_COMPART;
                        }
                        result += i;
                    }
                }
                return result;
            }
        } else {
            for (int i = 0; i < texts.size(); i++) {
                String text = texts.get(i);
                if (!TextUtils.isEmpty(result)) {
                    result += CODE_COMPART;
                }
                result += text;
            }
            return result;
        }
        return result;

    }

    /**
     * View向JavaBean赋值
     */
    public void viewToBean() {
        for (BeanBinder beanBinder : mBeanBinders) {
            Object object = beanBinder.getBean();
            List<Binder> binders = beanBinder.getBinders();
            for (Binder binder : binders) {
                Field field = binder.getBeanField();
                Table view = binder.getTable();
                int visibility = view.getVisibility();
                if (visibility == View.VISIBLE) {
                    try {
                        String text = getFieldCode(object, field, view);
                        Object value = transform(field, text);
                        field.set(object, value);
                    } catch (IllegalAccessException | IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    //根据属性的类型进行对应的转换
    private Object transform(Field field, String text) {
        Class<?> aClass = field.getType();
        if (aClass == String.class) {
            return text;
        }
        if (aClass == Integer.class || aClass == int.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Integer.valueOf(text);
            }
        }
        if (aClass == Float.class || aClass == float.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Float.valueOf(text);
            }
        }
        if (aClass == Double.class || aClass == double.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Double.valueOf(text);
            }
        }
        if (aClass == Short.class || aClass == short.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Short.valueOf(text);
            }
        }
        if (aClass == Long.class || aClass == long.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Long.valueOf(text);
            }
        }
        if (aClass == Boolean.class || aClass == boolean.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Boolean.valueOf(text);
            }
        }
        if (aClass == Byte.class || aClass == byte.class) {
            if (TextUtils.isEmpty(text)) {
                return null;
            } else {
                return Byte.valueOf(text);
            }
        }
        return text;
    }

}
