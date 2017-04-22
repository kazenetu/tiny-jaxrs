package web.entity.sample;

public class UploadEntity {
    private String fileName;
    private String imageData;
    private String tag;
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
    /**
     * @return tag
     */
    public String getTag() {
        return tag;
    }
    /**
     * @param tag セットする tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

}
