package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "register")
@SessionScoped
public class register implements Serializable {
    private String userName;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmation;
    private boolean notFilled = false;
    private boolean badPass = false;
    private boolean badNick = false;
    private boolean badEmail = false;
    private boolean created = false;

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean isNotFilled() {
        return notFilled;
    }

    public boolean isBadPass() {
        return badPass;
    }

    public boolean isBadNick() {
        return badNick;
    }

    public boolean isBadEmail() {
        return badEmail;
    }

    public boolean isCreated() {
        return created;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
    
    public register() {}
    
    public String register() throws ClassNotFoundException, SQLException {
        this.notFilled  = false;
        this.badPass    = false;
        this.badNick    = false;
        this.badEmail   = false;
        
        if(this.userName.equals(null) || this.userName.equals("") ||
                this.email.equals(null) || this.email.equals("") ||
                this.password.equals(null) || this.password.equals("") ||
                this.confirmation.equals(null) || this.confirmation.equals("")) {
            this.notFilled = true;
            return "register";
        }
        
        if(!password.equals(confirmation)) {
            badPass = true;
            return "register";
        }
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        System.out.println("baza OK");
        
        Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT * FROM user WHERE nickname = '" + this.userName + "'";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            badNick = true;
            return "register";
        }
        
        sql = "SELECT * FROM user WHERE email = '" + this.email + "'";
        rs = stm.executeQuery(sql);
        
        while(rs.next()){
            badEmail = true;
            return "register";
        }
        
        // inserting the data
        try {
            if((!this.name.equals(null) || !this.name.equals("")) && (!this.surname.equals(null) || !this.surname.equals(""))) {
                sql = "INSERT INTO user(nickname, email, password, name, surname) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.userName);
                pstm.setString(2, this.email);
                pstm.setString(3, this.password);
                pstm.setString(4, this.name);
                pstm.setString(5, this.surname);
                pstm.execute();
            } else if(this.name.equals(null) || !this.name.equals("")) {
                sql = "INSERT INTO user(nickname, email, password, surname) VALUES(?, ?, ?, ?)";
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.userName);
                pstm.setString(2, this.email);
                pstm.setString(3, this.password);
                pstm.setString(4, this.surname);
                pstm.execute();
            } else if(!this.surname.equals(null) || !this.surname.equals("")) {
                sql = "INSERT INTO user(nickname, email, password, name) VALUES(?, ?, ?, ?)";
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.userName);
                pstm.setString(2, this.email);
                pstm.setString(3, this.password);
                pstm.setString(4, this.name);
                pstm.execute();
            } else {
                sql = "INSERT INTO user(nickname, email, password) VALUES(?, ?, ?)";
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.userName);
                pstm.setString(2, this.email);
                pstm.setString(3, this.password);
                pstm.execute();
            }
        } catch(Exception e) {
            this.notFilled = false;
            return "register";
        }
        
        this.created = true;
        return "index";
    }
    
}
