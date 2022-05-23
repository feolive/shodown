package xyz.shodown.common.util.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import lombok.SneakyThrows;
import xyz.shodown.common.util.json.JsonUtil;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * protobuf bean转换工具类
 */
public class ProtobufBeanUtil {

    private static final String NEW_BUILDER_METHOD = "newBuilder";

    /**
     * 将message转为普通java实体
     * @param message protobuf message
     * @param beanClz 待转Java实体类型
     * @param <T> java实体类
     * @return java实体bean
     * @throws InvalidProtocolBufferException 异常：{@link InvalidProtocolBufferException}
     */
    public static <T> T fromProtobuf(@NotNull Message message,@NotNull Class<T> beanClz) throws InvalidProtocolBufferException {
        String json = JsonFormat.printer().print(message);
        return JsonUtil.jsonToBean(json,beanClz);
    }

    /**
     * 将java bean转换为protobuf message
     * @param bean Java bean
     * @param builder message对象的builder
     * @throws InvalidProtocolBufferException 异常：{@link InvalidProtocolBufferException}
     */
    public static void toProtobuf(@NotNull Object bean,@NotNull Message.Builder builder) throws InvalidProtocolBufferException {
        String json = JsonUtil.objectToJson(bean);
        JsonFormat.parser().merge(json,builder);
    }

    /**
     * 根据message type获取builder
     * @param msgClass message具体实现类
     * @return message对应的builder
     */
    @SneakyThrows
    public static <M extends Message> Message.Builder getBuilderByMsgClass(@NotNull Class<M> msgClass){
        Method method = msgClass.getDeclaredMethod(NEW_BUILDER_METHOD,null);
        return (Message.Builder) method.invoke(null,null);
    }
}
