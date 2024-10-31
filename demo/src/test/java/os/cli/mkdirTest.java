package os.cli;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
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
}
