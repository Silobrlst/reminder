import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.GregorianCalendar;

public class Main {
    void addRemindToDB(String dateIn, String timeIn, String messageIn, ){

    }

    public static void main(String[] args){
        GregorianCalendar calendar = new GregorianCalendar();

        RemindsTableWindow remindsTableWindow = new RemindsTableWindow();
        remindsTableWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:config.db");

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE reminds " +
                    "(id INT PRIMARY KEY, " +
                    " date TEXT, " +
                    " time TEXT, " +
                    " message TEXT, " +
                    " days TEXT, " +
                    " limNum INT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("table created");
    }
}
