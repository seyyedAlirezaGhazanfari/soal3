import java.util.ArrayList;

public class Drive {
    private String name;
    private int size;
    private ArrayList<File> files;
    private ArrayList<Folder> folders;
    private static ArrayList<Drive> allDrives = new ArrayList<>();
    private int remainingSpace;

    public Drive(String name, int size) {
        this.name = name;
        this.size = size;
        remainingSpace = size;
        folders = new ArrayList<>();
        files = new ArrayList<>();
        allDrives.add(this);
    }

    public void setRemainingSpaces(int remainingSpace) {
        this.remainingSpace = remainingSpace;
    }

    public String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int setRemainingSpace() {
        int result =0;
        for (Folder folder : folders) {
            result+=folder.getFolderSize();
        }
        for (File file : files) {
            result+=file.getSize();
        }

        return size -result;
    }

    public int getRemainingSpace() {
        return remainingSpace;
    }

    public static ArrayList<Drive> getAllDrives() {
        return allDrives;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }
    public static boolean doesThisNameExistInDrives(String name){
        for (Drive drive : allDrives) {
            if (drive.name.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
    public static Drive getDrive(String name){
        for (Drive drive : allDrives) {
            if (drive.name.equalsIgnoreCase(name))
                return drive;
        }
        return null;
    }

    public int getSize() {
        return size;
    }
    public void arrangeFilesAndFolders(){
        Folder[] arrayOfFolders = new Folder[folders.size()];
        folders.toArray(arrayOfFolders);
        for (int i=0;i<folders.size();i++){
            for (int k =0;k<folders.size()-1;k++){
                if (arrayOfFolders[k].getName().compareTo(arrayOfFolders[k+1].getName())>0) {
                    Folder helpFolder = arrayOfFolders[k];
                    arrayOfFolders[k] = arrayOfFolders[k + 1];
                    arrayOfFolders[k + 1] = helpFolder;
                }
            }
        }
        File[] files1 = new File[files.size()];
        files.toArray(files1);
        for (int i =0 ;i<files.size();i++){
            for (int k=0;k<files.size()-1;k++){
                if (!files1[k].getType().equals("img") && !files1[k+1].getType().equals("img") ){
                    File help = files1[k];
                    files1[k] = files1[k+1];
                    files1[k+1] = help;
                }
                if (files1[k].getType().equals("mp4") && files1[k+1].getType().equals("txt")){
                    File help = files1[k];
                    files1[k] = files1[k+1];
                    files1[k+1] = help;
                }
                if (files1[k].getType().equals(files1[k+1].getType())){
                    if (files1[k].getName().compareTo(files1[k+1].getName())>0){
                        File help = files1[k];
                        files1[k] = files1[k+1];
                        files1[k+1] = help;
                    }
                }
            }
        }
    }
    public boolean getFileOFDriveIsHere(String name){
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
    public boolean getFolderOfDriveIsHere(String name){
        for (Folder folder : folders) {
            if (folder.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    public File getAFile(String name){
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(name))
                return file;
        }
        return null;
    }
}
