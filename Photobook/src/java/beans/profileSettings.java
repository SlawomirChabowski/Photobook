package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@Named(value = "profileSettings")
@ConversationScoped
public class profileSettings implements Serializable {
    
    // controlling the alerts
    private boolean invalidPassword     = false;
    private boolean passwordsDiffer     = false;
    private boolean badEmail            = false;
    private boolean success             = false;
    private boolean incorrectImageType  = false;

    // data from database
    private String nickname;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String website;
    private String about;
    private String avatar;
    
    // data from form
    private String newNickname;
    private String newName;
    private String newSurname;
    private String newEmail;
    private String newPassword;
    private String newPasswordConfirmation;
    private String newWebsite;
    private String newAbout;
    private String newAvatarPath;
    private String oldPassword;
    
    // avatar data
    private Part uploadedFile;
    private String newAvatarName;
    
    
    // getters and setters
    public String getNickname() { return nickname; }
    public String getName()     { return name; }
    public String getSurname()  { return surname; }
    public String getEmail()    { return email; }
    public String getWebsite()  { return website; }
    public String getAbout()    { return about; }
    public String getAvatar()   { return avatar; }
    
    public void setNewNickname(String newNickname)                          { this.newNickname = newNickname; }
    public void setNewName(String newName)                                  { this.newName = newName; }
    public void setNewSurname(String newSurname)                            { this.newSurname = newSurname; }
    public void setNewEmail(String newEmail)                                { this.newEmail = newEmail;}
    public void setNewPassword(String newPassword)                          { this.newPassword = newPassword; }
    public void setNewPasswordConfirmation(String newPasswordConfirmation)  { this.newPasswordConfirmation = newPasswordConfirmation; }
    public void setNewWebsite(String newWebsite)                            { this.newWebsite = newWebsite; }
    public void setNewAbout(String newAbout)                                { this.newAbout = newAbout; }
    public void setNewAvatarPath(String newAvatarPath)                      { this.newAvatarPath = newAvatarPath; }
    public void setOldPassword(String oldPassword)                          { this.oldPassword = oldPassword; }
    
    public String getNewNickname()              { return newNickname; }
    public String getNewName()                  { return newName; }
    public String getNewSurname()               { return newSurname; }
    public String getNewEmail()                 { return newEmail; }
    public String getNewPassword()              { return newPassword; }
    public String getNewPasswordConfirmation()  { return newPasswordConfirmation; }
    public String getNewWebsite()               { return newWebsite; }
    public String getNewAbout()                 { return newAbout; }
    public String getNewAvatarPath()            { return newAvatarPath; }
    public String getOldPassword()              { return oldPassword; }
    
    public boolean isInvalidPassword()                              { return invalidPassword; }
    public void setInvalidPassword(boolean invalidPassword)         { this.invalidPassword = invalidPassword; }
    public boolean isPasswordsDiffer()                              { return passwordsDiffer; }
    public void setPasswordsDiffer(boolean passwordsDiffer)         { this.passwordsDiffer = passwordsDiffer; }
    public boolean isSuccess()                                      { return success; }
    public void setSuccess(boolean success)                         { this.success = success; }
    public boolean isBadEmail()                                     { return badEmail; }
    public void setBadEmail(boolean badEmail)                       { this.badEmail = badEmail; }
    public boolean isIncorrectImageType()                           { return incorrectImageType; }
    public void setIncorrectImageType(boolean incorrectImageType)   { this.incorrectImageType = incorrectImageType; }
    
    public Part getUploadedFile()                   { return uploadedFile; }
    public void setUploadedFile(Part uploadedFile)  { this.uploadedFile = uploadedFile; }
    
    
    // constructor
    public profileSettings() throws ClassNotFoundException, SQLException, IOException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        
        Statement stm = conn.createStatement();
        String sql = "SELECT * FROM user WHERE id = " + login.userId;
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            this.nickname   = rs.getString(2);
            this.name       = rs.getString(3);
            this.surname    = rs.getString(4);
            this.email      = rs.getString(5);
            this.password   = rs.getString(6);
            this.website    = rs.getString(9);
            this.about      = rs.getString(10);
            
            if(rs.getString(11) == null || rs.getString(11).trim().isEmpty())
                this.avatar = "resources/images/avatar-placeholder.png";
            else
                this.avatar = "resources/user-avatars/" + rs.getString(11);
        }
    }
    
    
    // method to change profile data
    public String setChanges() throws ClassNotFoundException, SQLException, IOException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";
        
        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        
        
        if(!this.oldPassword.equals(this.password)) {
            invalidPassword = true;
            conn.close();
            return "editProfile";
        }
        
        if(!(this.newEmail == null || this.newEmail.trim().isEmpty())) {
            Statement stm = conn.createStatement();
            String sql1 = "SELECT * FROM user WHERE email = '" + this.newEmail + "'";
            ResultSet rs = stm.executeQuery(sql1);
            while(rs.next()) {
                badEmail = true;
                conn.close();
                return "editProfile";
            }
            
            String sql = "UPDATE user SET email = ? WHERE id = " + login.userId;
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, this.newEmail);
            statement.execute();
        }
        
        if(!(this.newPassword == null || this.newPassword.trim().isEmpty())) {
            if(this.newPassword.length() < 5 || !this.newPassword.equals(this.newPasswordConfirmation)) {
                passwordsDiffer = true;
                conn.close();
                return "editProfile";
            } else {
                String sql = "UPDATE user SET password = ? WHERE id = " + login.userId;
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, this.newPassword);
                statement.execute();
            }
        }
        
        
        // update avatar
        try {
            InputStream input = uploadedFile.getInputStream();
            // check if extension is .png, .jpg, .jpeg or .gif
            String folder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "/resources/user-avatars/";
            String extension = uploadedFile.getSubmittedFileName().substring(uploadedFile.getSubmittedFileName().lastIndexOf(".")+1);
            if(extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif")) {
                // set file name
                newAvatarName = "av_" + login.userName + "_" + System.currentTimeMillis() + "." + extension;
                newAvatarPath = folder + newAvatarName;
                
                File f = new File(newAvatarPath);
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
                
                // update user table
                PreparedStatement tempstm = conn.prepareStatement("UPDATE user SET avatar_img_name=? WHERE id=" + login.userId);
                tempstm.setString(1, this.newAvatarName);
                tempstm.execute();
                login.avatarUrl = "resources/user-avatars/" + this.newAvatarName;
                this.avatar = "resources/user-avatars/" + this.newAvatarName;
            } else {
                incorrectImageType = true;
                input.close();
                conn.close();
                return "editProfile";
            }
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
        
        
        String sql = "UPDATE user SET "
                + "name = ?, "
                + "surname = ?, "
                + "website = ?, "
                + "about = ? "
                + "WHERE id = " + login.userId;
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, this.newName);
        statement.setString(2, this.newSurname);
        statement.setString(3, this.newWebsite);
        statement.setString(4, this.newAbout);
        statement.execute();
        

        success = true;
        conn.close();
        return "editProfile";
    }
}