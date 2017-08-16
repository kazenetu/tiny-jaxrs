package web.entity.sample;

public class UploadFileEntity {
    private String fileName;
    private String fileData;
    /**
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName セットする fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return fileData
     */
    public String getFileData() {
        return fileData;
    }
    /**
     * @param fileData セットする fileData
     */
    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
