public class User {
    private String osName;
    private String osVersion;
    private String address;
    private int hardSize;
    private int driveNumber;

    public User(String osName, String osVersion, String address) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.address = address;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public int getHardSize() {
        return hardSize;
    }

    public void setHardSize(int hardSize) {
        this.hardSize = hardSize;
    }

    public int getDriveNumber() {
        return driveNumber;
    }

    public void setDriveNumber(int driveNumber) {
        this.driveNumber = driveNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
