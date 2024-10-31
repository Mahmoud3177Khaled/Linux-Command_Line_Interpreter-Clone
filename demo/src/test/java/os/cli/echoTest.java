package os.cli;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class echoTest {
   public ByteArrayOutputStream buffer = new ByteArrayOutputStream();

   @Test
   void simpleInput(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("Hello World!");
      assertEquals("Hello World!",buffer.toString().substring(0,buffer.toString().length()-2));
      System.setOut(System.out);
      this.buffer.reset();        
   }

   @Test
   void option_E(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-E \\\\Hello\\n W\\torld!\\n");
      assertEquals("\\\\Hello\\n W\\torld!\\n\n",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }
   @Test
   void option_e_with_n(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-e Hello\\n World!\\n");
      assertEquals("Hello\n World!\n\n",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }

   @Test
   void option_e_with_t(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-e \\tHello \\tWorld!\\t");
      assertEquals("    Hello     World!    ",buffer.toString().substring(0,buffer.toString().length()-2));
      System.setOut(System.out);
      this.buffer.reset();        
   }
   @Test
   void option_e_with_v(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-e \\vHello \\vWorld!\\v");
      assertEquals("\nHello \n      World!\n            \n",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }

   @Test
   void option_e_with_b(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-e \\\\Hello\\\\    \\b  \'World!\'");
      assertEquals("\\Hello\\'World!'\n",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }

   @Test
   void option_e_with_r_c(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-e Hello\\r W\\0o\\crld!");
      assertEquals(" Wo",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }
   
   @Test
   void option_n(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("-n Hello World!");
      assertEquals("Hello World!",buffer.toString().replace(System.lineSeparator(), "\n"));
      System.setOut(System.out);
      this.buffer.reset();        
   }
   @Test
   void option_list(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.echo("*");
      File f = new File("c://");
      assertEquals(f.list().length,buffer.toString().split("\n").length);
      System.setOut(System.out);
      this.buffer.reset();        
   }
   
   @Test
   void override() throws FileNotFoundException{
      CLI cli = new CLI("c://");
      try {
         File f = new File("c:\\\\testEchoOption.txt");
         f.createNewFile();
         FileWriter fr = new FileWriter(f);
         fr.write("Good Morning!");
         fr.close(); 
         Scanner in = new Scanner(f);
         cli.echo("Hello World  > testEchoOption.txt");
         assertEquals("Hello World",in.nextLine());
         in.close();
      } catch (Exception e) {
      }
   }

   @Test
   void append() throws FileNotFoundException{
      CLI cli = new CLI("c://");
      try {
         File f = new File("c:\\\\testEchoOption.txt");
         f.createNewFile();
         FileWriter fr = new FileWriter(f,true);
         fr.write("Good Morning!");
         fr.close(); 
         Scanner in = new Scanner(f);
         cli.echo("Hello World  > testEchoOption.txt");
         assertEquals("Good Morning!Hello World",in.nextLine());
         in.close();
      } catch (Exception e) {
      }
   }

}
