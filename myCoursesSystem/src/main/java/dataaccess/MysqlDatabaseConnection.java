package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDatabaseConnection
{
    private static Connection connection=null;
    private MysqlDatabaseConnection(){

    }
    public static Connection getConnection(String url, String user, String pwd) throws SQLException, ClassNotFoundException {
        if(connection!=null){
            return connection;
        }else{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pwd);
            return connection;

        }
    }
}
