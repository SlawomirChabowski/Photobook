package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "addComment")
@RequestScoped
public class addComment {
    
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String imgId = params.get("img");
    public String getImgId() { return imgId; }
    public void setImgId(String imgId) { this.imgId = imgId; }
    
    private String comment;
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public addComment() {}
    
    public void newComment() throws ClassNotFoundException, SQLException {
        if(login.userId > 0 && comment.length() > 0) {
            String sterownik = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/photobook";

            Class.forName(sterownik);
            Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

            String sql = "INSERT INTO comment(image_id, author_id, comment_text) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, this.imgId);
            statement.setInt(2, login.userId);
            statement.setString(3, this.comment);
            statement.execute();
            conn.close();
        }
    }
}