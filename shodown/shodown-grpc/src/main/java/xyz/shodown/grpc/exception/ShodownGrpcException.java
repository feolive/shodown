package xyz.shodown.grpc.exception;

/**
 * Grpc异常
 */
public class ShodownGrpcException extends RuntimeException{

    public ShodownGrpcException(String message) {
        super(message);
    }

    public ShodownGrpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShodownGrpcException(Throwable cause) {
        super(cause);
    }
}
