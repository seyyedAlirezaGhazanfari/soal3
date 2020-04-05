import java.util.ArrayList;

public class File {
    protected String name;
    protected int size;
    protected String type;
    protected String address;
    protected CopyForm copyForm;
    private static ArrayList<File> allFiles = new ArrayList<>();

    public File(String name, int size, String type, String address) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.address = address;
        copyForm = CopyForm.FREE;
        allFiles.add(this);
    }

    public static ArrayList<File> getAllFiles() {
        return allFiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CopyForm getCopyForm() {
        return copyForm;
    }

    public void setCopyForm(CopyForm copyForm) {
        this.copyForm = copyForm;
    }
    public static File getFileByNameAndAddress(String name , String address){
        for (File file : allFiles) {
            if (file.getName().equalsIgnoreCase(name) && file.getAddress().equals(address))
                return file;
        }
        return null;
    }
    public static ArrayList<File> getFilesCopied(){
        ArrayList<File> copied = new ArrayList<>();
        for (File file : allFiles) {
            if (file.getCopyForm().equals(CopyForm.COPIED))
                copied.add(file);
        }
        return copied;
    }
    public static ArrayList<File> cutFile(){
        ArrayList<File> cutFiles = new ArrayList<>();
        for (File file : allFiles) {
            if (file.getCopyForm().equals(CopyForm.CUT))
                    cutFiles.add(file);
        }
        return cutFiles;
    }
    public static void setFiles(File file , File file2){
        if (file.getName().equalsIgnoreCase(file2.getName())){
            if (file.type.equals("txt")){
                Note note = (Note) file;
                Note note1 = (Note) file2;
                note.setText(note1.getText());
            }
            if (file.type.equals("img")){
                Image image = (Image) file;
                Image image1 = (Image) file2;
                image.setResolution(image1.getResolution());
                image.setFormat(image1.getFormat());
            }
            if (file.type.equals("mp4")){
                Video video = (Video) file;
                Video video1 = (Video) file2;
                video.setQuality(video1.getQuality());
                video.setVideoLength(video1.getVideoLength());
            }
        }

    }
}
