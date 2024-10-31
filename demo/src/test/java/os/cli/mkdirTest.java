package os.cli;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class mkdirTest {
    @Test
    void makeOneDir(){
        CLI cli = new CLI("c://");
        cli.mkdir("test1");
        File f = new File("c://test1");
        assertTrue(f.exists());
        f.delete();
    }

    @Test
    void makeMoreOneDir(){
        CLI cli = new CLI("c://");
        cli.mkdir("test1 test2 test3 test4");
        File f1 = new File("c://test1");
        File f2 = new File("c://test2");
        File f3 = new File("c://test3");
        File f4 = new File("c://test4");
        assertTrue(f1.exists() && f2.exists() && f3.exists() && f4.exists());
        f1.delete();
        f2.delete();
        f3.delete();
        f4.delete();
    }
    @Test
    void makeMoreOneDirWithDifferentPaths(){
        CLI cli = new CLI("c://");
        cli.mkdir("c://test1 test2 c:\\test1\\test3 test1\\test4");
        File f1 = new File("c://test1");
        File f2 = new File("c://test2");
        File f3 = new File("c:\\test1\\test3");
        File f4 = new File("c:\\test1\\test4");
        assertTrue(f1.exists() && f2.exists() && f3.exists() && f4.exists());
        f1.delete();
        f2.delete();
        f3.delete();
        f4.delete();
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
}
