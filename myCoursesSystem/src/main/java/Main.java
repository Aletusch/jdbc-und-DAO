import dataaccess.MySqlCourseRepository;
import dataaccess.MysqlDatabaseConnection;
import ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Test");

        try {
            Cli cli = new Cli(new MySqlCourseRepository());
            cli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: "+e.getMessage()+"SQL State: "+e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: "+e.getMessage());
        }


    }
}
