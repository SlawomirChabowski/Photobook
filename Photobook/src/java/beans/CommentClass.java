package beans;

public class CommentClass {
    private String date;
    private String author;
    private String content;
    
    public String getDate()     { return date; }
    public String getAuthor()   { return author; }
    public String getContent()  { return content; }
    
    public CommentClass(String date, String author, String content) {
        this.date = date.substring(0, date.length() - 2);
        this.author = author;
        this.content = content;
    }
}
