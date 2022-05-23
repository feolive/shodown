package xyz.shodown.boot.resource.exception;

/**
 * 异常类
 *
 * @Author : caodaohua
 * @Date: 2021/3/17 16:13
 * @Description : Minio异常
 */
public class MinioClientException extends Exception {
    public MinioClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioClientException(String message) {
        super(message);
    }
}
