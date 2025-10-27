import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class function {



    public static String  CLIENT = "SUGUS";
    public static String KONTRAK_NO = "AGR00001";
    public static double OTR = 240000000;
    public static double DP = 0.20;
    public static double bunga ;
    public static int LamaCicilan = 18;
    Connection conn = db_config.connection;
    DecimalFormat df = new DecimalFormat("#,###");
     public void HasilHitung() {
        
            
            if (LamaCicilan<=12 ) {
                bunga = 0.12;
                PerhitunganBulanan();
            }else if(LamaCicilan<=24){
                bunga = 0.14;
                PerhitunganBulanan();
            }else if (LamaCicilan>24){
                bunga = 0.16;
                PerhitunganBulanan();
            }
           
    
}
    public void PerhitunganBulanan(){
        String kontrakNo = KONTRAK_NO;


        try {
             // Ambil data kontrak
            String sqlKontrak = "SELECT * FROM kontrak WHERE kontrak_no = ?";
            PreparedStatement psKontrak = conn.prepareStatement(sqlKontrak);
            psKontrak.setString(1, kontrakNo);
            ResultSet rs = psKontrak.executeQuery();

            //update database bunga
            String sqlUpdateBunga = "UPDATE kontrak SET bunga = ? WHERE kontrak_no = ?";
            PreparedStatement psUpdateBunga = conn.prepareStatement(sqlUpdateBunga);
            psUpdateBunga.setDouble(1, bunga);
            psUpdateBunga.setString(2, KONTRAK_NO);
            psUpdateBunga.executeUpdate();
            if (rs.next()) {
                    String clientName = rs.getString("client_name");
                    double otr = rs.getDouble("otr");
                    double dpPersen = rs.getDouble("dp");
                    int jangkaWaktu = rs.getInt("jangka_waktu");
                    double bungaPersen = rs.getDouble("bunga");

                    // Hitung total pinjaman dan bunga
                    double dp = otr * dpPersen;
                    double pokokutang = otr - dp;
                    double totalBunga = pokokutang * bungaPersen;
                    double totalPembayaran = pokokutang + totalBunga;
                    double angsuranBulanan = totalPembayaran / jangkaWaktu;
                    // Siapkan query UPDATE
                   
                    String sqlUpdate = "UPDATE jadwal_angsuran SET angsuran_per_bulan = ?, tanggal_jatuh_tempo = ? WHERE kontrak_no = ? AND angsuran_ke = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

                    LocalDate tanggalJatuhTempo = LocalDate.of(2024, 1, 25);

                    for (int i = 1; i <= jangkaWaktu; i++) {
                        psUpdate.setDouble(1, angsuranBulanan);
                        psUpdate.setDate(2, java.sql.Date.valueOf(tanggalJatuhTempo));
                        psUpdate.setString(3, kontrakNo);
                        psUpdate.setInt(4, i);

                        int rows = psUpdate.executeUpdate();

                        // Jika data belum ada, tambahkan
                        if (rows == 0) {
                            String sqlInsert = "INSERT INTO jadwal_angsuran (kontrak_no, angsuran_ke, angsuran_per_bulan, tanggal_jatuh_tempo) VALUES (?, ?, ?, ?)";
                            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                            psInsert.setString(1, kontrakNo);
                            psInsert.setInt(2, i);
                            psInsert.setDouble(3, angsuranBulanan);
                            psInsert.setDate(4, java.sql.Date.valueOf(tanggalJatuhTempo));
                            psInsert.executeUpdate();
                        }

                        tanggalJatuhTempo = tanggalJatuhTempo.plusMonths(1);
                    }

                    System.out.println("Data jadwal angsuran berhasil diperbarui!");
                } else {
                    System.out.println("Kontrak dengan nomor " + kontrakNo + " tidak ditemukan!");
                }
                rs.close();
                psKontrak.close();
                psUpdateBunga.close();
                String Sqltampilangsuran ="SELECT * FROM JADWAL_ANGSURAN";
                PreparedStatement psTampilAngsuran = conn.prepareStatement(Sqltampilangsuran);
                ResultSet rstampil = psTampilAngsuran.executeQuery();
                System.out.println("KONTRAK_NO\t|ANGSURAN_KE\tANGSURAN_PERBULAN\tTANGGAL_JATUH_TEMPO");
                while (rstampil.next()) {
                    System.out.println(rstampil.getString("kontrak_no")+"\t|"+rstampil.getInt("angsuran_ke")+"\t\t|"+df.format(rstampil.getDouble("angsuran_per_bulan"))+"\t\t|"+rstampil.getDate("tanggal_jatuh_tempo")+"|");
                }
               
        } catch (SQLException e) {
            System.out.println("gagal Tampil : "+e.getMessage());
            e.getStackTrace();
        }
    }
   
    public void TampilkanTotalAngsuran() {
        try {
            
            
            String sql = "SELECT k.client_name, j.kontrak_no, " +
                         "SUM(j.angsuran_per_bulan) AS total_angsuran_jatuh_tempo " +
                         "FROM jadwal_angsuran j " +
                         "JOIN kontrak k ON j.kontrak_no = k.kontrak_no " +
                         "WHERE j.tanggal_jatuh_tempo <= ? " +
                         "AND k.client_name = ? " +
                         "GROUP BY j.kontrak_no, k.client_name";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "2024-08-14");
            ps.setString(2, "JIMMI");
            
            ResultSet rs = ps.executeQuery();
            
            System.out.println("KONTRAK_NO\t|CLIENT_NAME\t|TOTAL_ANGSURAN_JATUH_TEMPO");
            System.out.println("------------------------------------------------------------");
            
            if (rs.next()) {
                
                System.out.println( rs.getString("kontrak_no")+ "\t| " + rs.getString("client_name") + "\t\t| " + df.format(rs.getDouble("total_angsuran_jatuh_tempo")));
            } else {
                System.out.println("Tidak ada angsuran jatuh tempo untuk client tersebut.");
            }
            
            rs.close();
            ps.close();
            
            
        } catch (SQLException e) {
            System.out.println("Kesalahan SQL: " + e.getMessage());
        }
    }
    public void denda() {
        String sql = """
            SELECT 
                j.kontrak_no AS KONTRAK_NO,
                k.client_name AS CLIENT_NAME,
                j.angsuran_ke AS INSTALLMENT_NO,
                DATEDIFF('2024-08-14', j.tanggal_jatuh_tempo) AS HARI_KETERLAMBATAN,
                (j.angsuran_per_bulan * 0.001 * DATEDIFF('2024-08-14', j.tanggal_jatuh_tempo)) AS TOTAL_DENDA
            FROM jadwal_angsuran j
            JOIN kontrak k ON j.kontrak_no = k.kontrak_no
            WHERE j.kontrak_no = 'AGR00001'
              AND j.tanggal_jatuh_tempo < '2024-08-14'
              AND j.tanggal_jatuh_tempo > '2024-05-31';
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-12s %-10s %-15s %-20s %-15s%n", 
                "KONTRAK_NO", "CLIENT", "ANGSURAN_KE", "HARI_KETERLAMBATAN", "TOTAL_DENDA");
            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                String kontrak = rs.getString("KONTRAK_NO");
                String client = rs.getString("CLIENT_NAME");
                int angsuranKe = rs.getInt("INSTALLMENT_NO");
                int hariTelat = rs.getInt("HARI_KETERLAMBATAN");
                double totalDenda = rs.getDouble("TOTAL_DENDA");

                System.out.printf("%-12s %-10s %-15d %-20d Rp %-15.2f%n",
                    kontrak, client, angsuranKe, hariTelat, totalDenda);
            }

        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat mengambil data: " + e.getMessage());
        }
    }
    public void Tampilkan_Data() {
        this.conn = db_config.koneksi();
        String sql = "SELECT * FROM KONTRAK";
        System.out.println("-----------------------------------------------");
        System.out.println("Kontrak No\t|Client_Name\t|OTR");
        System.out.println("-----------------------------------------------");
        try(Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)){
        while (rs.next()) {
            System.out.println(rs.getString("kontrak_no")+"\t|"+rs.getString("client_name")+"\t\t|"+df.format(rs.getDouble("otr")));
            
        }
        System.out.println("----------------------------------------------");
       } catch (SQLException e) {
        System.out.println("gagal Tampil data : "+ e.getMessage());
       }

    }
   


}


