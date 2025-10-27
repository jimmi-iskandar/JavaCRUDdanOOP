import java.util.Scanner;


public class interview {

    
    private static Scanner in = new Scanner(System.in);

    
    public static void main(String[] args) {
     function k = new function();
     

     
     k.Tampilkan_Data();
     while (true) {
        System.out.println("1 . Hitung");
        System.out.println("2 . Tampilkan Total Angsuran ");
        System.err.println("3 . Denda");
        System.out.println("4 . keluar");
         System.out.println("Masukan pilihan anda : ");
         int pilih = in.nextInt();
         switch (pilih) {
            case 1:
                k.HasilHitung();
                break;
            case 2:
                k.TampilkanTotalAngsuran();
                break;
            case 3:
                k.denda();
                break;
            case 4:
                System.out.println("Keluar");
                return;
            default:
            System.out.println("Pilihan Tidak Valid");
        }
       
        
    }


    }



}
