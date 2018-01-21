package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@Named(value = "addImage")
@ConversationScoped
public class addImage implements Serializable {

    // image parameters
    private String  title;
    private String  description;
    private String  tags;
    private String  imgName;
    private int category;
    
    public String getTitle()           { return title; }
    public String getDescription()     { return description; }
    public String getTags()            { return tags; }
    public String getImgName()         { return imgName; }
    public int getCategory()   { return category; }
    
    public void setTitle(String title)             { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTags(String tags)               { this.tags = tags; }
    public void setImgName(String imgName)         { this.imgName = imgName; }
    public void setCategory(int category)  { this.category = category; }
    
    
    // validation parameters
    private boolean incorrectImageType  = false;
    private boolean titleNull           = false;
    
    public boolean getIncorrectImageType() { return incorrectImageType; }
    public boolean getTitleNull()          { return titleNull; }
    
    public void setIncorrectImageType(boolean incorrectImageType)  { this.incorrectImageType = incorrectImageType; }
    public void setTitleNull(boolean titleNull)                    { this.titleNull = titleNull; }
    
    
    // view categories on website
    private List<CategoryObj> categoryList = new ArrayList<CategoryObj>();
    public List<CategoryObj> getCategoryList() { return categoryList; }
    
    
    // file
    private Part uploadedFile;
    private String imgPath;
    
    public Part getUploadedFile() { return uploadedFile; }
    public void setUploadedFile(Part uploadedFile) { this.uploadedFile = uploadedFile; }
    
    public addImage() throws SQLException, ClassNotFoundException {
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
        
        conn.close();
    }
    
    public String newImage() {
        System.out.println("It works!!!!!!!");
        long id = 0;
        
        if(title == null || title.trim().equals("")) {
            titleNull = true;
        }
        else {
            try {
                if(this.title == null || this.title.trim().equals("")) {
                    titleNull = true;
                    return "addImage";
                } else {
                    InputStream input = uploadedFile.getInputStream();
                    // check if extension is .png, .jpg, .jpeg or .gif
                    String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "/resources/user-images/";
                    String extension = uploadedFile.getSubmittedFileName().substring(uploadedFile.getSubmittedFileName().lastIndexOf(".")+1);
                    if(extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif")) {
                        // set file name
                        imgName = "av_" + login.userName + "_" + System.currentTimeMillis() + "." + extension;
                        imgPath = folder + imgName;

                        File f = new File(imgPath);
                        if(!f.exists()) {
                            f.createNewFile();
                        }
                        FileOutputStream output = new FileOutputStream(f);
                        byte[] buffer = new byte[1024];
                        int length;
                        while((length = input.read(buffer)) > 0) {
                                output.write(buffer, 0, length);
                        }
                        input.close();
                        output.close();

                        // insert new image
                        String sterownik = "com.mysql.jdbc.Driver";
                        String url = "jdbc:mysql://localhost:3306/photobook";

                        Class.forName(sterownik);
                        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

                        String sql = "INSERT INTO image(title, description, tags, category_id, author_id, img_file_name) "
                                + "VALUES(?, ?, ?, ?, ?, ?)";

                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, this.title);
                        statement.setString(2, this.description);
                        statement.setString(3, this.tags);
                        statement.setInt(4, this.category);
                        statement.setInt(5, login.userId);
                        statement.setString(6, this.imgName);
                        statement.execute();

                        id = statement.getGeneratedKeys().getLong(1);
                        conn.close();
                    } else {
                        incorrectImageType = true;
                        input.close();
                        return "addImage";
                    }
                }
            } catch(Exception e) {
                e.printStackTrace(System.out);
            }
        }
        
        return "viewImage?img=" + id;
    }
}