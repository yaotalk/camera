package com.minivision.camaraplat.task;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    static final int BUFFER = 8192;

    private static File zipFile;

    /**
     * 压缩单个或多文件方法
     * @param zipPath 压缩后的文件路径
     * @param srcPathName 要压缩的文件路径
     * 参数srcPathName也可以定义成数组形式，需调用方把参数封装到数组中传过来即可
     */
    public static void compress(String zipPath,String... srcPathName) {
        //压缩后的文件对象
        zipFile = new File(zipPath);
        try {
            //创建写出流操作
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            for(String srcPath:srcPathName){
                //创建需要压缩的文件对象
                File file = new File(srcPath);
                if (!file.exists()){
                    throw new RuntimeException(srcPath + "不存在！");
                }
                /*
                 * (1)如果在zip压缩文件中不需要一级文件目录，定义String basedir = "";
                 * 下面的compress方法中当判断文件file是目录后不需要加上basedir = basedir + file.getName() + File.separator;
                 * (2)如果只是想在压缩后的zip文件里包含一级文件目录，不包含二级以下目录，
                 * 直接在这定义String basedir = file.getName() + File.separator;
                 * 下面的compress方法中当判断文件file是目录后不需要加上basedir = basedir + file.getName() + File.separator;
                 * (3)如果想压缩后的zip文件里包含一级文件目录，也包含二级以下目录，即zip文件里的目录结构和原文件一样
                 * 在此定义String basedir = "";
                 * 下面的compress方法中当判断文件file是目录后需要加上basedir = basedir + file.getName() + File.separator;
                 */
                //String basedir = file.getName() + File.separator;
                String basedir = "";
                compress(file, out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static void compress(File file, ZipOutputStream out, String basedir) {
        /*
         * 判断是目录还是文件
         */
        if (file.isDirectory()) {
            basedir += file.getName() + File.separator;
            compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()){
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir);
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            //创建Zip实体，并添加进压缩包
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            //读取待压缩的文件并写进压缩包里
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    }
