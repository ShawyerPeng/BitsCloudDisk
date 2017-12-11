package controller.reqbody;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RenameFolderReqBody {
    @NotNull
    @Size(min = 1, max = 100)
    private String folderName;

    public RenameFolderReqBody() {
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "RenameFolderReqBody{" +
                "folderName='" + folderName + '\'' +
                '}';
    }
}