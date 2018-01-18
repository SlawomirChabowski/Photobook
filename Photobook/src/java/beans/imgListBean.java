package beans;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

@Named(value = "imgListBean")
@Dependent
public class imgListBean {

    private int     id;
    private String  img;
    private String  title;
    private String  author;
    
    public int getId()          { return id; }
    public String getImg()      { return img; }
    public String getTitle()    { return title; }
    public String getAuthor()   { return author; }
    
    public imgListBean(int id, String img, String title, String author) {
        this.id     = id;
        this.img    = img;
        this.title  = title;
        this.author = author;
    }
    
    public imgListBean(int id, String img, String title) {
        this.id     = id;
        this.img    = img;
        this.title  = title;
    }
}
