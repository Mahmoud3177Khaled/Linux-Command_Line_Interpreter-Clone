package os.cli; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class cpTests {
    CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originaloutput = System.out;
    private final InputStream originalinput = System.in;

    @BeforeEach
    public void setup() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        // System.setIn(this.originalinput);

        cli.currentDir = System.getProperty("user.dir");
        cli.homeDir = System.getProperty("user.home");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(this.originaloutput);
        System.setIn(this.originalinput);
        this.outputStream.reset();
    }

    
}