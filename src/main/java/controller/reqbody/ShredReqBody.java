package controller.reqbody;

import java.util.List;

public class ShredReqBody {
    private List<Integer> files;
    private List<Integer> folders;

    public List<Integer> getFiles() {
        return files;
    }

    public void setFiles(List<Integer> files) {
        this.files = files;
    }

    public List<Integer> getFolders() {
        return folders;
    }

    public void setFolders(List<Integer> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "ShredReqBody{" +
                "files=" + files +
                ", folders=" + folders +
                '}';
    }
}