package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;

@Named(value = "editImage")
@ConversationScoped
public class editImage implements Serializable {
    
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String imgId = params.get("img");
    
    private boolean properUser = false;
    private boolean notFound   = true;
    
    public boolean isProperUser() { return properUser; }
    public boolean isNotFound()   { return notFound; }
    

    // image parameters
    private String  title;
    private String  description;
    private String  tags;
    private int     category;
    
    public String getTitle()            { return title; }
    public String getDescription()      { return description; }
    public String getTags()             { return tags; }
    public int    getCategory()         { return category; }
    
    public void setTitle(String title)              { this.title = title; }
    public void setDescription(String description)  { this.description = description; }
    public void setTags(String tags)                { this.tags = tags; }
    public void setCategory(int category)           { this.category = category; }
    
    
    // old image parameters
    private String  oldTitle;
    private String  oldDescription;
    private String  oldTags;
    private int     oldCategory;
    
    public String getOldTitle()         { return oldTitle; }
    public String getOldDescription()   { return oldDescription; }
    public String getOldTags()          { return oldTags; }
    public int    getOldCategory()      { return oldCategory; }
    
    
    // view categories on website
    private List<CategoryObj> categoryList = new ArrayList<CategoryObj>();
    public List<CategoryObj> getCategoryList() { return categoryList; }
    
    public editImage() throws SQLException, ClassNotFoundException {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

        Statement stm = conn.createStatement();
        String sql = "SELECT id, name FROM categories";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()) {
            categoryList.add(new CategoryObj(rs.getInt(1), rs.getString(2)));
        }
        
        sql = "SELECT * FROM image WHERE id=" + imgId;
        ResultSet rs2 = stm.executeQuery(sql);

        while(rs2.next()) {
            this.notFound = false;
            if(rs2.getInt(7) == login.userId)
                this.properUser = true;
            
            this.oldTitle       = rs2.getString(2);
            this.oldDescription = rs2.getString(3);
            this.oldTags        = rs2.getString(4);
            this.oldCategory    = rs2.getInt(6);
        }
        
        conn.close();
    }
    
    public String editImage() throws SQLException, ClassNotFoundException {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

        String sql;
        PreparedStatement statement;

        if(this.title != null && !this.title.trim().equals("") && !this.title.equals(this.oldTitle)) {
            sql = "UPDATE image SET title=? WHERE id=" + this.imgId;
            statement = conn.prepareStatement(sql);
            statement.execute();
        }

        if(this.description != null && !this.description.trim().equals("") && !this.description.equals(this.oldDescription)) {
            sql = "UPDATE image SET description=? WHERE id=" + this.imgId;
            statement = conn.prepareStatement(sql);
            statement.execute();
        }

        if(this.tags != null && !this.tags.trim().equals("") && !this.tags.equals(this.oldTags)) {
            sql = "UPDATE image SET tags=? WHERE id=" + this.imgId;
            statement = conn.prepareStatement(sql);
            statement.execute();
        }

        if(this.category != this.oldCategory) {
            sql = "UPDATE image SET category=? WHERE id=" + this.imgId;
            statement = conn.prepareStatement(sql);
            statement.execute();
        }

        conn.close();
        return "viewImage?img=" + this.imgId;
    }
    
    public String deleteImage() throws SQLException, ClassNotFoundException {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

        Statement stm = conn.createStatement();
        String sql = "DELETE FROM image WHERE id=" + this.imgId;
        stm.executeQuery(sql);
        
        conn.close();
        return "index";
    }
}