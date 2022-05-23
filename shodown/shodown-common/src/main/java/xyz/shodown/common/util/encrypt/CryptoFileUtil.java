package xyz.shodown.common.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;

/**
 * @ClassName: CryptoFile
 * @Description: 文件加解密依赖
 * @Author: wangxiang
 * @Date: 2021/6/16 19:36
 */
public class CryptoFileUtil {

    /**
     * 文件转字节数组
     * @param file 文件
     * @return 字节数组
     * @throws IOException IO异常
     */
    protected static byte[] flieToBytes(File file) throws IOException {
        try(FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream bout = new ByteArrayOutputStream()){
            byte[] tmpbuf = new byte[1024];
            int count = 0;
            while ((count = in.read(tmpbuf)) != -1) {
                bout.write(tmpbuf, 0, count);
                tmpbuf = new byte[1024];
            }
            return bout.toByteArray();
        }
    }

    /**
     * 文件输出
     * @param bytes 待输出字节数组
     * @param resultFile 用于接收的输出文件
     * @return 输出文件
     * @throws IOException
     */
    protected static File outputFile(byte[] bytes,File resultFile) throws IOException {
        try (OutputStream out = new FileOutputStream(resultFile)){
            out.write(bytes);
            out.flush();
            return resultFile;
        }
    }

    /**
     * 使用cipher对象进行文件加密
     * @param sourceFile 源文件
     * @param cipher cipher对象
     * @param result 输出文件
     * @return 输出文件
     * @throws IOException IO异常
     */
    protected static File streamCipherEncryptFile(File sourceFile,Cipher cipher,File result) throws IOException {
        try(
                InputStream inputStream = new FileInputStream(sourceFile);
                CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
                OutputStream outputStream = new FileOutputStream(result)) {
            byte[] cache = new byte[1024];
            int isRead = 0;
            while ((isRead = cipherInputStream.read(cache, 0, cache.length)) != -1) {
                outputStream.write(cache, 0, isRead);
                outputStream.flush();
            }
            return result;
        }
    }

    /**
     * 使用cipher对象进行文件解密
     * @param encryptFile 加密文件
     * @param cipher cipher对象
     * @param result 输出文件
     * @return 输出文件
     * @throws IOException IO异常
     */
    protected static File streamCipherDecryptFile(File encryptFile,Cipher cipher,File result) throws IOException{
        try (
                OutputStream outputStream = new FileOutputStream(result);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
                InputStream inputStream = new FileInputStream(encryptFile)) {

            byte[] cache = new byte[1024];
            int isRead = 0;
            while ((isRead = inputStream.read(cache, 0, cache.length)) != -1) {
                cipherOutputStream.write(cache, 0, isRead);
                cipherOutputStream.flush();
            }
            return result;
        }
    }
}
