package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;

@Named(value = "searchBean")
@Dependent
public class searchBean {

    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String search = params.get("search");
    public String getSearch() { return search; }
    
    private boolean anyProfile;
    private boolean anyImage;
    
    public boolean isAnyProfile()   { return anyProfile; }
    public boolean isAnyImage()     { return anyImage; }
    
    private List<String> userList = new ArrayList<String>();
    public List<String> getUserList() { return userList; }
    
    private List<imgListBean> imageList = new ArrayList<imgListBean>();
    public List<imgListBean> getImageList() { return imageList; }
    
    public searchBean() throws ClassNotFoundException, SQLException {
        if(this.search != null && !this.search.trim().equals("")) {
            String sterownik = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/photobook";

            Class.forName(sterownik);
            Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password

            Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
            String sql = "SELECT * FROM user WHERE nickname LIKE '%" + this.search + "%'";
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next()){
                anyProfile = true;
                userList.add(rs.getString(2));
            }

            sql = "SELECT i.*, u.nickname "
                    + "FROM image i INNER JOIN user u ON u.id = i.author_id "
                    + "WHERE i.title LIKE '%" + this.search + "%' "
                    + "OR i.description LIKE '%" + this.search + "%' "
                    + "OR FIND_IN_SET('" + this.search + "', i.tags) "
                    + "OR FIND_IN_SET(' " + this.search + "', i.tags) "
                    + "OR FIND_IN_SET(' " + this.search + " ', i.tags)";
            rs = stm.executeQuery(sql);

            while(rs.next()) {
                anyImage = true;
                imgListBean image = new imgListBean(rs.getInt(1), rs.getString(8), rs.getString(2), rs.getString(9));
                imageList.add(image);
            }
            
            conn.close();
        }
    }
}