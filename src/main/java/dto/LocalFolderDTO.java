package dto;

import java.time.LocalDateTime;

public class LocalFolderDTO extends Sortable {
    private Integer id;
    private LocalDateTime ldtCreate;
    private Integer userId;
    private Integer parent;

    public LocalFolderDTO() {
    }

    public void setLdtModified(LocalDateTime ldtModified) {
        this.ldtModified = ldtModified;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

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

    public Integer getUserID() {
        return userId;
    }

    public void setUserID(Integer userId) {
        this.userId = userId;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "LocalFolderDTO [id=" + id + ", ldtCreate=" + ldtCreate + ", userId=" + userId + ", parent=" + parent
                + ", ldtModified=" + ldtModified + ", localName=" + localName + "]";
    }
}