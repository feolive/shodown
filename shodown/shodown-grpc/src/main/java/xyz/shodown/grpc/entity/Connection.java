package xyz.shodown.grpc.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Connection implements Serializable {

    private static final long serialVersionUID = -8482030021262842577L;

    /**
     * 服务地址
     */
    private String host;

    /**
     * 服务端口
     */
    private Integer port;

    /**
     * 连接关闭超时时长
     */
    private Integer closeSeconds = 5;

}
