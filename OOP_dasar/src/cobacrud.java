import java.sql.*;
import java.util.Scanner;

public class cobacrud {
    
    private static String URL ="jdbc:mysql://localhost:3306/crud";
    private static String user ="root";
    private static String pass ="";

    private static Connection conn;

    private static Scanner in = new Scanner(System.in);

    public static void main (String[]args){
        try {
             conn = DriverManager.getConnection(URL, user, pass);
              //System.out.println("berhasil");
        } catch (SQLException e) {
            System.out.println("gagal konek : "+ e.getMessage());

        }
        while (true) {
            System.out.println("=========== menu ++++++++++++++");
        System.out.println("1 lihat data" );
        System.out.println("2 tambah data" );
        System.out.println("3 ubah data" );
        System.out.println("4 hapus data" );
        System.out.println("5 keluar....\n" );

        System.out.print("pilihan : ");
        int pilih = in.nextInt();

        switch (pilih) {
            case 1:
                lihatdata();
                break;
            case 2:
                tambahdata();
                break;
            case 3:
                ubahdata();
            case 4:
                hapusdata();
                break;
            case 5:
            System.out.println("keluar ");
            return;
            
        }
        }
        
        

       


    }

    private static void lihatdata() {

        String sql = "select * from user";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()) {

                System.out.println(rs.getString("iduser")+rs.getString("nama"));
                
                
            }
        } catch (SQLException e) {
            System.out.println("gagal mengambil data : "+ e.getMessage());
            // TODO: handle exception
        }
    }

    private static void tambahdata() {
        String sql = ("insert into user (iduser,nama) values (?,?)");

        System.out.print("masukan id user :");
        String id = in.next();
        System.out.print("Masukan nama : ");
        String nama = in.next();

        try (PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, id);
            ps.setString(2, nama);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("\n data berhasil di tambahkan \n\n");
            }
            
        } catch (SQLException e) {
            System.out.println("gagal ditambahkan : "+ e.getMessage());
            // TODO: handle exception
        }
    }

    private static void ubahdata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ubahdata'");
    }

    private static void hapusdata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hapusdata'");
    }

}
