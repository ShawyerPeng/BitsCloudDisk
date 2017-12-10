package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户的文件信息
 */
@Entity
@Table(name = "user_file")
public class UserFile implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "parent_id", nullable = false)
    private Integer parentId;

    @Column(name = "origin_id", nullable = false)
    private Integer originId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_status", nullable = false)
    private Byte fileStatus;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "modify_time", nullable = false)
    private Date modifyTime;

    @Column(name = "delete_time", nullable = false)
    private Date deleteTime;

    public UserFile() {
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
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

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "fileId=" + fileId +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", originId=" + originId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileStatus=" + fileStatus +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", deleteTime=" + deleteTime +
                '}';
    }
}