package xyz.shodown.boot.resource.entity;

import io.minio.messages.*;
import lombok.Data;

import java.util.Date;

/**
 * @Author : caodaohua
 * @Date: 2021/3/18 16:46
 * @Description :
 */
@Data
public class FileItem extends Contents {

    // 存储对象名称
    private String objectName;
    // 版本号
    private String versionId;
    // 大小
    private long size;
    // 时间
    private Date dateTime;
    // 是否是目录
    private boolean isDir;
    // 是否是最新内容
    private boolean isLatest;
    // 是否是一个删除标识
    private boolean deleteMarker;


    public FileItem() {
    }

    public FileItem(String prefix) {
        super(prefix);
    }
}
