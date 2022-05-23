package xyz.shodown.common.util.io;

import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.io.watch.WatchKind;
import cn.hutool.core.io.watch.WatchMonitor;

import javax.management.monitor.Monitor;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @Author : caodaohua
 * @Date: 2021/6/17 17:43
 * @Description : 文件工具类
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {


    /**
     * 构造
     * @param file 文件
     * @param charset 编码
     */
    public static FileReader getFileReader(File file, Charset charset){
        return new FileReader(file,charset);
    }


    /**
     * 构造
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset 编码
     */
    public static FileReader getFileReader(String filePath, Charset charset){
        return new FileReader(filePath,charset);
    }


    /**
     * 构造
     * @param file 文件
     * @param charset 编码
     */
    public static FileWriter getFileWriter(File file, Charset charset){
        return new FileWriter(file,charset);
    }

    /**
     * 构造
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset 编码
     */
    public static FileWriter getFileWriter(String filePath, Charset charset){
        return new FileWriter(filePath,charset);
    }

    /**
     * 构造
     *
     * @param destFile 目标文件
     * @param charset 编码
     * @param capacity 当行数积累多少条时刷入到文件
     * @param isNewLineMode 追加内容是否为新行
     */
    public static FileAppender getFileAppender(File destFile, Charset charset, int capacity, boolean isNewLineMode){
        return new FileAppender(destFile,charset,capacity,isNewLineMode);
    }

    /**
     * 构造
     *
     * @param file 文件
     * @param charset 编码
     * @param lineHandler 行处理器
     * @param initReadLine 启动时预读取的行数
     * @param period 检查间隔
     */
    public static Tailer getTailer(File file, Charset charset, LineHandler lineHandler, int initReadLine, long period){

        return new Tailer(file,charset,lineHandler,initReadLine,period);
    }



}
