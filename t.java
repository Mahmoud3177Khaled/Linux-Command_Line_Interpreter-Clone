
import java.io.File;

public class t {
 public static void main(String[] args) {
    File f = new File("d");
    f = f.listFiles()[1]; 
    System.out.println(f.delete());

 }   
}
