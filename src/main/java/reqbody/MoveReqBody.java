package reqbody;

import java.util.List;

public class MoveReqBody {
    private List<Integer> files;
    private List<Integer> folders;
    private int dest;

    public MoveReqBody() {
    }

    public void setFiles(List<Integer> files) {
        this.files = files;
    }

    public void setFolders(List<Integer> folders) {
        this.folders = folders;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "MoveActionForm [files=" + files + ", folders=" + folders + ", dest=" + dest + "]";
    }
}