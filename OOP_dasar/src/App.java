public class App {


    public static void main(String[] args) throws Exception {

        String text ="iskandar";
        String reversed ="";
        System.out.println("sebelum direversed : "+ text);

        for (int i=text.length() -1; i>=0;i--){
            reversed += text.charAt(i);
            
        }
        System.out.println("hasil reverse : "+ reversed);
        
        
    }
}
