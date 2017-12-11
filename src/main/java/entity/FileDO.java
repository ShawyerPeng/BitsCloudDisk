package entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "file")
public class FileDO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer id;
    
    @Column(name = "ldt_create", nullable = false)
    private LocalDateTime ldtCreate;
    
    @Column(name = "ldt_modified", nullable = false)
    private LocalDateTime modifyTime;
    
    @Column(name = "md5", unique = true, nullable = false)
    private String md5;
    
    @Column(name = "size", nullable = false)
    private Integer size;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "url", unique = true, nullable = false)
    private String url;
    
    public FileDO() {}

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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FileDO [id=" + id + ", ldtCreate=" + ldtCreate + ", modifyTime=" + modifyTime + ", md5=" + md5
                + ", size=" + size + ", type=" + type + ", url=" + url + "]";
    }
    
}
