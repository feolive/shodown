package xyz.shodown.crypto.processor;

import cn.hutool.core.io.IoUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.util.json.JsonUtil;
import xyz.shodown.crypto.entity.EncryptRes;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.helper.CryptoHelper;
import xyz.shodown.crypto.keychain.KeyChain;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @description: aes加解密切面处理
 * @author: wangxiang
 * @date: 2022/5/12 19:44
 */
public class AesCryptoProcessor implements CryptoProcessor{

    private final CryptoHelper cryptoHelper = new CryptoHelper(Algorithm.AES);

    @Override
    public HttpInputMessage decrypt(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws Exception {
        InputStream msgBody = inputMessage.getBody();
        HttpHeaders headers = inputMessage.getHeaders();
        String body = IoUtil.read(msgBody, Charsets.UTF8);
        cryptoHelper.setTopic("全局AES解密:");
        String data = cryptoHelper.decrypt(body);
        InputStream stream = IoUtil.toUtf8Stream(data);
        return new MappingJacksonInputMessage(stream,headers);
    }

    @Override
    public String decrypt(String encryptData) throws Exception {
        cryptoHelper.setTopic("全局AES解密:");
        return cryptoHelper.decrypt(encryptData);
    }

    @Override
    public Object encrypt(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) throws Exception {
        cryptoHelper.setTopic("全局AES加密:");
        String json = JsonUtil.objectToJson(body);
        EncryptRes encryptRes = cryptoHelper.encrypt(json);
        if(encryptRes!=null){
            return encryptRes.getEncryptData();
        }
        return null;
    }

    @Override
    public void setKeyChainClass(Class<? extends KeyChain> keyChain) {
        cryptoHelper.setKeyChain(keyChain);
    }

}
