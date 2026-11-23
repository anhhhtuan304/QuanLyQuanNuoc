
package KetNoi;

public class TestConnection {

    public static void main(String[] args) {
        if (DBConnection.getConnection() != null) {
            System.out.println("DATABASE DA KET NOI!");
        } else {
            System.out.println("KET NOI THAT BAI!");
        }
    }
}
