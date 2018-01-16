package beans;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Named(value = "profileSettings")
@ConversationScoped
public class profileSettings implements Serializable {
    
    // controlling the alerts
    private boolean invalidPassword = false;
    private boolean passwordsDiffer = false;
    private boolean badEmail        = false;
    private boolean success         = false;

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
    
    public boolean isInvalidPassword()                      { return invalidPassword; }
    public void setInvalidPassword(boolean invalidPassword) { this.invalidPassword = invalidPassword; }
    public boolean isPasswordsDiffer()                      { return passwordsDiffer; }
    public void setPasswordsDiffer(boolean passwordsDiffer) { this.passwordsDiffer = passwordsDiffer; }
    public boolean isSuccess()                              { return success; }
    public void setSuccess(boolean success)                 { this.success = success; }
    public boolean isBadEmail()                             { return badEmail; }
    public void setBadEmail(boolean badEmail)               { this.badEmail = badEmail; }
    
    // constructor
    public profileSettings() throws ClassNotFoundException, SQLException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
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
            this.newAbout   = rs.getString(10);
            
            if(rs.getString(11) == null || rs.getString(11).trim().isEmpty())
                this.avatar = "resources/images/avatar-placeholder.png";
            else
                this.avatar = "resources/user-avatars/" + rs.getString(11);
        }
    }
    
    
    // method to change profile data
    public String setChanges() throws ClassNotFoundException, SQLException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";
        
        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
        
        if(!this.oldPassword.equals(this.password)) {
            invalidPassword = true;
            return "editProfile";
        }
        
        if(!(this.newEmail == null || this.newEmail.trim().isEmpty())) {
            Statement stm = conn.createStatement();
            String sql1 = "SELECT * FROM user WHERE email = '" + this.newEmail + "'";
            ResultSet rs = stm.executeQuery(sql1);
            while(rs.next()) {
                badEmail = true;
                return "editProfile";
            }
            
            String sql = "UPDATE user SET email = ? WHERE id = " + login.userId;
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, this.newEmail);
            statement.execute();
        }
        
        if(!(this.newPassword == null || this.newPassword.trim().isEmpty())) {
            if(this.newPassword.length() < 5 || !this.newPassword.equals(this.newPasswordConfirmation)) {
                System.out.println("\"" + this.newPassword + "\"");
                passwordsDiffer = true;
                return "editProfile";
            } else {
                String sql = "UPDATE user SET password = ? WHERE id = " + login.userId;
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, this.newPassword);
                statement.execute();
            }
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
        return "editProfile";
    }
    
    
    
    /*
    // upload an avatar
    private UploadedFile avatarFile;
 
    public UploadedFile getAvatarFile() {
        return avatarFile;
    }
 
    public void setAvatarFile(UploadedFile avatarFile) {
        this.avatarFile = avatarFile;
    }
     
    public void upload() {
        if(avatarFile != null) {
            avatarFile.setName(this.nickname + "_avatar" + avatarFile.());
            FacesMessage message = new FacesMessage("Succesful, your avatar has been updated.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }*/
}