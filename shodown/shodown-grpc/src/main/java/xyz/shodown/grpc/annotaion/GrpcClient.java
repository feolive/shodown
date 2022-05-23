package xyz.shodown.grpc.annotaion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcClient {

    /**
     * 服务地址
     */
    String host() default "";

    /**
     * 服务端口
     */
    int port() default 0;

    /**
     * 连接关闭时长
     */
    int closeSeconds() default 5;

}
