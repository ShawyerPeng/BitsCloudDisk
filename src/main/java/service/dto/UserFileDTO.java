package service.dto;

import java.util.Date;

public class UserFileDTO extends Sortable {
    private Integer fileId;

    private Integer userId;

    private Integer parentId;

    private Integer originId;

    private String fileName;

    private String fileType;

    private Byte fileStatus;

    private Date createTime;

    private Date modifyTime;

    private Date deleteTime;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件URL
     */
    private String fileUrl;

    public UserFileDTO() {
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "UserFileDTO{" +
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
                ", fileSize=" + fileSize +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}