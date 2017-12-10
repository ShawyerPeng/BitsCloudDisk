package reqbody;

import javax.validation.constraints.NotNull;

public class UploadReqBody {
    @NotNull
    private Integer userId;
    @NotNull
    private String fileMd5;
    @NotNull
    private String fileName;
    @NotNull
    private String fileType;
    @NotNull
    private Integer parentId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "UploadReqBody{" +
                "userId=" + userId +
                ", fileMd5='" + fileMd5 + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}