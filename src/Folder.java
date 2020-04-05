import java.util.ArrayList;

public class Folder {
    private String name;
    private String address;
    private ArrayList<Folder> folders;
    private ArrayList<File> files;
    private static ArrayList<Folder> allFolders= new ArrayList<>();
    private int size;
    private CopyForm copyForm;
    private int numberOfOpening;
    private static ArrayList<Folder> recyclingBin = new ArrayList<>();

    public Folder(String name, String address) {
        this.name = name;
        this.address = address;
        numberOfOpening =0;
        folders = new ArrayList<>();
        files = new ArrayList<>();
        allFolders.add(this);
        copyForm = CopyForm.FREE;
        size = 0;
        recyclingBin.add(this);
    }
    public int getFolderSize(){
        int result = 0;
        for (File file : files) {
            result+=file.getSize();
        }
        for (Folder folder : folders) {
            result+=folder.getFolderSize();
        }
        return result;
    }

    public int getNumberOfOpening() {
        return numberOfOpening;
    }

    public void setNumberOfOpening(int numberOfOpening) {
        this.numberOfOpening = numberOfOpening;
    }

    public static ArrayList<Folder> getAllFolders() {
        return allFolders;
    }

    public String getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }

    public int countSize(){
        int size = 0;
        for (File file : files) {
            size+=file.getSize();
        }
        for (Folder folder : folders) {
            size+=folder.getSize();
        }
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static boolean isThereFolderWithThisName(String name,String address){
        for (Folder folder : allFolders) {
            if (folder.name.equalsIgnoreCase(name) && folder.address.equals(address))
                return true;
        }
        return false;
    }
    public static Folder getFolder(String name , String address){
        for (Folder folder : allFolders) {
            if (folder.getAddress().equals(address) && folder.getName().equalsIgnoreCase(name))
                return folder;
        }
        return null;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public ArrayList<File> getFiles() {
        return files;
    }
        public void arrangeFilesAndFolders() {
            Folder[] arrayOfFolders = new Folder[folders.size()];
            folders.toArray(arrayOfFolders);
            for (int i = 0; i < folders.size(); i++) {
                for (int k = 0; k < folders.size() - 1; k++) {
                    if (arrayOfFolders[k].getName().compareTo(arrayOfFolders[k + 1].getName()) > 0) {
                        changePositionOfFolders(arrayOfFolders, k);
                    }
                }
            }
            File[] files1 = new File[files.size()];
            files.toArray(files1);
            for (int i = 0; i < files.size(); i++) {
                for (int k = 0; k < files.size() - 1; k++) {
                    if (!files1[k].getType().equals("img") && !files1[k + 1].getType().equals("img")) {
                        File help = files1[k];
                        files1[k] = files1[k + 1];
                        files1[k + 1] = help;
                    }
                    if (files1[k].getType().equals("mp4") && files1[k + 1].getType().equals("txt")) {
                        File help = files1[k];
                        files1[k] = files1[k + 1];
                        files1[k + 1] = help;
                    }
                    if (files1[k].getType().equals(files1[k + 1].getType())) {
                        if (files1[k].getName().compareTo(files1[k + 1].getName()) > 0) {
                            File help = files1[k];
                            files1[k] = files1[k + 1];
                            files1[k + 1] = help;
                        }
                    }
                }
            }
        }
        public boolean doesItHaveThisFile(String name){
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(name))
                    return true;
            }
        return false;
        }
        public boolean doesItHaveThisFolder(String name){
            for (Folder folder : folders) {
                if (folder.getName().equalsIgnoreCase(name))
                    return true;
            }
            return  false;
        }

    public CopyForm getCopyForm() {
        return copyForm;
    }

    public void setCopyForm(CopyForm copyForm) {
        this.copyForm = copyForm;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static ArrayList<Folder> getCopiedFolders(){
        ArrayList<Folder> foldersCopy = new ArrayList<>();
        for (Folder folder : allFolders) {
            if (folder.getCopyForm().equals(CopyForm.COPIED))
                foldersCopy.add(folder);
        }
        return foldersCopy;
    }
    public File extractFileInThisFolder(String name){
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(name))
                return file;
        }
        return null;
    }
    public static Folder[] arrangeFoldersAtNumberOfOpen(){
        Folder[] arrangedList = new Folder[allFolders.size()];
        allFolders.toArray(arrangedList);
        for (int i=0;i<allFolders.size();i++){
            for (int k=0;k<allFolders.size()-1;k++){
                if (arrangedList[k].getNumberOfOpening()<arrangedList[k+1].getNumberOfOpening()){
                    changePositionOfFolders(arrangedList, k);
                }
                if (arrangedList[k].getNumberOfOpening()==arrangedList[k+1].getNumberOfOpening()){
                    if (arrangedList[k].getAddress().compareTo(arrangedList[k+1].getAddress())>0){
                        changePositionOfFolders(arrangedList, k);
                    }
                }
            }
        }
        return arrangedList;
    }

    private static void changePositionOfFolders(Folder[] arrangedList, int k) {
        Folder help = arrangedList[k];
        arrangedList[k] = arrangedList[k + 1];
        arrangedList[k + 1] = help;
    }
    public static ArrayList<Folder> cutFolders(){
        ArrayList<Folder> folderCut = new ArrayList<>();
        for (Folder folder : allFolders) {
            if (folder.getCopyForm().equals(CopyForm.CUT))
                folderCut.add(folder);
        }
        return folderCut;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}

