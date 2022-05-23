package xyz.shodown.crypto.processor;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import xyz.shodown.crypto.keychain.KeyChain;

import java.lang.reflect.Type;

/**
 * @ClassName: CryptoProcessor
 * @Description: 加解密处理接口
 * @Author: wangxiang
 * @Date: 2021/4/15 15:39
 */
public interface CryptoProcessor{

    /**
     * advice切面解密
     */
    HttpInputMessage decrypt(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws Exception;

    /**
     * 直接解密内容
     * @param encryptData 加密文本
     * @return 解密文本
     */
    String decrypt(String encryptData) throws Exception;

    /**
     * advice切面加密
     */
    Object encrypt(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) throws Exception;

    /**
     * 设置keychain的实例class
     */
    void setKeyChainClass(Class<? extends KeyChain> keyChain);

}
