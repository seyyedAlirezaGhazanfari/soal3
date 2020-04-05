
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CommandProcessor {
    private static Scanner scanner= new Scanner(System.in);
    private static User user;
    private static File fileSpecialForText;
    public static void run(){
        Scanner scanner = new Scanner(System.in);
        int level =0;
        int numberOfDrive =0;
        int hardSize=0;
        int total =0;
        int i =0;
        while (true){
            String command = scanner.nextLine();
            Matcher matcher;
            if (level==0){
                matcher = getMatcher(command,"\\s*install OS (.+) (\\S+)\\s*");
                if (matcher.matches()){
                    user = install(matcher.group(1),matcher.group(2));
                    level=1;
                    continue;
                }
            }
            if (level==1){
                matcher = getMatcher(command,"\\s*(\\d+) (\\d+)\\s*");
                if (matcher.matches()){
                    numberOfDrive= Integer.parseInt(matcher.group(2));
                    hardSize = Integer.parseInt(matcher.group(1));
                    level = 2;
                    continue;
                }
            }
            if (level==2){
                matcher = getMatcher(command,"\\s*(\\S+) (\\d+)\\s*");
                if (matcher.matches()){
                    Matcher matcher1 = getMatcher(command,"\\s*([A-Z]) (\\d+)\\s*");
                    if (matcher1.matches()){
                        if (driveMaker(matcher1.group(1),Integer.parseInt(matcher1.group(2)),total,hardSize)){
                            total+=Integer.parseInt(matcher1.group(2));
                            Drive drive = new Drive(matcher1.group(1),Integer.parseInt(matcher1.group(2)));
                            i+=1;
                            if (i==1){
                                user.setAddress(drive.getName()+":");
                            }
                            if (i==numberOfDrive)
                                level = 3;
                            continue;
                        }
                        continue;
                    }
                    System.out.println("invalid name");
                    continue;
                }
            }
            if (level==3){
                matcher = getMatcher(command,"\\s*open (\\S+)\\s*");
                if (matcher.matches()){
                    if (!Folder.isThereFolderWithThisName(matcher.group(1),user.getAddress())){
                        System.out.println("invalid name");
                        continue;
                    }
                    Folder folder = Folder.getFolder(matcher.group(1),user.getAddress());
                    folder.setNumberOfOpening(folder.getNumberOfOpening()+1);
                    user.setAddress(user.getAddress()+"\\"+matcher.group(1));
                    continue;
                }
                matcher = getMatcher(command,"\\s*go to drive (\\S+)\\s*");
                if (matcher.matches()){
                    goTo(matcher.group(1),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*back\\s*");
                if (matcher.matches()){
                    back(user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*create folder (\\S+)\\s*");
                if (matcher.matches()){
                        createFolder(matcher.group(1),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*create file (\\S+) (\\S+) (\\d+)\\s*");
                if (matcher.matches()){
                    createFile(matcher.group(1),matcher.group(2),Integer.parseInt(matcher.group(3)),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*delete file (\\S+)\\s*");
                if (matcher.matches()){
                    deleteFile(matcher.group(1),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*delete folder (\\S+)\\s*");
                if (matcher.matches()){
                    deleteFolder(matcher.group(1),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*rename file (\\S+) (\\S+)\\s*");
                if (matcher.matches()){
                        renameFile(matcher.group(1),matcher.group(2),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*rename folder (\\S+) (\\S+)\\s*");
                if (matcher.matches()){
                    renameFolder(matcher.group(1),matcher.group(2),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*status\\s*");
                if (matcher.matches()){
                    status(user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*print drives status\\s*");
                if (matcher.matches()){
                    printDrives();
                    continue;
                }
                matcher = getMatcher(command,"\\s*copy file (.+)\\s*");
                if (matcher.matches()){
                    String[] fileNames = matcher.group(1).split("\\s");
                    copyFile(fileNames,user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*copy folder (.+)\\s*");
                if (matcher.matches()){
                    String[] foldersNames = matcher.group(1).split("\\s");
                     copyFolder(foldersNames,user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*paste\\s*");
                if (matcher.matches()){
                    pasrting(user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*cut file (.+)\\s*");
                if (matcher.matches()){
                    String[] fileNames = matcher.group(1).split("\\s");
                    cutFile(fileNames,user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*cut folder (.+)\\s*");
                if (matcher.matches()){
                    String[] folderNames = matcher.group(1).split("\\s");
                    cutFolder(folderNames,user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*print file stats (\\S+)\\s*");
                if (matcher.matches()){
                    printFileStatus(matcher.group(1),user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*write text (\\S+)\\s*");
                if (matcher.matches()){
                    fileSpecialForText = writeText(matcher.group(1),user);
                    if (fileSpecialForText!=null){
                        level = 11;
                    }
                    continue;
                }
                    matcher = getMatcher(command,"\\s*print frequent folders\\s*");
                if (matcher.matches()){
                   printFrequentFolders(user);
                    continue;
                }
                matcher = getMatcher(command,"\\s*print OS information\\s*");
                if (matcher.matches()){
                    System.out.println("OS is "+user.getOsName()+" "+user.getOsVersion());
                    continue;
                }

            }
            matcher = getMatcher(command,"\\s*end\\s*");
            if (matcher.matches()){
                break;
            }
            if (level == 11){
                Note note = (Note) fileSpecialForText;
                note.setText(command);
                level = 3;
                continue;
            }
            System.out.println("invalid command");
        }
    }
    private static void createFile(String name ,String format , int size , User user){
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        String[] parts = user.getAddress().split("\\\\");
        Folder folder = null;
        for (File file : File.getAllFiles()) {
            if (file.getName().equalsIgnoreCase(name) && file.getAddress().equals(user.getAddress())){
                System.out.println("file exists with this name");
                return;
            }
        }
        if (!format.equals("txt") && !format.equals("img") && !format.equals("mp4")){
            System.out.println("invalid format");
            return;
        }

        if (drive.getRemainingSpace()-size<0){
            System.out.println("insufficient drive size");
            return;
        }
        if (format.equals("txt")){
            System.out.println("Text:");
            String text  = scanner.nextLine();
            Note note = new Note(name,size,"txt",user.getAddress(),text);
            if (parts.length==1){
                drive.getFiles().add(note);
            }
            else {
              folder =  Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                folder.getFiles().add(note);
            }
        }
        if (format.equals("img")){
            System.out.println("Resolution:");
            String resolution = scanner.nextLine();
            System.out.println("Extension:");
            String format2 = scanner.nextLine();
            Image image = new Image(name,size,"img",user.getAddress(),resolution,format2);
            if (parts.length==1){
                drive.getFiles().add(image);
            }
            else {
                folder =  Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                folder.getFiles().add(image);
            }
        }
        if (format.equals("mp4")){
            System.out.println("Quality:");
            String quality = scanner.nextLine();
            System.out.println("Video Length:");
            String videoLength = scanner.nextLine();
            Video video = new Video(name,size,"mp4",user.getAddress(),quality,videoLength);
            if (parts.length==1){
                drive.getFiles().add(video);
            }
            else {
                folder =  Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                folder.getFiles().add(video);
            }

        }
        drive.setRemainingSpaces(drive.setRemainingSpace());
        System.out.println("file created");
    }
    private static Matcher getMatcher(String command,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        return matcher;
    }
    private static User install(String name , String version){
        User user = new User(name,version,"");
        return user;
    }
    private static boolean driveMaker(String name , int size ,int total,int hardSize){
      if (Drive.doesThisNameExistInDrives(name)){
          System.out.println("invalid name");
          return false;
      }
        if (total+size>hardSize){
            System.out.println("insufficient hard size");
            return false;
        }
        return true;
    }
    private static void goTo(String name,User user){
        if (Drive.doesThisNameExistInDrives(name)){
            user.setAddress(name+":");
        }
        else {
            System.out.println("invalid name");
        }
    }
    private static void back(User user){
        if (user.getAddress().contains("\\"))
        user.setAddress(user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
    }
    private static void createFolder(String name,User user){
        for (Folder folder : Folder.getAllFolders()) {
            if (folder.getName().equalsIgnoreCase(name) && folder.getAddress().equals(user.getAddress())){
                System.out.println("folder exists with this name");
                return;
            }
        }
        String[] part = user.getAddress().split("\\\\");
        Folder folder = new Folder(name,user.getAddress());
        System.out.println("folder created");
        if (part.length==1){
            Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
            drive.getFolders().add(folder);
        }
        else {
            Folder folder1 = Folder.getFolder(part[part.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
            folder1.getFolders().add(folder);
        }
    }
    private static void deleteFile(String name ,User user){
       String[] parts = user.getAddress().split("\\s");
       Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
       Folder folder =null;
       if (parts.length==1){
           for (File file : drive.getFiles()) {
               if (file.getName().equalsIgnoreCase(name) && file.getAddress().equals(user.getAddress())){
                   drive.getFiles().remove(file);
                   drive.setRemainingSpaces(drive.setRemainingSpace());
                   System.out.println("file deleted");
                   return;
               }
           }
       }
       else {
           folder = Folder.getFolder(parts[parts.length-1],user.getAddress());
           for (File file : folder.getFiles()) {
               if (file.getName().equalsIgnoreCase(name) && file.getAddress().equals(user.getAddress())){
                   drive.getFiles().remove(file);
                   drive.setRemainingSpaces(drive.setRemainingSpace());
                   System.out.println("file deleted");
                   return;
               }
           }
       }
        System.out.println("invalid name");
    }
    private static void deleteFolder(String name,User user){
        String[] parts = user.getAddress().split("\\s");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        if(parts.length!=1){
            folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
        }
        Folder folder1 = Folder.getFolder(name,user.getAddress());
        if (folder1==null){
            System.out.println("invalid name");
            return;
        }
        for (File file : folder1.getFiles()) {
            File.getAllFiles().remove(file);
        }
        deleteFoldersIntoFolderWhichWasDeleted(folder1,user,folder,drive);
        if (parts.length==1){
            drive.getFolders().remove(folder1);
            Folder.getAllFolders().remove(folder1);
        }
        else {
            folder.getFolders().remove(folder1);
            Folder.getAllFolders().remove(folder1);
        }
        System.out.println("folder deleted");
    }
    private static void deleteFoldersIntoFolderWhichWasDeleted(Folder folder , User user , Folder hostFolder, Drive drive){
        for (File file : folder.getFiles()) {
            File.getAllFiles().remove(file);
        }
        if (folder.getFolders().size()==0){
            return;
        }
        for (Folder folder1 : folder.getFolders()) {
            deleteFoldersIntoFolderWhichWasDeleted(folder1,user,null,drive);
            Folder.getAllFolders().remove(folder1);
        }
    }
    private static void renameFile(String oldName,String newName,User user){
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        if (parts.length==1){
            File files = null;
            boolean isThereName = false;
            for (File file : drive.getFiles()) {
                if (file.getName().equalsIgnoreCase(oldName)){
                    files = file;
                    isThereName=true;
                }
            }
            if (!isThereName) {
                System.out.println("invalid name");
                return;
            }
            for (File file : drive.getFiles()) {
                if (file.getName().equalsIgnoreCase(newName)){
                    System.out.println("file exists with this name");
                    return;
                }
            }
            files.setName(newName);
            System.out.println("file renamed");
        }
        else {
            folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
            File files=null;
            boolean isItFile= false;
            for (File file : folder.getFiles()) {
                if (file.getName().equalsIgnoreCase(oldName)){
                    files = file;
                    isItFile = true;
                    break;
                }
            }
            if (!isItFile) {
                System.out.println("invalid name");
                return;
            }
            for (File file : folder.getFiles()) {
                if (file.getName().equalsIgnoreCase(newName)){
                    System.out.println("file exists with this name");
                    return;
                }
            }
            System.out.println("file renamed");
            return;
        }
    }
    private static void renameFolder(String oldName , String newName , User user){
            String[] parts = user.getAddress().split("\\\\");
            Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
            Folder folder = null;
            if (parts.length==1){
                boolean isItInThere = false;
                Folder folder1=null;
                for (Folder driveFolder : drive.getFolders()) {
                    if (driveFolder.getName().equalsIgnoreCase(oldName)){
                        isItInThere  = true;
                        folder1 = driveFolder;
                        break;
                    }
                }
                if (!isItInThere){
                    System.out.println("invalid name");
                    return;
                }
                for (Folder driveFolder : drive.getFolders()) {
                    if (driveFolder.getName().equalsIgnoreCase(newName)){
                        System.out.println("folder exists with this name");
                        return;
                    }
                }
                folder1.setName(newName);
                folderInsideRenameEffects(folder1,user);
                System.out.println("folder renamed");
                return;
            }
            else {
                folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                boolean isItInThere = false;
                Folder folder1 = null;
                for (Folder folderFolder : folder.getFolders()) {
                    if (folderFolder.getName().equalsIgnoreCase(oldName)){
                        isItInThere = true;
                        folder1 = folderFolder;
                        break;
                    }
                }
                if (!isItInThere){
                    System.out.println("invalid name");
                    return;
                }
                for (Folder folderFolder : folder.getFolders()) {
                    if (folderFolder.getName().equalsIgnoreCase(newName)){
                        System.out.println("folder exists with this name");
                        return;
                    }
                }
                if (parts.length==1){
                    for (Folder driveFolder : drive.getFolders()) {
                        if (driveFolder.getName().equalsIgnoreCase(oldName))
                        {
                            driveFolder.setName(newName);
                            break;
                        }
                    }
                }
                else {
                    for (Folder folderFolder : folder.getFolders()) {
                        if (folderFolder.getName().equalsIgnoreCase(oldName)){
                            folderFolder.setName(newName);
                            break;
                        }
                    }
                }
                System.out.println("folder renamed");
                folder1.setName(newName);
                folderInsideRenameEffects(folder1,user);
                return;

            }
    }
    private static void folderInsideRenameEffects(Folder folder ,User user){

        for (File file : folder.getFiles()) {
            file.setAddress(folder.getAddress()+"\\"+folder.getName());
        }
        if (folder.getFolders().size()==0)
            return;
        for (Folder folder1 : folder.getFolders()) {
            folderInsideRenameEffects(folder1,user);
            folder1.setAddress(folder.getAddress()+"\\"+folder.getName());
        }
    }
    private static void status(User user){
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        System.out.println(user.getAddress());
        if (parts.length==1){
            drive.arrangeFilesAndFolders();
            System.out.println("Folders:");
            for (Folder driveFolder : drive.getFolders()) {
                System.out.println(driveFolder.getName()+" "+driveFolder.getFolderSize()+"MB");
            }
            System.out.println("Files:");
            for (File file : drive.getFiles()) {
                System.out.println(file.getName()+" "+file.getType()+" "+file.getSize()+"MB");
            }
        }
        else {
            folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
            folder.arrangeFilesAndFolders();
            System.out.println("Folders:");
            for (Folder folderFolder : folder.getFolders()) {
                System.out.println(folderFolder.getName()+" "+folderFolder.getFolderSize()+"MB");
            }
            System.out.println("Files:");
            for (File file : folder.getFiles()) {
                System.out.println(file.getName()+" "+file.getType()+" "+file.getSize()+"MB");
            }
        }
    }
    private static void printDrives(){
        for (Drive drive : Drive.getAllDrives()) {
            drive.setRemainingSpaces(drive.setRemainingSpace());
            System.out.println(drive.getName()+" "+drive.getSize()+"MB"+" "+(drive.getSize()-drive.getRemainingSpace())+"MB");
        }
    }
    private static void copyFile(String[] allFileNames,User user){
        untiCopy();
        untiCut();
        String[]parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        int i =0;
        for (String fileName : allFileNames) {
            if (parts.length==1){
                if (drive.getFileOFDriveIsHere(fileName)){
                    i++;
                    File file = File.getFileByNameAndAddress(fileName,user.getAddress());
                    file.setCopyForm(CopyForm.COPIED);
                }
            }
            else {
                Folder folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                if (folder.doesItHaveThisFile(fileName)){
                    i++;
                    File file = File.getFileByNameAndAddress(fileName,user.getAddress());
                    file.setCopyForm(CopyForm.COPIED);
                }
            }
        }
        if (i!=allFileNames.length){
            System.out.println("invalid name");
            return;
        }
        System.out.println("files copied");

    }
    private static void copyFolder(String[] allFolderNames, User user){
        untiCopy();
        untiCut();
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        int i =0;
        for (String folderName : allFolderNames) {
            if (parts.length==1){
                if (drive.getFolderOfDriveIsHere(folderName)){
                    i++;
                    Folder folder = Folder.getFolder(folderName,user.getAddress());
                    folder.setCopyForm(CopyForm.COPIED);
                    for (File file : folder.getFiles()) {
                        file.setCopyForm(CopyForm.COPIED);
                    }
                }
            }
            else {
                Folder folder1 = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                if (folder1.doesItHaveThisFolder(folderName)){
                    i++;
                    Folder folder = Folder.getFolder(folderName,user.getAddress());
                    folder.setCopyForm(CopyForm.COPIED);
                }
            }
        }
        if (i!=allFolderNames.length){
            System.out.println("invalid name");
            return;
        }
        System.out.println("folders copied");
    }
    private static void untiCopy(){
        untyCopyFile();
        untiCopyFolders();
    }
    private static void untiCopyFolders() {
        for (Folder folder : Folder.getAllFolders()) {
            if (folder.getCopyForm().equals(CopyForm.FREE))
                folder.setCopyForm(CopyForm.FREE);
        }
    }
    private static void untyCopyFile() {
        for (File file : File.getAllFiles()) {
            if (file.getCopyForm().equals(CopyForm.COPIED)){
                file.setCopyForm(CopyForm.FREE);
            }
        }
    }
    private static void pasrting(User user) {
        boolean thereWasAAction = false;
        String[] part = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        if (part.length != 1) {
            folder = Folder.getFolder(part[part.length - 1], user.getAddress().substring(0, user.getAddress().lastIndexOf("\\")));
        }
        //file copy
        for (File file : File.getFilesCopied()) {
            file.setCopyForm(CopyForm.FREE);
            if (part.length == 1) {
                if (drive.getFileOFDriveIsHere(file.getName())) {
                    System.out.println("file exists with this name");
                    return;
                }
                if (drive.getRemainingSpace()-file.getSize()<0){
                    System.out.println("insufficient drive size");
                    return;
                }
                fileCopingMaker(user, part, drive, folder, file);

            } else {
                if (folder.doesItHaveThisFile(file.getName())) {
                    System.out.println("file exists with this name");
                    return;
                }
                if (drive.getRemainingSpace()-file.getSize()<0){
                    System.out.println("insufficient drive size");
                    return;
                }
                fileCopingMaker(user,part,drive,folder,file);
            }
thereWasAAction=true;
        }
        //end file copy
        //file cut
        for (File file : File.cutFile()) {
            Drive driveOfCutFile = Drive.getDrive(String.valueOf(file.getAddress().charAt(0)));
            String[] parts2  = file.getAddress().split("\\\\");
            Folder folderOfCutFile = null;
            if (parts2.length!=1)
                folderOfCutFile = Folder.getFolder(parts2[parts2.length-1],file.getAddress().substring(0,file.getAddress().lastIndexOf("\\")));
           file.setCopyForm(CopyForm.FREE);
            if (part.length == 1) {
                if (drive.getFileOFDriveIsHere(file.getName())) {
                    System.out.println("file exists with this name");
                    return;
                }
                if (drive.getRemainingSpace()-file.getSize()<0){
                    System.out.println("insufficient drive size");
                    return;
                }
                fileCopingMaker(user,part,drive,folder,file);
                if (parts2.length==1)
                          driveOfCutFile.getFiles().remove(file);
                else {
                    if (folderOfCutFile != null) {
                        folderOfCutFile.getFiles().remove(file);
                    }
                }
            } else {
                if (folder.doesItHaveThisFile(file.getName())) {
                    System.out.println("file exists with this name");
                    return;
                }
                if (drive.getRemainingSpace()-file.getSize()<0){
                    System.out.println("insufficient drive size");
                    return;
                }
                fileCopingMaker(user,part,drive,folder,file);
                if (parts2.length==1)
                    driveOfCutFile.getFiles().remove(file);
                else {
                    if (folderOfCutFile != null) {
                        folderOfCutFile.getFiles().remove(file);
                    }
                }
            }
            File.getAllFiles().remove(file);
            thereWasAAction=true;
        }
        //file cut end
        //folder copy
        for (Folder copiedFolder : Folder.getCopiedFolders()) {
            if (drive.getFolderOfDriveIsHere(copiedFolder.getName()) && part.length==1){
                System.out.println("folder exists with this name");
                return;
            }
            else if (folder != null && folder.doesItHaveThisFolder(copiedFolder.getName())) {
                System.out.println("folder exists with this name");
                return;
            }
            if (drive.getRemainingSpace()-copiedFolder.getSize()<0){
                System.out.println("insufficient drive size");
                return;
            }
            copiedFolder.setCopyForm(CopyForm.FREE);
            Folder folder1 = new Folder(copiedFolder.getName(),user.getAddress());
            folderCopingMaker(user,part,drive,folder1,copiedFolder,user.getAddress());
            if (part.length==1){
                drive.getFolders().add(folder1);
            }
            else {
                folder.getFolders().add(folder1);
            }
            thereWasAAction=true;
        }
        //folder copy end
        //folder cut
        for (Folder cutFolder : Folder.cutFolders()) {

            if (drive.getFolderOfDriveIsHere(cutFolder.getName()) && part.length==1){
                System.out.println("folder exists with this name");
                return;
            }
            else if (folder != null && folder.doesItHaveThisFolder(cutFolder.getName())) {
                System.out.println("folder exists with this name");
                return;
            }
            if (drive.getRemainingSpace()-cutFolder.getSize()<0){
                System.out.println("insufficient drive size");
                return;
            }
            cutFolder.setCopyForm(CopyForm.FREE);
            Folder folder1 = new Folder(cutFolder.getName(),user.getAddress());
            folderCopingMaker(user,part,drive,folder1,cutFolder,user.getAddress());
            if (part.length==1){
                drive.getFolders().add(folder1);
            }
            else {
                folder.getFolders().add(folder1);
            }
            Drive drive1 = Drive.getDrive(String.valueOf(cutFolder.getAddress().charAt(0)));
            String[] parts2 = cutFolder.getAddress().split("\\\\");
            Folder folder2 = null;
            if (parts2.length!=1)
                folder2 = Folder.getFolder(parts2[parts2.length-1],cutFolder.getAddress().substring(0,cutFolder.getAddress().lastIndexOf("\\")));
            deleteFoldersIntoFolderWhichWasDeleted(cutFolder,user,folder2,drive1);
            if (parts2.length==1){
                drive1.getFolders().remove(cutFolder);
            }
            else {
                folder2.getFolders().remove(cutFolder);
            }
            Folder.getAllFolders().remove(cutFolder);
            thereWasAAction=true;

        }
        if (thereWasAAction)
                     System.out.println("paste completed");
        }
    private static void folderCopingMaker(User user,String[] part,Drive drive , Folder folder , Folder copiedFolder,String address){
        for (File file : copiedFolder.getFiles()) {
            File file1 = null;
            if (file.getType().equals("txt")) {
                Note note1 = (Note) file;
                Note note = new Note(file.getName(),file.getSize(),file.getType(),address,note1.getText());
                file1 = note;
                folder.getFiles().add(note);
            }
            if (file.getType().equals("img")) {
                Image image1 = (Image) file;
                Image image = new Image(file.getName(),file.getSize(),file.getType(),address,image1.getResolution(),image1.getFormat());
file1 = image;
                    folder.getFiles().add(image);
            }
            if (file.getType().equals("mp4")) {
                Video video1 = (Video) file;
                Video video = new Video(file.getName(),file.getSize(),file.getType(),address,video1.getQuality(),video1.getVideoLength());
                  file1 = video;
                    folder.getFiles().add(video);
            }
            File.getAllFiles().add(file1);
        }
        if (copiedFolder.getFolders().size()==0){
            return;
        }
        for (Folder folderFolder : copiedFolder.getFolders()) {
            address = address+"\\"+folder.getName();
                Folder folder1 = new Folder(folderFolder.getName(),address);
                folder.getFolders().add(folder1);
                folderCopingMaker(user,part,drive,folder1,folderFolder,address);

        }
    }
    private static void fileCopingMaker(User user, String[] part, Drive drive, Folder folder, File file) {
        File file1 = null;
        if (file.getType().equals("txt")) {
            Note note1 = (Note) file;
        Note note = new Note(file.getName(),file.getSize(),file.getType(),user.getAddress(),note1.getText());
        file1 = note;
       if (part.length==1)
        drive.getFiles().add(note);
       else
           folder.getFiles().add(note);
        }
        if (file.getType().equals("img")) {
            Image image1 = (Image) file;
        Image image = new Image(file.getName(),file.getSize(),file.getType(),user.getAddress(),image1.getResolution(),image1.getFormat());
          file1 = image;
            if (part.length==1)
                drive.getFiles().add(image);
            else
                folder.getFiles().add(image);
        }
        if (file.getType().equals("mp4")) {
            Video video1 = (Video) file;
        Video video = new Video(file.getName(),file.getSize(),file.getType(),user.getAddress(),video1.getQuality(),video1.getVideoLength());
          file1 = video;
            if (part.length==1)
                drive.getFiles().add(video);
            else
                folder.getFiles().add(video);
        }
        File.getAllFiles().add(file1);
    }
    private static boolean fileCopyPaste(User user, String[] parts, Drive drive, Folder folder) {
        for (File file : File.getFilesCopied()) {
            if (parts.length==1){
                if (fileCopyAndCutChecking(drive, file)) return true;
                File file1 = null;
                getFile(user,file,file1);
                file.setCopyForm(CopyForm.FREE);
                file1.setCopyForm(CopyForm.FREE);
                drive.getFiles().add(file);
                drive.setRemainingSpaces(drive.setRemainingSpace());
                System.out.println("paste completed");
            }
            else {
                if (fileCopyAndCutCheckForFolders(drive, folder.doesItHaveThisFile(file.getName()), "file exists with this name", file.getSize()))
                    return true;
                File file1 = null;
                getFile(user,file,file1);
                    folder.getFiles().add(file1);
                drive.setRemainingSpaces(drive.setRemainingSpace());
                file.setCopyForm(CopyForm.FREE);
                    file1.setCopyForm(CopyForm.FREE);
                    System.out.println("paste completed");
            }

        }
        return false;
    }
    private static File getFile(User user, File file, File file1) {
        if (file.type.equals("txt")){
            Note note2 = (Note) file;
            Note note = new Note(file.getName(),file.getSize(),file.getType(),user.getAddress(),note2.getText());
            file1= note;
        }
        if (file.type.equals("img")){
            Image image2 = (Image) file;
            Image image = new Image(file.getName(),file.getSize(),file.getType(),user.getAddress(),image2.getResolution(),image2.getFormat());
            file1 = image;
        }
        if (file.type.equals("mp4")){
            Video video2 = (Video) file;
            Video video = new Video(file.getName(),file.getSize(),file.getType(),user.getAddress(),video2.getQuality(),video2.getVideoLength());
            file1 = video;
        }
        return file1;
    }
    private static boolean fileCopyAndCutCheckForFolders(Drive drive, boolean b, String s, int size) {
        if (b) {
            System.out.println(s);
            return true;
        }
        if (drive.getRemainingSpace() - size < 0) {
            System.out.println("insufficient drive size");
            return true;
        }
        return false;
    }
    private static boolean fileCopyAndCutChecking(Drive drive, File file) {
        if (fileCopyAndCutCheckForFolders(drive, drive.getFileOFDriveIsHere(file.getName()), "file exists with this name", file.getSize()))
            return true;
        return false;
    }
    private static void cutFile(String[] fileNames,User user) {
        untiCopy();
        untiCut();
        String[]parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        int i =0;
        for (String fileName : fileNames) {
            if (parts.length==1){
                if (drive.getFileOFDriveIsHere(fileName)){
                    i++;
                    File file = File.getFileByNameAndAddress(fileName,user.getAddress());
                    file.setCopyForm(CopyForm.CUT);
                }
            }
            else {
                Folder folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                if (folder.doesItHaveThisFile(fileName)){
                    i++;
                    File file = File.getFileByNameAndAddress(fileName,user.getAddress());
                    file.setCopyForm(CopyForm.CUT);
                }
            }
        }
        if (i!=fileNames.length){
            System.out.println("invalid name");
            for (String name : fileNames) {
                File file = File.getFileByNameAndAddress(name,user.getAddress());
                file.setCopyForm(CopyForm.FREE);
            }
            return;
        }
        System.out.println("files cut completed");
    }
   private static void cutFolder(String[] folderNames , User user){
        untiCopy();
        untiCut();
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        int i =0;
        for (String folderName : folderNames) {
            if (parts.length==1){
                if (drive.getFolderOfDriveIsHere(folderName)){
                    i++;
                    Folder folder = Folder.getFolder(folderName,user.getAddress());
                    folder.setCopyForm(CopyForm.CUT);
                }
            }
            else {
                Folder folder1 = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
                if (folder1.doesItHaveThisFolder(folderName)){
                    i++;
                    Folder folder = Folder.getFolder(folderName,user.getAddress());
                    folder.setCopyForm(CopyForm.CUT);
                }
            }
        }
        if (i!=folderNames.length){
            System.out.println("invalid name");
            return;
        }
        System.out.println("folders cut completed");
    }
    private static void untiCut(){
        for (File file : File.getAllFiles()) {
            if (file.getCopyForm().equals(CopyForm.CUT))
                file.setCopyForm(CopyForm.FREE);
        }
        for (Folder folder : Folder.getAllFolders()) {
            if (folder.getCopyForm().equals(CopyForm.CUT))
                folder.setCopyForm(CopyForm.FREE);
        }
    }
    private static void printFileStatus(String name , User user){
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        if (parts.length!=1){
            folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
        }
        if (parts.length==1){
            if (!drive.getFileOFDriveIsHere(name)){
                System.out.println("invalid name");
                return;
            }
            File file = drive.getAFile(name);
            filePrintData(name,file);
        }
        else {
            if (!folder.doesItHaveThisFile(name)){
                System.out.println("invalid name");
                return;
            }
            File file = folder.extractFileInThisFolder(name);
            filePrintData(name,file);
        }
        }
    private static void filePrintData(String name, File file) {
        System.out.println(file.getName()+" "+file.getType());
        System.out.println(file.getAddress()+"\\"+file.getName());
        System.out.println("Size: "+file.getSize()+"MB");
        if (file.getType().equals("txt")){
            Note note = (Note) file;
            System.out.println("Text: "+note.getText());
        }
        if (file.getType().equals("img")){
            Image image = (Image) file;
            System.out.println("Resolution: "+image.getResolution());
            System.out.println("Extension: "+image.getFormat());
        }
        if (file.getType().equals("mp4")){
            Video video = (Video) file;
            System.out.println("Quality: "+video.getQuality());
            System.out.println("Video Length: "+video.getVideoLength());
        }
    }
    private static Note writeText(String name , User user){
        String[] parts = user.getAddress().split("\\\\");
        Drive drive = Drive.getDrive(String.valueOf(user.getAddress().charAt(0)));
        Folder folder = null;
        if (parts.length!=1){
            folder = Folder.getFolder(parts[parts.length-1],user.getAddress().substring(0,user.getAddress().lastIndexOf("\\")));
        }
        if (parts.length==1){
            if(!drive.getFileOFDriveIsHere(name)){
                System.out.println("invalid name");
                return null;
            }
            File file = File.getFileByNameAndAddress(name,user.getAddress());
            if (!file.getType().equals("txt")){
                System.out.println("this file is not a text file");
                return null;
            }
            return (Note) file;
        }
        else {
            if (!folder.doesItHaveThisFile(name)){
                System.out.println("invalid name");
                return  null;
            }
            File file = File.getFileByNameAndAddress(name,user.getAddress());
            if (!file.getType().equals("txt")){
                System.out.println("this file is not a text file");
                return null;
            }
            return (Note) file;

        }
    }
    private static void printFrequentFolders(User user){
        if (Folder.arrangeFoldersAtNumberOfOpen().length<5){
            for (Folder folder : Folder.arrangeFoldersAtNumberOfOpen()) {
                if (folder.getNumberOfOpening()!=0)
                System.out.println(folder.getAddress()+"\\"+folder.getName()+" "+folder.getNumberOfOpening());
            }
        }
        else {
            for (int i=0;i<5;i++){
                if (Folder.arrangeFoldersAtNumberOfOpen()[i].getNumberOfOpening()!=0)
                    System.out.println(Folder.arrangeFoldersAtNumberOfOpen()[i].getAddress()+"\\"+Folder.arrangeFoldersAtNumberOfOpen()[i].getName()+" "+Folder.arrangeFoldersAtNumberOfOpen()[i].getNumberOfOpening());
            }
        }
    }
}