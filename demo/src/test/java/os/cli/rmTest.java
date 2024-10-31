package os.cli;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class rmTest {
    @Test
    void removeOneFile(){
        CLI cli = new CLI("c://");
        try {
            File f = new File("c://test1.txt");
            f.createNewFile();
            cli.rm("test1.txt");
            assertTrue(!f.exists());
        } catch (IOException ex) {
        }
    }
    @Test
    void removeOneDir(){
        CLI cli = new CLI("c://");
        File f = new File("c://test1");
        f.mkdir();
        System.setOut(new PrintStream(this.buffer));
        cli.rm("-v test1");
        assertEquals("rm: cannot remove 'test1': Is a directory",buffer.toString().substring(0,buffer.toString().length()-2));
        f.delete();
        System.setOut(System.out);
        this.buffer.reset();  
    }

    @Test
    void removeMoreOneFile(){
        CLI cli = new CLI("c://");
        File f1 = new File("c://test1.txt");
        File f2 = new File("c://test2.txt");
        File f3 = new File("c://test3.txt");
        File f4 = new File("c://test4.txt");
        try {
            f1.createNewFile();
            f2.createNewFile();
            f3.createNewFile();
            f4.createNewFile();
        } catch (IOException ex) {
        }
        cli.rm("test1 test2 test3 test4");
        assertTrue(!f1.exists() && !f2.exists() && !f3.exists() && !f4.exists());
        f1.delete();
        f2.delete();
        f3.delete();
        f4.delete();
    }
    @Test
    void rmMoreOneFileWithDifferentPaths(){
        CLI cli = new CLI("c://");
        File f1 = new File("c://test1.txt");
        File f2 = new File("c://test2.txt");
        File fd1 = new File("c://test1");
        File fd2 = new File("c://test2");
        File f3 = new File("c:\\test1\\test3.txt");
        File f4 = new File("c:\\test1\\test4.txt");
        try {
            fd1.mkdir();
            fd2.mkdir();
            f1.createNewFile();
            f2.createNewFile();
            f3.createNewFile();
            f4.createNewFile();
        } catch (IOException ex) {
        }
        cli.rm("c:\\test1\\test3 test1\\test4 c://test1.txt test2.txt");
        assertTrue(!f1.exists() && !f2.exists() && !f3.exists() && !f4.exists());
        f1.delete();
        f2.delete();
        f3.delete();
        f4.delete();
        fd1.delete();
        fd2.delete();
    }
    @Test
    void makeDirAndItsParents(){
        CLI cli = new CLI("c://");
        cli.mkdir("-p a/b/c/d/e/f/g/h");
        File f = new File("c://a/b/c/d/e/f/g/h");
        assertTrue(f.exists());
        f.delete();
        f = new File("c://a/b/c/d/e/f/g");       
        f.delete();
        f = new File("c://a/b/c/d/e/f");       
        f.delete();
        f = new File("c://a/b/c/d/e");       
        f.delete();
        f = new File("c://a/b/c/d");       
        f.delete();
        f = new File("c://a/b/c");       
        f.delete();
        f = new File("c://a/b");       
        f.delete();
        f = new File("c://a");       
        f.delete();
    }
    public ByteArrayOutputStream buffer = new ByteArrayOutputStream();

   @Test
   void withVerbose(){
      CLI cli = new CLI("c://");
      System.setOut(new PrintStream(this.buffer));
      cli.mkdir("-v test");
      assertEquals("mkdir: created directory 'test'",buffer.toString().substring(0,buffer.toString().length()-2));
      System.setOut(System.out);
      this.buffer.reset();  
      File f = new File("c://test");
      f.delete();      
   }
}
