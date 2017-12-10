package entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "local_folder", uniqueConstraints = {@UniqueConstraint(columnNames = { "user_id", "parent", "local_name" })})
public class LocalFolderDO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "ldt_create", nullable = false)
    private LocalDateTime ldtCreate;
    
    @Column(name = "ldt_modified", nullable = false)
    private LocalDateTime ldtModified;
    
    @Column(name = "user_id", nullable = false)
    private Integer userID;
    
    @Column(name = "local_name", nullable = false) @NotNull @Size(min=1, max=100)
    private String localName;
    
    @Column(name = "parent", nullable = false) @NotNull
    private Integer parent;
    
    public LocalFolderDO() {}

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
        return ldtModified;
    }

    public void setLdtModified(LocalDateTime ldtModified) {
        this.ldtModified = ldtModified;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "LocalFolderDO [id=" + id + ", ldtCreate=" + ldtCreate + ", ldtModified=" + ldtModified + ", userID="
                + userID + ", localName=" + localName + ", parent=" + parent + "]";
    }
    
}
