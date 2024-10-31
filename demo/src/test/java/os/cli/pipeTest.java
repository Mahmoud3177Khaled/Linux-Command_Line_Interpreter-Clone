package os.cli;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class pipeTest {
    public ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    @Test
    void echo_rm(){
        CLI cli = new CLI("c://");
        File d = new File("c://a");
        d.mkdir();
        cli.pipe("echo hello world > a/test.txt  | rm a/test.txt");
        File f = new File("c://a/test.txt");
        assertTrue(!f.exists());
        f.delete();
        d.delete();
    }
    @Test
    void mkdir_rm(){
        CLI cli = new CLI("c://");
        cli.pipe("mkdir a  | rm -d  a");
        File d = new File("c://a");
        assertTrue(!d.exists());
        d.delete();
    }
    @Test
    void cp_rm(){
        CLI cli = new CLI("c://");
        File d1 = new File("c://a");
        File d2 = new File("c://b");
        File f1 = new File("c://a/f.txt");
        File f2 = new File("c://b/f.txt");
        d1.mkdir();
        d2.mkdir();
        try {        
            f1.createNewFile();
        } catch (IOException ex) {
        }
        cli.pipe("cp a/f.txt  b  | rm a/f.txt");
        assertTrue(!f1.exists() && f2.exists());
        f1.delete();
        f2.delete();
        d1.delete();
        d2.delete();
    }
    @Test
    void cd_pwd() throws IOException{
        System.setOut(new PrintStream(this.buffer));
        CLI cli = new CLI("c://");
        cli.pipe("cd Users | pwd");
        assertEquals("c:\\Users\\",buffer.toString().substring(0,buffer.toString().length()-2));
        System.setOut(System.out);
        this.buffer.reset();  
    }
}
