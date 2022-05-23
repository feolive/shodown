package xyz.shodown.crypto.enums;


import xyz.shodown.crypto.processor.AdviceCryptoProcessor;
import xyz.shodown.crypto.processor.AesCryptoProcessor;

/**
 * @ClassName: ProcessorEnum
 * @Description: 处理器枚举类
 * @Author: wangxiang
 * @Date: 2021/4/20 11:18
 */
public enum ProcessorEnum {
    /**
     * 切面处理器
     */
    ADVICE("advice", AdviceCryptoProcessor.class),
    AES("aes", AesCryptoProcessor.class);

    /**
     * key
     */
    private String key;

    /**
     * class
     */
    private Class<?> clazz;

    ProcessorEnum(String key, Class<?> clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
