import java.sql.*;


public class db_config {
    private static final String URL = "jdbc:mysql://localhost:3306/db_angsuran";
    private static final String user ="root";
    private static final String pass ="";
    public static Connection connection;

   

    public static Connection koneksi (){
    try {
            connection = DriverManager.getConnection(URL, user, pass);
            System.out.println("Berhasil konek" + URL);
            

        } catch (SQLException e) {
           System.out.println("gagal terhubung ke DB_angsuran : "+ e.getMessage());
        }
        return connection;
    }
}
   
    