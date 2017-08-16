package web.entity.sample;

public class UploadFileEntity {
    private String fileName;
    private String imageData;
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
     * @return imageData
     */
    public String getImageData() {
        return imageData;
    }
    /**
     * @param imageData セットする imageData
     */
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
