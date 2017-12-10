package reqbody;

import java.util.List;

public class ShredReqBody {
    private List<Integer> files;
    private List<Integer> folders;
    
    @Override
    public String toString() {
        return "ShredReqBody [files=" + files + ", folders=" + folders + "]";
    }

    public void setFiles(List<Integer> files) {
        this.files = files;
    }

    public void setFolders(List<Integer> folders) {
        this.folders = folders;
    }
}