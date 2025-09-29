import java.sql.*;
import java.util.Scanner;;


public class cobalogin {

    
    private static String URL = "jdbc:mysql://localhost:3306/login";
    private static String user ="root";
    private static String pass = "";

    private static Connection conn;


    private static Scanner in = new Scanner (System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, user, pass);
            //System.out.println("konek db berhasil");
        } catch (SQLException e) {
            System.out.println("gagal konek db :"+ e.getMessage());
            // TODO: handle exception
        }
        while (true) {
            System.out.println("   menu \n1 . menambahkan user\n2 . coba user\n3 . keluar..... \nmasukan pilihan anda :");
        int pilih = in.nextInt();

        switch (pilih) {
            case 1:
                tambahdata();
                break;
            case 2:
                cekdata();
                break;
            case 3:
                System.out.println("exit....");
                return;
            
        }
        }
        

    }

    private static void cekdata() {
        String sql = "SELECT * FROM admin where username =? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            System.out.println("Masukan username :"); String name = in.next();
            System.out.println("masukan password :"); String pass = in.next();

            ps.setString(1, name);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                System.out.println(" username dan password benar");
            }else{
                System.out.println("username atau password salah");
            }
            
        } catch (SQLException e) {
            System.out.println("gagal : "+ e.getMessage());
            // TODO: handle exception
        }
    }

    private static void tambahdata() {
      String sql = "insert into admin (username,password) values (?,?)";
        System.out.println("masukan  nama user :");
        String nama = in.next();
        System.out.println(" masukan password : ");
        String pass = in.next();

      try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nama);
            ps.setString(2, pass);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("\n berhasil menambahkan data \n\n");
            }

        
      } catch (SQLException e) {
        System.out.println("gagal menambahkan data : "+ e.getMessage());
        // TODO: handle exception
      }
    }
}
