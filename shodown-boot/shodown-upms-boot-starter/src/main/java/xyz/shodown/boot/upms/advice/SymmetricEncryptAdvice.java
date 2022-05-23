package xyz.shodown.boot.upms.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.keychain.DynamicSecretKeyChain;
import xyz.shodown.boot.upms.keychain.UserSecretKeyStorage;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.util.encrypt.CryptoKeyGenerator;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.ProcessorEnum;
import xyz.shodown.crypto.factory.ProcessorFactory;
import xyz.shodown.crypto.processor.CryptoProcessor;
import xyz.shodown.crypto.properties.CryptoProperties;

import javax.annotation.Resource;

/**
 * @description: 全局处理接口的对称加密
 * @author: wangxiang
 * @date: 2022/5/12 19:20
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@ControllerAdvice
public class SymmetricEncryptAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private CryptoProperties cryptoProperties;

    @Resource
    private AdditionalProperties additionalProperties;

    @Resource
    private UserSecretKeyStorage userSecretKeyStorage;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if(returnType.hasMethodAnnotation(IgnoreGeneralCrypto.class)){
            return false;
        }
        if(returnType.hasMethodAnnotation(Crypto.class)){
            return false;
        }
        return ShodownUpmsUtil.shouldCrypto(cryptoProperties);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            String iv = CryptoKeyGenerator.generateIv();
            // iv向量
            userSecretKeyStorage.storeIv(iv);
            CryptoProcessor processor = ProcessorFactory.getInstance(ProcessorEnum.AES);
            processor.setKeyChainClass(DynamicSecretKeyChain.class);
            Object obj = processor.encrypt(body,returnType,selectedContentType,selectedConverterType,request,response);
            if(obj==null){
                return null;
            }
            // 加密数据
            String encryptData = (String) obj;
            // 默认将iv向量置于加密文本首部
            int ivOffset = 1;
            if(additionalProperties!=null){
                ivOffset = additionalProperties.getAccess().getIvOffset();
            }
            return ShodownUpmsUtil.blendIvAndEncryptData(iv,ivOffset,encryptData);
        } catch (Exception e) {
           log.error(e.getMessage(),e);
           return null;
        } finally {
            userSecretKeyStorage.removeIv();
        }
    }
}
