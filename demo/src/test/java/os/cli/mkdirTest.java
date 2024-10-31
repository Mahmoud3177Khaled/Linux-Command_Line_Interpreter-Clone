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
}
