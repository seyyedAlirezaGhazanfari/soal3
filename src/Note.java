public class Note extends File{
    private String text;

    public Note(String name, int size, String type, String address, String text) {
        super(name, size, type, address);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
