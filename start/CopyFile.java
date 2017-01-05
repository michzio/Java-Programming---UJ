package start; 
import java.io.*; 
import java.lang.*;
import java.util.*;

public class CopyFile {
    private static final int BUFF_SIZE = 1000;
    private static byte[] buff = new byte[BUFF_SIZE];
    
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.out.println("Użycie: java start.CopyFile srcFile dstFile");
            return;
        }
        
        String srcFile = args[0];
        String dstFile = args[1];
        
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        
        try {
           input = new BufferedInputStream( new FileInputStream(srcFile));
           output = new BufferedOutputStream( new FileOutputStream(dstFile));
            
            int fileSize = input.available();
            int bytesReaded = 0, readedNum = 0;
        
            while(bytesReaded < fileSize) {
                readedNum = input.read(buff);
                output.write(buff, 0, readedNum); //ponieważ ostatni odczyt nie zapełnia w pełni bufora i by nie dopisywać śmieci na końcu 
                System.out.print("\nKOPIUJE: \n" + new String(buff));
                bytesReaded += BUFF_SIZE;
            }
        } catch(FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku: " + e);
        } catch(IOException e) {
            System.out.println("Wystąpił błąd wejścia/wyjścia: " + e);
        } finally {
            if(input != null) input.close();
            if(output != null) output.close();
        }
        
    }
}
