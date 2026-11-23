
package KetNoi;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=QuanLyQuanNuoc;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

            String user = "javauser";
            String password = "Java@123456";

            Connection conn = DriverManager.getConnection(
                    url, user, password
            );

            System.out.println("KET NOI SQL THANH CONG!");
            return conn;

        } catch (Exception e) {
            System.out.println("LOI KET NOI: " + e.getMessage());
            return null;
        }
    }
}
