package xyz.shodown.grpc.server;

import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import xyz.shodown.common.util.protobuf.ProtobufBeanUtil;

public class GrpcServerPublisher {

    @SneakyThrows
    public static <M extends Message> void publishService(Object result, Class<M> responseType, StreamObserver<M> responseObserver){
        Message.Builder builder = ProtobufBeanUtil.getBuilderByMsgClass(responseType);
        ProtobufBeanUtil.toProtobuf(result,builder);
        M response = (M) builder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
