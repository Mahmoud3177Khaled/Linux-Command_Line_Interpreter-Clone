package os.cli;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class manTest {
    public ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    @Test
    void manAExistCommand(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.man("pwd");
      assertTrue(buffer.toString().contains("The pwd command prints the full filename of the current working directory."));
      System.setOut(System.out);
      this.buffer.reset();  
    }
    @Test
    void manANotExistCommand(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.man("car");
      assertTrue(buffer.toString().contains("no entry for"));
      System.setOut(System.out);
      this.buffer.reset();  
    }
    @Test
    void manAExistCommandWithFOption(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.man("-f mv");
      assertTrue(buffer.toString().contains("mv - move (rename) files."));
      System.setOut(System.out);
      this.buffer.reset();  
    }
    @Test
    void helpOption(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.man("--help");
      assertTrue(buffer.toString().contains("Usage: man [OPTION]... [PAGE]..."));
      System.setOut(System.out);
      this.buffer.reset();  
    }
    @Test
    void versionOption(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.man("--version");
      assertTrue(buffer.toString().contains("man (GNU man) 2.9.3"));
      System.setOut(System.out);
      this.buffer.reset();  
    }
}
