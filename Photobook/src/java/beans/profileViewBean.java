package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;

@Named(value = "profileViewBean")
@Dependent
public class profileViewBean {
    
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String user         = params.get("user");
    private String name;
    private String surname;
    private String website;
    private String about;
    private String avatar;
    
    public String getUser()     { return user; }
    public String getName()     { return name; }
    public String getSurname()  { return surname; }
    public String getWebsite()  { return website; }
    public String getAbout()    { return about; }
    public String getAvatar()   { return avatar; }
    
    private boolean userFound;
    public boolean isUserFound() { return userFound; }
    
    private boolean photosFound;
    public boolean isPhotosFound() { return photosFound; }
    
    private boolean descExists;
    public boolean isDescExists() { return descExists; }
    
    public profileViewBean() throws ClassNotFoundException, SQLException {
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
        Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT * FROM user WHERE nickname = '" + this.user + "'";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            userFound       = true;
            this.user       = rs.getString(2); // because sometimes you can write name with small letters while it's capitals only
            this.name       = returnValue(rs.getString(3));
            this.surname    = returnValue(rs.getString(4));
            this.website    = returnValue(rs.getString(9));
            this.about      = returnValue(rs.getString(10));
            
            if(this.about.trim().equals(""))
                descExists = false;
            else
                descExists = true;
            
            if(rs.getString(11) == null || rs.getString(11).trim().equals(""))
                this.avatar = "resources/images/avatar-placeholder.png";
            else
                this.avatar = "resources/user-avatars/" + rs.getString(11);
        }
    }
    
    public String returnValue(String param) {
        if(param == null || param.trim().equals(""))
            return "";
        return param;
    }
    
}