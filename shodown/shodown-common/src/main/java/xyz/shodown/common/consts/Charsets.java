package xyz.shodown.common.consts;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: Charsets
 * @Description: 字符编码集
 * @Author: wangxiang
 * @Date: 2021/6/15 13:56
 */
public interface Charsets{
    /**
     * UTF-8
     */
    Charset UTF8 = StandardCharsets.UTF_8;

    /**
     * UTF-16
     */
    Charset UTF16 = StandardCharsets.UTF_16;

    /**
     * ASCII
     */
    Charset ASCII = StandardCharsets.US_ASCII;

    /**
     * ISO_8859_1
     */
    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    /**
     * GBK
     */
    Charset GBK = Charset.forName("GBK");

    /**
     * 跟随操作系统使用编码
     */
    Charset SYSTEM_CHARSET = Charset.defaultCharset();

}
