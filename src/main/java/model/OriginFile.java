package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 真实原始文件信息
 */
@Entity
@Table(name = "origin_file")
public class OriginFile implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer originFileId;

    @Column(name = "file_md5", unique = true, nullable = false)
    private String fileMd5;

    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_url", unique = true, nullable = false)
    private String fileUrl;

    @Column(name = "file_count", nullable = false)
    private Integer fileCount;

    @Column(name = "file_status", nullable = false)
    private Byte fileStatus;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "modify_time", nullable = false)
    private Date modifyTime;

    public OriginFile() {
    }

    public Integer getOriginFileId() {
        return originFileId;
    }

    public void setOriginFileId(Integer originFileId) {
        this.originFileId = originFileId;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5 == null ? null : fileMd5.trim();
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Byte getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Byte fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "OriginFile{" +
                "originFileId=" + originFileId +
                ", fileMd5='" + fileMd5 + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileCount=" + fileCount +
                ", fileStatus=" + fileStatus +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}