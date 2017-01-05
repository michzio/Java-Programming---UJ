package start;
import java.io.*;
import java.lang.*;
import java.util.*;

public class PrintFile  {
    private static List<String> buffer = new ArrayList<String>();
    
    public static void main(String[] args) {
        
        if(args.length != 1) {
            System.out.println("Użycie: java start.PrintFile srcFile");
            return;
        }
        
        String srcFile = args[0];
        
        try {
            BufferedReader reader =
                new BufferedReader( new FileReader( new File(srcFile).getAbsoluteFile()));
        
            try {
                String line;
                while((line = reader.readLine()) != null) {
                    buffer.add(line);
                }
            } finally {
                reader.close();
            }
            
            
        } catch(IOException e) {
            System.out.println("IOExeption: " + e);
        }
        
        
        for(int i=0; i < buffer.size(); i++) {
            System.out.println(i + ": " + buffer.get(i));
        }
    }

}