package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "viewImage")
@RequestScoped
public class viewImage {
    
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String imgId = params.get("img");
    public String getImgId() { return this.imgId; }
    
    private boolean imgNotFound = false;
    public boolean getImgNotFound() { return imgNotFound; }
    public void setImgNotFound(boolean imgNotFound) { this.imgNotFound = imgNotFound; }
    private boolean commentAdded = false;
    public boolean getCommentAdded() { return commentAdded; }
    public void setCommentAdded(boolean commentAdded) { this.commentAdded = commentAdded; }
    private boolean noComments = true;
    public boolean getNoComments() { return noComments; }
    public void setNoComments(boolean noComments) { this.noComments = noComments; }
    private boolean imgOwner = false;
    public boolean isImgOwner() { return imgOwner; }
    
    private String image;
    private String title;
    private String author;
    private String dateAdded;
    private List<String> tags = new ArrayList<>();
    private String description;
    private String category;
    private List<CommentClass> commentList = new ArrayList<>();
    
    public String getImage()                    { return image; }
    public String getTitle()                    { return title; }
    public String getAuthor()                   { return author; }
    public String getDateAdded()                { return dateAdded; }
    public List<String> getTags()               { return tags; }
    public String getDescription()              { return description; }
    public String getCategory()                 { return category; }
    public List<CommentClass> getCommentList()  { return commentList; }
    
    
    public viewImage() {
        try {
            String sterownik = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/photobook";
            
            Class.forName(sterownik);
            Connection conn = DriverManager.getConnection(url, "root", "");
            
            Statement stm = conn.createStatement();
            String sql = "SELECT i.*, u.nickname, c.name FROM image i "
                    + "INNER JOIN user u ON u.id=i.author_id "
                    + "INNER JOIN categories c ON c.id=i.category_id "
                    + "WHERE i.id=" + this.imgId;
            ResultSet rs = stm.executeQuery(sql);
            
            while(rs.next()) {
                this.title = rs.getString(2);
                this.description = rs.getString(3);
                this.tags = Arrays.asList(rs.getString(4).split("\\s*,\\s*"));
                this.dateAdded = rs.getString(5).substring(0, rs.getString(5).length() - 2);
                this.category = rs.getString(10);
                this.author = rs.getString(9);
                this.image = "resources/user-images/" + rs.getString(8);
                if(rs.getInt(7) == login.userId) imgOwner = true;
            }
            
            sql = "SELECT u.nickname, c.comment_text, c.date_added FROM comment c "
                    + "INNER JOIN user u ON u.id=c.author_id "
                    + "WHERE image_id=" + this.imgId;
            ResultSet rs2 = stm.executeQuery(sql);
            
            while(rs2.next()) {
                noComments = false;
                this.commentList.add(new CommentClass(rs2.getString(3), rs2.getString(1), rs2.getString(2)));
            }
            
            conn.close();
        } catch (Exception ex) {
            imgNotFound = true;
        }
    }
    
}
