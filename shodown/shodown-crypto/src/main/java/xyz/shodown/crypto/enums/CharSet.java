package xyz.shodown.crypto.enums;

import xyz.shodown.common.consts.Charsets;

import java.nio.charset.Charset;

/**
 * @ClassName: CharSet
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/6/15 14:40
 */
public enum CharSet {

    /**
     * UTF-8
     */
    UTF8(Charsets.UTF8),

    /**
     * UTF-16
     */
    UTF16(Charsets.UTF16),

    /**
     * GBK
     */
    GBK(Charsets.GBK),

    /**
     * ISO_8859_1
     */
    ISO_8859_1(Charsets.ISO_8859_1),

    /**
     * ASCII
     */
    ASCII(Charsets.ASCII)

    ;

    CharSet(Charset charset) {
        this.charset = charset;
    }

    private Charset charset;

    public Charset getCharset() {
        return charset;
    }
}
