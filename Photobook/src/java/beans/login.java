package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "login")
@SessionScoped
public class login implements Serializable {
    
    private String userName;
    private String userPassword;
    private String avatarUrl = "images/avatar-placeholder.png";
    private int userId = 0;
    private boolean logged = false;
    private boolean badData = false;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isBadData() {
        return badData;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
    
    // constructor
    public login(){}
    
    //methods
    public void logout() {
        logged = false;
    }
    
    public String login() throws ClassNotFoundException, SQLException  {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
        Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT * FROM user WHERE nickname = '" + userName + "' AND password = '" + userPassword + "'";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            logged = true;
            badData = false;
            if(rs.getString(9) != null)
                avatarUrl = "user-avatars/" + rs.getString(9);
            userId = rs.getInt(1);
            return "index";
        }
        
        badData = true;
        avatarUrl = "images/avatar-placeholder.png";
        userId = 0;
        return "login";
    }
    
    public String login(String userName, String userPassword) throws ClassNotFoundException, SQLException  {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
        Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT * FROM user WHERE nickname = '" + userName + "' AND password = '" + userPassword + "'";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            logged = true;
            badData = false;
            if(rs.getString(9) != null)
                avatarUrl = "user-avatars/" + rs.getString(9);
            userId = rs.getInt(1);
            return "index";
        }
        
        badData = true;
        avatarUrl = "images/avatar-placeholder.png";
        userId = 0;
        return "login";
    }
}
