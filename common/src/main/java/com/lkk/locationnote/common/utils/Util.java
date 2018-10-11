package com.lkk.locationnote.common.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Util {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    public static String formatTime(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static String getLatestLogName() {
        return "log-" + formatTime(System.currentTimeMillis()) + ".txt";
    }

    public static String getFormatTime(Context context, long time) {
        if (DateUtils.isToday(time)) {
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(context, time,
                DateUtils.FORMAT_SHOW_YEAR
                    | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_NUMERIC_DATE);
    }

    public static String getLogsDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/logs";
    }

    /**
     * 压缩文件
     *
     * @param filePath 待压缩的文件路径
     * @param zipName 压缩后的文件名
     * @param keepStructure 是否保持现有文件目录结构
     * @return 压缩后的文件
     */
    public static File zip(String filePath, String zipName, boolean keepStructure) {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String outName = (TextUtils.isEmpty(zipName) ? source.getName() : zipName) + ".zip";
            target = new File(source.getParent(), outName);
            if (target.exists()) {
                target.delete(); // 删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 添加对应的文件Entry
                addEntry("/", source, zos, keepStructure);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zos != null) {
                        zos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return target;
    }

    /**
     * 扫描添加文件Entry
     *
     * @param base   基路径
     * @param source 源文件
     * @param zos    Zip文件输出流
     * @param keepStructure 是否保持现有文件目录结构
     * @throws IOException
     */
    private static void addEntry(String base, File source, ZipOutputStream zos, boolean keepStructure)
            throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            for (File file : source.listFiles()) {
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(keepStructure ? entry + "/" : "/", file, zos, keepStructure);
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        }
    }

    /**
     * 解压文件
     *
     * @param filePath 压缩文件路径
     */
    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zis != null) {
                        zis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
