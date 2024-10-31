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

public class redirectOutputTest {
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
    public void testRedirectOutput_EmptyCommand() {
        cli.redirectOutput("");
        String expectedOutput = ">: missing file operand\n";
        String actualOutput = outputStream.toString().replace(System.lineSeparator(), "\n");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRedirectOutput_FileDoesNotExist() {
        String path = Paths.get("").toAbsolutePath().toString();
        File testFile = new File(path + "\\" + "test2.txt");
        assertFalse(testFile.exists());
        cli.redirectOutput("test2.txt");
        assertTrue(testFile.exists());
    }

    @Test
    void testRedirectOutput_FileExists() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        File testFile = new File(path + "\\" + "test.txt");
        testFile.createNewFile();

        assertTrue(testFile.exists());

        long originalModifiedTime = testFile.lastModified();
        cli.redirectOutput("test.txt");

        assertTrue(testFile.exists());
        assertNotEquals(originalModifiedTime, testFile.lastModified());
    }

}
