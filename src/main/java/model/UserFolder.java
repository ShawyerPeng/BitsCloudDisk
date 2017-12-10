package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户的文件夹信息
 */
@Entity
@Table(name = "local_folder")
public class UserFolder implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer folderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "parent_id", nullable = false)
    @NotNull
    private Integer parentId;

    @Column(name = "folder_name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String folderName;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "modify_time", nullable = false)
    private Date modifyTime;

    @Column(name = "delete_time", nullable = false)
    private Date deleteTime;

    public UserFolder() {
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName == null ? null : folderName.trim();
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
        return "UserFolder{" +
                "folderId=" + folderId +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", folderName='" + folderName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", deleteTime=" + deleteTime +
                '}';
    }
}