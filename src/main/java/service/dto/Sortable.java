package service.dto;


import java.util.Date;

public abstract class Sortable {
    protected Date modifyTime;
    protected String fileFolderName;

    public Date getModifyTime() {
        return modifyTime;
    }

    public String getFileFolderName() {
        return fileFolderName;
    }
}