package xyz.shodown.crypto.processor;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.util.basic.ListUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.json.JsonUtil;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.enums.CryptoErr;
import xyz.shodown.crypto.exception.VerifySignException;
import xyz.shodown.crypto.factory.AlgorithmFactory;
import xyz.shodown.crypto.handler.AlgorithmHandler;
import xyz.shodown.crypto.keychain.KeyChain;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: AdviceCryptoProcessor
 * @Description: advice切面处理器
 * @Author: wangxiang
 * @Date: 2021/4/19 10:18
 */
@Slf4j(topic = LogCategory.BUSINESS)
public class AdviceCryptoProcessor implements CryptoProcessor{


    @Override
    public HttpInputMessage decrypt(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws Exception {
        InputStream msgBody = inputMessage.getBody();
        HttpHeaders headers = inputMessage.getHeaders();
        // 注解
        Crypto crypto = Objects.requireNonNull(parameter.getMethodAnnotation(Crypto.class));
        if(!crypto.decrypt()){
            return inputMessage;
        }
        // 解密
        String data = doDecrypt(crypto,msgBody,headers);
        if(StringUtil.isEmpty(data)){
            return inputMessage;
        }
        InputStream stream = IoUtil.toUtf8Stream(data);
        return new MappingJacksonInputMessage(stream,headers);
    }

    @Override
    public String decrypt(String encryptData) {
        return null;
    }

    @Override
    public Object encrypt(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) throws Exception {
        if(body==null){
            return null;
        }
        // 将数据转化成字符串
        String json = JsonUtil.objectToJson(body);
        Crypto crypto = Objects.requireNonNull(returnType.getMethodAnnotation(Crypto.class));
        if(crypto.encrypt()){
            return doEncrypt(crypto,json,response);
        }else {
            return body;
        }

    }

    @Override
    public void setKeyChainClass(Class<? extends KeyChain> keyChain) {

    }

    private String doEncrypt(@NotNull Crypto crypto,String body,ServerHttpResponse response) throws Exception {
        Algorithm algorithm = crypto.algorithm();
        // 加密算法处理器
        AlgorithmHandler handler = AlgorithmFactory.create(algorithm);
        String topic = StringUtil.isBlank(crypto.topic())?
                crypto.topic():Symbols.LEFT_SQUARE_BRACKET+crypto.topic()+Symbols.RIGHT_SQUARE_BRACKET+Symbols.MINUS;
        log.info("[加密处理],"+topic+"加密开始,接收数据为:{}",body);
        // 总开关
        boolean fl = handler.fetchSwitch();
        if(!fl){
            log.info("[加密处理],"+topic+"加解密总开关关闭,保持原始数据:{}",body);
            return body;
        }
        handler.prepare(crypto).load(body);
        if (!algorithm.isSymmetric()) {
            // 非对称加密
            String sign = handler.sign();
            response.getHeaders().set(HttpConst.Header.SIGN, sign);
            log.info("[加密处理],"+topic+"签名生成:{}",sign);
        }
        String res = handler.encrypt();
        log.info("[加密处理],加密结束,加密后结果:{}", res);
        return res;
    }

    /**
     * 具体解密方法
     * @param crypto 注解
     * @param msgBody 消息
     * @param headers 消息头
     * @return 解密明文
     * @throws IOException IOException
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException IllegalAccessException
     */
    private String doDecrypt(@NotNull Crypto crypto,InputStream msgBody,HttpHeaders headers) throws Exception {
        // 判断是否是对称加密
        Algorithm algorithm = crypto.algorithm();
        String topic = StringUtil.isBlank(crypto.topic())?
                crypto.topic():Symbols.LEFT_SQUARE_BRACKET+crypto.topic()+Symbols.RIGHT_SQUARE_BRACKET+Symbols.MINUS;
        String sign = "";
        if(!algorithm.isSymmetric()){
            // 非对称加密则需要验证签名
            // 判断签名
            List<String> list = headers.getOrEmpty(HttpConst.Header.SIGN);
            if (ListUtil.isEmpty(list)){
                throw new VerifySignException(topic,CryptoErr.NO_SIGN_IN_HEADER);
            }
            // 签名
            sign = list.get(0);
        }
        if(msgBody.available()<=0){
            log.info("[解密处理],"+topic+"消息内容为空");
            return null;
        }
        String encryptData = IoUtil.read(msgBody, Charsets.UTF8);
        // 加密算法处理器
        AlgorithmHandler handler = AlgorithmFactory.create(algorithm);
        // 总开关
        boolean fl = handler.fetchSwitch();
        if(!fl){
            return encryptData;
        }
        log.info("[解密处理],"+topic+"解密开始,接收数据为:{}",encryptData);
        String data = handler.prepare(crypto).load(encryptData).decrypt();
        boolean verify = handler.load(data).verify(sign);
        if(!verify){
            throw new VerifySignException(CryptoErr.VERIFY_ERR);
        }
        log.info("[解密处理],"+topic+"解密结束,解密后结果:{}", data);
        return data;
    }

}
