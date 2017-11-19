package beans;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

@Named(value = "login")
@Dependent
public class login {
    
    private String userName;
    private String userPassword;

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
    
    public login() {
    }
    
}
