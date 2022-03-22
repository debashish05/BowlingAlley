import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement; 
public class query {
    public String highestScore()
    {
    String sql="Select MAX(score) as score from playerScore";
   
    try {  
        database ob=new database();
        Connection c = ob.connect();  
        Statement stmt  = c.createStatement();  
        ResultSet rs    = stmt.executeQuery(sql);  
        // loop through the result set  
        
           return rs.getString("score");  
    } catch (SQLException e) {  
        System.out.println(e.getMessage());  
    }  
    return null;
    }
    public String mostFrequentPlayer()
    {
    String sql="Select playerName  from playerScore  group by playerName order by count(*) desc limit 1)";
   
    try {  
        database ob=new database();
        Connection c = ob.connect();  
        Statement stmt  = c.createStatement();  
        ResultSet rs    = stmt.executeQuery(sql);  
        // loop through the result set  
        
           return rs.getString("playerName");  
    } catch (SQLException e) {  
        System.out.println(e.getMessage());  
    }  
    return null;
    }
    public String lowestScore()
    {
    String sql="Select MIN(score) as score from playerScore";
   
    try {  
        database ob=new database();
        Connection c = ob.connect();  
        Statement stmt  = c.createStatement();  
        ResultSet rs    = stmt.executeQuery(sql);  
        // loop through the result set  
        
           return rs.getString("score");  
    } catch (SQLException e) {  
        System.out.println(e.getMessage());  
    }  
    return null;
    }
    public String topPlayer()
    {
    String sql="Select playerName from playerScore where score=(Select MAX(score) from playerScore)";
   
    try {  
        database ob=new database();
        Connection c = ob.connect();  
        Statement stmt  = c.createStatement();  
        ResultSet rs    = stmt.executeQuery(sql);  
        // loop through the result set  
        
           return rs.getString("playerName");  
    } catch (SQLException e) {  
        System.out.println(e.getMessage());  
    }  
    return null;
    }
}
