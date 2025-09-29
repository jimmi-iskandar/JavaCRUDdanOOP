
import java.util.Scanner;
import java.sql.*;

public class coba_crud {


private static final String URL = "jdbc:mysql://localhost:3306/mhs";
private static final String user = "root";
private static final String pass = "";

private static Connection conn ;
private static Scanner input = new Scanner(System.in);
    public static void main (String[]args){

        try {
            conn = DriverManager.getConnection(URL, user, pass);
            System.out.println("konek Db Sukses");

            menu();
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("Koneksi gagal : "+ e.getMessage());
        }
        


    }

    private static void menu(){
            while (true) {

                System.out.println("MENU PILIHAN : ");
                System.out.println("1 . lihat data ");
                System.out.println("2 . Tambah data  ");
                System.out.println("3 . Uabah data");
                System.out.println("4 . Hapus Data");
                System.out.println("5 . Keluar ");


                System.out.println("Masukan pilihan : ");
                int pilih = input.nextInt();

                if (pilih == 1) {
                    lihatdata();

                }
                else if(pilih == 2){
                    tambahdata();

                }else if(pilih == 3){
                    ubahdata();
                    
                }else if (pilih == 4){
                    hapusdata();
                    
                }else {
                    System.out.println("keluar ");
                    return;
                }

            }
        }

    private static void hapusdata() {
        lihatdata();

        String sql = "DELETE FROM user WHERE iduser = ?";

        System.out.println("masukan nomer urut yang akan dihapus :");
        int id = input.nextInt();

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 1) {
                System.out.println("gagal mengahapus ");

            }
        } catch (SQLException e) {
            System.out.println("erorr hapus : "+e.getMessage());
            // TODO: handle exception
        }
    }

    private static void ubahdata() {
        lihatdata();

        int id ;
        String sql = "UPDATE user set nama =?, umur =? Where iduser = ?";

        System.out.println("masukan No urut user : ");
         id = input.nextInt();
        System.out.println("masukan nama baru : ");
        String Nama = input.next();
        System.out.println("masukan umur : ");
        int umur =input.nextInt();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Nama);
            ps.setInt(2, umur);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0 ) {
                System.out.println("data berhasil di Update");
            }
        } catch (SQLException e) {
            System.out.println("gagal update : "+ e.getMessage());
            // TODO: handle exception
        }
    }

    private static void tambahdata() {

            System.out.println("nama : ");
            String nama = input.next();
            
            System.out.println("Umur : ");
            int umur = input.nextInt();

        String sql ="INSERT INTO user (nama,umur) values (?,?)";
       try (PreparedStatement ps = conn.prepareStatement(sql);){
            
            
            

            ps.setString(1, nama);
            ps.setInt(2, umur);

            int rows = ps.executeUpdate();

            if (rows>0){
                System.out.println("berhasil ditambahkan");

            }else{
                System.out.println("gagal ditambahkan");
            }
       } catch (SQLException e) {
            System.out.println("error tambah : "+e);
        // TODO: handle exception
       }
       
    }

    private static void lihatdata() {
        String sql ="SELECT * FROM user ";

        
        try (PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()){
            System.out.println(" DATA KARYAWAN ");
            System.out.println(" No "+"  ID  "+"NAMA         "+"UMUR ");
            int i =1;
            while (rs.next()) {
                System.out.println(i+"      k00"+rs.getInt("iduser")+"   "+ rs.getString("nama")+"   "+rs.getInt("umur"));
                i++;
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("error Tampildata"+e.getMessage());
        }
    }
        
}
