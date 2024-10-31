package os.cli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class touchTest {
    private CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        // Set initial directories for testing
        cli.currentDir = System.getProperty("user.dir"); // Set to current working directory
        cli.homeDir = System.getProperty("user.home");   // Set to home directory
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);  // Reset console output
        this.outputStream.reset();        // Clear output
    }

    @Test
    void testTouchCreatesFile() {
        String path = Paths.get("").toAbsolutePath().toString();
        cli.cd(path);
        cli.touch("test.txt");

        File testFile = new File(path + "\\" + "test.txt");
        assertTrue(testFile.exists() && testFile.isFile());
        testFile.delete();
    }

    @Test
    void testTouchWithEmptyOperand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        cli.touch("");

        String expectedOutput = "touch: missing file operand";
        String actualOutput = outContent.toString().replace(System.lineSeparator(), "").trim();

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }

}
