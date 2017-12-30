package model;

public class Image {
    private Integer id;

    private String name;

    private String md5;

    private String url;

    private String description;

    public Image() {
    }

    public Image(String name, String md5, String url) {
        this.name = name;
        this.md5 = md5;
        this.url = url;
    }

    public Image(String name, String md5, String url, String description) {
        this.name = name;
        this.md5 = md5;
        this.url = url;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}