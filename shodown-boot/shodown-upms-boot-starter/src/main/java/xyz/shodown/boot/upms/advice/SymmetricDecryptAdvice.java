package xyz.shodown.boot.upms.advice;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.keychain.DynamicSecretKeyChain;
import xyz.shodown.boot.upms.keychain.UserSecretKeyStorage;
import xyz.shodown.boot.upms.model.AesEncryptDto;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.ProcessorEnum;
import xyz.shodown.crypto.factory.ProcessorFactory;
import xyz.shodown.crypto.processor.CryptoProcessor;
import xyz.shodown.crypto.properties.CryptoProperties;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @description: 全局处理接口的对称解密
 * @author: wangxiang
 * @date: 2022/5/12 19:22
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@ControllerAdvice
public class SymmetricDecryptAdvice implements RequestBodyAdvice {

    @Resource
    private CryptoProperties cryptoProperties;

    @Resource
    private AdditionalProperties additionalProperties;

    @Resource
    private UserSecretKeyStorage userSecretKeyStorage;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if(methodParameter.hasMethodAnnotation(IgnoreGeneralCrypto.class)){
            return false;
        }
        if(methodParameter.hasMethodAnnotation(Crypto.class)){
            return false;
        }
        return ShodownUpmsUtil.shouldCrypto(cryptoProperties);
    }

    @Override
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage,@NonNull MethodParameter parameter,@NonNull Type targetType,@NonNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            CryptoProcessor processor = ProcessorFactory.getInstance(ProcessorEnum.AES);
            processor.setKeyChainClass(DynamicSecretKeyChain.class);
            AesEncryptDto dto = extractIvAndData(inputMessage);
            if(dto==null){
                return processor.decrypt(inputMessage,parameter,targetType,converterType);
            }
            HttpHeaders headers = inputMessage.getHeaders();
            userSecretKeyStorage.storeIv(dto.getIv());
            String data = processor.decrypt(dto.getEncryptData());
            InputStream stream = IoUtil.toUtf8Stream(data);
            return new MappingJacksonInputMessage(stream,headers);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            userSecretKeyStorage.removeIv();
            return null;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 提取出iv和加密body
     * @param inputMessage {@link HttpInputMessage}
     * @return {@link AesEncryptDto}
     */
    private AesEncryptDto extractIvAndData(@NonNull HttpInputMessage inputMessage) throws IOException {
        if(!ShodownUpmsUtil.shouldUseDynamicSecretKey(cryptoProperties,additionalProperties)){
            return null;
        }
        InputStream msgBody = inputMessage.getBody();
        String body = IoUtil.read(msgBody, Charsets.UTF8);
        if(StringUtil.isBlank(body)){
            AesEncryptDto dto = new AesEncryptDto();
            dto.setEncryptData("");
            dto.setIv("");
            return dto;
        }
        int offset = additionalProperties==null?1:additionalProperties.getAccess().getIvOffset();
        return ShodownUpmsUtil.extractDataAndIv(body,offset);
    }
}
