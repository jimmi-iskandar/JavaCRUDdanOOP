import java.sql.*;
import java.util.Scanner;

public class MainCRUD {
    // Konfigurasi koneksi
    private static final String URL = "jdbc:mysql://localhost:3306/db_console?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // ganti dengan user MySQL kamu
    private static final String PASS = "";     // ganti dengan password MySQL kamu

    private static Connection conn;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi sukses ke database!");
            menu();
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }

    private static void menu() {
        while (true) {
            System.out.println("\\n=== MENU CRUD KARYAWAN ===");
            System.out.println("1. Lihat Data");
            System.out.println("2. Tambah Data");
            System.out.println("3. Ubah Data");
            System.out.println("4. Hapus Data");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // buang newline

            switch (pilihan) {
                case 1: tampilData(); break;
                case 2: tambahData(); break;
                case 3: ubahData(); break;
                case 4: hapusData(); break;
                case 5: System.out.println("Keluar..."); return;
                default: System.out.println("Pilihan tidak valid");
            }
        }
    }

    private static void tampilData() {
        String sql = "SELECT * FROM karyawan";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\\n--- Daftar Karyawan ---");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nama"));
            }
        } catch (SQLException e) {
            System.out.println("Error tampil data: " + e.getMessage());
        }
    }

    private static void tambahData() {
        System.out.print("Masukkan nama karyawan: ");
        String nama = scanner.nextLine();
        String sql = "INSERT INTO karyawan (nama) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.executeUpdate();
            System.out.println("Data berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Error tambah data: " + e.getMessage());
        }
    }

    private static void ubahData() {
        tampilData();
        System.out.print("Masukkan ID yang mau diubah: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Masukkan nama baru: ");
        String nama = scanner.nextLine();
        String sql = "UPDATE karyawan SET nama=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Data berhasil diupdate!");
            else System.out.println("ID tidak ditemukan.");
        } catch (SQLException e) {
            System.out.println("Error update data: " + e.getMessage());
        }
    }

    private static void hapusData() {
        tampilData();
        System.out.print("Masukkan ID yang mau dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        String sql = "DELETE FROM karyawan WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Data berhasil dihapus!");
            else System.out.println("ID tidak ditemukan.");
        } catch (SQLException e) {
            System.out.println("Error hapus data: " + e.getMessage());
        }
    }
}
