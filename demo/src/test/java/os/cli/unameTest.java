package os.cli; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class unameTest {
    CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOutput = System.out;

    @BeforeEach
    public void setup() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));

        cli.currentDir = System.getProperty("user.dir");
        cli.homeDir = System.getProperty("user.home");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOutput);
        this.outputStream.reset();
    }

    @Test
    public void sOptiontrue() {
        cli.uname("-s");
        assertTrue(!this.outputStream.toString().contains("is not recognized"));
    }

    @Test
    public void rOptiontrue() {
        cli.uname("-r");
        assertTrue(!this.outputStream.toString().contains("is not recognized"));
    }

    @Test
    public void mOptiontrue() {
        cli.uname("-m");
        assertTrue(!this.outputStream.toString().contains("is not recognized"));
    }

    @Test
    public void nOptiontrue() {
        cli.uname("-n");
        assertTrue(!this.outputStream.toString().contains("is not recognized"));
    }

    @Test
    public void InvalidOption() {
        cli.uname("-x");
        assertTrue(this.outputStream.toString().contains("is not recognized"));
    }
}