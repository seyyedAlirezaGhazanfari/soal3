public class Video extends File {
    private String quality;
    private String videoLength;

    public Video(String name, int size, String type, String address, String quality, String videoLength) {
        super(name, size, type, address);
        this.quality = quality;
        this.videoLength = videoLength;
    }

    public String getQuality() {
        return quality;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }
}
