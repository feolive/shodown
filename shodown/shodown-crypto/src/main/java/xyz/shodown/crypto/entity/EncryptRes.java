package xyz.shodown.crypto.entity;

import lombok.Data;

/**
 * @ClassName: EncryptRes
 * @Description: 加密返回实体
 * @Author: wangxiang
 * @Date: 2021/7/12 10:09
 */
@Data
public class EncryptRes {

    /**
     * 签名
     */
    private String sign;

    /**
     * 加密数据
     */
    private String encryptData;
}
