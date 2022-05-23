package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: aes加密文本及iv向量
 * @author: wangxiang
 * @date: 2022/5/15 23:25
 */
@Data
public class AesEncryptDto implements Serializable {

    /**
     * 加密数据
     */
    private String encryptData;

    /**
     * iv向量
     */
    private String iv;
}
