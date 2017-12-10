package reqbody;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

public class RenameFolderReqBody {
    @NonNull @Size(min=1, max=100)
    private String fileName;
    
    public RenameFolderReqBody() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "RenameFolderReqBody{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}