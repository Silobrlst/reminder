import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBase {
    DataBase(){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:config.db");

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE reminds " +
                    "(id INT PRIMARY KEY, " +
                    " active INT, " +
                    " date TEXT, " +
                    " time TEXT, " +
                    " message TEXT, " +
                    " days TEXT, " +
                    " limNum INT, " +
                    " curNum INT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    void addRemind(Remind remindIn){

    }
}
