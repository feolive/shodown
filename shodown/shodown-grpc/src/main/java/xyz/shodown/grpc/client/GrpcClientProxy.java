package xyz.shodown.grpc.client;

import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractBlockingStub;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.protobuf.ProtobufBeanUtil;
import xyz.shodown.grpc.annotaion.GrpcClient;
import xyz.shodown.grpc.entity.Connection;
import xyz.shodown.grpc.exception.ShodownGrpcException;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * grpc客户端代理抽象类
 */
public abstract class GrpcClientProxy implements InitializingBean {

    /**
     * 连接配置
     */
    private Connection connection;

    /**
     * 信道
     */
    private ManagedChannel channel;

    private ManagedChannel getChannel(String host, Integer port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        return channel;
    }

    private ManagedChannel getChannel(String target) {
        channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .defaultLoadBalancingPolicy("round_robin")
                .build();
        return channel;
    }

    /**
     * 关闭连接
     *
     * @throws InterruptedException
     */
    private void shutdown() throws InterruptedException {
        //关闭连接
        if (!channel.isShutdown()) {
            channel.shutdown().awaitTermination(connection.getCloseSeconds(), TimeUnit.SECONDS);
        }
    }

    /**
     * 连接服务
     *
     * @return 当前对象
     */
    private GrpcClientProxy connect() {
        connection = initConnection();
        if (connection == null) {
            throw new ShodownGrpcException("未配置Connection,请先配置连接参数");
        }
        Integer port = connection.getPort();
        String host = connection.getHost();
        if (port == null || port == 0) {
            throw new ShodownGrpcException("服务的端口需要配置");
        }
        if (StringUtil.isBlank(host)) {
            throw new ShodownGrpcException("服务的地址需要配置");
        }
        channel = getChannel(host, port);
        return this;
    }

    /**
     * 阻塞执行grpc服务端提供的service方法
     *
     * @param serviceMethod grpc service方法名称
     * @param messages      proto message参数
     * @param <R>           proto message子类
     * @return 返回proto message执行结果
     */
    @SneakyThrows
    public <R extends Message> R invokeServiceBlocking(@NotNull String serviceMethod, Message... messages) {
        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            connect();
        }
        AbstractBlockingStub blockingStub = getBlockingStub(channel);
        Method[] methods = blockingStub.getClass().getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.equals(serviceMethod)) {
                R r = (R) method.invoke(blockingStub, messages);
                this.shutdown();
                return r;
            }
        }
        this.shutdown();
        return null;
    }

    /**
     * 阻塞执行grpc服务端提供的service方法
     *
     * @param resultClass   返回结果,自定义业务实体类型
     * @param serviceMethod grpc service方法名称
     * @param messages      proto message参数
     * @return 返回结果, 自定义返回实体对象
     */
    public <T> T invokeServiceBlocking(@NotNull Class<T> resultClass, @NotNull String serviceMethod, Message... messages) {
        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            connect();
        }
        return responseResult(invokeServiceBlocking(serviceMethod, messages), resultClass);
    }

    /**
     * 重写方法,返回生成GRPC对象中的静态方法newBlockingStub
     *
     * @param channel
     * @return GRPC对象下的BlockingStub对象
     */
    public abstract <BS extends AbstractBlockingStub> BS getBlockingStub(ManagedChannel channel);

    /**
     * 初始化连接配置
     *
     * @return 连接配置对象
     */
    public Connection initConnection() {
        return connection;
    }

    /**
     * @param protoResultBean proto定义的最终接口返回的message实体
     * @param resultClass     最终返回的实体对象,包含结果码,错误消息,数据
     * @param <T>             实体类型
     * @return grpc处理结果转换后的自定义返回实体
     */
    @SneakyThrows
    public <T> T responseResult(@NotNull Message protoResultBean, @NotNull Class<T> resultClass) {
        return ProtobufBeanUtil.fromProtobuf(protoResultBean, resultClass);
    }

    /**
     * 将DTO参数对象转换成proto定义的参数对象
     *
     * @param param   DTO参数
     * @param builder proto参数对象的内置静态Builder
     * @return 可用于grpc调用的参数
     */
    @SneakyThrows
    public Message requestParam(@NotNull Object param, @NotNull Message.Builder builder) {
        ProtobufBeanUtil.toProtobuf(param, builder);
        return builder.build();
    }

    /**
     * 将DTO参数转换成proto定义的参数对象
     *
     * @param param      DTO参数
     * @param protoClass proto参数对象类型
     * @return proto message对象
     */
    @SneakyThrows
    public <M extends Message> Message requestParam(@NotNull Object param, @NotNull Class<M> protoClass) {
        Message.Builder builder = ProtobufBeanUtil.getBuilderByMsgClass(protoClass);
        ProtobufBeanUtil.toProtobuf(param, builder);
        return builder.build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GrpcClient annotation = this.getClass().getAnnotation(GrpcClient.class);
        if (annotation != null && this.connection == null) {
            connection = new Connection();
            connection.setHost(annotation.host());
            connection.setPort(annotation.port());
            connection.setCloseSeconds(annotation.closeSeconds());
        }
    }
}
