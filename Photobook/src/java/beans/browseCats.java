package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "browseCats")
@Dependent
public class browseCats {
    
    private List<String> categories = new ArrayList<>();
    public List<String> getCategories() { return categories; }
    
    public browseCats() {
        try {
            String sterownik = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/photobook";
            
            Class.forName(sterownik);
            Connection conn = DriverManager.getConnection(url, "root", "");
            
            Statement stm = conn.createStatement();
            String sql = "SELECT name FROM categories";
            ResultSet rs = stm.executeQuery(sql);
            
            while(rs.next()) {
                this.categories.add(rs.getString(1));
            }
        } catch (Exception ex) {}
    }
    
}
