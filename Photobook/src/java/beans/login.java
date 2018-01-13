package beans;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "login")
@SessionScoped
public class login implements Serializable {
    
    private String userName;
    private String userPassword;
    private boolean logged = true;

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
    public login() {
 /*       SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sess = factory.openSession();
        Transaction tx = null;
        try {
            tx = sess.beginTransaction();
            List<User> users = sess.createQuery("from User").list();
            for (User u : users) {
                System.out.println(u);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            sess.close();
        }*/
    }
    
    //methods
    public void logout() {
        logged = false;
    }
    
    public String login() {
        logged = true;
        return "index";
    }
}
