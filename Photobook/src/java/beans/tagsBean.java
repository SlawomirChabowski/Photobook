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

@Named(value = "tagsBean")
@Dependent
public class tagsBean {

    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private String value = params.get("value");
    public String getValue() { return value; }
    
    private boolean anyImage;
    public boolean isAnyImage()     { return anyImage; }
    
    private List<imgListBean> imageList = new ArrayList<imgListBean>();
    public List<imgListBean> getImageList() { return imageList; }
    
    public tagsBean() throws ClassNotFoundException, SQLException {
        if(this.value != null && !this.value.trim().equals("")) {
            String sterownik = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/photobook";

            Class.forName(sterownik);
            System.out.println("sterownik OK");
            Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
            System.out.println("baza OK");

            Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
            String sql = "SELECT i.*, u.nickname FROM image i "
                    + "INNER JOIN user u ON i.author_id=u.id "
                    + "WHERE FIND_IN_SET('" + this.value + "', tags) "
                    + "OR FIND_IN_SET(' " + this.value + "', tags) "
                    + "OR FIND_IN_SET(' " + this.value + " ', tags)";
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next()) {
                anyImage = true;
                imgListBean image = new imgListBean(rs.getInt(1), rs.getString(8), rs.getString(2), rs.getString(9));
                imageList.add(image);
            }
            
            conn.close();
        }
    }
}