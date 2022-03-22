import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;  
public class database
{  
    static Connection conn = null; 
    public static void createNewTable(Connection conn) {   
        try{ 
        Statement stmt = conn.createStatement();  
        stmt.executeUpdate("create table if not exists playerScore(playerName string,date string,score string)"); 
        }
        catch(SQLException e)
        {
          System.err.println(e.getMessage());
        }
    }
    public Connection connect() {  
        try {  
            // db parameters
            if(conn!=null)
            return conn;
            
            String url = "jdbc:sqlite:score.db";  
            // create a connection to the database  
            conn = DriverManager.getConnection(url);  
            createNewTable(conn);
            
              
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        return conn;
    }  
    public void insert(String playerName, String date, String score) {  
        Connection c=null;
       
        String sql = "INSERT INTO playerScore(playerName, date,score) VALUES(?,?,?)";  
   
        try{  
             c=connect();  
            PreparedStatement pstmt = c.prepareStatement(sql);  
            pstmt.setString(1, playerName);  
            pstmt.setString(2,date);
            pstmt.setString(3,score);
            pstmt.executeUpdate();  
        }
        catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
       
}   
public void selectAll(){  
    String sql = "SELECT * FROM playerScore";  
      
    try {  
        Connection c = this.connect();  
        Statement stmt  = c.createStatement();  
        ResultSet rs    = stmt.executeQuery(sql);  
        // loop through the result set  
        while (rs.next()) {  
            System.out.println(rs.getString("playerName") +  "\t" +   
                               rs.getString("score") + "\t" +  
                               rs.getString("date"));  
        }  
    } catch (SQLException e) {  
        System.out.println(e.getMessage());  
    }  
}

}

