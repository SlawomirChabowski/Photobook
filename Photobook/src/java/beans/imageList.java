package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

@Named(value = "imageList")
@Dependent
public class imageList {

    private List<imgListBean> imageList = new ArrayList<imgListBean>();
    public List<imgListBean> getImageList() { return imageList; }
    
    public imageList() throws ClassNotFoundException, SQLException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        Connection conn = DriverManager.getConnection(url, "root", "");         // db link, user, password
        
        Statement stm = conn.createStatement();                                 //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT i.*, u.nickname FROM image i INNER JOIN user u ON u.id = i.author_id ORDER BY date_added DESC LIMIT 15";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            imgListBean image = new imgListBean(rs.getInt(1), rs.getString(8), rs.getString(2), rs.getString(9));
            imageList.add(image);
        }
        
        conn.close();
    }
}