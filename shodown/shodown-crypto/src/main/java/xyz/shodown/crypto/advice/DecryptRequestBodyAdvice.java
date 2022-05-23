package xyz.shodown.crypto.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.ProcessorEnum;
import xyz.shodown.crypto.factory.ProcessorFactory;
import xyz.shodown.crypto.processor.CryptoProcessor;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @ClassName: DecryptRequestBodyAdvice
 * @Description: 解密RequestBody注解参数
 * @Author: wangxiang
 * @Date: 2021/4/13 16:29
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean flag = methodParameter.hasMethodAnnotation(Crypto.class);
        boolean isDecrypt = false;
        if(flag){
            Crypto crypto = methodParameter.getMethodAnnotation(Crypto.class);
            isDecrypt = Objects.requireNonNull(crypto).decrypt();
        }
        return flag&&isDecrypt;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            CryptoProcessor processor = ProcessorFactory.getInstance(ProcessorEnum.ADVICE);
            return processor.decrypt(inputMessage,parameter,targetType,converterType);
        } catch (Exception e) {
           log.error(e.getMessage(),e);
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
}
