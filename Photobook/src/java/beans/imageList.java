package beans;

import entities.Image;
import entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import org.hibernate.SessionFactory;

/**
 *
 * @author Slawek
 */
@Named(value = "imageList")
@Dependent
public class imageList {

    private static SessionFactory factory;
    private List<Image> imageList = new ArrayList<Image>();
    private Date data = new Date(2017, 01, 01);

    public List<Image> getImageList() {
        return imageList;
    }
    
    public imageList() throws ClassNotFoundException, SQLException {
        
        String sterownik = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/photobook";

        Class.forName(sterownik);
        System.out.println("sterownik OK");
        Connection conn = DriverManager.getConnection(url, "root", "");
        System.out.println("baza OK");
        
        Statement stm = conn.createStatement(); //uwaga na import - ma być z pakietu java.sql
        String sql = "SELECT * FROM image ORDER BY date_added DESC LIMIT 6";
        ResultSet rs = stm.executeQuery(sql);
        
        while(rs.next()){
            Image image = new Image(rs.getInt(1), rs.getString(2), rs.getDate(5), rs.getString(8));
            // 1 - id
            // 2 - title
            // 3 - description
            // 4 - tags
            // 5 - date
            // 6 - category
            // 7 - author
            // 8 - img_file_name
            imageList.add(image);
        }
    }
}