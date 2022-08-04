package com.ljming.excel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.github.mjdev.libaums.fs.UsbFileOutputStream;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.WritableWorkbookImpl;

/**
 * Title: ExcelUtils
 * <p>
 * Description:Bean上加类注解
 * </p>
 * Author Jming.L
 * Date 2022/8/4 10:14
 */
public class ExcelUtils {

    /**
     * 1.Bean上无注解则会正常输出数据
     * 2.Bean上加类注解则按带标题注解导出，成员变量存在注解才会输出
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface Excel {
        //表头命名
        String name() default "";

        //输出第几列
        int column() default 0;
    }

    private static final String EXCEL_DIR = "excel";
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_DEF = "sheet1";

    public static File[] getExcels(Context context) {
        File file = context.getExternalFilesDir(EXCEL_DIR);
        return file.listFiles(pathname -> pathname.getName().endsWith(EXCEL_XLS));
    }

    public static <T> File writeExcelToTemp(Context context, List<T> list) {
        File tempFile = null;
        WritableWorkbook workbook = null;
        try {
            tempFile = File.createTempFile("temp", EXCEL_XLS, context.getCacheDir());
            WorkbookSettings settings = new WorkbookSettings();
            settings.setEncoding("UTF-8");
            settings.setFormulaAdjust(true);
            settings.setRefreshAll(true);
            workbook = Workbook.createWorkbook(tempFile, settings);
            writeSheet(workbook, list);
            workbook.write();
        } catch (IOException | WriteException | IllegalAccessException e) {
            Toast.makeText(context, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (WriteException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempFile;
    }

    /**
     * 生成Excel表，类注解则按顺序生成标题栏
     */
    public static <T> File writeExcelToLocal(Context context, List<T> list) {
        File tempFile = writeExcelToTemp(context, list);
        String fileName = null;
        if (list.size() > 0) {
            //获取第一行数据
            Class<?> clazz = list.get(0).getClass();
            if (clazz.isAnnotationPresent(Excel.class)) {
                Excel clazzExcel = clazz.getAnnotation(Excel.class);
                if (clazzExcel != null && !TextUtils.isEmpty(clazzExcel.name())) {
                    fileName = clazzExcel.name() + EXCEL_XLS;
                }
            }
        }

        if (TextUtils.isEmpty(fileName)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA);
            fileName = format.format(new Date()) + EXCEL_XLS;
        }

        File file = new File(context.getExternalFilesDir(EXCEL_DIR), Objects.requireNonNull(fileName));
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
            outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            Toast.makeText(context, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }

        return file;
    }

    /**
     * 向Excel写入数据
     */
    private static <T> void writeSheet(WritableWorkbook workbook, List<T> list)
            throws IllegalAccessException, WriteException {
        String sheetName = EXCEL_DEF;
        WritableSheet sheet = null;
        //设置表格的名字
        //设置列宽 ，第一个参数是列的索引，第二个参数是列宽
        //sheet.setColumnView(i, value.length());
        //设置行高，第一个参数是行数，第二个参数是行高
        //sheet.setRowView(j, 340);
        WritableCellFormat format = getFormat();
        //标题行占据的行数
        int titleNum = 0;
        boolean isBeanAnnotation = false;
        if (list.size() > 0) {
            //获取第一行数据
            Class<?> clazz = list.get(0).getClass();
            isBeanAnnotation = clazz.isAnnotationPresent(Excel.class);
            if (isBeanAnnotation) {
                Excel clazzExcel = clazz.getAnnotation(Excel.class);
                if (clazzExcel != null && !TextUtils.isEmpty(clazzExcel.name())) {
                    sheetName = clazzExcel.name();
                }
                sheet = workbook.createSheet(sheetName, 0);
                Field[] fields = clazz.getDeclaredFields();

                int startColumn = Integer.MAX_VALUE;
                int endColumn = 0;
                //添加目录行
                for (Field field : fields) {
                    field.setAccessible(true);
                    //当存在类注解时成员变量存在注解才会被解析
                    if (field.isAnnotationPresent(Excel.class)) {
                        Excel excel = field.getAnnotation(Excel.class);
                        if (excel != null) {
                            startColumn = Math.min(startColumn, excel.column());
                            endColumn = Math.max(endColumn, excel.column());
                            sheet.addCell(new Label(excel.column() - 1, 1, excel.name(), format));
                        }
                    }
                }

                //添加标题行
                sheet.mergeCells(startColumn - 1, 0, endColumn - 1, 0);
                sheet.addCell(new Label(startColumn - 1, 0, sheetName, format));

                //标题行和目录行计数
                titleNum += 2;
            } else {
                sheet = workbook.createSheet(sheetName, 0);
            }
        }


        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            Field[] fields = item.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                field.setAccessible(true);

                Object object = field.get(item);
                String value = object == null ? "" : object.toString();
                //存在类注解按注解解析
                if (isBeanAnnotation) {
                    //当存在类注解时，成员变量只有存在注解才会被解析
                    if (field.isAnnotationPresent(Excel.class)) {
                        Excel excel = field.getAnnotation(Excel.class);
                        if (excel != null) {
                            sheet.addCell(new Label(excel.column() - 1, i + titleNum, value, format));
                        }
                    }
                } else {
                    sheet.addCell(new Label(j, i + titleNum, value, format));
                }
            }
        }
    }

    private static WritableCellFormat getFormat() {
        WritableCellFormat format = null;
        try {
            WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD);
            font.setColour(Colour.BLACK);
            format = new WritableCellFormat(font);
            // 左右居中
            format.setAlignment(Alignment.CENTRE);
            // 上下居中
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 黑色边框
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            //可以换行
            format.setWrap(true);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 读取Excel，第一行默认标题栏，标题栏名字应当与Excel注解中name对应
     */
    public static <T> List<T> readExcel(File file, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            //总行数
            int rows = sheet.getRows();
            //总列数
            int cols = sheet.getColumns();
            //只有1行数据或者没有列数据则直接返回
            if (rows <= 1 || cols <= 0) {
                return list;
            }
            //获取标题行数据
            Cell[] tCells = sheet.getRow(0);
            Field[] tFields = clazz.getDeclaredFields();
            //遍历每一列确定第一行的标题索引
            HashMap<Integer, Field> hashMap = new HashMap<>();
            //遍历每列数据
            for (int i = 0; i < tCells.length; i++) {
                Cell cell = tCells[i];
                for (int j = 0; j < tFields.length; j++) {
                    Field field = tFields[j];
                    if (field.isAnnotationPresent(Excel.class)) {
                        Excel excel = field.getAnnotation(Excel.class);
                        if (excel == null) {
                            continue;
                        }
                        if (TextUtils.equals(cell.getContents(), excel.name())) {
                            hashMap.put(i, field);
                        }
                    }
                }
            }

            //获取每行
            for (int i = 1; i < rows; i++) {
                T t = clazz.newInstance();
                //获取每列
                for (int j = 0; j < cols; j++) {
                    String value = sheet.getCell(j, i).getContents();
                    Field field = hashMap.get(j);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(t, value);
                    }
                }
                list.add(t);
            }
        } catch (IOException | BiffException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }

}









