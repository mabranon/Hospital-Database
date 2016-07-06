
package util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Creates a connector object with methods to open and close a DB connection
 */



public class DBConnector{
    
    public static Connection getConnection(){
        try{
            Context context = new InitialContext();
            DataSource datasource = (DataSource) context.
                    lookup("java:comp/env/jdbc/MySQLDS");
            Connection con = datasource.getConnection();
            return con;
        }catch(NamingException | SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public static void close(Connection con){
        try{
            con.close();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
}