package reqbody;

//import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

public class RenameFileReqBody {
    //@NonNull
    @Size(min = 1, max = 100)
    private String fileName;

    //@NonNull
    @Size(min = 1, max = 100)
    private String localType;

    public RenameFileReqBody() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalType() {
        return localType;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    @Override
    public String toString() {
        return "RenameFileReqBody{" +
                "fileName='" + fileName + '\'' +
                ", localType='" + localType + '\'' +
                '}';
    }
}