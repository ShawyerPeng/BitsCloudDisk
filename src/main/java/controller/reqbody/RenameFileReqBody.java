package controller.reqbody;

//import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

public class RenameFileReqBody {
    //@NonNull
    @Size(min = 1, max = 100)
    private String fileName;

    //@NonNull
    @Size(min = 1, max = 100)
    private String fileType;

    public RenameFileReqBody() {
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

    @Override
    public String toString() {
        return "RenameFileReqBody{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}