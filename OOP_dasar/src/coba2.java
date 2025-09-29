import java.sql.*;
import java.util.Scanner;



public class coba2 {

private static String URL = "jdbc:mysql://localhost:3306/mhs";
private static String user = "root";
private static String pass = "";

private static Connection conn;

private static Scanner input = new Scanner (System.in);

public static void main(String[] args) {

    try {
        conn = DriverManager.getConnection(URL, user, pass);
        //System.out.println("sukses");
    } catch (SQLException e) {
        // TODO: handle exception
        System.out.println("gagal konek DB : "+ e.getMessage());
    }
    while (true) {
         System.out.println("++++++++++++++++ MENU +++++++++++++");
    System.out.println("1  : Lihat Data");
    System.out.println("2  : Tambah data");
    System.out.println("3  : Ubah data");
    System.out.println("4  : Hapus data");
    System.out.println("5  : Keluar ");
    System.out.print(" Masukan Pilihan : ");
    int pilih = input.nextInt();

    switch (pilih) {
        case 1:
            lihatdata();
            break;
        case 2:
            tambahdata();
            break;
        case 3:
            ubahdata();
            break;
        case 4:
            hapusdata();
        case 5:
        System.out.println("Keluar....");
            return;
        default:
            break;
    }
    }
   

    

}

private static void lihatdata() {
    String sql = "SELECT * FROM user";

    try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
        System.out.println("+++++++++++++++ DATA USER ++++++++++++++");
        System.out.println("NO   "+"ID   "+"         NAMA" + "     UMUR" );

        int i = 1;
        while (rs.next()) {
        System.out.println(i +"   "+"K00"+ rs.getInt("iduser")+"     "+rs.getString("nama")+"   "+rs.getInt("umur"));

            i++;

        }
        System.out.println("\n\n++++++++++++++++++++++++\n");

    } catch (SQLException e) {
        System.out.println("gagal di tampilkan : "+ e.getMessage());
        // TODO: handle exception
    }
}

private static void tambahdata() {

  String sql = "INSERT INTO user (nama,umur) values (?,?)";

  try (PreparedStatement ps = conn.prepareStatement(sql)){

    System.out.println("Masukan Nama : ");
    String nama = input.next();
    System.out.println("Masukan Umur : ");
    int umur = input.nextInt();

    ps.setString(1, nama);
    ps.setInt(2, umur);

    int rows = ps.executeUpdate();
    if (rows > 0) {
        System.out.println("\n\n++++++++++++++++++++++++\n");
        System.out.println("data berhasil di tambahlan");
        System.out.println("\n\n");
    }

    
  } catch (SQLException e) {
    // TODO: handle exception
    System.out.println(" gagal menambahkan data : " + e.getMessage());
  }
}
private static void ubahdata() {
    String sql = "UPDATE user SET nama =?, umur =? where iduser =?";
    System.out.println("\n\n\n");
    lihatdata();
    try (PreparedStatement ps = conn.prepareStatement(sql)){
        System.out.println("masukan ID user ");
        int id = input.nextInt();
        System.out.println("masukan nama baru : ");
        String nama = input.next();
        System.out.println("Mmasukan umur : ");
        int umur = input.nextInt();

        ps.setString(1, nama);
        ps.setInt(2, umur);
        ps.setInt(3, id);

        

        int rows = ps.executeUpdate();
        if (rows > 0 ) {
            System.out.println("\n\n++++++++++++++++++++++++\n");
            System.out.println("data berhasil diubah ");
            System.out.println("\n++++++++++++++++++++++++\n");
            
        }

        
    } catch (SQLException e) {
        // TODO: handle exception
        System.out.println("gagal mengubah : "+ e.getMessage());
    }
}

private static void hapusdata() {
    String sql = "DELETE FROM user WHERE iduser=? ";
    lihatdata();
    System.out.println("pilih id user yang akan dihapus : ");
        int id = input.nextInt();
    try (PreparedStatement ps = conn.prepareStatement(sql)){

        

        ps.setInt(1, id);

        ps.executeUpdate();
        
    } catch (SQLException e) {
        // TODO: handle exception
        System.out.println("gagal hapus : "+ e.getMessage());
    }
}


    
}