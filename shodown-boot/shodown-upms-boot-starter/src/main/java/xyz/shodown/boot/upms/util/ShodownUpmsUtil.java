package xyz.shodown.boot.upms.util;

import org.springframework.lang.NonNull;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.model.AesEncryptDto;
import xyz.shodown.common.util.basic.ListUtil;
import xyz.shodown.crypto.properties.CryptoProperties;

import java.util.List;

/**
 * @description: 工具类
 * @author: wangxiang
 * @date: 2022/5/12 00:00
 */
public class ShodownUpmsUtil {

    /**
     * 判断是否需要忽略掉当前的url
     * @param additionalProperties 配置
     * @param requestUrl 请求地址
     * @return true忽略 false不忽略
     */
    public static boolean shouldIgnoreUrl(AdditionalProperties additionalProperties, String requestUrl){
        if(additionalProperties!=null){
            List<String> ignoreUrls = additionalProperties.getAccess().getIgnoreUrls();
            if(ListUtil.isEmpty(ignoreUrls)){
                return false;
            }else {
                if(ignoreUrls.contains(requestUrl)){
                    return true;
                }else {
                    for (String ignoreUrl : ignoreUrls) {
                        boolean flag = requestUrl.contains(ignoreUrl);
                        if(flag){
                            return true;
                        }
                    }
                    return false;
                }
            }
        }else {
            return false;
        }
    }

    /**
     * 判断是否使用动态secret key
     * @return true使用动态secret key
     */
    public static boolean shouldUseDynamicSecretKey(CryptoProperties cryptoProperties,AdditionalProperties additionalProperties){
        if(!shouldCrypto(cryptoProperties)){
            return false;
        }
        return additionalProperties != null && additionalProperties.getAccess().isDynamicSecretKey();
    }

    /**
     * 是否开启加解密
     * @param cryptoProperties 加解密配置
     * @return true开启 false关闭
     */
    public static boolean shouldCrypto(CryptoProperties cryptoProperties){
        return cryptoProperties!=null&&cryptoProperties.isSwitcher();
    }

    /**
     * 从加密内容中提取iv和加密的数据
     * @param encryptTxt 加密文本
     * @param ivOffset iv向量从第几位提取
     * @return iv向量
     */
    public static AesEncryptDto extractDataAndIv(@NonNull String encryptTxt, int ivOffset){
        AesEncryptDto dto = new AesEncryptDto();
        if(ivOffset<1){
            throw new RuntimeException("iv文本的起始位置必须大于1");
        }
        if(ivOffset>encryptTxt.length()){
            throw new RuntimeException("iv文本的起始位置不可超过原文本长度");
        }
        if(ivOffset+15>encryptTxt.length()){
            throw new RuntimeException("iv文本的起始位置导致最终长度超出原文本");
        }
        if(ivOffset==1){
            String iv = encryptTxt.substring(0,16);
            String data = encryptTxt.substring(16);
            dto.setEncryptData(data);
            dto.setIv(iv);
        }else {
            String prefix = encryptTxt.substring(0,ivOffset);
            String iv = encryptTxt.substring(ivOffset-1,ivOffset+15);
            String suffix = encryptTxt.substring(ivOffset+15);
            dto.setEncryptData(prefix+suffix);
            dto.setIv(iv);
        }
        return dto;
    }

    /**
     * 混合iv向量和加密文本
     * @param iv iv
     * @param ivOffset 混入的偏移量
     * @param data 加密数据
     * @return 混淆后的文本
     */
    public static String blendIvAndEncryptData(@NonNull String iv,int ivOffset,@NonNull String data){
        if(ivOffset==1){
            return iv+data;
        }
        String prefix = data.substring(0,ivOffset);
        String suffix = data.substring(ivOffset-1);
        return prefix + iv + suffix;
    }

}
