package entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "local_file", uniqueConstraints = {@UniqueConstraint(columnNames = { "user_id", "parent", "local_name", "local_type" })})
public class LocalFileDO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer id;
    
    @Column(name = "ldt_create", nullable = false)
    private LocalDateTime ldtCreate;
    
    @Column(name = "ldt_modified", nullable = false)
    private LocalDateTime modifyTime;
    
    @Column(name = "user_id", nullable = false)
    private Integer userID;
    
    @Column(name = "file_id", nullable = false)
    private Integer fileID;
    
    @Column(name = "local_name", nullable = false)
    private String fileName;
    
    @Column(name = "local_type", nullable = false)
    private String fileType;
    
    @Column(name = "parent", nullable = false)
    private Integer parent;
    
    public LocalFileDO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getLdtCreate() {
        return ldtCreate;
    }

    public void setLdtCreate(LocalDateTime ldtCreate) {
        this.ldtCreate = ldtCreate;
    }

    public LocalDateTime getLdtModified() {
        return modifyTime;
    }

    public void setLdtModified(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public String getLocalName() {
        return fileName;
    }

    public void setLocalName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalType() {
        return fileType;
    }

    public void setLocalType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "LocalFileDO [id=" + id + ", ldtCreate=" + ldtCreate + ", modifyTime=" + modifyTime + ", userID="
                + userID + ", fileID=" + fileID + ", fileName=" + fileName + ", fileType=" + fileType + ", parent="
                + parent + "]";
    }
}