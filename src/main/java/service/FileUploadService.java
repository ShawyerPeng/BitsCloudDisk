package service;


public interface FileUploadService {
    List<FileInfo> listFiles();

    FileInfo getFile(Integer id);

    FileInfo saveFile(FileInfo image);
}