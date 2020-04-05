public class Image extends File {
    private String resolution;
    private String format;

    public Image(String name, int size, String type, String address, String resolution, String format) {
        super(name, size, type, address);
        this.resolution = resolution;
        this.format = format;
    }

    public String getResolution() {
        return resolution;
    }

    public String getFormat() {
        return format;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
