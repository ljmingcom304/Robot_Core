package com.mmednet.library.util;

import android.util.Log;

import com.mmednet.library.log.Logger;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:StringUtils
 * <p>
 * Description:字符串工具类
 * </p>
 * Author Jming.L
 * Date 2017/8/30 17:37
 */
public class StringUtils {

    private static final String TAG = "StringUtils";

    /**
     * 求解两个字符号的最长公共子串
     *
     * @param strOne 字符串一
     * @param strTwo 字符串二
     * @return 公共子串
     */
    public static String maxSubstring(String strOne, String strTwo) {
        // 参数检查
        if (strOne == null || strTwo == null) {
            return null;
        }
        if (strOne.equals("") || strTwo.equals("")) {
            return null;
        }
        // 二者中较长的字符串
        String max = "";
        // 二者中较短的字符串
        String min = "";
        if (strOne.length() < strTwo.length()) {
            max = strTwo;
            min = strOne;
        } else {
            max = strTwo;
            min = strOne;
        }
        String current = "";
        // 遍历较短的字符串，并依次减少短字符串的字符数量，判断长字符是否包含该子串
        for (int i = 0; i < min.length(); i++) {
            for (int begin = 0, end = min.length() - i; end <= min.length(); begin++, end++) {
                current = min.substring(begin, end);
                if (max.contains(current)) {
                    return current;
                }
            }
        }
        return null;
    }

    /**
     * 去除文字中空白字符
     *
     * @param str 字符串
     * @return 结果
     */
    public static String removeBlank(String str) {
        String dest;
        if (str == null) {
            return null;
        } else {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }

    /**
     * 去除字符串中标点符号
     *
     * @param str 字符串
     * @return 结果
     */
    public static String removeMark(String str) {
        String dest;
        if (str == null) {
            return null;
        } else {
            Pattern p = Pattern.compile("[\\p{P}‘’“”]");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }

    public static String formatNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * 汉字转Float
     */
    public static float toNumber(String num) {
        if (num == null || "".equals(num)) {
            return 0;
        }
        String regex = "[\\d零一二三四五六七八九十百千万亿点\\.]+";
        String chNumber = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(num);
        // 截取出第一组汉字数字
        while (matcher.find()) {
            if (!"".equals(matcher.group())) {
                chNumber = matcher.group();
                break;
            } else {
                return 0;
            }
        }

        float result = 0;
        if (!isNumeric(chNumber)) {
            String[] albArr = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
            String[] numArr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            String[] unitArr = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿", "兆"};
            String[] pointArr = {"点", "."};

            // 遍历汉字数字中每个字符
            int length = chNumber.length();
            int point = length;
            float number = 1;
            boolean hasUnit = false;
            for (int i = 0; i < length; i++) {
                String c = String.valueOf(chNumber.charAt(i));

                //判断是否读取到小数点
                if (pointArr[0].equals(c) || pointArr[1].equals(c)) {
                    point = i;
                    break;
                }

                // 判断整数位
                for (int j = 0; j < albArr.length; j++) {
                    if (albArr[j].equals(c) || numArr[j].equals(c)) {
                        if (!hasUnit && i != 0) {
                            result += number;
                            result *= 10;
                            hasUnit = false;
                        }
                        number = j;
                        break;
                    }
                }

                // 整数位单位
                for (int j = 0; j < unitArr.length; j++) {
                    if (unitArr[j].equals(c)) {
                        int intUnit = (int) Math.pow(10, j);// 整数位单位
                        number *= intUnit;
                        result += number;
                        number = 0;
                        hasUnit = true;
                        break;
                    }
                }
                System.out.println(c + "=" + result);
            }

            //添加个位数
            result += number;

            //存在小数位时
            for (int i = point; i < length; i++) {
                String c = String.valueOf(chNumber.charAt(i));
                // 判断整数位
                for (int j = 0; j < albArr.length; j++) {
                    if (albArr[j].equals(c) || numArr[j].equals(c)) {
                        float decUnit = (int) Math.pow(10, i - point);
                        number = j;
                        number /= decUnit;
                        result += number;
                        break;
                    }
                }
            }

        } else {
            result = Float.parseFloat(chNumber);
        }
        return result;
    }

    private static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 汉字转拼音
     *
     * @param inputString 输入汉字
     * @return 输出拼音
     */
    public static String toPinyin(String inputString) {
        if (inputString == null || inputString.trim().length() == 0) {
            return inputString;
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //WITHOUT_TONE:无音标 (xing);WITH_TONE_NUMBER:1-4数字表示音标 (xing2);WITH_TONE_MARK:直接用音标符
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String output = "";
        char[] input = inputString.trim().toCharArray();
        String regEx = "[\\u4E00-\\u9FA5]+";
        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches(regEx)) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            input[i], format);
                    output += temp[0];
                } else {
                    output += Character.toString(input[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toUpperCase();
    }

    /**
     * 获取字符串的相似度
     *
     * @param str    输入字符串
     * @param target 目标字符串
     * @return 相似度
     */
    public static float getSimilarity(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                int one = d[i - 1][j] + 1;
                int two = d[i][j - 1] + 1;
                int three = d[i - 1][j - 1] + temp;
                //去三者最小值
                d[i][j] = (one = one < two ? one : two) < three ? one : three;
            }
        }
        return 1 - (float) d[n][m] / Math.max(str.length(), target.length());
    }

    /**
     * 目标与源是否匹配
     *
     * @param str      输入字符串
     * @param target   目标字符串
     * @param keywords 关键词
     * @return 匹配结果
     */
    public static boolean isMatch(String str, String target, String... keywords) {
        for (String keyword : keywords) {
            if (!str.contains(keyword)) {
                return false;
            }
        }
        return getMatchDegree(str, target) >= 0.75f;
    }

    /**
     * 获取字符串匹配度（转换成拼音后取最大公共子串后匹配）
     *
     * @param str    输入字符串
     * @param target 目标字符串
     * @return 匹配度
     */
    public static float getMatchDegree(String str, String target) {
        return getMatchDegree(str, target, false);
    }

    /**
     * 获取字符串匹配度（转换成拼音后取最大公共子串后匹配）
     *
     * @param str     输入字符串
     * @param target  目标字符串
     * @param hasMark 是否包含标点时比较
     * @return 匹配度
     */
    public static float getMatchDegree(String str, String target, boolean hasMark) {
        String src = toPinyin(str);
        String dest = toPinyin(target);
        if (!hasMark) {
            src = removeMark(src);
            dest = removeMark(dest);
        }
        String common = getSupplement(src, dest);
        return getSimilarity(common, dest);
    }

    public static List<String> getMatchOptions(String text, List<String> options, boolean isSingle) {
        return getMatchOptions(text, options, 0.75f, isSingle, false);
    }

    /**
     * 获取文本中可以匹配到的选项（转换成拼音后取最大公共子串后匹配）
     *
     * @param text       文本内容
     * @param options    选项集合
     * @param similarity 相似度阈值0~1
     * @param isSingle   是否单项匹配
     * @param hasMark    是否包含标点时比较
     * @return 匹配到的选项集合
     */
    public static List<String> getMatchOptions(String text, List<String> options, float similarity, boolean isSingle, boolean hasMark) {
        if (options == null)
            return null;
        List<String> sOptions = new ArrayList<>();
        String sOption = null;
        String pText = toPinyin(text);
        if (!hasMark) {
            pText = removeMark(pText);
        }
        for (String option : options) {
            String pOption = toPinyin(option);
            if (!hasMark) {
                pOption = removeMark(pOption);
            }

            String common = getSupplement(pText, pOption);

            float rate = getSimilarity(common, pOption);
            if (rate >= similarity) {
                if (isSingle) {
                    sOption = option;
                } else {
                    sOptions.add(option);
                }
            }
            Logger.i(TAG, "[条目子串=" + option + "][公共子串=" + common + "][阈值=" + rate + "]");
        }

        if (isSingle && sOption != null) {
            sOptions.add(sOption);
        }
        return sOptions;
    }

    /**
     * 字符串前后长度补齐
     */
    private static String getSupplement(String str1, String str2) {
        String common = maxSubstring(str1, str2);
        if (common == null) {
            return "";
        }

        //选项向前补齐
        int beforeEnd1 = str1.lastIndexOf(common);
        int beforeEnd2 = str2.lastIndexOf(common);
        int beforeStart = Math.max(0, beforeEnd1 - beforeEnd2);
        String sb = str1.substring(beforeStart, beforeEnd1);

        //选项向后补齐
        int afterStart1 = beforeEnd1 + common.length();
        int afterStart2 = beforeEnd2 + common.length();
        int afterLength = str2.length() - afterStart2;//需要补码的长度
        int afterEnd = Math.min(afterStart1 + afterLength, str1.length());
        String sa = str1.substring(afterStart1, afterEnd);

        //选项前后补齐
        return sb + common + sa;
    }


}
