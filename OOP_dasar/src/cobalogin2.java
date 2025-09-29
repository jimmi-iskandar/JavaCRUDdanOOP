import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.print.DocFlavor.STRING;

public class cobalogin2 {

    private static String URL = "jdbc:mysql://localhost:3306/login";
    private static String user = "root";
    private static String pass = "";

    private static Connection conn;

    private static Scanner in = new Scanner (System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL,user,pass);
            //System.out.println("konek DB berhasil ");
        } catch (SQLException e) {
            System.out.println(" konek db GAGAL : " + e.getMessage());
        }
        while (true) {
             System.out.println("+++++menu+++++++");
        System.out.print("\n\n1 . lihat data \n2 . tambah data\n3 . ubah data\n4. cari data\n5 . hapus data\n6 . keluar \n masukan pilihan anda : ");
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
                break;
            case 4:
                caridata();
                break;
            case 5:
                hapusdata();
                break;
            case 6:
            System.out.println("\n+++++++++++keluar++++++++++++++++\n");
            return;
        }
        }
       
    }

    private static void lihatdata() {
      String sql = "SELECT * FROM ADMIN";
      try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.println("\n   Nama username\n");
        while (rs.next()) {
            System.out.println(rs.getInt("id")+"   "+rs.getString("username"));
        }

      } catch (SQLException e){
        System.out.println("gagal memuat data :"+ e.getMessage());

      }
    }

    private static void tambahdata() {
       String sql ="INSERT INTO admin (username,password) VALUE (?,?)";
       System.out.print("masukan username : ");
       String nama =in.next();
       System.out.println("masukan password : ");

       String pass = in.next();

        try {
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, pass);

            int rows = ps.executeUpdate();
            if (rows >0) {
                System.out.println("data berhasil ditambahkan : ");
                
            }



        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data : " + e.getMessage()) ;
        }
    }

    private static void ubahdata() {
        lihatdata();
        System.out.println("\npilih data yang mau di ganti : ");
        int id = in.nextInt();

        String sql = "UPDATE admin set username=?, password =? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            System.out.println("Masukan nama baru : ");
            String nama = in.next();
            System.out.println("Masukan password baru : ");
            String pass = in.next();

            ps.setString(1, nama);
            ps.setString(2, pass);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            if (rows > 0 ) {
                System.out.println("data berhasil diupdate ");
            }
        } catch (SQLException e) {
            System.out.println(" data gagal diupdate : "+ e.getMessage());
            // TODO: handle exception
        }
    }

    private static void caridata() {
        
        String sql = "SELECT * FROM admin where username = ?";
        
        System.out.println(" masukan nama yang mau anda cari : ");
        String nama = in.next();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);

            ResultSet rs = ps.executeQuery();
           if (rs.next()) {
            //rs.getInt("id");
            System.out.println("data ditemukan : ");
            System.out.println("username : "+ rs.getString("username"));
            System.out.println("password : "+ rs.getString("password")); 
           } 
            
             
        } catch (SQLException e) {
            System.err.println("gagal menemukan data : "+e.getMessage());
             e.printStackTrace();
            // TODO: handle exception
        }
    }

    private static void hapusdata() {
       String SQL = "DELETE FROM admin WHERE id = ?";
       try {
        PreparedStatement ps = conn.prepareStatement(SQL);

        lihatdata();
        System.out.println("pilih data yang akan dihapus : ");
        int pilih = in.nextInt();

        ps.setInt(1, pilih);

        int rows = ps.executeUpdate();
        if (rows <= 1) {
            System.out.println("data berhasil dihapus\n");
        }
       } catch (SQLException e) {
        e.printStackTrace();// TODO: handle exception
       }
    }
}
