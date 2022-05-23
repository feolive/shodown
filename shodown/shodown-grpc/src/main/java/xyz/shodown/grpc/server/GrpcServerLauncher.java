package xyz.shodown.grpc.server;

import cn.hutool.extra.spring.SpringUtil;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import xyz.shodown.grpc.annotaion.GrpcService;

import java.io.IOException;
import java.util.Map;

/**
 * grpc服务端启动器
 */
@Slf4j
public class GrpcServerLauncher {



    /**
     * 定义Grpc Server
     */
    private static final ThreadLocal<Server> SERVER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * GRPC 服务启动方法
     * @param grpcServiceBeanMap @GrpcService注解的bean map
     * @param port 端口
     */
    private static void grpcStart(Map<String, Object> grpcServiceBeanMap,Integer port) {
        try{
            ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
            for (Object bean : grpcServiceBeanMap.values()){
                serverBuilder.addService((BindableService) bean);
                log.info(bean.getClass().getSimpleName() + " is registered in Spring Boot");
            }
            Server server = serverBuilder.build().start();
            log.info("grpc server is started at " +  port);
            server.awaitTermination();
            SERVER_THREAD_LOCAL.set(server);
            Runtime.getRuntime().addShutdownHook(new Thread(GrpcServerLauncher::grpcStop));
        } catch (IOException | InterruptedException e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * GRPC 服务Stop方法
     */
    private static void grpcStop(){
        if (SERVER_THREAD_LOCAL.get() != null){
            SERVER_THREAD_LOCAL.get().shutdownNow();
            SERVER_THREAD_LOCAL.remove();
        }
    }

    /**
     * 启动grpc服务
     * @param port 服务端口
     */
    public static void launch(Integer port){
        Map<String, Object> grpcServiceBeanMap = SpringUtil.getApplicationContext().getBeansWithAnnotation(GrpcService.class);
        grpcStart(grpcServiceBeanMap,port);
    }
}
