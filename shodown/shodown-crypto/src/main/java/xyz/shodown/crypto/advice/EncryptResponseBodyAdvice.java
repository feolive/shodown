package xyz.shodown.crypto.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.ProcessorEnum;
import xyz.shodown.crypto.factory.ProcessorFactory;
import xyz.shodown.crypto.processor.CryptoProcessor;

import java.util.Objects;

/**
 * @ClassName: EncryptResponseBodyAdvice
 * @Description: 出参加密
 * @Author: wangxiang
 * @Date: 2021/4/13 16:29
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean flag = returnType.hasMethodAnnotation(Crypto.class);
        boolean isEncrypt = false;
        if(flag){
            Crypto crypto = returnType.getMethodAnnotation(Crypto.class);
            isEncrypt = Objects.requireNonNull(crypto).encrypt();
        }
        return flag&&isEncrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            CryptoProcessor processor = ProcessorFactory.getInstance(ProcessorEnum.ADVICE);
            // 加密body
            return processor.encrypt(body,returnType,selectedContentType,selectedConverterType,request,response);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }
}
